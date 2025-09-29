package com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity;

import lombok.Data;

@Data
public class CommodityParam {
    private Long curPage;
    private Long pageSize;

    private String goods_name;
    private String goods_type_id;
    private String goods_price;
    private String goods_inventory;
    private String goods_color_name;


}
