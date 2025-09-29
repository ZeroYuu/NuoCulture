package com.nuoculture.spring_part.FrontEnd.Active.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("active_moment_info")
public class ActiveMomentInfo {
    @TableId
    private int mom_id;
    private String mom_content;

    private int actv_id;

    private int is_delete;

}
