package com.nuoculture.spring_part.BackEnd.Active.Moment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Active.Moment.entity.ActiveMomentInfo;
import com.nuoculture.spring_part.BackEnd.Active.Moment.mapper.MomentMapper;
import com.nuoculture.spring_part.BackEnd.Active.Moment.service.MomentService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, ActiveMomentInfo> implements MomentService {

    @Autowired
    MomentMapper momentMapper;
    public List<String> getImages(int id) {
        return momentMapper.getImages(id);
    };

    public List<ActiveInfo> getActv(int id) {
        return momentMapper.getActv(id);
    };

    public int setImg(int id,String img){
//        System.out.println("img:"+img);
        return momentMapper.setImg(id,img);
    };
    public int getId(){
        return momentMapper.getId();
    };






}
