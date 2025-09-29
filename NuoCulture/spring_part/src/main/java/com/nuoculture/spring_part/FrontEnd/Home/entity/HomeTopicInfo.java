package com.nuoculture.spring_part.FrontEnd.Home.entity;

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
public class HomeTopicInfo {
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

    private String img_path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date topic_time;

    private int is_delete;

}
