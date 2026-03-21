package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class StatisticsDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;          // 开始日期，如不传则默认近7天

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;            // 结束日期，默认今天

    @Min(1)
    private Integer topN = 5;
}
