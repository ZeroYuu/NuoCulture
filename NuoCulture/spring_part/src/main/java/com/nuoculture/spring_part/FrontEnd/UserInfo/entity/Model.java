package com.nuoculture.spring_part.FrontEnd.UserInfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_address_info")
public class Model {
    @TableId
    private int addr_id;
    private String addr_name;
    private String addr_phone;
    private int addr_province;
    private int addr_city;
    private int addr_country;
    private String addr_detail;

    private int usr_id;

    private int is_delete;

}
