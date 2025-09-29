package com.nuoculture.spring_part.BackEnd.User.Address.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.BackEnd.User.Address.mapper.AddressMapper;
import com.nuoculture.spring_part.BackEnd.User.Address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, UserAddress> implements AddressService {

    @Autowired
    AddressMapper addressMapper;
    public String getAccount(int usr_id) {
        return addressMapper.getAccount(usr_id);
    };
    public String getAddrText(int id) {
        return addressMapper.getAddrText(id);
    }



}
