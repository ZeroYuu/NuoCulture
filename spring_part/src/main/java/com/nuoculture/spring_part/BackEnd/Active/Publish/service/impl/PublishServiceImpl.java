package com.nuoculture.spring_part.BackEnd.Active.Publish.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.mapper.PublishMapper;
import com.nuoculture.spring_part.BackEnd.Active.Publish.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, ActiveInfo> implements PublishService {

    @Autowired
    PublishMapper publishMapper;
    public List<String> getImages(int id) {
        return publishMapper.getImages(id);
    };

    public boolean deleteImg(String url) {
        return publishMapper.deleteImg(url);
    };
    public boolean deleteAvatar(String url) {
        return publishMapper.deleteAvatar(url);
    };

    public int setImg(int id,String img){
//        System.out.println("img:"+img);
        return publishMapper.setImg(id,img);
    };
    public int getId(){
        return publishMapper.getId();
    };






}
