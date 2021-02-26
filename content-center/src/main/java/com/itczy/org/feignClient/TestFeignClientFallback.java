package com.itczy.org.feignClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFeignClientFallback implements TestUserCenterFeignClient{
    @Override
    public String query(int userId) {
        return "进入TestFeignClientFallback";
    }

    @Override
    public Boolean addBonus(int userId, int bonus) {
        log.info("进入TestFeignClientFallback",userId);
        return true;
    }
}
