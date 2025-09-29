package com.nuoculture.spring_part.BackEnd.User.Address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;

public interface AddressService extends IService<UserAddress> {
    public String getAccount(int usr_id);
    public String getAddrText(int usr_id);

}
