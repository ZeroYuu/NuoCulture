package com.nuoculture.spring_part.FrontEnd.Topic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.entity.TopicTypeInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;

import java.util.List;

public interface FrontTopicService extends IService<TopicInfo> {
    public List<String> getImages(int id);
    public String  getImgurl(int id);
    public List<TopicInfo>  getDetailApi(int id);
    public List<TopicReviewInfo>  getReview(int id);
    public List<UserInfo> getAccount(int id);

    public IPage<TopicInfo> getTopic(IPage<TopicInfo> page,String content);
    public IPage<TopicInfo> getTypeTopic(IPage<TopicInfo> page,String id,String content);
    public List<TopicTypeInfo> getType();


    public List<UserStarInfo> getStarTopic(int id);
    public Boolean getStar(int id,int uid);
    public int StarTopic(int id,int uid,boolean star);
}
