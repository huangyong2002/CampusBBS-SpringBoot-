package com.huang.admin.service;

import com.huang.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired(required = false)
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //登录的时候会把username传到这里
        //通过username查admin表，如果admin存在，将密码告诉spring security
        //如果不存在 返回null 认证失败了
        Admin admin = adminService.findAdminByUserName(username);
        if(admin==null){
            //登录失败
            return null;
        }

        UserDetails userDetails =new User(username,admin.getPassword(),new ArrayList<>());
        return userDetails;
    }
}
