package com.z.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author z
 * @date 2021/8/26
 * @apiNote
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.z")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.z")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
