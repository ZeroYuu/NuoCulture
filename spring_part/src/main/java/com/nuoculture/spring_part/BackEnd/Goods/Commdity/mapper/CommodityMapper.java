package com.nuoculture.spring_part.BackEnd.Goods.Commdity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityMapper extends BaseMapper<GoodsInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE goods_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Insert("INSERT INTO detail_image_info(goods_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Insert("INSERT INTO goods_color_info(goods_color_name,goods_id,is_delete)" +
            " VALUES (#{name},#{id},0)")
    int setColorName(@Param("name") String name,@Param("id") int id);


    @Select("select @@identity")
    int getId();

    @Select("SELECT goods_type_name from goods_type_info \n" +
            "WHERE goods_type_id=#{id} and is_delete=0")
    String getTypeName(@Param("id") int id);

    @Select("SELECT goods_color_name from goods_color_info \n" +
            "WHERE goods_id=#{id} and is_delete=0")
    List<String> getColor(@Param("id") int id);

    @Select("SELECT goods_color_id from goods_color_info \n" +
            "WHERE goods_color_name like CONCAT('%',#{color_name},'%') and is_delete=0")
    String[] getGoodsId(@Param("color_name") String color_name);




}
