package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.export")
public class ExportConfig {
    private int threadsNumber;
    private int executionTimeoutSec;

    public int getThreadsNumber() {
        return threadsNumber;
    }

    public void setThreadsNumber(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    public int getExecutionTimeoutSec() {
        return executionTimeoutSec;
    }

    public void setExecutionTimeoutSec(int executionTimeoutSec) {
        this.executionTimeoutSec = executionTimeoutSec;
    }
}
