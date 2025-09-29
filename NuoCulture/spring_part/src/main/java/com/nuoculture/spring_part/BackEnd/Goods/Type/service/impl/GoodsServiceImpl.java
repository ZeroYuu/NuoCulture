package com.nuoculture.spring_part.BackEnd.Goods.Type.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Type.mapper.GoodsTypeMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Type.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsTypeInfo> implements GoodsService {

    @Autowired
    GoodsTypeMapper addressMapper;
    public String getAccount(int usr_id) {
        return addressMapper.getAccount(usr_id);
    };
    public String getAddrText(int id) {
        return addressMapper.getAddrText(id);
    }



}
