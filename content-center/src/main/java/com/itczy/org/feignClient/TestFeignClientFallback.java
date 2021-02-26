package com.itczy.org.feignClient;

public class TestFeignClientFallback implements TestUserCenterFeignClient{
    @Override
    public String query(int userId) {
        return "进入TestFeignClientFallback";
    }
}
