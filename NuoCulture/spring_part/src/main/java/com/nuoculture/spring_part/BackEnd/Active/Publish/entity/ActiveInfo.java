package com.nuoculture.spring_part.BackEnd.Active.Publish.entity;

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
@TableName("active_info")
public class ActiveInfo {
    @TableId
    private int actv_id;
    private String actv_name;
    private String actv_addr;
    private String actv_region;
    private String actv_leader;
    private String actv_phone;
    private String actv_content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date start_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date end_time;
    private boolean actv_recommend;

    @TableField(exist = false)
    private String[] actv_img;
    @TableField(exist = false)
    private String actv_img_url;
    @TableField(exist = false)
    private String join_reason;
    @TableField(exist = false)
    private String join_remark;
    @TableField(exist = false)
    private int join_status;
    @TableField(exist = false)
    private int join_id;

    @TableField(exist = false)
    private int star_id;

    private int is_delete;

}
