package com.nuoculture.spring_part.BackEnd.Culture.Message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper extends BaseMapper<CultureInfo> {
    @Select("SELECT cul_type_name from culture_type_info \n" +
            "WHERE cul_type_id=#{id} and is_delete=0")
    String getTypeName(@Param("id") int id);

    @Select("SELECT cul_type_path from culture_type_info \n" +
            "WHERE cul_type_id=#{id} and is_delete=0")
    String getPath(@Param("id") int id);


}
