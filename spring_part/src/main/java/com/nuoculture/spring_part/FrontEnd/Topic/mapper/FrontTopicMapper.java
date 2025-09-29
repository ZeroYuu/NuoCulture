package com.nuoculture.spring_part.FrontEnd.Topic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.entity.TopicTypeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrontTopicMapper extends BaseMapper<TopicInfo> {
    @Select("SELECT * from topic_review_info \n" +
            "WHERE topic_id=#{id} and is_delete=0")
    List<TopicReviewInfo> getReview(@Param("id") int id);

    @Select("SELECT * from topic_info \n" +
            "WHERE topic_id=#{id} and is_delete=0")
    List<TopicInfo> getDetailApi(@Param("id") int id);

    @Select("SELECT * from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<UserInfo> getAccount(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info where topic_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE Topic_id=#{id} and is_delete=0 LIMIT 1")
    String getImgurl(@Param("id") int id);


    @Select("SELECT topic_type_name,topic_type_id from topic_type_info where is_delete=0")
    List<TopicTypeInfo> getType();


    @Select("SELECT t.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.topic_id = t.topic_id  and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM topic_info t\n" +
            "WHERE t.topic_status=1 and is_delete=0 and (topic_titile like CONCAT('%',#{content},'%') or topic_content like CONCAT('%',#{content},'%'))")
    IPage<TopicInfo> getTopic(IPage<TopicInfo> page,
                              @Param("content") String content);

    @Select("SELECT t.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.topic_id = t.topic_id  and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM topic_info t\n" +
            "WHERE t.topic_status=1 and is_delete=0 and t.topic_type_id=#{id} and (topic_titile like CONCAT('%',#{content},'%') or topic_content like CONCAT('%',#{content},'%'))")
    IPage<TopicInfo> getTypeTopic(IPage<TopicInfo> page,
                                  @Param("id") String id,
                                  @Param("content") String content);
}
