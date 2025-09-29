package com.nuoculture.spring_part.BackEnd.Culture.Message.entity;

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
@TableName("culture_info")
public class CultureInfo {
    @TableId
    private int cul_id;
    private String cul_title;
    private int cul_type_id;
    private String cul_pub;
    private String cul_content;
    private String cul_cover;
    private boolean cul_shelves;
    private boolean cul_recommend;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date cul_time;

    @TableField(exist = false)
    private int star_id;

    private int is_delete;

}
