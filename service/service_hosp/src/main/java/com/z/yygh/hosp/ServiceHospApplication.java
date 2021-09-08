package com.z.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author z
 * @date 2021/8/14
 * @apiNote
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.z")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.z")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
