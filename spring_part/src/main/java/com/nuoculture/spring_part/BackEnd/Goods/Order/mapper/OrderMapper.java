package com.nuoculture.spring_part.BackEnd.Goods.Order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper extends BaseMapper<OrderInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE goods_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Insert("INSERT INTO detail_image_info(topic_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Select("select @@identity")
    int getId();

    @Select("SELECT topic_type_name from topic_type_info \n" +
            "WHERE topic_type_id=#{id} and is_delete=0")
    String getTypeName(@Param("id") int id);

    @Select("SELECT payment_name from order_payment_info \n" +
            "WHERE payment_id=#{id} and is_delete=0")
    String getPayment(@Param("id") int id);

    @Select("SELECT usr_account from user_info \n" +
            "WHERE usr_id=#{id} and is_delete=0")
    String getAccount(@Param("id") int id);


    @Select("SELECT * from user_address_info \n" +
            "WHERE addr_id=#{id}  and is_delete=0")
    List<UserAddress> getAddr(@Param("id") int id);

    @Select("SELECT * from goods_info as gi\n" +
            "WHERE goods_id=#{id} and is_delete=0")
    List<GoodsInfo> getGoods(@Param("id") int id);

    @Select("SELECT * from goods_review_info \n" +
            "WHERE order_id=#{id} and is_delete=0")
    List<GoodsReviewInfo> getReview(@Param("id") int id);


    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_city like CONCAT('%',#{city},'%') and is_delete=0")
    String[] getAddrIdFromCity(@Param("city") String city);

    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_province like CONCAT('%',#{province},'%') and is_delete=0")
    String[] getAddrIdFromProvince(@Param("province") String province);

    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_country like CONCAT('%',#{country},'%') and is_delete=0")
    String[] getAddrIdFromCountry(@Param("country") String country);

    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_detail like CONCAT('%',#{detail},'%') and is_delete=0")
    String[] getAddrIdFromDetail(@Param("detail") String detail);

    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_name like CONCAT('%',#{name},'%') and is_delete=0")
    String[] getAddrIdFromName(@Param("name") String name);

    @Select("SELECT addr_id from user_address_info \n" +
            "WHERE addr_phone like CONCAT('%',#{phone},'%') and is_delete=0")
    String[] getAddrIdFromPhone(@Param("detail") String phone);


    @Select("SELECT usr_id from user_info \n" +
            "WHERE usr_account like CONCAT('%',#{account},'%') and is_delete=0")
    String[] getUsrId(@Param("account") String account);

}
