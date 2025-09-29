package com.nuoculture.spring_part.BackEnd.Active.Aduit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.mapper.JoinMapper;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.service.JoinService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinServiceImpl extends ServiceImpl<JoinMapper, ActiveJoinInfo> implements JoinService {

    @Autowired
    JoinMapper joinMapper;
    public List<String> getImages(int id) {
        return joinMapper.getImages(id);
    };

    public List<ActiveInfo> getActv(int id) {
        return joinMapper.getActv(id);
    };

    public int setImg(int id,String img){
//        System.out.println("img:"+img);
        return joinMapper.setImg(id,img);
    };
    public int getId(){
        return joinMapper.getId();
    };
    public List<UserInfo> getAccount(int usr_id) {
        return joinMapper.getAccount(usr_id);
    };
    public String[] getUsrIdFromAccount(String usr_account) {
        return joinMapper.getUsrIdFromAccount(usr_account);
    };
    public String[] getUsrIdFromName(String usr_name) {
        return joinMapper.getUsrIdFromName(usr_name);
    };






}
