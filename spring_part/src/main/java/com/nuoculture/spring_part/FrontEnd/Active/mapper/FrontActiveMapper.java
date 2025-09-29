package com.nuoculture.spring_part.FrontEnd.Active.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nuoculture.spring_part.FrontEnd.Active.entity.ActiveMomentInfo;

import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrontActiveMapper extends BaseMapper<ActiveInfo> {
    @Select("SELECT * from active_info \n" +
            "WHERE actv_id=#{id} and is_delete=0")
    List<ActiveInfo>  getDetail(@Param("id") int id);

    @Select("SELECT * from active_moment_info \n" +
            "WHERE actv_id=#{id} and is_delete=0")
    List<ActiveMomentInfo>  getMoment(@Param("id") int id);


    @Select("SELECT img_path from detail_image_info where actv_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE actv_id=#{id} and is_delete=0 LIMIT 1")
    String getImgurl(@Param("id") int id);


    @Select("SELECT a.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.actv_id = a.actv_id and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS actv_img_url\n" +
            "FROM active_info a\n" +
            "WHERE is_delete=0 and (actv_name like CONCAT('%',#{content},'%') or actv_content like CONCAT('%',#{content},'%'))")
    IPage<ActiveInfo> getActive(IPage<ActiveInfo> page,
                                @Param("content") String content);


}
