package com.nuoculture.spring_part.BackEnd.Topic.Type.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("topic_type_info")
public class TopicTypeInfo {
    @TableId
    private int topic_type_id;
    private String topic_type_name;
    private String topic_type_remark;
    private String topic_type_encode;

    private int is_delete;

}
