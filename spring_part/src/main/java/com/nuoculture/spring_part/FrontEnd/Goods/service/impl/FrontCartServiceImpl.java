package com.nuoculture.spring_part.FrontEnd.Goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsColorInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.mapper.FrontCartMapper;
import com.nuoculture.spring_part.FrontEnd.Goods.mapper.FrontGoodsMapper;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontCartService;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontGoodsService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrontCartServiceImpl extends ServiceImpl<FrontCartMapper, CartInfo> implements FrontCartService {

    @Autowired
    FrontCartMapper frontCartMapper;


}
