package com.nuoculture.spring_part.FrontEnd.Active.entity;

import lombok.Data;

@Data
public class PageParam {
    private Long curPage;
    private Long pageSize;
    private String actv_content;

}
