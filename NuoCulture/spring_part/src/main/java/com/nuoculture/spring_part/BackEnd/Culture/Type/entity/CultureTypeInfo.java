package com.nuoculture.spring_part.BackEnd.Culture.Type.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("culture_type_info")
public class CultureTypeInfo {
    @TableId
    private int cul_type_id;
    private String cul_type_name;
    private String cul_type_remark;
    private String cul_type_path;

    private int is_delete;

}
