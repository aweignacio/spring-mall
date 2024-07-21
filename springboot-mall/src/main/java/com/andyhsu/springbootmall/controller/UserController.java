package com.andyhsu.springbootmall.controller;

import com.andyhsu.springbootmall.dto.UserRegisterRequest;
import com.andyhsu.springbootmall.model.User;
import com.andyhsu.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }
}
