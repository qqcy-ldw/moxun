package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDailyStatistic implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private LocalDate statDate;
    private Integer totalUsers;
    private Integer newUsers;
    private Integer activeUsers;
    private Integer courseViews;
    private Integer lessonViews;
    private Integer totalStudyTime;
    private Integer assignmentSubmissions;
    private Integer questionPosts;
    private Integer answerPosts;
    private Integer commentPosts;
    private LocalDateTime createTime;
}
