package com.nuoculture.spring_part.Login.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper extends BaseMapper<UserInfo> {
}
