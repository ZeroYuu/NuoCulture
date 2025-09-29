package com.nuoculture.spring_part.FrontEnd.Goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.mapper.ReviewMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Review.service.ReviewService;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.mapper.GoodsReviewMapper;
import com.nuoculture.spring_part.FrontEnd.Goods.service.GoodsReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsReviewServiceImpl extends ServiceImpl<GoodsReviewMapper, GoodsReviewInfo> implements GoodsReviewService {

    @Autowired
    GoodsReviewMapper goodsreviewMapper;






}
