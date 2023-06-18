package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.VKFetchConfig;
import org.example.entity.UserInfo;
import org.example.entity.remote.vk.UserToUserInfoMapper;
import org.example.entity.remote.vk.UsersGetResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VKUserInfoFetchService implements UserInfoFetchService {

    private static final String BASE_URL = "https://api.vk.com";
    private static final String USERS_GET_METHOD = "/method/users.get";
    private final VKFetchConfig config;

    public VKUserInfoFetchService(VKFetchConfig config) {
        this.config = config;
    }

    @Override
    public List<UserInfo> get(Collection<Long> ids) {
        var client = getWebClient();
        var userList = client.get()
                .uri(uriBuilder -> buildUsersGetUri(uriBuilder, ids))
                .retrieve()
                .bodyToMono(UsersGetResponse.class)
                .map(UsersGetResponse::getResponse)
                .block();
        return userList != null ? userList.stream().map(UserToUserInfoMapper::map).toList() : Collections.emptyList();
    }

    private WebClient getWebClient() {
        var mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("d.M.yyyy"));

        var strategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON));
                })
                .build();
        return WebClient.builder().exchangeStrategies(strategies).baseUrl(BASE_URL).build();
    }

    private URI buildUsersGetUri(UriBuilder uriBuilder, Collection<Long> ids) {
        return uriBuilder.path(USERS_GET_METHOD)
                .queryParam("user_ids", ids.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .queryParam("fields", "bdate,city,contacts")
                .queryParam("access_token", config.getAccessToken())
                .queryParam("v", config.getVersion())
                .build();
    }
}
