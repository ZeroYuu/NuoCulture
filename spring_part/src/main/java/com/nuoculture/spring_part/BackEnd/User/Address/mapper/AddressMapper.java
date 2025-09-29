package com.nuoculture.spring_part.BackEnd.User.Address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMapper extends BaseMapper<UserAddress> {
    @Select("SELECT usr_account from user_info \n" +
            "WHERE usr_id=#{usr_id}  and is_delete=0")
    String getAccount(@Param("usr_id") int usr_id);

    @Select("SELECT PLACE_NAME from ass_address \n" +
            "WHERE PLACE_CODE=#{id} and is_delete=0")
    String getAddrText(@Param("id") int id);

}
