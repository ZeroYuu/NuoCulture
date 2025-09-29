package com.nuoculture.spring_part.BackEnd.Active.Moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Active.Moment.entity.ActiveMomentInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentMapper extends BaseMapper<ActiveMomentInfo> {
    @Select("SELECT img_path from detail_image_info \n" +
            "WHERE actv_id=#{id} and is_delete=0")
    List<String> getImages(@Param("id") int id);

    @Insert("INSERT INTO detail_image_info(actv_id,img_path,is_delete)" +
            " VALUES (#{id},#{img},0)")
    int setImg(@Param("id") int id,
               @Param("img") String img);

    @Select("select @@identity")
    int getId();

    @Select("SELECT * from active_info \n" +
            "WHERE actv_id=#{id} and is_delete=0")
    List<ActiveInfo> getActv(@Param("id") int id);


}
