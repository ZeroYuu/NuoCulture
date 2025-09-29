package com.nuoculture.spring_part.BackEnd.Topic.Type.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Topic.Type.entity.TopicTypeInfo;

public interface TopicService extends IService<TopicTypeInfo> {
    public String getAccount(int usr_id);
    public String getAddrText(int usr_id);

}
