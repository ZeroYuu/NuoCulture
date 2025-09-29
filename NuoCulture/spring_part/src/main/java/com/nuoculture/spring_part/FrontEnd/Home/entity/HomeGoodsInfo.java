package com.nuoculture.spring_part.FrontEnd.Home.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_info")
public class HomeGoodsInfo {
    @TableId
    private int goods_id;
    private String goods_name;
    private int goods_type_id;
    private double goods_price;
    private int goods_inventory;
    private String goods_intro;
    private int goods_color_id;
    private boolean goods_shelves;

    private String img_path;

    @TableField(exist = false)
    private String goods_color_name;

    private int is_delete;

}
