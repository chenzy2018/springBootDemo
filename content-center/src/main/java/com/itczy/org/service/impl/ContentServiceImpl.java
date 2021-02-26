package com.itczy.org.service.impl;

import com.itczy.org.domain.dto.TestDTO;
import com.itczy.org.feignClient.TestUserCenterFeignClient;
import com.itczy.org.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TestUserCenterFeignClient feignClient;

    @Override
    public TestDTO getContent() {
        String str = feignClient.query(1);

        return TestDTO.builder().ID("1").name(str).build();
    }

}
