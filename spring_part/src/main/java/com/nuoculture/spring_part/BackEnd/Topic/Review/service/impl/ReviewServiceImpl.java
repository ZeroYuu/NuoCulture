package com.nuoculture.spring_part.BackEnd.Topic.Review.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.mapper.ReviewMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Review.service.ReviewService;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, TopicReviewInfo> implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;
    public List<String> getImages(int id) {
        return reviewMapper.getImages(id);
    };

    public List<TopicInfo> getTopic(int id) {
        return reviewMapper.getTopic(id);
    };

    public int setImg(int id,String img){
//        System.out.println("img:"+img);
        return reviewMapper.setImg(id,img);
    };
    public int getId(){
        return reviewMapper.getId();
    };
    public String[] getUsrIdFromAccount(String usr_account) {
        return reviewMapper.getUsrIdFromAccount(usr_account);
    };
    public String[] getUsrIdFromName(String usr_name) {
        return reviewMapper.getUsrIdFromName(usr_name);
    };






}
