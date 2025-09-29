package com.nuoculture.spring_part.BackEnd.Topic.Content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Topic.Content.mapper.ContentMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Content.service.ContentService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, TopicInfo> implements ContentService {

    @Autowired
    ContentMapper contentMapper;
    public List<String> getImages(int id) {
        return contentMapper.getImages(id);
    };

    public int setImg(int id,String img){
//        System.out.println("img:"+img);
        return contentMapper.setImg(id,img);
    };
    public int getId(){
        return contentMapper.getId();
    };

    public String getTypeName(int id) {
        return contentMapper.getTypeName(id);
    };
    public String[] getUsrIdFromAccount(String usr_account) {
        return contentMapper.getUsrIdFromAccount(usr_account);
    };
    public String[] getUsrIdFromName(String usr_name) {
        return contentMapper.getUsrIdFromName(usr_name);
    };







}
