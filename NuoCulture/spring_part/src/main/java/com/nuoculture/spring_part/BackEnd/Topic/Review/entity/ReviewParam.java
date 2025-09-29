package com.nuoculture.spring_part.BackEnd.Topic.Review.entity;

import lombok.Data;

@Data
public class ReviewParam {
    private Long curPage;
    private Long pageSize;

    private String topic_id;
    private String usr_id;
    private String topic_review_time;
    private String usr_name;
    private String usr_account;

}
