package com.nuoculture.spring_part.BackEnd.Active.Publish.entity;

import lombok.Data;

@Data
public class PublishParam {
    private Long curPage;
    private Long pageSize;

    private String actv_name;
    private String actv_region;
    private String actv_leader;
    private String actv_phone;
    private String start_time;
    private String end_time;

    private String usr_id;

}
