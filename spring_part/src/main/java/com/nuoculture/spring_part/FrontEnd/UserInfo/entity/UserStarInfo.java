package com.nuoculture.spring_part.FrontEnd.UserInfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_start_info")
public class UserStarInfo {
    @TableId
    private int star_heart_id;
    private int usr_id;
    private int topic_id;
    private int cul_id;
    private int goods_id;
    private int actv_id;
    private Boolean star;
    private Boolean heart;

    private int is_delete;

}
