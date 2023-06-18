package org.example.service;

import org.example.config.ImportConfig;
import org.example.entity.UserId;
import org.example.entity.UserInfo;
import org.example.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserInfoService implements UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExportService.class);

    private final UserInfoRepository repository;
    private final UserInfoFetchService fetchService;
    private final ImportConfig importConfig;

    public DefaultUserInfoService(
            UserInfoRepository repository,
            UserInfoFetchService fetchService,
            ImportConfig importConfig
    ) {
        this.repository = repository;
        this.fetchService = fetchService;
        this.importConfig = importConfig;
    }

    @Override
    public boolean check() {
        return repository.check() > 0;
    }

    @Override
    public void importAllInfoFromRemote() {
        var page = 0;
        var size = importConfig.getPageSize();
        LOGGER.info("Getting UserInfo identifiers page #{} from DB", page);
        var userIds = repository.findAllIdsByOrderById(PageRequest.of(page, size));
        while (!userIds.isEmpty()) {
            var ids = userIds.stream().map(UserId::getId).toList();
            LOGGER.info("Fetching UserInfo page #{} from remote API", page);
            var userInfos = fetchService.get(ids);
            LOGGER.info("Saving UserInfo page #{} to DB", page);
            repository.saveAll(userInfos);
            page++;
            LOGGER.info("Getting UserInfo identifiers page #{} from DB", page);
            userIds = repository.findAllIdsByOrderById(PageRequest.of(page, size));
        }
        LOGGER.info("Filling all info finished");
    }

    @Override
    public List<UserInfo> findAllOrderById(int page, int size) {
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return repository.findAllByOrderById(PageRequest.of(page, size));
    }
}
