package com.nuoculture.spring_part.FrontEnd.UserInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStarMapper extends BaseMapper<UserStarInfo> {

    @Select("SELECT * from user_start_info \n" +
            "WHERE cul_id=#{id} and is_delete=0 and usr_id=#{uid} LIMIT 1")
    List<UserStarInfo> select(@Param("id") int id,@Param("uid") int uid);



}
