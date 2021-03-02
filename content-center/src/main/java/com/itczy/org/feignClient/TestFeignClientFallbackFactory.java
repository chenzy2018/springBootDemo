package com.itczy.org.feignClient;

import com.itczy.org.domain.dto.UserDTO;
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
            public UserDTO getUser(int userId, String token) {
                log.warn("异常：", throwable);
                return null;
            }

            @Override
            public Boolean addBonus(int userId, int bonus) {
                return null;
            }
        };
    }
}
