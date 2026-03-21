package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendStatisticsVo implements Serializable {

    /**
     * 日期列表（x轴），格式 yyyy-MM-dd
     */
    private List<LocalDate> dates;

    /**
     * 用户数趋势
     */
    private List<Integer> users;

    /**
     * 课程数趋势
     */
    private List<Integer> courses;

    /**
     * 作业数趋势
     */
    private List<Integer> assignments;

    /**
     * 问题数趋势
     */
    private List<Integer> questions;

    /**
     * 学习数趋势（学习人次或学习时长，根据业务而定）
     */
    private List<Integer> studies;
}
