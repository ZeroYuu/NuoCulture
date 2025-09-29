package com.nuoculture.spring_part.BackEnd.Topic.Review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;

import java.util.List;

public interface ReviewService extends IService<TopicReviewInfo> {
    public List<String> getImages(int id);
    public List<TopicInfo> getTopic(int id);
    public int setImg(int id,String img);
    public int getId();
    public String[] getUsrIdFromAccount(String usr_account);
    public String[] getUsrIdFromName(String usr_name);




}
