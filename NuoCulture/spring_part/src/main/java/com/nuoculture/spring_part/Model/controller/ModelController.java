package com.nuoculture.spring_part.Model.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.Model.entity.Model;
import com.nuoculture.spring_part.Model.entity.ModelParam;
import com.nuoculture.spring_part.Model.entity.SelectItem;
import com.nuoculture.spring_part.Model.service.ModelService;
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
@RequestMapping("/api/model")
public class ModelController {

    @Autowired
    ModelService modelService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody Model userAddress){
        System.out.println(userAddress);
        boolean temp = modelService.save(userAddress);
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
        boolean temp = modelService.removeById(id);
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
            temp = modelService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody Model userAddress){
        System.out.println("useEdit");
        System.out.println(userAddress);
        boolean temp = modelService.updateById(userAddress);
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
            File avatarDir = new File("img/avatars");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
            preservePath=staticPath+"img/avatars/"+fileName;
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
        File avatarDir = new File("img/msg");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();

        }
        System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
        // 2. 获取安全的文件名（避免覆盖或路径攻击）
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
        preservePath=staticPath+"img/msg/"+fileName;
        System.out.println(preservePath);
        // 3. 保存文件
        file.transferTo(new File(filePath));

        map.put("errno",0);
        map.put("data", CollUtil.newArrayList(Dict.create().set("url",preservePath)));


        return map;
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(ModelParam param){
            System.out.println("param"+param);
        //构建分页对象
        IPage<Model> page=new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<Model> wrapper=new QueryWrapper<>();
        //收货人姓名模糊查询
        if(StringUtils.isNotEmpty(param.getAddr_name())){
            wrapper.lambda().like(Model::getAddr_name,param.getAddr_name());
        }
        //收货人电话模糊查询
        if(StringUtils.isNotEmpty(param.getAddr_phone())){
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(Model::getAddr_phone,param.getAddr_phone());
        }
        //用户id查询
        if(StringUtils.isNotEmpty(param.getUsr_id())){
            wrapper.lambda().eq(Model::getUsr_id,param.getUsr_id());
        }
        //省份查询
        if(StringUtils.isNotEmpty(param.getAddr_province())){
            wrapper.lambda().like(Model::getAddr_province,param.getAddr_province());
        }
        //城市查询
        if(StringUtils.isNotEmpty(param.getAddr_city())){
            wrapper.lambda().like(Model::getAddr_city,param.getAddr_city());
        }
        //区查询
        if(StringUtils.isNotEmpty(param.getAddr_country())){
            wrapper.lambda().like(Model::getAddr_country,param.getAddr_country());
        }
        //详细地址查询
        if(StringUtils.isNotEmpty(param.getAddr_detail())){
            wrapper.lambda().like(Model::getAddr_detail,param.getAddr_detail());
        }

        
        IPage<Model> pages = modelService.page(page, wrapper);

        if (pages!=null){
            return ResultVo.success("查询成功",pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getUsr/{id}")
    public ResultVo getUsr(@PathVariable("id")int id){
        String account = modelService.getAccount(id);
        System.out.println(account);
        if (account!=null){
            return ResultVo.success(account);
        }
        return ResultVo.error("查询失败!");
    }


    //获取用户下拉数据
    @GetMapping("/selectList")
    //获取用户下拉数据
    public ResultVo selectList(){
        List<Model> list = modelService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getUsr_id());
                    vo.setLabel(item.getAddr_phone());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }





}
