package com.careerflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CareerFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerFlowApplication.class, args);
    }
}
