package com.nuoculture.spring_part.BackEnd.Active.Moment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Active.Moment.entity.ActiveMomentInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;

import java.util.List;

public interface MomentService extends IService<ActiveMomentInfo> {
    public List<String> getImages(int id);
    public List<ActiveInfo> getActv(int id);
    public int setImg(int id,String img);
    public int getId();



}
