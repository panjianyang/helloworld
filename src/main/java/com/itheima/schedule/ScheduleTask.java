package com.itheima.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Scheduled(cron = "0/5 * * * * ?")
    public void testSchedule() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        System.out.println("当前发送消息的时间===" + format.format(calendar.getTime()));
    }
}

