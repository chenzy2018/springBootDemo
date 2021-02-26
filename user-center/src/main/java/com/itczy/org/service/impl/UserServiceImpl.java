package com.itczy.org.service.impl;

import com.itczy.org.dao.UserMapper;
import com.itczy.org.domain.entity.User;
import com.itczy.org.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getUserByID(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user;
    }

    @Override
    public void addUser() {
        userMapper.insert(
            User.builder()
                    .wxId("1")
                    .wxNickname("czy")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .bonus(50)
                    .avatarUrl("")
                    .roles("admin")
                    .build()
        );
    }

    @Override
    public Boolean addBonus(int userId, int bonus) {
        User user = getUserByID(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKey(user);
        return null;
    }
}
