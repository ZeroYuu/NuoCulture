package com.nuoculture.spring_part.BackEnd.Topic.Type.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Topic.Type.entity.TopicTypeInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Type.mapper.TopicTypeMapper;
import com.nuoculture.spring_part.BackEnd.Topic.Type.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicTypeMapper, TopicTypeInfo> implements TopicService {

    @Autowired
    TopicTypeMapper addressMapper;
    public String getAccount(int usr_id) {
        return addressMapper.getAccount(usr_id);
    };
    public String getAddrText(int id) {
        return addressMapper.getAddrText(id);
    }



}
