package com.kkrepo.blog.properties;

import com.kkrepo.blog.common.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author WangRuofei
 * @create 2020-05-24 10:02 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:application.yml"}, encoding = "utf-8", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "blog")
public class BlogProperties {

    private QiniuProperties qiniu = new QiniuProperties();

}
