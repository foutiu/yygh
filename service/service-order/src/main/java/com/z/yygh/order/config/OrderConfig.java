package com.z.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author z
 * @date 2021/8/29
 * @apiNote
 */
@Configuration
@MapperScan("com.z.yygh.order.mapper")
public class OrderConfig {
}
