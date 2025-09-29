package com.nuoculture.spring_part.BackEnd.User.Account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class UserInfo {
    @TableId
    private int usr_id;
    private String usr_account;
    private String usr_name;
    private String usr_psd;
    private String usr_phone;
    private String usr_email;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8",locale="zh")
    private Date usr_bday;
    private String usr_avater;
    private int usr_points=1;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date last_login_time;

    private int is_admin;
    private int is_delete;


}
