package com.nuoculture.spring_part.BackEnd.Topic.Content.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.service.JoinService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.service.ContentService;
import com.nuoculture.spring_part.BackEnd.Topic.Type.service.TopicService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.ContentParam;
import com.nuoculture.spring_part.BackEnd.Topic.Type.entity.TopicTypeInfo;
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
@RequestMapping("/api/cont")
public class ContentController {

    @Autowired
    ContentService contentService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody TopicInfo topicInfo) {
        System.out.println(topicInfo);
        topicInfo.setTopic_view(0);
        topicInfo.setTopic_time(new Date());
        boolean temp = contentService.save(topicInfo);

        int flag = 0;
        int id = contentService.getId();
//        System.out.println(id);
        for (int i = 0; i < topicInfo.getImg_path().length; i++) {
            flag = contentService.setImg(id, topicInfo.getImg_path()[i]);
        }


        if (temp && flag == 1) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id") int id) {
        boolean temp = contentService.removeById(id);
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
            temp = contentService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody TopicInfo topicInfo) {
        System.out.println("useEdit");
        System.out.println(topicInfo);
        boolean temp = contentService.updateById(topicInfo);
        //修改图片
        int flag = 0;
        if (topicInfo.getImg_path()!=null){
            int id = topicInfo.getTopic_id();
//        System.out.println(id);
            for (int i = 0; i < topicInfo.getImg_path().length; i++) {
                flag = contentService.setImg(id, topicInfo.getImg_path()[i]);
            }
        }else {
            flag = 1;
        }
        //修改审核状态
        if (topicInfo.getTopic_status()==0) {
            return ResultVo.error("请进行审核");
        }else if(temp && flag == 1){
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
            File avatarDir = new File("img/topic");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
//            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            for (MultipartFile f : file) {
                String fileName = System.currentTimeMillis() + "_" + f.getOriginalFilename();
                String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
                preservePath.add(staticPath + "img/topic/" + fileName);
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
        File avatarDir = new File("img/topic");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();

        }
//        System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
        // 2. 获取安全的文件名（避免覆盖或路径攻击）
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
        preservePath = staticPath + "img/topic/" + fileName;
        System.out.println(preservePath);
        // 3. 保存文件
        file.transferTo(new File(filePath));

        map.put("errno", 0);
        map.put("data", CollUtil.newArrayList(Dict.create().set("url", preservePath)));


        return map;
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(ContentParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<TopicInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<TopicInfo> wrapper = new QueryWrapper<>();

        //标题模糊查询
        if (StringUtils.isNotEmpty(param.getTopic_titile())) {
            wrapper.lambda().like(TopicInfo::getTopic_titile, param.getTopic_titile());
        }
        //活动类型查询
        if (StringUtils.isNotEmpty(param.getTopic_type_id())) {
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(TopicInfo::getTopic_type_id, param.getTopic_type_id());
        }
        //审核状态查询
        if (StringUtils.isNotEmpty(param.getTopic_status())) {

            wrapper.lambda().eq(TopicInfo::getTopic_status, param.getTopic_status());
        }
        //备注查询
        if (StringUtils.isNotEmpty(param.getTopic_remark())) {
            wrapper.lambda().like(TopicInfo::getTopic_remark, param.getTopic_remark());
        }
        //参与人账号
        if (StringUtils.isNotEmpty(param.getUsr_account())) {
            String[] usr=contentService.getUsrIdFromAccount(param.getUsr_account());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i]+' ');
            }
            wrapper.lambda().in(TopicInfo::getUsr_id, usr);
        }
        //参与人姓名
        if (StringUtils.isNotEmpty(param.getUsr_name())) {
            String[] usr=contentService.getUsrIdFromName(param.getUsr_name());
            for (int i = 0; i < usr.length; i++) {
                System.out.print(usr[i]+' ');
            }
            wrapper.lambda().in(TopicInfo::getUsr_id, usr);
        }
        //浏览量查询
        if (StringUtils.isNotEmpty(param.getTopic_view())) {
            String[] Date = param.getTopic_view().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(TopicInfo::getTopic_view, Date[0], Date[1]);
        }

        IPage<TopicInfo> pages = contentService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getUsr(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = contentService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getTypeName/{id}")
    public ResultVo getTypeName(@PathVariable("id")int id){
        String account = contentService.getTypeName(id);
        System.out.println(account);
        if (account!=null){
            return ResultVo.success(account);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账户信息
    @Autowired
    JoinService joinService;
    @GetMapping("/getAccount/{usr_id}")
    public ResultVo getAccount(@PathVariable("usr_id")int usr_id){
        List<UserInfo> account = joinService.getAccount(usr_id);
        System.out.println(account);
        if (account!=null){
            return ResultVo.success("",account);
        }
        return ResultVo.error("查询失败!");
    }


    //获取类型下拉数据
    @Autowired
    TopicService topicService;
    @GetMapping("/selectTypeList")
    public ResultVo selectTypeList(){
        List<TopicTypeInfo> list = topicService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getTopic_type_id());
                    vo.setLabel(item.getTopic_type_name());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }

    //获取标题下拉
    @GetMapping("/selecTitletList")
    public ResultVo selecTitletList(){
        List<TopicInfo> list = contentService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getTopic_id());
                    vo.setLabel(item.getTopic_titile());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }

    //获取用户下拉数据
    @Autowired
    UserService userService;
    @GetMapping("/selectUsrList")
    public ResultVo selectUsrList() {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getIs_admin, "1");
        List<UserInfo> list = userService.list(queryWrapper);
//        System.out.println(list);
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
