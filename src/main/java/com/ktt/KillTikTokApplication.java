package com.ktt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(value = "com.ktt.dao")
@EnableCaching
public class KillTikTokApplication {

    public static void main(String[] args) {
        SpringApplication.run(KillTikTokApplication.class, args);
    }

}
