package org.example.service;

import org.example.entity.UserInfo;

import java.util.List;

public interface UserInfoService {

    boolean check();

    void importAllInfoFromRemote();

    List<UserInfo> findAllOrderById(int page, int size);
}
