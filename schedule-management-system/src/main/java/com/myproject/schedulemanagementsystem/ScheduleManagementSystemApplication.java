package com.myproject.schedulemanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.myproject.schedulemanagementsystem.mapper")
public class ScheduleManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleManagementSystemApplication.class, args);
    }

}
