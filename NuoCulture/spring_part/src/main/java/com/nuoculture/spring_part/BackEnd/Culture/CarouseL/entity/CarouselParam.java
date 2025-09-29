package com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity;

import lombok.Data;

@Data
public class CarouselParam {
    private Long curPage;
    private Long pageSize;

    private String hp_title;
    private String hp_remark;

}
