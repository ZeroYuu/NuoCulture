package com.nuoculture.spring_part.BackEnd.Goods.Order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.mapper.OrderMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderService;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    public List<String> getImages(int id) {
        return orderMapper.getImages(id);
    }

    ;

    public int setImg(int id, String img) {
//        System.out.println("img:"+img);
        return orderMapper.setImg(id, img);
    }

    ;

    public int getId() {
        return orderMapper.getId();
    }

    ;

    public List<GoodsReviewInfo> getReview(int id) {
        return orderMapper.getReview(id);
    }

    public String getTypeName(int id) {
        return orderMapper.getTypeName(id);
    }

    ;

    public String getPayment(int id) {
        return orderMapper.getPayment(id);
    }

    ;

    public String getAccount(int id) {
        return orderMapper.getAccount(id);
    }

    ;

    public List<UserAddress> getAddr(int id) {
        return orderMapper.getAddr(id);
    }

    ;

    public List<GoodsInfo> getGoods(int id) {
        return orderMapper.getGoods(id);
    }

    ;


    public String[] getAddrIdFromCity(String city) {
        return orderMapper.getAddrIdFromCity(city);
    }

    ;

    public String[] getAddrIdFromProvince(String province) {
        return orderMapper.getAddrIdFromProvince(province);
    }

    ;

    public String[] getAddrIdFromCountry(String country) {
        return orderMapper.getAddrIdFromCountry(country);
    }

    ;

    public String[] getAddrIdFromDetail(String detail) {
        return orderMapper.getAddrIdFromDetail(detail);
    }

    ;

    public String[] getAddrIdFromName(String name) {
        return orderMapper.getAddrIdFromName(name);
    }

    ;

    public String[] getAddrIdFromPhone(String phone) {
        return orderMapper.getAddrIdFromPhone(phone);
    }

    ;

    public String[] getUsrId(String account) {
        return orderMapper.getUsrId(account);
    }

    ;


}
