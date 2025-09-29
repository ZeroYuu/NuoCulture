package com.nuoculture.spring_part.FrontEnd.Culture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Culture.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;

import java.util.List;

public interface FrontCultureService extends IService<CultureInfo> {
    public List<CultureInfo> getDetail(int id);
    public List<String> getImages();
    public List<CultureInfo> getCulture();
    public List<CultureInfo> getPartCulture(String id);
    public List<CultureTypeInfo> getType();


    public List<UserStarInfo> getStarCul(int id);
    public Boolean getStar(int id,int uid);
    public int StarCul(int id,int uid,boolean star);
}
