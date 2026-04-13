package com.moxun;


import org.junit.jupiter.api.Test;

public class test {

    @Test
    public void test() {
        String s = new String("我的密码是:password:123456");
        StringBuilder stringBuilder = new StringBuilder();
        if (isSensitiveField(s)) {
            System.out.println(stringBuilder.append(s).append("=[FILTERED]"));
        }

    }

    private boolean isSensitiveField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        // 将字段名转换为小写
        String lowerField = fieldName.toLowerCase();
        return lowerField.contains("password") ||
                lowerField.contains("pwd") ||
                lowerField.contains("secret") ||
                lowerField.contains("token") ||
                lowerField.contains("key") ||
                lowerField.contains("credential");
    }
}
