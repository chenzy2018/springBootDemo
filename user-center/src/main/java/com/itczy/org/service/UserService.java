package com.itczy.org.service;

import com.itczy.org.domain.entity.User;

public interface UserService {

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    User getUserByID(int userId);

    /**
     * 添加用户
     * @return
     */
    void addUser();

    /**
     * 给用户增加积分
     *
     * @param userId
     * @param bonus
     * @return
     */
    Boolean addBonus(int userId, int bonus);


}
