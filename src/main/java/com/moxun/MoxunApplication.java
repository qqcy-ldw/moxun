package com.moxun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 启用定时任务
@EnableAsync // 启用异步任务支持（AOP日志记录需要）
public class MoxunApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoxunApplication.class, args);
    }

}
