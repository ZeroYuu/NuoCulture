package com.nuoculture.spring_part.Model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuoculture.spring_part.Model.entity.Model;

public interface ModelService extends IService<Model> {
    public String getAccount(int id);

}
