package com.nuoculture.spring_part.BackEnd.Culture.Message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;

public interface MessageService extends IService<CultureInfo> {
    public String getTypeName(int id);
    public String getPath(int id);

}
