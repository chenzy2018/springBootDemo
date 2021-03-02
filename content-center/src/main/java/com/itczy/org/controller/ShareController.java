package com.itczy.org.controller;

import com.itczy.org.domain.dto.ShareDTO;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//动态刷新nacos配置，修改配置后之后可以不用重启服务
@RefreshScope
public class ShareController {

    private final ShareService shareService;

    /**
     * 根据id获取share
     *
     * 此处@RequestHeader表示从请求头里面获取名为X-token的参数的值，并赋给token
     *
     */
    @GetMapping("/findById/{id}")
    public ShareDTO findById(@PathVariable Integer id, @RequestHeader("X-token") String token){
        return shareService.findById(id, token);
    }

    @Value("${your.configuration}")
    private String yourConfiguration;

    @GetMapping("/test-config")
    public String TestConfiguration(){
        return yourConfiguration;
    }
}
