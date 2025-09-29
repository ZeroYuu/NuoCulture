package com.nuoculture.spring_part.BackEnd.Culture.Type.entity;

import lombok.Data;

@Data
public class TypeParam {
    private Long curPage;
    private Long pageSize;
    private String cul_type_name;
    private String cul_type_remark;

}
