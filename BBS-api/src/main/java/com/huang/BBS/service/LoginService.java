package com.huang.BBS.service;

import com.huang.BBS.dao.pojo.SysUser;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.params.LoginParam;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
