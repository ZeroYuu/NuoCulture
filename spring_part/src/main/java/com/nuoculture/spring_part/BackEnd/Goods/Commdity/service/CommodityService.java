package com.nuoculture.spring_part.BackEnd.Goods.Commdity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;

import java.util.List;

public interface CommodityService extends IService<GoodsInfo> {
    public List<String> getImages(int id);
    public int setImg(int id,String img);
    public int getId();
    public String getTypeName(int id);
    public String getColor(int id);
    public String[] getGoodsId(String color_name);
    public int setColorName(String name,int id);




}
