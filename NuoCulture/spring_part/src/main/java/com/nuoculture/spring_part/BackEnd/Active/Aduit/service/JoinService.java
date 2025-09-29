package com.nuoculture.spring_part.BackEnd.Active.Aduit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;

import java.util.List;

public interface JoinService extends IService<ActiveJoinInfo> {
    public List<String> getImages(int id);
    public List<ActiveInfo> getActv(int id);
    public int setImg(int id,String img);
    public int getId();
    public List<UserInfo> getAccount(int usr_id);
    public String[] getUsrIdFromAccount(String usr_account);
    public String[] getUsrIdFromName(String usr_name);



}
