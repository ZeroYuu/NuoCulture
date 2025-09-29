package com.nuoculture.spring_part.FrontEnd.Culture.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.PublishParam;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.MessageParam;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.FrontEnd.Culture.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Culture.entity.PageParam;
import com.nuoculture.spring_part.FrontEnd.Culture.service.FrontCultureService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.jwt.JwtInterceptor;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/frontcult")
public class FrontCultureController {

    @Autowired
    FrontCultureService frontCultureService;

    @Value("${file.staticPath}")
    private String staticPath;


    @GetMapping("/StarCul/{id}/{uid}/{star}")
    public ResultVo StarCul(@PathVariable("id") int id, @PathVariable("uid") int uid,
                            @PathVariable("star") boolean star) {
        System.out.println(id + " " + uid);
        int temp = frontCultureService.StarCul(id, uid, star);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {
            return ResultVo.success("收藏成功");
        } else if (temp == 2) {
            return ResultVo.success("取消收藏");
        }
        return ResultVo.error("收藏失败");
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(PageParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<CultureInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());

        QueryWrapper<CultureInfo> wrapper = new QueryWrapper<>();


        //文化标题模糊查询
        if (StringUtils.isNotEmpty(param.getCul_shelves())) {
            wrapper.lambda().eq(CultureInfo::isCul_shelves, param.getCul_shelves());
        }

        if (StringUtils.isNotEmpty(param.getCul_type_id())) {
            wrapper.lambda().eq(CultureInfo::getCul_type_id, param.getCul_type_id());
        }

        if (StringUtils.isNotEmpty(param.getCul_content())) {
            wrapper.lambda().and(i -> i.like(CultureInfo::getCul_content, param.getCul_content())
                    .or().like(CultureInfo::getCul_title, param.getCul_content()));
        }

        IPage<CultureInfo> pages = frontCultureService.page(page, wrapper);
        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getStarCul/{id}")
    public ResultVo getStarCul(@PathVariable("id") int id) {
        List<UserStarInfo> account = frontCultureService.getStarCul(id);
        if (account.isEmpty()){
            return ResultVo.success("", account);
        }
        System.out.println(account);

        Set<Integer> CulIds = account.stream()
                .map(UserStarInfo::getCul_id)
                .collect(Collectors.toSet());

        // 使用actvIds在ActvInfo表中查询相关信息
        List<CultureInfo> infos = frontCultureService.listByIds(CulIds);

        IntStream.range(0, infos.size()) // 假设infos和account列表长度相同
                .forEach(i -> {
                    CultureInfo info = infos.get(i); // 获取infos列表中的元素
                    UserStarInfo userStarInfo = account.get(i); // 获取account列表中的对应元素
                    info.setStar_id(userStarInfo.getStar_heart_id()); // 设置star_id
                });


        return ResultVo.success("", infos);

    }

    @GetMapping("/getStar/{id}/{uid}")
    public ResultVo getStar(@PathVariable("id") int id, @PathVariable("uid") int uid) {
        Boolean account = frontCultureService.getStar(id, uid);
        System.out.println(account);

        return ResultVo.success("", account);

    }


    @GetMapping("/getImages")
    public ResultVo getImages() {
//        System.out.println(id);
        List<String> list = frontCultureService.getImages();
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }


    //获取活动信息
    @GetMapping("/getCultureType")
    public ResultVo getCultureType() {
        List<CultureTypeInfo> type = frontCultureService.getType();
        System.out.println(type);
        if (type != null) {
            return ResultVo.success("", type);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getDetail/{id}")
    public ResultVo getDetail(@PathVariable("id") int id) {
        List<CultureInfo> detail = frontCultureService.getDetail(id);
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }


}
