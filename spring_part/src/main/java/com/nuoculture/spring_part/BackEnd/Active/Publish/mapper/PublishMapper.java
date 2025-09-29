package com.nuoculture.spring_part.BackEnd.Active.Publish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishMapper extends BaseMapper<ActiveInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE actv_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);


    @Update("UPDATE detail_image_info " +
            "SET is_delete=1 " +
            "WHERE img_path LIKE CONCAT('%', #{url}, '%')")
    boolean deleteImg(@Param("url") String url);

    @Update("UPDATE user_info " +
            "SET usr_avater='' " +
            "WHERE usr_avater LIKE CONCAT('%', #{url}, '%')")
    boolean deleteAvatar(@Param("url") String url);

    @Insert("INSERT INTO detail_image_info(actv_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Select("select @@identity")
    int getId();



}
