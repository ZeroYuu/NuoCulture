package com.nuoculture.spring_part.BackEnd.Active.Aduit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinMapper extends BaseMapper<ActiveJoinInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE actv_id=#{id} AND is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Insert("INSERT INTO detail_image_info(actv_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Select("select @@identity")
    int getId();

    @Select("SELECT * from active_info \n" +
            "WHERE actv_id=#{id} AND is_delete=0")
    List<ActiveInfo> getActv(@Param("id") int id);
    @Select("SELECT * from user_info \n" +
            "WHERE usr_id=#{usr_id} AND is_delete=0")
    List<UserInfo> getAccount(@Param("usr_id") int usr_id);

    @Select("SELECT usr_id from user_info \n" +
            "WHERE usr_account like CONCAT('%',#{usr_account},'%')  AND is_delete=0")
    String[] getUsrIdFromAccount(@Param("usr_account") String usr_account);

    @Select("SELECT usr_id from user_info \n" +
            "WHERE usr_name like CONCAT('%',#{usr_name},'%')  AND is_delete=0")
    String[] getUsrIdFromName(@Param("usr_name") String usr_name);


}
