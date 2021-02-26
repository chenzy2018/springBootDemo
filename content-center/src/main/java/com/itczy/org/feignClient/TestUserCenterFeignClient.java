package com.itczy.org.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * fallback用于处理远程调用异常情况(限流之类的)，出现异常就会执行类中方法，不能拿到异常
 * fallbackFactory用于处理远程调用异常情况(限流之类的)，出现异常就会执行类中方法，可以拿到异常
 * 以上两个参数互相独立，用其中一个即可，fallbackFactory比较强大
 *
 */
@FeignClient(name = "user-center",
        //fallback = TestFeignClientFallback.class,
        fallbackFactory = TestFeignClientFallbackFactory.class)
public interface TestUserCenterFeignClient {

    /**
     * 该方法被调用，就会构造以下链接去请求
     * http://user-center/getUser/{userId}
     * @param userId
     * @return
     */
    @GetMapping("/getUser/{userId}")
    String query(@PathVariable int userId);

    @GetMapping("/addBonus")
    Boolean addBonus(@RequestParam(value = "userId") int userId, @RequestParam(value = "bonus") int bonus);
}
