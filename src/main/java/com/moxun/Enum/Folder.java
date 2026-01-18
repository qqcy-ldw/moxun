package com.moxun.Enum;

public enum Folder {
    AVATAR("头像/"),
    COURSE_COVER("课程封面/");

    private String value;
    Folder(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
