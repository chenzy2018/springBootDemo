package com.itczy.org.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class UserController {

    @GetMapping("/getUser")
    public String getUser(@PathVariable int userId){
        log.info("这是个log");
        return "user01";
    }
}
