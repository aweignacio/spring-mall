package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.UserDao;
import com.andyhsu.springbootmall.dto.UserRegisterRequest;
import com.andyhsu.springbootmall.model.User;
import com.andyhsu.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
       return userDao.creatUser(userRegisterRequest);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userDao.getUserByUserId(userId);
    }
}
