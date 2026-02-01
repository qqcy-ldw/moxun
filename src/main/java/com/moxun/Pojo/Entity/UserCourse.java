package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCourse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 状态(active/expired)
     */
    private String status;
}
