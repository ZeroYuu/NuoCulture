package com.nuoculture.spring_part.BackEnd.Topic.Type.entity;

import lombok.Data;

@Data
public class TypeParam {
    private Long curPage;
    private Long pageSize;
    private String topic_type_name;
    private String topic_type_remark;
    private String topic_type_encode;

}
