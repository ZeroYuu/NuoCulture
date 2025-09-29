package com.nuoculture.spring_part.BackEnd.Culture.Type.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Culture.Type.entity.CultureTypeInfo;

public interface CultureService extends IService<CultureTypeInfo> {
    public String getAccount(int usr_id);
    public String getAddrText(int usr_id);

}
