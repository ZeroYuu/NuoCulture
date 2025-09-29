package com.nuoculture.spring_part.FrontEnd.Goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsColorInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;

import java.util.List;

public interface FrontGoodsService extends IService<GoodsInfo> {
    public List<String> getImages(int id);
    public List<GoodsInfo>  getDetailApi(int id);
    public String  getImgurl(int id);
    public String  getColor(int id);
    public List<GoodsReviewInfo>  getReview(int id);
    public List<UserInfo> getAccount(int id);
    public List<GoodsTypeInfo> getGoodsTypeName(int id);

    public IPage<GoodsInfo> getGoods(IPage<GoodsInfo> page,String content);
    public IPage<GoodsInfo> getTypeGoods(IPage<GoodsInfo> page,String id,String content);
    public List<GoodsTypeInfo> getType();
    public List<GoodsInfo> getRecmdList();

    public List<CartInfo> getCartList(int id,String content);

    public List<UserStarInfo> getStarGoods(int id);
    public Boolean getStar(int id,int uid);
    public int StarGoods(int id,int uid,boolean star);
    public int StarCart(int id,int uid,int cid);

    public List<GoodsColorInfo> getColorList(int id);



}
