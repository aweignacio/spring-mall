package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.UserDao;
import com.andyhsu.springbootmall.dto.UserLoginRequest;
import com.andyhsu.springbootmall.dto.UserRegisterRequest;
import com.andyhsu.springbootmall.model.User;
import com.andyhsu.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查該email是否已經註冊過
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        //如果不是null，表示該email已經被註冊過
        if (user != null) {
            log.warn("該emil {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashedCode = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedCode);
        //創建帳號
        return userDao.creatUser(userRegisterRequest);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userDao.getUserByUserId(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        //判斷email是否正確
        if (user == null) {
            log.warn("email不存在");
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       //使用MD5生成密碼的雜湊值
        String hashedCode = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //比較密碼
        if (user.getPassword().equals(hashedCode)){
            return user;
        }else {
            log.warn("輸入的密碼錯誤");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
