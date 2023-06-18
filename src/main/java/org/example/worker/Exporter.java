package org.example.worker;

import org.example.entity.UserInfo;
import org.example.service.ResultWriter;
import org.example.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Exporter implements Runnable {
    private static final int PAGE_SIZE = 1;

    private final int id;
    private final AtomicInteger readQueue;
    private final UserInfoService userInfoService;
    private final ResultWriter writer;
    private final Logger logger;

    public Exporter(int id, AtomicInteger readQueue, UserInfoService userInfoService, ResultWriter writer) {
        this.id = id;
        this.writer = writer;
        this.readQueue = readQueue;
        this.userInfoService = userInfoService;
        this.logger = LoggerFactory.getLogger(Exporter.class.getName() + "#" + id);
    }

    @Override
    public void run() {
        var userInfos = getNextUserInfos();
        while (!userInfos.isEmpty()) {
            synchronized (writer) {
                logger.debug("ids = {}", userInfos.stream().map(UserInfo::getId).toList());
                userInfos.forEach(writer::write);
            }
            userInfos = getNextUserInfos();
        }
    }

    private List<UserInfo> getNextUserInfos() {
        var page = readQueue.getAndIncrement();
        return getUserInfos(page);
    }

    private List<UserInfo> getUserInfos(int page) {
        return userInfoService.findAllOrderById(page, PAGE_SIZE);
    }
}
