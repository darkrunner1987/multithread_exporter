package org.example;

import org.example.service.ExportService;
import org.example.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    public CommandLineRunner run(
            UserInfoService userInfoService,
            ExportService exportService
    ) {
        return args -> {
            LOGGER.info("Check {}", userInfoService.check() ? "OK": "FAILED");
            userInfoService.importAllInfoFromRemote();
            exportService.run();
            LOGGER.info("All operations finished");
        };
    }
}