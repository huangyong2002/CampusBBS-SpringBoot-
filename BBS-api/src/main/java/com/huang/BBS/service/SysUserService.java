package com.huang.BBS.service;

import com.huang.BBS.dao.pojo.SysUser;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.UserVo;


public interface SysUserService {


    UserVo findUserVoById(Long id);


    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token来查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);


}
