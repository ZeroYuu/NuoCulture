package com.nuoculture.spring_part.BackEnd.Goods.Type.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.TypeParam;
import com.nuoculture.spring_part.BackEnd.Goods.Type.service.GoodsService;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/godstype")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody GoodsTypeInfo goodsTypeInfo){
        System.out.println(goodsTypeInfo);
        boolean temp = goodsService.save(goodsTypeInfo);
        if(temp){
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id")int id){
        boolean temp = goodsService.removeById(id);
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
            temp = goodsService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody GoodsTypeInfo goodsTypeInfo){
        System.out.println("useEdit");
        System.out.println(goodsTypeInfo);
        boolean temp = goodsService.updateById(goodsTypeInfo);
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
            File avatarDir = new File("img/topic");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
            preservePath=staticPath+"img/topic/"+fileName;
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

    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(TypeParam param){
            System.out.println("param"+param);
        //构建分页对象
        IPage<GoodsTypeInfo> page=new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<GoodsTypeInfo> wrapper=new QueryWrapper<>();
        //收货人姓名模糊查询
        if(StringUtils.isNotEmpty(param.getGoods_type_name())){
            wrapper.lambda().like(GoodsTypeInfo::getGoods_type_id,param.getGoods_type_name());
        }
        if(StringUtils.isNotEmpty(param.getGoods_type_encode())){
            wrapper.lambda().like(GoodsTypeInfo::getGoods_type_id,param.getGoods_type_encode());
        }
        //收货人电话模糊查询
        if(StringUtils.isNotEmpty(param.getGoods_type_remark())){
            wrapper.lambda().like(GoodsTypeInfo::getGoods_type_remark,param.getGoods_type_remark());
        }

        
        IPage<GoodsTypeInfo> pages = goodsService.page(page, wrapper);

        if (pages!=null){
            return ResultVo.success("查询成功",pages);
        }
        return ResultVo.error("查询失败!");
    }



    //获取用户下拉数据
    @GetMapping("/getCodeSelectList")
    public ResultVo getCodeSelectList(){
        List<GoodsTypeInfo> list = goodsService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getGoods_type_id());
                    vo.setLabel(item.getGoods_type_encode());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }

    //获取用户下拉数据
    @GetMapping("/getNameSelectList")
    public ResultVo getNameSelectList(){
        List<GoodsTypeInfo> list = goodsService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getGoods_type_id());
                    vo.setLabel(item.getGoods_type_name());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

    }





}
