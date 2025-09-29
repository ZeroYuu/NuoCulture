package com.nuoculture.spring_part.BackEnd.Goods.Order.entity;

import lombok.Data;

@Data
public class OrderParam {
    private Long curPage;
    private Long pageSize;

    private String order_number;
    private String usr_id;
    private String addr_name;
    private String addr_phone;
    private String addr_province;
    private String addr_city;
    private String addr_country;
    private String addr_detail;
    private String order_remark;
    private String order_track_num;
    private String payment_id;
    private String order_status;
    private String order_time;

}
