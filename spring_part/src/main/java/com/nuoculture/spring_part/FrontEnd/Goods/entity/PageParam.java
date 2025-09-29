package com.nuoculture.spring_part.FrontEnd.Goods.entity;

import lombok.Data;

@Data
public class PageParam {
    private Long curPage;
    private Long pageSize;
    private String goods_type_id;
    private String goods_shelves;
    private String goods_content;

}
