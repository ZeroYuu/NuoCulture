package com.nuoculture.spring_part.BackEnd.Topic.Review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMapper extends BaseMapper<TopicReviewInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE topic_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Insert("INSERT INTO detail_image_info(topic_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Select("select @@identity")
    int getId();

    @Select("SELECT * from topic_info \n" +
            "WHERE topic_id=#{id} and is_delete=0")
    List<TopicInfo> getTopic(@Param("id") int id);

    @Select("SELECT usr_id from user_info \n" +
            "WHERE usr_account like CONCAT('%',#{usr_account},'%') and is_delete=0")
    String[] getUsrIdFromAccount(@Param("usr_account") String usr_account);

    @Select("SELECT usr_id from user_info \n" +
            "WHERE usr_name like CONCAT('%',#{usr_name},'%') and is_delete=0")
    String[] getUsrIdFromName(@Param("usr_name") String usr_name);


}
