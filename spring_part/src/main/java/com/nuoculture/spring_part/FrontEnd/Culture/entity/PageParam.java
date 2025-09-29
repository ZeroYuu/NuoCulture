package com.nuoculture.spring_part.FrontEnd.Culture.entity;

import lombok.Data;

@Data
public class PageParam {
    private Long curPage;
    private Long pageSize;
    private String cul_type_id;
    private String cul_shelves;
    private String cul_content;

}
