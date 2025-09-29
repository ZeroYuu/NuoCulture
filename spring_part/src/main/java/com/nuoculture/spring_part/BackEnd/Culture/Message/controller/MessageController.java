package com.nuoculture.spring_part.BackEnd.Culture.Message.controller;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.MessageParam;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Culture.Message.service.MessageService;
import com.nuoculture.spring_part.BackEnd.Culture.Type.entity.CultureTypeInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Type.service.CultureService;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.collection.CollUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/msg")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Value("${file.staticPath}")
    private String staticPath;



    //增加用户

    @PostMapping
    public ResultVo add(@RequestBody CultureInfo cultureInfo){
        if (StringUtils.isEmpty(cultureInfo.getCul_cover())){
            int id=cultureInfo.getCul_type_id();
            String path=messageService.getPath(id);
            cultureInfo.setCul_cover(path);
            System.out.println(path);
        }
        System.out.println(cultureInfo);
        cultureInfo.setCul_time(new Date());
        boolean temp = messageService.save(cultureInfo);
        if(temp){
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id")int id){
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
        boolean temp = messageService.removeById(id);
        if(temp){
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
            temp = messageService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody CultureInfo cultureInfo){
        System.out.println("useEdit");
        System.out.println(cultureInfo);
        boolean temp = messageService.updateById(cultureInfo);
        if(temp){
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
    }

    //上传
    @PostMapping("/upload")
    public ResultVo uploadAvatar(@RequestParam(value = "file", required = false) MultipartFile file) {
        boolean tags;
        String preservePath="";
        if (file==null){
            return ResultVo.success();
        }
        try {
            System.out.println("file:" + file);
            // 1. 确保目录存在
            File avatarDir = new File("img/culture");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
            preservePath=staticPath+"img/culture/"+fileName;
            System.out.println(preservePath);
            // 3. 保存文件
            file.transferTo(new File(filePath));

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
    public Map<String, Object> uploadRichText(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
        boolean tags;
        String preservePath="";
        Map<String, Object> map =new HashMap<>();

            System.out.println("file:" + file);
            // 1. 确保目录存在
            File avatarDir = new File("img/culture");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
            preservePath=staticPath+"img/culture/"+fileName;
            System.out.println(preservePath);
            // 3. 保存文件
            file.transferTo(new File(filePath));

            map.put("errno",0);
            map.put("data", CollUtil.newArrayList(Dict.create().set("url",preservePath)));


        return map;
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(MessageParam param){
            System.out.println("param"+param);
        //构建分页对象
        IPage<CultureInfo> page=new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<CultureInfo> wrapper=new QueryWrapper<>();
        //文化标题模糊查询
        if(StringUtils.isNotEmpty(param.getCul_title())){
            wrapper.lambda().like(CultureInfo::getCul_title,param.getCul_title());
        }
        //文化类型id查询
        if(StringUtils.isNotEmpty(param.getCul_type_id())){
            wrapper.lambda().eq(CultureInfo::getCul_type_id,param.getCul_type_id());
        }
        //发布者查询
        if(StringUtils.isNotEmpty(param.getCul_pub())){
            wrapper.lambda().like(CultureInfo::getCul_pub,param.getCul_pub());
        }

        IPage<CultureInfo> pages = messageService.page(page, wrapper);

        if (pages!=null){
            return ResultVo.success("查询成功",pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getTypeName/{id}")
    public ResultVo getTypeName(@PathVariable("id")int id){
        String account = messageService.getTypeName(id);
        System.out.println(account);
        if (account!=null){
            return ResultVo.success(account);
        }
        return ResultVo.error("查询失败!");
    }



    //获取用户下拉数据
    @Autowired
    CultureService cultureService;
    @GetMapping("/selectTypeList")
    public ResultVo selectTypeList(){
        List<CultureTypeInfo> list = cultureService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getCul_type_id());
                    vo.setLabel(item.getCul_type_name());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }





}
