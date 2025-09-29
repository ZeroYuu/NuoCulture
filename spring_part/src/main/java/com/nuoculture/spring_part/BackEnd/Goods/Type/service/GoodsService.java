package com.nuoculture.spring_part.BackEnd.Goods.Type.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.GoodsTypeInfo;

public interface GoodsService extends IService<GoodsTypeInfo> {
    public String getAccount(int usr_id);
    public String getAddrText(int usr_id);

}
