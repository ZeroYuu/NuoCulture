package com.nuoculture.spring_part.BackEnd.Culture.CarouseL.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity.homepageImageInfo;

public interface CarouselService extends IService<homepageImageInfo> {
    public String getAccount(int usr_id);
    public String getAddrText(int usr_id);

}
