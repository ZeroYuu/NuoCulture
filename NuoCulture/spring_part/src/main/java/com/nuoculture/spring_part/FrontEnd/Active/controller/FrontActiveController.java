package com.nuoculture.spring_part.FrontEnd.Active.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.service.JoinService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.ActiveMomentInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.CultureTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Active.entity.PageParam;
import com.nuoculture.spring_part.FrontEnd.Active.service.FrontActiveService;
import com.nuoculture.spring_part.FrontEnd.Home.entity.HomeActiveInfo;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
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
@RequestMapping("/api/frontactv")
public class FrontActiveController {

    @Autowired
    FrontActiveService frontActiveService;

    @Value("${file.staticPath}")
    private String staticPath;

    @Autowired
    JoinService joinService;

    @PostMapping
    public ResultVo add(@RequestBody ActiveJoinInfo activeJoinInfo) {
        System.out.println(activeJoinInfo);
        boolean temp = false;
        QueryWrapper<ActiveJoinInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actv_id", activeJoinInfo.getActv_id())
                .eq("usr_id", activeJoinInfo.getUsr_id())
                .ne("join_status", 2).ne("join_status", -1);
        List<ActiveJoinInfo> activeJoinInfos = joinService.list(queryWrapper);
        if (activeJoinInfos.size() > 0) {
            return ResultVo.error("已经参加过活动");
        } else {
            temp = joinService.save(activeJoinInfo);
        }

        if (temp) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id") int id) {
        boolean temp = joinService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除
    @DeleteMapping("/allDel/{id}")
    public ResultVo allDelete(@PathVariable("id") int[] Id) {
        boolean temp = false;
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = joinService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }


    //取消报名
    @PutMapping("/cancel/{id}")
    public ResultVo cancel(@PathVariable("id") int id) {
        UpdateWrapper<ActiveJoinInfo> updateWrapper = Wrappers.update();
        updateWrapper.eq("join_id", id);
        updateWrapper.set("join_status", "2");
        boolean temp = joinService.update(updateWrapper);
        if (temp) {
            return ResultVo.success("取消成功");
        }
        return ResultVo.error("取消失败");
    }

    @GetMapping("/StarActv/{id}/{uid}/{star}")
    public ResultVo StarCul(@PathVariable("id") int id, @PathVariable("uid") int uid,
                            @PathVariable("star") boolean star) {
        System.out.println(id + " " + uid);
        int temp = frontActiveService.StarActv(id, uid, star);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {
            return ResultVo.success("收藏成功");
        } else if (temp == 2) {
            return ResultVo.success("取消收藏");
        }
        return ResultVo.error("收藏失败");
    }


    @GetMapping("/getStarActv/{id}")
    public ResultVo getStarActv(@PathVariable("id") int id) {
        List<UserStarInfo> account = frontActiveService.getStarActv(id);
        if (account.isEmpty()){
            return ResultVo.success("", account);
        }
        System.out.println(account);

        Set<Integer> actvIds = account.stream()
                .map(UserStarInfo::getActv_id)
                .collect(Collectors.toSet());

        // 使用actvIds在ActvInfo表中查询相关信息
        List<ActiveInfo> infos = frontActiveService.listByIds(actvIds);

        IntStream.range(0, infos.size()) // 假设infos和account列表长度相同
                .forEach(i -> {
                    ActiveInfo info = infos.get(i); // 获取infos列表中的元素
                    UserStarInfo userStarInfo = account.get(i); // 获取account列表中的对应元素
                    info.setStar_id(userStarInfo.getStar_heart_id()); // 设置star_id
                });

        for (Integer actvId : actvIds) {
            String url = frontActiveService.getImgurl(actvId);
            for (ActiveInfo activeInfo : infos) {
                if (activeInfo.getActv_id() == (actvId)) {
                    activeInfo.setActv_img_url(url);
                    break;  // 找到匹配的goodsInfo后跳出内层循环
                }
            }
        }


        if (account != null) {
            return ResultVo.success("", infos);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getStar/{id}/{uid}")
    public ResultVo getStar(@PathVariable("id") int id, @PathVariable("uid") int uid) {
        Boolean account = frontActiveService.getStar(id, uid);
        System.out.println(account);

        return ResultVo.success("", account);

    }

    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(PageParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<ActiveInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        QueryWrapper<ActiveInfo> wrapper = new QueryWrapper<>();


        //文化标题模糊查询
//        if (StringUtils.isNotEmpty(param.getActv_content())) {
//            wrapper.lambda().and(i -> i.like(ActiveInfo::getActv_content, param.getActv_content())
//                    .or().like(ActiveInfo::getActv_name, param.getActv_content()));
//        }

//        IPage<ActiveInfo> pages=frontActiveService.page(page,wrapper);
        IPage<ActiveInfo> pages = frontActiveService.getActive(page, param.getActv_content());
        System.out.println(pages);
        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/getImages/{id}")
    public ResultVo getImages(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = frontActiveService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }


    //获取账号名称
    @GetMapping("/getDetail/{id}")
    public ResultVo getDetail(@PathVariable("id") int id) {
        List<ActiveInfo> detail = frontActiveService.getDetail(id);
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getMoment/{id}")
    public ResultVo getMoment(@PathVariable("id") int id) {
        List<ActiveMomentInfo> detail = frontActiveService.getMoment(id);
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }


}
