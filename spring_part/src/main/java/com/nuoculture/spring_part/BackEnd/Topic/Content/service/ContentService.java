package com.nuoculture.spring_part.BackEnd.Topic.Content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;

import java.util.List;

public interface ContentService extends IService<TopicInfo> {
    public List<String> getImages(int id);
    public int setImg(int id,String img);
    public int getId();
    public String getTypeName(int id);
    public String[] getUsrIdFromAccount(String usr_account);
    public String[] getUsrIdFromName(String usr_name);




}
