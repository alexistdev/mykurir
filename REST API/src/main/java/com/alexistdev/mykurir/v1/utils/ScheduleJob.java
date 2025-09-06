package com.alexistdev.mykurir.v1.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Configuration
public class ScheduleJob {

    @Component
    public class ReportScheduler {
        @Scheduled(cron = "0 0 12 * * ?")
        public void sendDailyReport() {
            //todo send report daily on 12pm
        }
    }
}
