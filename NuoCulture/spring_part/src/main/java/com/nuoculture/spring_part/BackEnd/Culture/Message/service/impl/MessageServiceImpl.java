package com.nuoculture.spring_part.BackEnd.Culture.Message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.mapper.MessageMapper;
import com.nuoculture.spring_part.BackEnd.Culture.Message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, CultureInfo> implements MessageService {

    @Autowired
    MessageMapper messageMapper;
    public String getTypeName(int id) {
        return messageMapper.getTypeName(id);
    };
    public String getPath(int id) {
        return messageMapper.getPath(id);
    };


}
