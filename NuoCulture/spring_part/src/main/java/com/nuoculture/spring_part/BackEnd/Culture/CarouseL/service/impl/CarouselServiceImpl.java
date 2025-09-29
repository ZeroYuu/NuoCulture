package com.nuoculture.spring_part.BackEnd.Culture.CarouseL.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity.homepageImageInfo;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.mapper.CarouselMapper;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, homepageImageInfo> implements CarouselService {

    @Autowired
    CarouselMapper addressMapper;
    public String getAccount(int usr_id) {
        return addressMapper.getAccount(usr_id);
    };
    public String getAddrText(int id) {
        return addressMapper.getAddrText(id);
    }



}
