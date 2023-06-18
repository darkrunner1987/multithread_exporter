package org.example.service;

import org.example.entity.UserInfo;

import java.util.Collection;
import java.util.List;

public interface UserInfoFetchService {
    List<UserInfo> get(Collection<Long> ids);
}
