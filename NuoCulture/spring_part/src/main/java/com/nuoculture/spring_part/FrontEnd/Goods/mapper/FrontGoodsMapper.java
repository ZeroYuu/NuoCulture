package com.nuoculture.spring_part.FrontEnd.Goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsColorInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsTypeInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrontGoodsMapper extends BaseMapper<GoodsInfo> {
    @Select("SELECT gri.*, oi.usr_id, oi.goods_id\n" +
            "FROM goods_review_info gri\n" +
            "JOIN order_info oi ON gri.order_id = oi.order_id \n" +
            "WHERE oi.goods_id = #{id} and gri.is_delete=0 and oi.is_delete=0")
    List<GoodsReviewInfo> getReview(@Param("id") int id);

    @Select("SELECT * from goods_info \n" +
            "WHERE goods_id=#{id} and is_delete=0")
    List<GoodsInfo> getDetailApi(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE goods_id=#{id} and is_delete=0 LIMIT 1")
    String getImgurl(@Param("id") int id);

    @Select("SELECT goods_color_name from goods_color_info \n" +
            "WHERE goods_color_id=#{id} and is_delete=0")
    String getColor(@Param("id") int id);

    @Select("SELECT * from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<UserInfo> getAccount(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info where goods_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Select("SELECT goods_type_name,goods_type_id from goods_type_info where goods_type_id=#{id} and is_delete=0")
    List<GoodsTypeInfo> getGoodsTypeName(@Param("id") int id);



    @Select("SELECT g.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.goods_id = g.goods_id  and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM goods_info g\n" +
            "WHERE g.goods_shelves = 1 and is_delete=0 ORDER BY RAND() DESC LIMIT 3")
    List<GoodsInfo> getRecmdList();




    @Select("SELECT goods_type_name,goods_type_id from goods_type_info where is_delete=0")
    List<GoodsTypeInfo> getType();


    @Select("SELECT g.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.goods_id = g.goods_id  and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM goods_info g\n" +
            "WHERE g.goods_shelves = 1 and is_delete=0 and goods_name like CONCAT('%',#{content},'%') ")
    IPage<GoodsInfo> getGoods(IPage<GoodsInfo> page,
                              @Param("content") String content);

    @Select("SELECT g.*, (\n" +
            "    SELECT img_path \n" +
            "    FROM detail_image_info di \n" +
            "    WHERE di.goods_id = g.goods_id  and is_delete=0\n" +
            "    LIMIT 1\n" +
            ") AS img_path_url\n" +
            "FROM goods_info g\n" +
            "WHERE g.goods_shelves = 1 and is_delete=0  and goods_type_id=#{id} and goods_name like CONCAT('%',#{content},'%')")
    IPage<GoodsInfo> getTypeGoods(IPage<GoodsInfo> page,
                                  @Param("id") String id,
                                  @Param("content") String content);

    @Select("SELECT *\n" +
            "FROM goods_color_info g\n" +
            "WHERE goods_id =#{id} and is_delete=0")
    List<GoodsColorInfo> getColorList(@Param("id") int id);




}
