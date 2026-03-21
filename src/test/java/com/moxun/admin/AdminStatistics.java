package com.moxun.admin;

import com.moxun.service.admin.AdminStatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

//@SpringBootTest
public class AdminStatistics {

//    @Autowired
//    private AdminStatisticsService adminStatisticsService;

    @Test
    public void testGetDashboardOverview() {
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.NOON));
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        HashMap<Object, Object> map = new HashMap<>();
//        map.put("name",new HashMap<>())
//        System.out.println(adminStatisticsService.getDashboardOverview());
    }
}
