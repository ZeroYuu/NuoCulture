package com.nuoculture.spring_part.Login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.Login.mapper.LoginMapper;
import com.nuoculture.spring_part.Login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, UserInfo> implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

//    @Override
//    public void saveUser(UserInfo userInfo) {
//
//    }

    @Override
    public boolean checkLoginToday(int userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getUsr_id, userId)
                .ge(UserInfo::getLast_login_time, new java.sql.Date(System.currentTimeMillis()));
        return count(wrapper) > 0;
    }

    @Override
    public void updateLoginPointsAndTime(int userId) {
        UserInfo user = getById(userId);
        if (user != null) {
            user.setUsr_points(user.getUsr_points() + 1); // 每次登录增加1积分
            user.setLast_login_time(new Date());
            updateById(user);
        }
    }
}
