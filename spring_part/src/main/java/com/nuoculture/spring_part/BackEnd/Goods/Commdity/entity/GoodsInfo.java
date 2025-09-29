package com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity;

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
public class GoodsInfo {
    @TableId
    private int goods_id;
    private String goods_name;
    private int goods_type_id;
    private double goods_price;
    private int goods_inventory;
    private String goods_intro;
    private boolean goods_shelves;

    @TableField(exist = false)
    private int goods_color_id;

    @TableField(exist = false)
    private String[] img_path;

    @TableField(exist = false)
    private String img_path_url;

    @TableField(exist = false)
    private String goods_color_name;


    @TableField(exist = false)
    private int star_id;

    private int is_delete;

}
