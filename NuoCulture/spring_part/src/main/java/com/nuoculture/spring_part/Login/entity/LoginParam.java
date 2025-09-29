package com.nuoculture.spring_part.Login.entity;

import lombok.Data;

@Data
public class LoginParam {
    private String usr_account;
    private String usr_psd;
    private String code;
}
