package com.nuoculture.spring_part.BackEnd.Topic.Review.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("topic_review_info")
public class TopicReviewInfo {
    @TableId
    private int topic_review_id;
    private int topic_id;
    private int usr_id;
    private String topic_review_content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date topic_review_time;

    private int is_delete;

}
