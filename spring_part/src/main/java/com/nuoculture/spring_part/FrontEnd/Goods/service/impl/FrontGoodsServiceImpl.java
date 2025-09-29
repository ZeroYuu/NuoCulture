package com.nuoculture.spring_part.FrontEnd.Goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsColorInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.mapper.FrontCartMapper;
import com.nuoculture.spring_part.FrontEnd.Goods.mapper.FrontGoodsMapper;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontGoodsService;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.mapper.UserStarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrontGoodsServiceImpl extends ServiceImpl<FrontGoodsMapper, GoodsInfo> implements FrontGoodsService {

    @Autowired
    FrontGoodsMapper frontGoodsMapper;

    public List<GoodsInfo> getDetailApi(int id) {
        return frontGoodsMapper.getDetailApi(id);
    }

    ;

    public String getImgurl(int id) {
        return frontGoodsMapper.getImgurl(id);
    }

    ;

    public String getColor(int id) {
        return frontGoodsMapper.getColor(id);
    }

    ;

    public List<UserInfo> getAccount(int id) {
        return frontGoodsMapper.getAccount(id);
    }

    ;

    public List<GoodsReviewInfo> getReview(int id) {
        return frontGoodsMapper.getReview(id);
    }

    ;

    public List<String> getImages(int id) {
        return frontGoodsMapper.getImages(id);
    }

    ;

    public List<GoodsTypeInfo> getGoodsTypeName(int id) {
        return frontGoodsMapper.getGoodsTypeName(id);
    }

    ;


    public List<GoodsInfo> getRecmdList() {
        return frontGoodsMapper.getRecmdList();
    }

    ;


    public List<GoodsTypeInfo> getType() {
        return frontGoodsMapper.getType();
    }

    ;

    public IPage<GoodsInfo> getGoods(IPage<GoodsInfo> page, String content) {
        return frontGoodsMapper.getGoods(page, content);
    }

    ;

    public IPage<GoodsInfo> getTypeGoods(IPage<GoodsInfo> page, String id, String content) {
        return frontGoodsMapper.getTypeGoods(page, id, content);
    }

    ;


    @Autowired
    FrontCartMapper frontCartMapper;

    public List<CartInfo> getCartList(int id, String content) {


        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.like("goods_name", content);//相当于where id=1

        List<GoodsInfo> list1 = frontGoodsMapper.selectObjs(wrapper1);
        System.out.println(list1);

        if (list1.isEmpty()) {
            return new ArrayList<>(); // 如果没有找到商品，返回空列表
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("usr_id", id);
        wrapper.eq("cart_status", 0);
        wrapper.in("goods_id", list1);

        List<CartInfo> list = frontCartMapper.selectList(wrapper);
        return list;
    }

    ;

    @Autowired
    UserStarMapper userStarMapper;

    public List<UserStarInfo> getStarGoods(int id) {
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usr_id", id).ne("goods_id", 0).eq("star",1);
        return userStarMapper.selectList(queryWrapper);
    }


    public Boolean getStar(int id, int uid) {
        QueryWrapper<UserStarInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("star").eq("goods_id", id).eq("usr_id", uid);
        UserStarInfo userStarInfo = userStarMapper.selectOne(queryWrapper);
        return userStarInfo != null ? userStarInfo.getStar() : false;
    }


    public int StarGoods(int id, int uid, boolean star) {
        System.out.println(star);
        if (star) {
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_id", id).eq("usr_id", uid);
            List<UserStarInfo> detail = userStarMapper.selectList(updateWrapper);

            System.out.println(detail);
//            System.out.println(q);
            if (detail.isEmpty()) { // 如果查询结果为空（即没有记录），则插入新记录
                UserStarInfo newStarInfo = new UserStarInfo();
                newStarInfo.setGoods_id(id);
                newStarInfo.setUsr_id(uid);
                newStarInfo.setStar(star);
                newStarInfo.setIs_delete(0); // 假设is_delete默认为0

                userStarMapper.insert(newStarInfo);
            } else {

                updateWrapper.set("star", 1);
                userStarMapper.update(null, updateWrapper);
            }
            return 1;
        } else {
            UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_id", id).eq("usr_id", uid);
            updateWrapper.set("star", 0);
            userStarMapper.update(null, updateWrapper);

            return 2;
        }

    }


    public int StarCart(int id, int uid,int cid) {

        UpdateWrapper<UserStarInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("goods_id", id).eq("usr_id", uid);
        List<UserStarInfo> detail = userStarMapper.selectList(updateWrapper);

//        System.out.println(detail);
//            System.out.println(q);
        if (detail.isEmpty()) { // 如果查询结果为空（即没有记录），则插入新记录
            UserStarInfo newStarInfo = new UserStarInfo();
            newStarInfo.setGoods_id(id);
            newStarInfo.setUsr_id(uid);
            newStarInfo.setStar(true);
            newStarInfo.setIs_delete(0); // 假设is_delete默认为0

            userStarMapper.insert(newStarInfo);
        } else {

            updateWrapper.set("star", 1);
            userStarMapper.update(null, updateWrapper);
        }

        UpdateWrapper<CartInfo> updateCart = new UpdateWrapper<>();
        updateCart.eq("cart_id", cid).eq("usr_id", uid);
        updateCart.set("cart_status", 2);
        frontCartMapper.update(null, updateCart);
        return 1;


    }

    public List<GoodsColorInfo> getColorList(int id) {
        return frontGoodsMapper.getColorList(id);
    }

}
