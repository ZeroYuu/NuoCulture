package com.nuoculture.spring_part.FrontEnd.Culture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Culture.entity.CultureTypeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrontCultureMapper extends BaseMapper<CultureInfo> {
    @Select("SELECT * from culture_info \n" +
            "WHERE cul_id=#{id} and is_delete=0")
    List<CultureInfo> getDetail(@Param("id") int id);

    @Select("SELECT hp_path from homepage_image_info where is_delete=0")
    List<String> getImages();

    @Select("SELECT cul_type_name,cul_type_id from culture_type_info where is_delete=0")
    List<CultureTypeInfo> getType();

    @Select("SELECT *" +
            " from culture_info where cul_shelves=1 and is_delete=0")
    List<CultureInfo> getCulture();

    @Select("SELECT * from culture_info " +
            " where cul_shelves=1 and is_delete=0 and cul_type_id=#{id}")
    List<CultureInfo> getPartCulture(@Param("id") String id);

}
