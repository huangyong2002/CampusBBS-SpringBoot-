package com.huang.BBS.controller;

import com.huang.BBS.service.LoginService;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){

        //sso 单点登录，后期如果把登录注册功能提出去(单独的服务，可以提供独立的接口服务)
        return loginService.register(loginParam);
    }
}
