package com.nuoculture.spring_part.BackEnd.Active.Aduit.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("active_join_info")
public class ActiveJoinInfo {
    @TableId
    private int join_id;
    private String join_reason;
    private String join_remark;
    private int join_status;

    private int usr_id;
    private int actv_id;


    private int is_delete;

}
