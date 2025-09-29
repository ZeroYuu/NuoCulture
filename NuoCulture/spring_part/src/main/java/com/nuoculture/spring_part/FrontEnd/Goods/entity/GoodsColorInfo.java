package com.nuoculture.spring_part.FrontEnd.Goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_color_info")
public class GoodsColorInfo {
    @TableId
    private int goods_color_id;
    private String goods_color_name;
    private int goods_id;

    private int is_delete;

}
