package com.huang.BBS.vo;

import lombok.Data;

@Data
public class LoginUserVo {
    private Long id;

    private String account;

    private String nickname;

    private String avatar;
}
