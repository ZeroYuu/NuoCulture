package com.nuoculture.spring_part.BackEnd.Goods.Type.entity;

import lombok.Data;

@Data
public class TypeParam {
    private Long curPage;
    private Long pageSize;
    private String goods_type_name;
    private String goods_type_remark;
    private String goods_type_encode;

}
