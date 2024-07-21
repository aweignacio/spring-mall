package com.andyhsu.springbootmall.service;

import com.andyhsu.springbootmall.dto.UserLoginRequest;
import com.andyhsu.springbootmall.dto.UserRegisterRequest;
import com.andyhsu.springbootmall.model.User;

public interface UserService {

    /**
     * 會員註冊的方法
     * @param userRegisterRequest 註冊會員所需要的參數
     * @return 返回該註冊會員的ID
     */
    Integer register(UserRegisterRequest userRegisterRequest);

    /**
     * 藉由會員ID查詢該筆會員資料的方法
     * @param userId 要查詢的會員ID
     * @return 返回該會員ID的資料
     */
    User getUserByUserId(Integer userId);

    /**
     * 登入功能
     * @param userLoginRequest 登入的帳號密碼
     * @return 登入的會員資訊
     */
    User login(UserLoginRequest userLoginRequest);
}
