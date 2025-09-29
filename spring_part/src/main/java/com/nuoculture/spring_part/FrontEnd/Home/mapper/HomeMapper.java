package com.nuoculture.spring_part.FrontEnd.Home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.Model;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMapper extends BaseMapper<Model> {
    @Select("SELECT usr_name from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    String getAccount(@Param("id") int id);

    @Select("SELECT hp_path from homepage_image_info where is_delete=0")
    List<String> getImages();

    @Select("SELECT * from culture_info where cul_recommend=1 " +
            " and cul_shelves=1 and is_delete=0")
    List<CultureInfo> getCulture();

    @Select("SELECT a.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.actv_id = a.actv_id and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS actv_img_url\n" +
            "FROM active_info a\n" +
            "WHERE a.actv_recommend = 1 and is_delete=0 ")
    List<ActiveInfo> getActive();

    @Select("SELECT t.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.topic_id = t.topic_id and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM topic_info t\n" +
            "WHERE t.topic_recommend = 1 and t.topic_status=1 and is_delete=0")
    List<TopicInfo> getTopic();

    @Select("SELECT g.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.goods_id = g.goods_id and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM goods_info g\n" +
            "WHERE g.goods_shelves = 1 and is_delete=0  LIMIT 6")
    List<GoodsInfo> getGoods();

}
