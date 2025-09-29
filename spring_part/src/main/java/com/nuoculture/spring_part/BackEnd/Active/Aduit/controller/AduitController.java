package com.nuoculture.spring_part.BackEnd.Active.Aduit.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.JoinParam;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.service.JoinService;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.service.PublishService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.service.UserService;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/adu")
public class AduitController {

    @Autowired
    JoinService joinService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody ActiveJoinInfo activeJoinInfo) {
        System.out.println(activeJoinInfo);
        boolean temp = false;
        QueryWrapper<ActiveJoinInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actv_id", activeJoinInfo.getActv_id())
                .eq("usr_id", activeJoinInfo.getUsr_id())
                .ne("join_status", 2).ne("join_status",-1);
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
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
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

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody ActiveJoinInfo activeJoinInfo) {
        System.out.println("useEdit");
        System.out.println(activeJoinInfo);
        boolean temp = joinService.updateById(activeJoinInfo);

        if (activeJoinInfo.getJoin_status() == 0) {
            return ResultVo.error("请进行审核");
        } else if (temp) {
            return ResultVo.success("审核成功");
        }
        return ResultVo.error("审核失败");
    }

    //上传
    @PostMapping("/upload")
    public ResultVo uploadAvatar(@RequestParam(value = "file", required = false) MultipartFile[] file) {
        boolean tags;
        List<String> preservePath = new ArrayList<>();
        if (file == null) {
            return ResultVo.success();
        }
        try {
            System.out.println("file:" + file);
            // 1. 确保目录存在
            File avatarDir = new File("img/active");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
//            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            for (MultipartFile f : file) {
                String fileName = System.currentTimeMillis() + "_" + f.getOriginalFilename();
                String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
                preservePath.add(staticPath + "img/active/" + fileName);
                f.transferTo(new File(filePath));
            }

            System.out.println(preservePath);
            // 3. 保存文件

            tags = true;
        } catch (IOException e) {
            tags = false;
        }
        if (tags) {
            return ResultVo.success("上传成功", preservePath);
        } else {
            return ResultVo.error("上传失败: ");
        }
    }

    //富文本上传
    @PostMapping("/uploadRichText")
    public Map<String, Object> uploadRichText(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        boolean tags;
        String preservePath = "";
        Map<String, Object> map = new HashMap<>();

        System.out.println("file:" + file);
        // 1. 确保目录存在
        File avatarDir = new File("img/active");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();

        }
//        System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
        // 2. 获取安全的文件名（避免覆盖或路径攻击）
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
        preservePath = staticPath + "img/active/" + fileName;
        System.out.println(preservePath);
        // 3. 保存文件
        file.transferTo(new File(filePath));

        map.put("errno", 0);
        map.put("data", CollUtil.newArrayList(Dict.create().set("url", preservePath)));


        return map;
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(JoinParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<ActiveJoinInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<ActiveJoinInfo> wrapper = new QueryWrapper<>();

        //活动名称
        if (StringUtils.isNotEmpty(param.getActv_id())) {
            wrapper.lambda().like(ActiveJoinInfo::getActv_id, param.getActv_id());
        }

        //参与人账号
        if (StringUtils.isNotEmpty(param.getUsr_account())) {
            String[] usr = joinService.getUsrIdFromAccount(param.getUsr_account());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i] + ' ');
            }
            wrapper.lambda().in(ActiveJoinInfo::getUsr_id, usr);
        }
        //参与人姓名
        if (StringUtils.isNotEmpty(param.getUsr_name())) {
            String[] usr = joinService.getUsrIdFromName(param.getUsr_name());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i] + ' ');
            }
            wrapper.lambda().in(ActiveJoinInfo::getUsr_id, usr);
        }
        //参加原因
        if (StringUtils.isNotEmpty(param.getJoin_reason())) {
            wrapper.lambda().like(ActiveJoinInfo::getJoin_reason, param.getJoin_reason());
        }
        //参加备注
        if (StringUtils.isNotEmpty(param.getJoin_remark())) {
            wrapper.lambda().like(ActiveJoinInfo::getJoin_remark, param.getJoin_remark());
        }
        //参加状态
        if (StringUtils.isNotEmpty(param.getJoin_status())) {
            wrapper.lambda().eq(ActiveJoinInfo::getJoin_status, param.getJoin_status());
        }


        IPage<ActiveJoinInfo> pages = joinService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getImages(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = joinService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/getAccount/{usr_id}")
    public ResultVo getAccount(@PathVariable("usr_id") int usr_id) {
        List<UserInfo> account = joinService.getAccount(usr_id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    //获取活动信息
    @GetMapping("/getActv/{id}")
    public ResultVo getActv(@PathVariable("id") int id) {
//        System.out.println(id);
        List<ActiveInfo> list = joinService.getActv(id);
        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取用户下拉数据
    @Autowired
    PublishService publishService;

    @GetMapping("/actvSelectList")
    public ResultVo actvSelectList() {
        List<ActiveInfo> list = publishService.list();
        //封装返回值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item -> {
                    SelectItem vo = new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getActv_id());
                    vo.setLabel(item.getActv_name());
                    selectItems.add(vo);
                });
        System.out.println(selectItems);
        return ResultVo.success("查询成功", selectItems);

    }

    @Autowired
    UserService userService;

    @GetMapping("/usrSelectList")
    public ResultVo usrSelectList() {
        List<UserInfo> list = userService.list();
        //封装返回值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item -> {
                    SelectItem vo = new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getUsr_id());
                    vo.setLabel(item.getUsr_account());
                    selectItems.add(vo);
                });
        System.out.println(selectItems);
        return ResultVo.success("查询成功", selectItems);

    }

}
