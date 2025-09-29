package com.nuoculture.spring_part.FrontEnd.Topic.entity;

import lombok.Data;

@Data
public class PageParam {
    private Long curPage;
    private Long pageSize;
    private String topic_type_id;
    private String topic_status;
    private String topic_content;

}
