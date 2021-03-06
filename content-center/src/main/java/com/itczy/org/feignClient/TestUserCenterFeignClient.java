package com.itczy.org.feignClient;

import com.itczy.org.domain.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * name属性必须要加，缺少就会报错，指定调用的某个微服务的名称
 *
 * configuration = UserCenterFeignConfiguration.class：用于定义feignClient的配置类
 * 或者使用添加配置的方式指定配置类
 *
 * fallback用于处理远程调用异常情况(限流之类的)，出现异常就会执行类中方法，不能拿到异常
 * fallbackFactory用于处理远程调用异常情况(限流之类的)，出现异常就会执行类中方法，可以拿到异常
 * 以上两个参数互相独立，用其中一个即可，fallbackFactory比较强大
 *
 */
//@FeignClient(name = "user-center",
//        configuration = UserCenterFeignConfiguration.class
//        //fallback = TestFeignClientFallback.class,
//        fallbackFactory = TestFeignClientFallbackFactory.class)
@FeignClient(name = "user-center",
        //fallback = TestFeignClientFallback.class,
        fallbackFactory = TestFeignClientFallbackFactory.class)
public interface TestUserCenterFeignClient {

    /**
     * 该方法被调用，就会构造以下链接去请求
     * http://user-center/getUser/{userId}
     *
     * 此处@RequestHeader("X-Token")表示feign在构建请求的时候会在header里加一个token
     * 这种方案非常有局限性，因为会修改接口定义，在远程调用多的时候，就会引起大量修改
     * 存在类似公共需求，建议采用RequestInterceptor的方式支持
     */
    @GetMapping("/getUser/{userId}")
    UserDTO getUser(@PathVariable(value = "userId") int userId, @RequestHeader("X-Token") String token);

    @GetMapping("/addBonus")
    Boolean addBonus(@RequestParam(value = "userId") int userId, @RequestParam(value = "bonus") int bonus);
}
