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
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("SELECT * from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    String getAccount(@Param("id") int id);


    @Select("SELECT ai.actv_id,usr_id,join_reason,join_remark,join_status,actv_name,actv_leader,actv_content,start_time,end_time,join_id,(\n" +
            "SELECT img_path FROM detail_image_info di WHERE di.actv_id = ai.actv_id and is_delete=0 LIMIT 1 ) AS actv_img_url\n" +
            "FROM active_join_info aji JOIN active_info ai ON aji.actv_id = ai.actv_id WHERE aji.usr_id = #{id} AND ai.is_delete = 0 and aji.is_delete=0;")
    List<ActiveInfo> getUsrActv(@Param("id") int id);

    @Select("SELECT * from topic_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<TopicInfo> getUsrTopic(@Param("id") int id);

    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE topic_id=#{id} and is_delete=0 LIMIT 1")
    String getImgurl(@Param("id") int id);

    @Select("SELECT * from cart_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<CartInfo> getUsrCart(@Param("id") int id);


    @Select("SELECT * from order_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<OrderInfo> getUsrOrd(@Param("id") int id);


    @Select("SELECT * from user_address_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<UserAddress> getUsrAddr(@Param("id") int id);

    @Select("SELECT * from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    List<UserInfo> getUsrInfo(@Param("id") int id);





}
