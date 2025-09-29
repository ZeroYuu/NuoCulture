package com.nuoculture.spring_part.Login.entity;

import lombok.Data;

@Data
public class LoginVo {
    private int usr_id;
    private String usr_name;
    private int is_admin;
    private String token; // 登录token
}
