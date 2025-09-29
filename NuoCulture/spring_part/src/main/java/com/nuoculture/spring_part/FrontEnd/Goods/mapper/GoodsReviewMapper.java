package com.nuoculture.spring_part.FrontEnd.Goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReviewMapper extends BaseMapper<GoodsReviewInfo> {


}
