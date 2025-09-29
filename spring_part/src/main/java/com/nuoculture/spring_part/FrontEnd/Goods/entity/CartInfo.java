package com.nuoculture.spring_part.FrontEnd.Goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("cart_info")
public class CartInfo {
    @TableId
    private int cart_id;
    private int goods_id;
    private int goods_count;
    private int goods_color_id;
    private int cart_status;
    private int usr_id;

    private int is_delete;

}
