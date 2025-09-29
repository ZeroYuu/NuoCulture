package com.nuoculture.spring_part.Model.entity;

import lombok.Data;

@Data
public class ModelParam {
    private Long curPage;
    private Long pageSize;

    private String addr_name;
    private String addr_phone;
    private String addr_province;
    private String addr_city;
    private String addr_country;
    private String addr_detail;

    private String usr_id;

}
