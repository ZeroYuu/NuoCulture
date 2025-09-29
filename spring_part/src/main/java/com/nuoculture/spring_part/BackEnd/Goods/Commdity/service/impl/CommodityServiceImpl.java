package com.nuoculture.spring_part.BackEnd.Goods.Commdity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.mapper.CommodityMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, GoodsInfo> implements CommodityService {

    @Autowired
    CommodityMapper commodityMapper;

    public List<String> getImages(int id) {
        return commodityMapper.getImages(id);
    }

    ;

    public int setImg(int id, String img) {
//        System.out.println("img:"+img);
        return commodityMapper.setImg(id, img);
    }

    ;

    public int setColorName(String name, int id) {

        return commodityMapper.setColorName(name, id);
    }

    ;

    public int getId() {
        return commodityMapper.getId();
    }

    ;

    public String getTypeName(int id) {
        return commodityMapper.getTypeName(id);
    }

    ;

    //    public String getColor(int id) {
//        return commodityMapper.getColor(id);
//    };
// 在CommodityService.java中
    public String getColor(int id) {
        List<String> colors = commodityMapper.getColor(id); // 假设这里是通过某个字段查询颜色
        System.out.println(colors);
        if (colors.isEmpty()) {
            return null;
        } else if (colors.size() == 1) {
            return colors.get(0);
        } else {
            return String.join(",", colors);
        }
    }

    public String[] getGoodsId(String color_name) {
        return commodityMapper.getGoodsId(color_name);
    }

    ;


}
