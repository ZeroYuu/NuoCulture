package com.nuoculture.spring_part.BackEnd.Goods.Order.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_payment_info")
public class OrderPaymentInfo {

    @TableId
    private int payment_id;
    private String payment_name;
    private int is_delete;
}
