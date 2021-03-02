package com.itczy.org.service.impl;

import com.itczy.org.domain.dto.UserDTO;
import com.itczy.org.feignClient.TestUserCenterFeignClient;
import com.itczy.org.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor( onConstructor = @__(@Autowired))
public class ContentServiceImpl implements ContentService {

    //@Autowired(required = false)
    private final TestUserCenterFeignClient feignClient;

    @Override
    public UserDTO getContent(String token) {
       UserDTO userDTO = feignClient.getUser(1, token);

       return userDTO;
    }

}
