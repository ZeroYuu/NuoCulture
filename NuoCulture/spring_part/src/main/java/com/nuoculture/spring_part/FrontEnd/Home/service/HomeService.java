package com.nuoculture.spring_part.FrontEnd.Home.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeGoodsInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.Model;

import java.util.List;

public interface HomeService extends IService<Model> {
    public String getAccount(int id);
    public List<String> getImages();
    public List<CultureInfo> getCulture();
    public List<ActiveInfo> getActive();
    public List<TopicInfo> getTopic();
    public List<GoodsInfo> getGoods();

}
