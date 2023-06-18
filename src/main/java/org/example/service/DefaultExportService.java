package org.example.service;

import org.example.config.ExportConfig;
import org.example.worker.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DefaultExportService implements ExportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExportService.class);
    private final ExportConfig config;
    private final UserInfoService userInfoService;
    private final ResultWriter resultWriter;

    public DefaultExportService(ExportConfig config, UserInfoService userInfoService, ResultWriter resultWriter) {
        this.config = config;
        this.userInfoService = userInfoService;
        this.resultWriter = resultWriter;
    }

    @Override
    public void run() {
        var startTime = System.currentTimeMillis();
        LOGGER.info("Export started");
        var pool = Executors.newFixedThreadPool(config.getThreadsNumber());
        var readQueue = new AtomicInteger();
        for (var i = 0; i < config.getThreadsNumber(); i++) {
            LOGGER.info("Exporter #{} created", i);
            pool.execute(new Exporter(i, readQueue, userInfoService, resultWriter));
        }
        shutdownAndAwaitTermination(pool, config.getExecutionTimeoutSec());
        LOGGER.info("Export finished in {}ms", System.currentTimeMillis() - startTime);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool, long timeoutSec) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(timeoutSec, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}
