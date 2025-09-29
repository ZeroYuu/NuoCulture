package com.nuoculture.spring_part.FrontEnd.UserInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;

import java.util.List;

public interface UserInfoService extends IService<UserInfo> {
    public String getAccount(int id);

    public List<ActiveInfo> getUsrActv(int id);
    public List<TopicInfo> getUsrTopic(int id);
    public List<CartInfo> getUsrCart(int id);
    public List<OrderInfo> getUsrOrd(int id);

    public List<UserAddress> getUsrAddr(int id);
    public List<UserInfo> getUsrInfo(int id);

    public String  getImgurl(int id);


    public int updateUserInfo(UserInfo userInfo);
    public int cancelStar(int id);

}
