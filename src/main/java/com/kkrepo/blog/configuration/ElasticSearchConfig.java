package com.kkrepo.blog.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangRuofei
 * @date 2020-11-22 3:11 下午
 * @copyright (c) 2020, bitmain.com All Rights Reserved
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${blog.elasticsearch.host:localhost}")
    private String host;

    @Value("${blog.elasticsearch.port:9200}")
    private int port;

    @Value("${blog.elasticsearch.connTimeout:3000}")
    private int connTimeout;

    @Value("${blog.elasticsearch.socketTimeout:5000}")
    private int socketTimeout;

    @Value("${blog.elasticsearch.connectionRequestTimeout:500}")
    private int connectionRequestTimeout;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http"))
            .setRequestConfigCallback( requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(connTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout))
        );
    }

}
