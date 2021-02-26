package com.itczy.org.controller;

import com.itczy.org.domain.entity.User;
import com.itczy.org.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor( onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable int userId){
        return userService.getUserByID(userId);
    }

    /**
     * 添加积分
     * @param userId
     * @param bonus 积分
     * @return
     */
    @GetMapping("/addBonus")
    public Boolean addBonus(@RequestParam(value = "userId") int userId, @RequestParam(value = "bonus") int bonus){
        log.info("这是个log");
        return true;
    }
}
