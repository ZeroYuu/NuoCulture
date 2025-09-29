package com.nuoculture.spring_part.BackEnd.Active.Aduit.entity;

import lombok.Data;

@Data
public class JoinParam {
    private Long curPage;
    private Long pageSize;
    private String join_reason;
    private String join_remark;
    private String join_status;

    private String usr_account;
    private String usr_name;
    private String actv_id;

}
