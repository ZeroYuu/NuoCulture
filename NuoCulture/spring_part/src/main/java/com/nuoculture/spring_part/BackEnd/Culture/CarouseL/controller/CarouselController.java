package com.nuoculture.spring_part.BackEnd.Culture.CarouseL.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity.CarouselParam;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity.homepageImageInfo;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.service.CarouselService;
import com.nuoculture.spring_part.BackEnd.Culture.CarouseL.entity.SelectItem;
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
@RequestMapping("/api/cult")
public class CarouselController {

    @Autowired
    CarouselService carouselService;

    @Value("${file.staticPath}")
    private String staticPath;


    @GetMapping("/getList")
    public ResultVo getList(CarouselParam param){
            System.out.println("param"+param);
        //构建分页对象
        IPage<homepageImageInfo> page=new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<homepageImageInfo> wrapper=new QueryWrapper<>();
        //轮播图标题
        if(StringUtils.isNotEmpty(param.getHp_title())){
            wrapper.lambda().like(homepageImageInfo::getHp_title,param.getHp_title());
        }
        //轮播图备注
        if(StringUtils.isNotEmpty(param.getHp_remark())){
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(homepageImageInfo::getHp_remark,param.getHp_remark());
        }

        
        IPage<homepageImageInfo> pages = carouselService.page(page, wrapper);

        if (pages!=null){
            return ResultVo.success("查询成功",pages);
        }
        return ResultVo.error("查询失败!");
    }


    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody homepageImageInfo cultureCarousel){
        System.out.println(cultureCarousel);
        boolean temp = carouselService.save(cultureCarousel);
        if(temp){
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id")int id){
        boolean temp = carouselService.removeById(id);
        if(temp){
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除
    @DeleteMapping("/allDel/{id}")
    public ResultVo allDelete(@PathVariable("id") int[] Id) {
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
        boolean temp = false;
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = carouselService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody homepageImageInfo cultureCarousel){
        System.out.println("useEdit");
        System.out.println(cultureCarousel);
        boolean temp = carouselService.updateById(cultureCarousel);
        if(temp){
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
    }

    //获取用户下拉数据
    @GetMapping("/selectList")
    public ResultVo selectList(){
        List<homepageImageInfo> list = carouselService.list();
        //封装返回值
        List<SelectItem> selectItems=new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    SelectItem vo=new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getHp_id());
                    vo.setLabel(item.getHp_title());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功",selectItems);

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
            File avatarDir = new File("img/covers");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();
            }
            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = avatarDir.getAbsolutePath() + "/"+ fileName;
            preservePath=staticPath+"img/covers/"+fileName;
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


}
