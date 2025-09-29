package com.nuoculture.spring_part.BackEnd.Topic.Content.entity;

import lombok.Data;

@Data
public class ContentParam {
    private Long curPage;
    private Long pageSize;

    private String topic_titile;
    private String topic_type_id;
    private String topic_view;
    private String usr_account;
    private String usr_name;
    private String topic_status;
    private String topic_remark;


}
