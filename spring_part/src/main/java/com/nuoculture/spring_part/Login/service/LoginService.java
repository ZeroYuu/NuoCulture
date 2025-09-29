package com.nuoculture.spring_part.Login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;

public interface LoginService extends IService<UserInfo> {
    //增加
//    void saveUser(UserInfo userInfo);

    /**
     * 检查用户当天是否已登录
     * @param userId 用户ID
     * @return 如果当天已登录返回true，否则返回false
     */
    boolean checkLoginToday(int userId);

    /**
     * 更新用户积分和最后登录时间
     * @param userId 用户ID
     */
    void updateLoginPointsAndTime(int userId);
}



