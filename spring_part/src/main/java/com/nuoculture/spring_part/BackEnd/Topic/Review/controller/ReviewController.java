package com.nuoculture.spring_part.BackEnd.Topic.Review.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.service.ContentService;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Topic.Review.service.ReviewService;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.ReviewParam;
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
@RequestMapping("/api/rev")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody TopicReviewInfo topicReviewInfo) {
        System.out.println(topicReviewInfo);
        boolean temp = reviewService.save(topicReviewInfo);
        if (temp) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id") int id) {
        boolean temp = reviewService.removeById(id);
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
            temp = reviewService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody TopicReviewInfo userAddress) {
        System.out.println("useEdit");
        System.out.println(userAddress);
        boolean temp = reviewService.updateById(userAddress);
        if (temp) {
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
    public ResultVo getList(ReviewParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<TopicReviewInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<TopicReviewInfo> wrapper = new QueryWrapper<>();

        //话题名称
        if (StringUtils.isNotEmpty(param.getTopic_id())) {
            wrapper.lambda().like(TopicReviewInfo::getTopic_id, param.getTopic_id());
        }
        //评论人账号
        if (StringUtils.isNotEmpty(param.getUsr_account())) {
            String[] usr=reviewService.getUsrIdFromAccount(param.getUsr_account());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i]+' ');
            }
            wrapper.lambda().in(TopicReviewInfo::getUsr_id, usr);
        }
        //评论人姓名
        if (StringUtils.isNotEmpty(param.getUsr_name())) {
            String[] usr=reviewService.getUsrIdFromName(param.getUsr_name());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i]+' ');
            }
            wrapper.lambda().in(TopicReviewInfo::getUsr_id, usr);
        }
//开始时间查询
        if (StringUtils.isNotEmpty(param.getTopic_review_time())) {
            String[] Date = param.getTopic_review_time().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(TopicReviewInfo::getTopic_review_time, Date[0], Date[1]);
        }


        IPage<TopicReviewInfo> pages = reviewService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getUsr(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = reviewService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取活动信息
    @GetMapping("/getTopic/{id}")
    public ResultVo getTopic(@PathVariable("id") int id) {
//        System.out.println(id);
        List<TopicInfo> list = reviewService.getTopic(id);
        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取用户下拉数据
    @Autowired
    ContentService contentService;

    @GetMapping("/selectList")
    public ResultVo selectList() {
        List<TopicInfo> list = contentService.list();
        //封装返回值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item -> {
                    SelectItem vo = new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getTopic_id());
                    vo.setLabel(item.getTopic_titile());
                    selectItems.add(vo);
                });
        System.out.println(selectItems);
        return ResultVo.success("查询成功", selectItems);

    }

}
