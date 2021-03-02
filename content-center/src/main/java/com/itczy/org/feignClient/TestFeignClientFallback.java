package com.itczy.org.feignClient;

import com.itczy.org.domain.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFeignClientFallback implements TestUserCenterFeignClient{

    @Override
    public UserDTO getUser(int userId, String token) {

        return null;
    }

    @Override
    public Boolean addBonus(int userId, int bonus) {
        log.info("进入TestFeignClientFallback",userId);
        return true;
    }
}
