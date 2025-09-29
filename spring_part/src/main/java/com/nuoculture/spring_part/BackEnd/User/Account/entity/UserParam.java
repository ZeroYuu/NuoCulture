package com.nuoculture.spring_part.BackEnd.User.Account.entity;

import lombok.Data;

@Data
public class UserParam {
    private Long curPage;
    private Long pageSize;
    private String usr_account;
    private String usr_name;
    private String usr_email;
    private String usr_phone;
    private String is_admin;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8",locale="zh")
    private String usr_bday;
//    private String usr_name;

//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8",locale="zh")
//    @TableField(exist = false)
//    private Date startTime;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8",locale="zh")
//    @TableField(exist = false)
//    private Date endTime;

}
