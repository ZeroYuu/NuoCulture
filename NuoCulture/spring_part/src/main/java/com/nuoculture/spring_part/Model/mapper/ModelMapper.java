package com.nuoculture.spring_part.Model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.Model.entity.Model;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMapper extends BaseMapper<Model> {
    @Select("SELECT usr_name from user_info \n" +
            "WHERE usr_id=#{id}")
    String getAccount(@Param("id") int id);


}
