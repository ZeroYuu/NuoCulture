package com.nuoculture.spring_part.BackEnd.Culture.Message.entity;

import lombok.Data;

@Data
public class MessageParam {
    private Long curPage;
    private Long pageSize;

    private String cul_title;
    private String cul_type_id;
    private String cul_pub;

}
