package com.nuoculture.spring_part.BackEnd.Active.Publish.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;

import java.util.List;

public interface PublishService extends IService<ActiveInfo> {
    public List<String> getImages(int id);
    public boolean deleteImg(String url);
    public boolean deleteAvatar(String url);
    public int setImg(int id,String img);
    public int getId();



}
