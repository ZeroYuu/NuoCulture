package com.nuoculture.spring_part.BackEnd.Goods.Commdity.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.service.JoinService;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.CommodityParam;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.service.CommodityService;
import com.nuoculture.spring_part.BackEnd.Goods.Type.entity.GoodsTypeInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Type.service.GoodsService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
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
@RequestMapping("/api/comd")
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody GoodsInfo goodsInfo) {
        System.out.println(goodsInfo);

        boolean temp = commodityService.save(goodsInfo);
        int goodsId=commodityService.getId();

        String[] colorNames = goodsInfo.getGoods_color_name().split(",");
        for (String colorName : colorNames) {
            // 去除颜色名称两端的空白字符
            colorName = colorName.trim();
            if (!colorName.isEmpty()) {
                commodityService.setColorName(colorName, goodsId);
            }
        }

        int flag = 0;
        int id = commodityService.getId();
        for (int i = 0; i < goodsInfo.getImg_path().length; i++) {
            flag = commodityService.setImg(id, goodsInfo.getImg_path()[i]);
        }

        if (temp && flag == 1) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id") int id) {
        boolean temp = commodityService.removeById(id);
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
            temp = commodityService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody GoodsInfo goodsInfo) {
        System.out.println("useEdit");
        System.out.println(goodsInfo);

        boolean temp = commodityService.updateById(goodsInfo);
        int goodsId=goodsInfo.getGoods_id();

        String[] colorNames = goodsInfo.getGoods_color_name().split(",");
        for (String colorName : colorNames) {
            // 去除颜色名称两端的空白字符
            colorName = colorName.trim();
            if (!colorName.isEmpty()) {
                commodityService.setColorName(colorName, goodsId);
            }
        }

        //修改图片
        int flag = 0;
        if (goodsInfo.getImg_path()!=null){
            int id = goodsInfo.getGoods_id();
//        System.out.println(id);
            for (int i = 0; i < goodsInfo.getImg_path().length; i++) {
                flag = commodityService.setImg(id, goodsInfo.getImg_path()[i]);
            }
        }else {
            flag = 1;
        }
        //修改审核状态
        if (temp && flag == 1){
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
            File avatarDir = new File("img/goods");
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();

            }
//            System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
            // 2. 获取安全的文件名（避免覆盖或路径攻击）
            for (MultipartFile f : file) {
                String fileName = System.currentTimeMillis() + "_" + f.getOriginalFilename();
                String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
                preservePath.add(staticPath + "img/goods/" + fileName);
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
        File avatarDir = new File("img/goods");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();

        }
//        System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
        // 2. 获取安全的文件名（避免覆盖或路径攻击）
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
        preservePath = staticPath + "img/goods/" + fileName;
        System.out.println(preservePath);
        // 3. 保存文件
        file.transferTo(new File(filePath));

        map.put("errno", 0);
        map.put("data", CollUtil.newArrayList(Dict.create().set("url", preservePath)));


        return map;
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(CommodityParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<GoodsInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<GoodsInfo> wrapper = new QueryWrapper<>();

        //标题模糊查询
        if (StringUtils.isNotEmpty(param.getGoods_name())) {
            wrapper.lambda().like(GoodsInfo::getGoods_name, param.getGoods_name());
        }
        //活动类型查询
        if (StringUtils.isNotEmpty(param.getGoods_type_id())) {
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(GoodsInfo::getGoods_type_id, param.getGoods_type_id());
        }
        //审核状态查询
//        if (StringUtils.isNotEmpty(param.getGoods_color_name())) {
//            String[] usr= commodityService.getGoodsId(param.getGoods_color_name());
//            for (int i = 0; i < usr.length; i++) {
//                System.out.print(usr[i]+' ');
//            }
//            wrapper.lambda().in(GoodsInfo::getGoods_color_id, usr);
//        }

        //价格查询
        if (StringUtils.isNotEmpty(param.getGoods_price())) {
            String[] Date = param.getGoods_price().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(GoodsInfo::getGoods_price, Date[0], Date[1]);
        }
        //库存查询
        if (StringUtils.isNotEmpty(param.getGoods_inventory())) {
            String[] Date = param.getGoods_inventory().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(GoodsInfo::getGoods_inventory, Date[0], Date[1]);
        }

        IPage<GoodsInfo> pages = commodityService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getUsr(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = commodityService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getTypeName/{id}")
    public ResultVo getTypeName(@PathVariable("id")int id){
        String account = commodityService.getTypeName(id);
        System.out.println(account);
        if (account!=null){
            return ResultVo.success(account);
        }
        return ResultVo.error("查询失败!");
    }

    //获取商品颜色信息
    @GetMapping("/getColor/{id}")
    public ResultVo getColor(@PathVariable("id")int id){
        String name = commodityService.getColor(id);
//        System.out.println(name);
        if (name!=null){
            return ResultVo.success("",name);
        }
        return ResultVo.error("查询失败!");
    }


    //获取类型下拉数据
    @Autowired
    GoodsService goodsService;
    @GetMapping("/selectTypeList")
    public ResultVo selectTypeList(){
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
