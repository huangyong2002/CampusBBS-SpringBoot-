package com.huang.BBS.utils;

import com.huang.BBS.dao.pojo.SysUser;

public class UserThreadLocal {

    private UserThreadLocal(){}

    //线程变量隔离

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}