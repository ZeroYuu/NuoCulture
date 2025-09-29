package com.nuoculture.spring_part.Login.entity;

import lombok.Data;

@Data
public class UpdatePasswordParam {
    private Long userId; //用户id
    private String oldPassword; //旧密码
    private String newPassword; //新密码
}
