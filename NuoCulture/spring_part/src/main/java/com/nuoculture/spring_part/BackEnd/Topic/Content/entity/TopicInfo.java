package com.nuoculture.spring_part.BackEnd.Topic.Content.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("topic_info")
public class TopicInfo {
    @TableId
    private int topic_id;
    private String topic_titile;
    private int topic_type_id;
    private int topic_view;
    private String topic_content;
    private String topic_remark;
    private int topic_status;
    private int usr_id;
    private boolean topic_recommend;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date topic_time;

    @TableField(exist = false)
    private String[] img_path;

    @TableField(exist = false)
    private String img_path_url;

    @TableField(exist = false)
    private int star_id;

    private int is_delete;

}
