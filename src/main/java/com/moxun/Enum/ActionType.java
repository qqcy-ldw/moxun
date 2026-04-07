package com.moxun.Enum;

import lombok.Getter;

/**
 * 用户操作日志 - 操作类型枚举
 */
@Getter
public enum ActionType {

    /**
     * 登录
     */
    LOGIN("LOGIN", "用户登录"),

    /**
     * 登出
     */
    LOGOUT("LOGOUT", "用户登出"),

    /**
     * 注册
     */
    REGISTER("REGISTER", "用户注册"),

    /**
     * 创建课程
     */
    CREATE_COURSE("CREATE_COURSE", "创建课程"),

    /**
     * 更新课程
     */
    UPDATE_COURSE("UPDATE_COURSE", "更新课程"),

    /**
     * 删除课程
     */
    DELETE_COURSE("DELETE_COURSE", "删除课程"),

    /**
     * 发布课程
     */
    PUBLISH_COURSE("PUBLISH_COURSE", "发布课程"),

    /**
     * 下架课程
     */
    OFFLINE_COURSE("OFFLINE_COURSE", "下架课程"),

    /**
     * 创建章节
     */
    CREATE_CHAPTER("CREATE_CHAPTER", "创建章节"),

    /**
     * 更新章节
     */
    UPDATE_CHAPTER("UPDATE_CHAPTER", "更新章节"),

    /**
     * 删除章节
     */
    DELETE_CHAPTER("DELETE_CHAPTER", "删除章节"),

    /**
     * 创建小节
     */
    CREATE_SECTION("CREATE_SECTION", "创建小节"),

    /**
     * 更新小节
     */
    UPDATE_SECTION("UPDATE_SECTION", "更新小节"),

    /**
     * 删除小节
     */
    DELETE_SECTION("DELETE_SECTION", "删除小节"),

    /**
     * 创建作业
     */
    CREATE_ASSIGNMENT("CREATE_ASSIGNMENT", "创建作业"),

    /**
     * 更新作业
     */
    UPDATE_ASSIGNMENT("UPDATE_ASSIGNMENT", "更新作业"),

    /**
     * 删除作业
     */
    DELETE_ASSIGNMENT("DELETE_ASSIGNMENT", "删除作业"),

    /**
     * 提交作业
     */
    SUBMIT_ASSIGNMENT("SUBMIT_ASSIGNMENT", "提交作业"),

    /**
     * 批改作业
     */
    GRADE_ASSIGNMENT("GRADE_ASSIGNMENT", "批改作业"),

    /**
     * 创建问答
     */
    CREATE_QUESTION("CREATE_QUESTION", "创建问答"),

    /**
     * 创建回答
     */
    CREATE_ANSWER("CREATE_ANSWER", "创建回答"),

    /**
     * 点赞回答
     */
    LIKE_ANSWER("LIKE_ANSWER", "点赞回答"),

    /**
     * 收藏课程
     */
    FAVORITE_COURSE("FAVORITE_COURSE", "收藏课程"),

    /**
     * 取消收藏
     */
    UNFAVORITE_COURSE("UNFAVORITE_COURSE", "取消收藏"),

    /**
     * 选课
     */
    ENROLL_COURSE("ENROLL_COURSE", "选课"),

    /**
     * 退课
     */
    DROP_COURSE("DROP_COURSE", "退课"),

    /**
     * 学习课程
     */
    STUDY_COURSE("STUDY_COURSE", "学习课程"),

    /**
     * 创建评论
     */
    CREATE_COMMENT("CREATE_COMMENT", "创建评论"),

    /**
     * 删除评论
     */
    DELETE_COMMENT("DELETE_COMMENT", "删除评论"),

    /**
     * 上传文件
     */
    UPLOAD_FILE("UPLOAD_FILE", "上传文件"),

    /**
     * 删除文件
     */
    DELETE_FILE("DELETE_FILE", "删除文件"),

    /**
     * 修改密码
     */
    CHANGE_PASSWORD("CHANGE_PASSWORD", "修改密码"),

    /**
     * 更新个人信息
     */
    UPDATE_PROFILE("UPDATE_PROFILE", "更新个人信息"),

    /**
     * 创建角色
     */
    CREATE_ROLE("CREATE_ROLE", "创建角色"),

    /**
     * 更新角色
     */
    UPDATE_ROLE("UPDATE_ROLE", "更新角色"),

    /**
     * 删除角色
     */
    DELETE_ROLE("DELETE_ROLE", "删除角色"),

    /**
     * 分配角色权限
     */
    ASSIGN_PERMISSION("ASSIGN_PERMISSION", "分配角色权限"),

    /**
     * 其他操作
     */
    OTHER("OTHER", "其他操作");

    private final String code;
    private final String description;

    ActionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static ActionType getByCode(String code) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getCode().equals(code)) {
                return actionType;
            }
        }
        return OTHER;
    }
}
