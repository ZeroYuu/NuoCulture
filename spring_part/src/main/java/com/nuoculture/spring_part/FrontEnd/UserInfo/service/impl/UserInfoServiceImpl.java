package com.nuoculture.spring_part.FrontEnd.UserInfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserInfoMapper;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import com.nuoculture.spring_part.FrontEnd.UserInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    public String getAccount(int id) {
        return userInfoMapper.getAccount(id);
    };


    public List<ActiveInfo> getUsrActv(int id){
        return userInfoMapper.getUsrActv(id);
    }
    public List<TopicInfo> getUsrTopic(int id){
        return userInfoMapper.getUsrTopic(id);
    }
    public List<CartInfo> getUsrCart(int id){
        return userInfoMapper.getUsrCart(id);
    }
    public List<OrderInfo> getUsrOrd(int id){
        return userInfoMapper.getUsrOrd(id);
    }

    public List<UserAddress> getUsrAddr(int id){
        return userInfoMapper.getUsrAddr(id);
    }
    public List<UserInfo> getUsrInfo(int id){
        return userInfoMapper.getUsrInfo(id);
    }


    public String getImgurl(int id) {
        return userInfoMapper.getImgurl(id);
    };


    public int updateUserInfo(UserInfo userInfo) {
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("usr_id", userInfo.getUsr_id());


        if (userInfo.getUsr_name() != "") {
            updateWrapper.set("usr_name", userInfo.getUsr_name());
        }

        if (userInfo.getUsr_phone() != "") {
            updateWrapper.set("usr_phone", userInfo.getUsr_phone());
        }
        if (userInfo.getUsr_email() != "") {
            updateWrapper.set("usr_email", userInfo.getUsr_email());
        }
        if (userInfo.getUsr_bday() != null) {
            updateWrapper.set("usr_bday", userInfo.getUsr_bday());
        }
        if (userInfo.getUsr_avater() != null) {
            updateWrapper.set("usr_avater", userInfo.getUsr_avater());
        }

        int update = userInfoMapper.update(null, updateWrapper);
        return update;
    }

    @Autowired
    UserStarMapper userStarMapper;
    public int cancelStar(int id){
        UpdateWrapper<UserStarInfo> cancelStar = new UpdateWrapper<>();
        cancelStar.eq("star_heart_id",id);
        cancelStar.set("star",false);
        int cancel = userStarMapper.update(null,cancelStar);
        return cancel;

    }


}
