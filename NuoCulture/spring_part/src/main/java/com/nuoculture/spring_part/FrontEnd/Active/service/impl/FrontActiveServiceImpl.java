package com.nuoculture.spring_part.FrontEnd.Active.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.FrontEnd.Active.entity.ActiveMomentInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Active.mapper.FrontActiveMapper;
import com.nuoculture.spring_part.FrontEnd.Active.service.FrontActiveService;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontActiveServiceImpl extends ServiceImpl<FrontActiveMapper, ActiveInfo> implements FrontActiveService {

    @Autowired
    FrontActiveMapper frontActiveMapper;
    public List<ActiveInfo> getDetail(int id) {
        return frontActiveMapper.getDetail(id);
    };
    public List<ActiveMomentInfo> getMoment(int id) {
        return frontActiveMapper.getMoment(id);
    };
    public List<String> getImages(int id) {
        return frontActiveMapper.getImages(id);
    };

    public String getImgurl(int id) {
        return frontActiveMapper.getImgurl(id);
    }



    public IPage<ActiveInfo> getActive(IPage<ActiveInfo> page,String content) {
        return frontActiveMapper.getActive(page,content);
    };

    @Autowired
    UserStarMapper userStarMapper;

    public List<UserStarInfo> getStarActv(int id){
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usr_id", id).ne("actv_id", 0).eq("star",1);
        return userStarMapper.selectList(queryWrapper);
    }


    public Boolean getStar(int id,int uid){
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("star").eq("actv_id", id).eq("usr_id", uid);
        UserStarInfo userStarInfo = userStarMapper.selectOne(queryWrapper);
        return userStarInfo != null ? userStarInfo.getStar() : false;
    }


    public int StarActv(int id,int uid,boolean star){
        System.out.println(star);
        if(star){
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("actv_id", id).eq("usr_id", uid);
            List<UserStarInfo> detail=userStarMapper.selectList(updateWrapper);

            System.out.println(detail);
//            System.out.println(q);
            if ( detail.isEmpty()) { // 如果查询结果为空（即没有记录），则插入新记录
                UserStarInfo newStarInfo = new UserStarInfo();
                newStarInfo.setActv_id(id);
                newStarInfo.setUsr_id(uid);
                newStarInfo.setStar(star);
                newStarInfo.setIs_delete(0); // 假设is_delete默认为0

                userStarMapper.insert(newStarInfo);
            } else {


                updateWrapper.set("star",1);
                userStarMapper.update(null, updateWrapper);
            }
            return 1;
        }else {
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("actv_id", id).eq("usr_id", uid);
            updateWrapper.set("star",0);
            userStarMapper.update(null, updateWrapper);

            return 2;
        }

    }



}
