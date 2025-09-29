package com.nuoculture.spring_part.BackEnd.Topic.Type.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Type.entity.TopicTypeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicTypeMapper extends BaseMapper<TopicTypeInfo> {
    @Select("SELECT usr_name from user_info \n" +
            "WHERE usr_id=#{usr_id} and is_delete=0")
    String getAccount(@Param("usr_id") int usr_id);

    @Select("SELECT PLACE_NAME from ass_address \n" +
            "WHERE PLACE_CODE=#{id} and is_delete=0")
    String getAddrText(@Param("id") int id);

}
