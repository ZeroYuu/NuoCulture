package com.nuoculture.spring_part.BackEnd.Active.Moment.entity;

import lombok.Data;

@Data
public class MomentParam {
    private Long curPage;
    private Long pageSize;

    private String actv_id;

}
