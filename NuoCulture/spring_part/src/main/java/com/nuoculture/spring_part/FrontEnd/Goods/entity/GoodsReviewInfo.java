package com.nuoculture.spring_part.FrontEnd.Goods.entity;

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
@TableName("goods_review_info")
public class GoodsReviewInfo {
    @TableId
    private int goods_review_id;
    private String goods_review_content;
    private int order_id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date goods_review_time;

    @TableField(exist = false)
    private int usr_id;
    private int is_delete;

}
