package com.nuoculture.spring_part.BackEnd.Goods.Type.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.GoodsTypeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsTypeMapper extends BaseMapper<GoodsTypeInfo> {
    @Select("SELECT usr_name from user_info \n" +
            "WHERE usr_id=#{usr_id} and is_delete=0")
    String getAccount(@Param("usr_id") int usr_id);

    @Select("SELECT PLACE_NAME from ass_address \n" +
            "WHERE PLACE_CODE=#{id} and is_delete=0")
    String getAddrText(@Param("id") int id);

}
