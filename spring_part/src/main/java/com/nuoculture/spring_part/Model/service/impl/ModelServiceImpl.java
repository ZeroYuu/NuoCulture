package com.nuoculture.spring_part.Model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.Model.entity.Model;
import com.nuoculture.spring_part.Model.mapper.ModelMapper;
import com.nuoculture.spring_part.Model.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Autowired
    ModelMapper addressMapper;
    public String getAccount(int id) {
        return addressMapper.getAccount(id);
    };




}
