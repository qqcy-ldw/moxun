package com.moxun.admin;

import cn.hutool.json.JSONUtil;
import com.moxun.service.admin.AdminStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @Test
    public void test1(){
        LocalDate date = LocalDate.now();  // 假设要统计今天
        LocalDateTime localDateTime = date.atStartOfDay();// 2026-04-06 00:00:00
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay(); // 2026-04-07 00:00:00
        log.info("开始时间：{},结束时间{}", localDateTime, endDateTime);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 25);
        String json = JSONUtil.toJsonStr(map);
        String jsonStr = JSONUtil.formatJsonStr(json);
        log.info("json:{}", json);
        log.info("jsonStr:{}", jsonStr);
    }
}
