package com.nuoculture.spring_part.BackEnd.Goods.Type.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_type_info")
public class GoodsTypeInfo {
    @TableId
    private int goods_type_id;
    private String goods_type_name;
    private String goods_type_remark;
    private String goods_type_encode;

    private int is_delete;

}
