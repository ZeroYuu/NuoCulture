package com.nuoculture.spring_part.FrontEnd.Home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeGoodsInfo;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeTopicInfo;
import com.nuoculture.spring_part.FrontEnd.Home.service.HomeService;
import com.nuoculture.spring_part.jwt.JwtInterceptor;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/fronthome")
public class HomeController {

    @Autowired
    HomeService homeService;

    @Value("${file.staticPath}")
    private String staticPath;



    @GetMapping("/getImages")
    public ResultVo getImages() {
//        System.out.println(id);
        List<String> list = homeService.getImages();
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获得资讯信息
    @GetMapping("/getCulture")
    public ResultVo getCulture() {
//        System.out.println(id);
        List<CultureInfo> list = homeService.getCulture();
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取活动信息
    @GetMapping("/getActive")
    public ResultVo getActive() {
//        System.out.println(id);
        List<ActiveInfo> list = homeService.getActive();
        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }
    //获取话题信息
    @GetMapping("/getTopic")
    public ResultVo getTopic() {
//        System.out.println(id);
        List<TopicInfo> list = homeService.getTopic();
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }
    // 获得商品信息
    @GetMapping("/getGoods")
    public ResultVo getGoods() {
//        System.out.println(id);
        List<GoodsInfo> list = homeService.getGoods();
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getUsr/{id}")
    public ResultVo getUsr(@PathVariable("id") int id) {
        String account = homeService.getAccount(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("",account);
        }
        return ResultVo.error("查询失败!");
    }


}
