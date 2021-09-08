package com.z.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author z
 * @date 2021/8/26
 * @apiNote
 */
@Configuration
@MapperScan("com.z.yygh.user.mapper")
public class UserConfig {
}
