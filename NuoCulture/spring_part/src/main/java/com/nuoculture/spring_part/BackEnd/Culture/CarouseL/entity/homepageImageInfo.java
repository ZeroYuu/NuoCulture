package com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("homepage_image_info")
public class homepageImageInfo {
    @TableId
    private int hp_id;
    private String hp_path;
    private String hp_title;
    private String hp_remark;

    private int is_delete;

}
