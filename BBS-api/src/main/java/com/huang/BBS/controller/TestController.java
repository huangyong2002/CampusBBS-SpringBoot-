package com.huang.BBS.controller;

import com.huang.BBS.dao.pojo.SysUser;
import com.huang.BBS.utils.UserThreadLocal;
import com.huang.BBS.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){

        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
