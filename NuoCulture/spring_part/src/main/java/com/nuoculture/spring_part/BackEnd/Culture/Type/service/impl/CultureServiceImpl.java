package com.nuoculture.spring_part.BackEnd.Culture.Type.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Culture.Type.entity.CultureTypeInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Type.mapper.CultureTypeMapper;
import com.nuoculture.spring_part.BackEnd.Culture.Type.service.CultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CultureServiceImpl extends ServiceImpl<CultureTypeMapper, CultureTypeInfo> implements CultureService {

    @Autowired
    CultureTypeMapper addressMapper;
    public String getAccount(int usr_id) {
        return addressMapper.getAccount(usr_id);
    };
    public String getAddrText(int id) {
        return addressMapper.getAddrText(id);
    }



}
