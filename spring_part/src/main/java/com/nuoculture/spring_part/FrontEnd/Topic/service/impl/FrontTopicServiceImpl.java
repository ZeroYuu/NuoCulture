package com.nuoculture.spring_part.FrontEnd.Topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.entity.TopicTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.mapper.FrontTopicMapper;
import com.nuoculture.spring_part.FrontEnd.Topic.service.FrontTopicService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontTopicServiceImpl extends ServiceImpl<FrontTopicMapper, TopicInfo> implements FrontTopicService {

    @Autowired
    FrontTopicMapper frontTopicMapper;
    public List<TopicInfo> getDetailApi(int id) {
        return frontTopicMapper.getDetailApi(id);
    };
    public List<UserInfo> getAccount(int id) {
        return frontTopicMapper.getAccount(id);
    };
    public List<TopicReviewInfo> getReview(int id) {
        return frontTopicMapper.getReview(id);
    };
    public List<String> getImages(int id) {
        return frontTopicMapper.getImages(id);
    };

    public String getImgurl(int id) {
        return frontTopicMapper.getImgurl(id);
    }

    public List<TopicTypeInfo> getType() {
        return frontTopicMapper.getType();
    };

    public IPage<TopicInfo> getTopic(IPage<TopicInfo> page,String content) {
        return frontTopicMapper.getTopic(page,content);
    };
    public IPage<TopicInfo> getTypeTopic(IPage<TopicInfo> page,String id,String content) {
        return frontTopicMapper.getTypeTopic(page,id,content);
    };

    @Autowired
    UserStarMapper userStarMapper;
    public List<UserStarInfo> getStarTopic(int id){
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usr_id", id).ne("topic_id", 0).eq("star",1);
        return userStarMapper.selectList(queryWrapper);
    }


    public Boolean getStar(int id,int uid){
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("star").eq("topic_id", id).eq("usr_id", uid);
        UserStarInfo userStarInfo = userStarMapper.selectOne(queryWrapper);
        return userStarInfo != null ? userStarInfo.getStar() : false;
    }


    public int StarTopic(int id,int uid,boolean star){
        System.out.println(star);
        if(star){
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("topic_id", id).eq("usr_id", uid);
            List<UserStarInfo> detail=userStarMapper.selectList(updateWrapper);


            System.out.println(detail);
//            System.out.println(q);
            if ( detail.isEmpty()) { // 如果查询结果为空（即没有记录），则插入新记录
                UserStarInfo newStarInfo = new UserStarInfo();
                newStarInfo.setTopic_id(id);
                newStarInfo.setUsr_id(uid);
                newStarInfo.setStar(star);
                newStarInfo.setIs_delete(0); // 假设is_delete默认为0

                userStarMapper.insert(newStarInfo);
            } else {

                updateWrapper.set("star",1);
                userStarMapper.update(null, updateWrapper);
            }
            return 1;
        }else {
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("topic_id", id).eq("usr_id", uid);
            updateWrapper.set("star",0);
            userStarMapper.update(null, updateWrapper);

            return 2;
        }

    }


}
