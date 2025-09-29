package com.nuoculture.spring_part.FrontEnd.Culture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Culture.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Culture.mapper.FrontCultureMapper;
import com.nuoculture.spring_part.FrontEnd.Culture.service.FrontCultureService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontCultureServiceImpl extends ServiceImpl<FrontCultureMapper, CultureInfo> implements FrontCultureService {

    @Autowired
    FrontCultureMapper frontCultureMapper;

    public List<CultureInfo> getDetail(int id) {
        return frontCultureMapper.getDetail(id);
    }

    ;

    public List<String> getImages() {
        return frontCultureMapper.getImages();
    }

    ;



    public List<CultureTypeInfo> getType() {
        return frontCultureMapper.getType();
    }

    ;

    public List<CultureInfo> getCulture() {
        return frontCultureMapper.getCulture();
    }

    ;

    public List<CultureInfo> getPartCulture(String id) {
        return frontCultureMapper.getPartCulture(id);
    }

    ;

    @Autowired
    UserStarMapper userStarMapper;

    public List<UserStarInfo> getStarCul(int id) {
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usr_id", id).ne("cul_id", 0).eq("star",1);
        return  userStarMapper.selectList(queryWrapper);

    }


    public Boolean getStar(int id, int uid) {
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("star").eq("cul_id", id).eq("usr_id", uid);
        UserStarInfo userStarInfo = userStarMapper.selectOne(queryWrapper);
        return userStarInfo != null ? userStarInfo.getStar() : false;
    }


    public int StarCul(int id, int uid, boolean star) {
        System.out.println(star);
        if (star) {
            List<UserStarInfo> detail = userStarMapper.select(id, uid);

            System.out.println(detail);
//            System.out.println(q);
            if (detail.isEmpty()) { // 如果查询结果为空（即没有记录），则插入新记录
                UserStarInfo newStarInfo = new UserStarInfo();
                newStarInfo.setCul_id(id);
                newStarInfo.setUsr_id(uid);
                newStarInfo.setStar(star);
                newStarInfo.setIs_delete(0); // 假设is_delete默认为0

                userStarMapper.insert(newStarInfo);
            } else {

                UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("cul_id", id).eq("usr_id", uid);
                updateWrapper.set("star", 1);
                userStarMapper.update(null, updateWrapper);
            }
            return 1;
        } else {
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("cul_id", id).eq("usr_id", uid);
            updateWrapper.set("star", 0);
            userStarMapper.update(null, updateWrapper);

            return 2;
        }

    }


}
