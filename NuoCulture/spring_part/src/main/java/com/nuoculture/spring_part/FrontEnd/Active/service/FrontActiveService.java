package com.nuoculture.spring_part.FrontEnd.Active.service;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.FrontEnd.Active.entity.ActiveMomentInfo;

import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface FrontActiveService extends IService<ActiveInfo> {
    public List<ActiveInfo> getDetail(int id);
    public List<ActiveMomentInfo> getMoment(int id);
    public List<String> getImages(int id);
    public String  getImgurl(int id);


    public IPage<ActiveInfo> getActive(IPage<ActiveInfo> page,String content);


    public List<UserStarInfo> getStarActv(int id);
    public Boolean getStar(int id,int uid);
    public int StarActv(int id,int uid,boolean star);
}
