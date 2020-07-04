package com.kkrepo.blog.configuration;

import com.kkrepo.blog.configuration.filter.CustomHttpServletRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangRuofei
 * @create 2020-07-01 2:41 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Configuration
public class AppConfiguration {

    // TODO 这个过滤器添加上的话,后端请求会报错,原因未知
//    @Bean
//    public CustomHttpServletRequestFilter customHttpServletRequestFilter(){
//        return new CustomHttpServletRequestFilter();
//    }
}
