package com.itczy.org.service.impl;

import com.itczy.org.dao.UserMapper;
import com.itczy.org.domain.entity.User;
import com.itczy.org.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getUserByID(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user;
    }
}
