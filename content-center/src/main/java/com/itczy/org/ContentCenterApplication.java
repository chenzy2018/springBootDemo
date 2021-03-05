package com.itczy.org;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.itczy.org.feignClient.TestRestTemplateTokenRelayInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collections;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = "com.itczy.org.dao")
@EnableBinding(Source.class)
@EnableSwagger2
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    @Bean
    //开启负载均衡
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate getRestTemplate(){
        //基本方式
        //return new RestTemplate();

        //增加拦截器的方式
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(
                //可以传入多个拦截器
                Collections.singletonList(
                        new TestRestTemplateTokenRelayInterceptor()
                )
        );
        return restTemplate;
    }
}
