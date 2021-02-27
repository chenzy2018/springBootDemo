package com.itczy.org.feignClient;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 如果此处加了@Configuration，就必须把这个类放到启动类扫描不到的包里去，否则就会让所有的feignClient共享这个配置类
 *
 * 父子上下文引起的
 */

public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level lever(){
        //让feign打印所有级别的日志
        return Logger.Level.FULL;
    }

}
