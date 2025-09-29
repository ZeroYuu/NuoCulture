package com.nuoculture.spring_part.BackEnd.Goods.Order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;

import java.util.List;

public interface OrderService extends IService<OrderInfo> {
    public List<String> getImages(int id);
    public int setImg(int id,String img);
    public int getId();
    public String getTypeName(int id);
    public String getPayment(int id);
    public String getAccount(int id);
    public List<UserAddress> getAddr(int id);
    public List<GoodsInfo> getGoods(int id);

    public List<GoodsReviewInfo> getReview(int id);

    public String[] getAddrIdFromCity(String city);
    public String[] getAddrIdFromProvince(String province);
    public String[] getAddrIdFromCountry(String country);
    public String[] getAddrIdFromDetail(String detail);
    public String[] getAddrIdFromName(String name);
    public String[] getAddrIdFromPhone(String phone);
    public String[] getUsrId(String account);



}
