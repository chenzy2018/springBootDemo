package com.itczy.org.controller;

import com.itczy.org.domain.dto.ShareDTO;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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

    @Value("${your.configration}")
    private String yourConfigration;

    @GetMapping("/test-config")
    public String TestConfigration(){
        return yourConfigration;
    }
}
