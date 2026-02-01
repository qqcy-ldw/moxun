package com.moxun.Pojo.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时忽略空值字段，减少返回数据量
public class UserListVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名（登录名）
     */
    private String username;

    /**
     * 用户昵称（展示用）
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 状态中文描述（前端直接展示，无需转换）
     */
    private String statusDesc;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码（可选，用于前端权限判断）
     */
    private String roleCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
