package com.itczy.org.feignClient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 必须加上@Component
 *
 * 源码：SentinelFeign
 */
@Component
@Slf4j
public class TestFeignClientFallbackFactory implements FallbackFactory<TestUserCenterFeignClient> {
    @Override
    public TestUserCenterFeignClient create(Throwable throwable) {
        return new TestUserCenterFeignClient(){
            @Override
            public String query(int userId) {
                log.warn("异常：", throwable);
                return "进入TestFeignClientFallbackFactory";
            }

            @Override
            public Boolean addBonus(int userId, int bonus) {
                return null;
            }
        };
    }
}
