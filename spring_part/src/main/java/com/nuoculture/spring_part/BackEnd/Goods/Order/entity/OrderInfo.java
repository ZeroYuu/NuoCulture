package com.nuoculture.spring_part.BackEnd.Goods.Order.entity;

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
@TableName("order_info")
public class OrderInfo {
    @TableId
    private int order_id;
    private String order_number;
    private int usr_id;
    private int addr_id;
    private String order_remark;
    private String order_track_num;

    private int goods_id;
    private int payment_id;
    private double order_price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",locale="zh")
    private Date order_time;
    private int order_status;
    private int goods_count;
    @TableField(exist = false)
    private String order_review;

    private int goods_color_id;

    private int is_delete;

}
