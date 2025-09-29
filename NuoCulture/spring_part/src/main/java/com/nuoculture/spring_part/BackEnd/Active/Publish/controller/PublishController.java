package com.nuoculture.spring_part.BackEnd.Active.Publish.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.PublishParam;
import com.nuoculture.spring_part.BackEnd.Active.Publish.service.PublishService;
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
@RequestMapping("/api/pub")
public class PublishController {

    @Autowired
    PublishService publishService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody ActiveInfo activeInfo) {
        System.out.println(activeInfo);
        boolean temp = publishService.save(activeInfo);

        int flag = 0;
        int id = publishService.getId();
//        System.out.println(id);
        for (int i = 0; i < activeInfo.getActv_img().length; i++) {
            flag = publishService.setImg(id, activeInfo.getActv_img()[i]);
        }

        if (temp && flag == 1) {
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
        boolean temp = publishService.removeById(id);
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
            temp = publishService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody ActiveInfo activeInfo) {
        System.out.println("useEdit");
        System.out.println(activeInfo);
        boolean temp = publishService.updateById(activeInfo);
        int flag = 0;
        if (activeInfo.getActv_img() != null) {
            int id = activeInfo.getActv_id();
            for (int i = 0; i < activeInfo.getActv_img().length; i++) {
                flag = publishService.setImg(id, activeInfo.getActv_img()[i]);
            }
        }else {
            flag = 1;
        }

        if (temp && flag == 1) {
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
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
    public ResultVo getList(PublishParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<ActiveInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<ActiveInfo> wrapper = new QueryWrapper<>();

        //负责人姓名模糊查询
        if (StringUtils.isNotEmpty(param.getActv_leader())) {
            wrapper.lambda().like(ActiveInfo::getActv_leader, param.getActv_leader());
        }
        //负责人电话模糊查询
        if (StringUtils.isNotEmpty(param.getActv_phone())) {
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(ActiveInfo::getActv_phone, param.getActv_phone());
        }
        //活动名称模糊查询
        if (StringUtils.isNotEmpty(param.getActv_name())) {

            wrapper.lambda().like(ActiveInfo::getActv_name, param.getActv_name());
        }
        //活动地址模糊查询
        if (StringUtils.isNotEmpty(param.getActv_region())) {

            wrapper.lambda().and(i -> i.like(ActiveInfo::getActv_region, param.getActv_region()).or()
                    .like(ActiveInfo::getActv_addr, param.getActv_region()));

        }
        //开始时间查询
        if (StringUtils.isNotEmpty(param.getStart_time())) {
            String[] Date = param.getStart_time().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(ActiveInfo::getStart_time, Date[0], Date[1]);
        }

        //结束时间查询
        if (StringUtils.isNotEmpty(param.getEnd_time())) {
            String[] Date = param.getEnd_time().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(ActiveInfo::getEnd_time, Date[0], Date[1]);
        }


        IPage<ActiveInfo> pages = publishService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getUsr(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = publishService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    @DeleteMapping("/delImg/{url}")
    public ResultVo delImg(@PathVariable("url") String url) {
        System.out.println(url);
        boolean list = publishService.deleteImg(url);
        boolean avatar = publishService.deleteAvatar(url);
//        System.out.println(list);
        if (list) {
            return ResultVo.success("", list);
        }
        return ResultVo.success("", list);
    }


}
