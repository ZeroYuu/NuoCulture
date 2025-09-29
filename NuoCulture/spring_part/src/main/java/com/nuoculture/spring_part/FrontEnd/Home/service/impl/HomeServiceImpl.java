package com.nuoculture.spring_part.FrontEnd.Home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeGoodsInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.Model;
import com.nuoculture.spring_part.FrontEnd.Home.mapper.HomeMapper;
import com.nuoculture.spring_part.FrontEnd.Home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Model> implements HomeService {

    @Autowired
    HomeMapper homeMapper;
    public String getAccount(int id) {
        return homeMapper.getAccount(id);
    };
    public List<String> getImages() {
        return homeMapper.getImages();
    };
    public List<CultureInfo> getCulture() {
        return homeMapper.getCulture();
    };

    public List<ActiveInfo> getActive() {
        return homeMapper.getActive();
    };
    public List<TopicInfo> getTopic() {
        return homeMapper.getTopic();
    };
    public List<GoodsInfo> getGoods() {
        return homeMapper.getGoods();
    };



}
