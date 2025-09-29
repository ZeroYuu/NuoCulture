package com.nuoculture.spring_part.BackEnd.Goods.Order.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.service.CommodityService;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderParam;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderPaymentInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderPaymentService;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderService;
import com.nuoculture.spring_part.BackEnd.Topic.Type.entity.TopicTypeInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Type.service.TopicService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.service.UserService;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.BackEnd.User.Address.service.AddressService;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
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
@RequestMapping("/api/ord")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Value("${file.staticPath}")
    private String staticPath;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody OrderInfo orderInfo) {
        System.out.println(orderInfo);
        boolean temp = orderService.save(orderInfo);

//        int flag = 0;
//        int id = orderService.getId();
////        System.out.println(id);
//        for (int i = 0; i < orderInfo.getImg_path().length; i++) {
//            flag = orderService.setImg(id, orderInfo.getImg_path()[i]);
//        }

        if (temp) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{id}")
    public ResultVo delete(@PathVariable("id") int id) {
        boolean temp = orderService.removeById(id);
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
            temp = orderService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping("/deliver")
    public ResultVo deliver(@RequestBody OrderInfo orderInfo) {
        System.out.println("useEdit");
        System.out.println(orderInfo);
        boolean temp = false;
        //修改图片
        if (orderInfo.getOrder_status() == 1) {
            orderInfo.setOrder_status(2);
            temp = orderService.updateById(orderInfo);
        }
        if (temp) {
            return ResultVo.success("发货成功");
        }
        return ResultVo.error("发货失败");
    }

    @PutMapping("/remark")
    public ResultVo remark(@RequestBody OrderInfo orderInfo) {
        System.out.println("useEdit");
        System.out.println(orderInfo);
        boolean temp = false;
        //修改图片
        temp = orderService.updateById(orderInfo);

        if (temp) {
            return ResultVo.success("备注成功");
        }
        return ResultVo.error("发货失败");
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
    public ResultVo getList(OrderParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<OrderInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();

        //城市
        if (StringUtils.isNotEmpty(param.getAddr_city())) {
            String[] id = orderService.getAddrIdFromCity(param.getAddr_city());

            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }
            wrapper.lambda().in(OrderInfo::getAddr_id, typeList);
        }
        //省份
        if (StringUtils.isNotEmpty(param.getAddr_province())) {
            String[] id = orderService.getAddrIdFromProvince(param.getAddr_province());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }
                wrapper.lambda().in(OrderInfo::getAddr_id, typeList);
        }
        //区
        if (StringUtils.isNotEmpty(param.getAddr_country())) {
            String[] id = orderService.getAddrIdFromCountry(param.getAddr_country());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }
                wrapper.lambda().in(OrderInfo::getAddr_id, typeList);
        }
        //详细地址
        if (StringUtils.isNotEmpty(param.getAddr_detail())) {
            String[] id = orderService.getAddrIdFromDetail(param.getAddr_detail());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            System.out.println(typeList);
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }
                wrapper.lambda().in(OrderInfo::getAddr_id, typeList);

        }
        //参与人账号
        if (StringUtils.isNotEmpty(param.getAddr_name())) {
            String[] id = orderService.getAddrIdFromName(param.getAddr_name());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }
                wrapper.lambda().in(OrderInfo::getAddr_id, typeList);
        }
        //参与人姓名
        if (StringUtils.isNotEmpty(param.getAddr_phone())) {
            String[] id = orderService.getAddrIdFromPhone(param.getAddr_phone());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }

                wrapper.lambda().in(OrderInfo::getUsr_id, typeList);
        }


        //参与人姓名
        if (StringUtils.isNotEmpty(param.getUsr_id())) {
            String[] id = orderService.getUsrId(param.getUsr_id());
            List<String> typeList = new ArrayList<>();
            for (int i = 0; i < id.length; i++) {
                typeList.add(id[i]);
            }
            if (typeList.isEmpty()) {
                // 如果typeList为空，直接返回空结果集
                return ResultVo.success("", new Page<>());
            }

                wrapper.lambda().in(OrderInfo::getUsr_id, typeList);
        }


//参与人姓名
        if (StringUtils.isNotEmpty(param.getPayment_id())) {

            wrapper.lambda().eq(OrderInfo::getPayment_id, param.getPayment_id());
        }

        //参与人姓名
        if (StringUtils.isNotEmpty(param.getOrder_number())) {

            wrapper.lambda().like(OrderInfo::getOrder_number, param.getOrder_number());
        }
        //参与人姓名
        if (StringUtils.isNotEmpty(param.getOrder_track_num())) {

            wrapper.lambda().like(OrderInfo::getOrder_track_num, param.getOrder_track_num());
        }

        //参与人姓名
        if (StringUtils.isNotEmpty(param.getOrder_status())) {

            wrapper.lambda().eq(OrderInfo::getOrder_status, param.getOrder_status());
        }
        if (StringUtils.isNotEmpty(param.getOrder_remark())) {

            wrapper.lambda().like(OrderInfo::getOrder_remark, param.getOrder_remark());
        }


        //浏览量查询
        if (StringUtils.isNotEmpty(param.getOrder_time())) {
            String[] Date = param.getOrder_time().split(",");

            System.out.println(Date[0]);
            System.out.println(Date[1]);

            wrapper.lambda().between(OrderInfo::getOrder_time, Date[0], Date[1]);
        }

        IPage<OrderInfo> pages = orderService.page(page, wrapper);
        System.out.println(pages.getRecords());

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //获取图片
    @GetMapping("/getImages/{id}")
    public ResultVo getImages(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = orderService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getTypeName/{id}")
    public ResultVo getTypeName(@PathVariable("id") int id) {
        String typeName = orderService.getTypeName(id);
        System.out.println(typeName);
        if (typeName != null) {
            return ResultVo.success("", typeName);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getAddr/{id}")
    public ResultVo getAddr(@PathVariable("id") int id) {
        List<UserAddress> adress = orderService.getAddr(id);
        System.out.println(adress);
        if (adress != null) {
            return ResultVo.success("", adress);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getGoods/{id}")
    public ResultVo getGoods(@PathVariable("id") int id) {
        List<GoodsInfo> goods = orderService.getGoods(id);
        System.out.println(goods);
        if (goods != null) {
            return ResultVo.success("", goods);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getGoodsReview/{id}")
    public ResultVo getGoodsReview(@PathVariable("id") int id) {
        List<GoodsReviewInfo> review = orderService.getReview(id);
        System.out.println(review);
        if (review != null) {
            return ResultVo.success("", review);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号
    @GetMapping("/getAccount/{id}")
    public ResultVo getAccount(@PathVariable("id") int id) {
        String account = orderService.getAccount(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }


    //获取支付方式
    @GetMapping("/getPayment/{id}")
    public ResultVo getPayment(@PathVariable("id") int id) {
        String payment = orderService.getPayment(id);
        System.out.println(payment);
        if (payment != null) {
            return ResultVo.success("", payment);
        }
        return ResultVo.error("查询失败!");
    }


    //获取类型下拉数据
    @Autowired
    OrderPaymentService orderPaymentService;

    @GetMapping("/selectPaymentList")
    public ResultVo selectTypeList() {
        List<OrderPaymentInfo> list = orderPaymentService.list();
        //封装返回值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item -> {
                    SelectItem vo = new SelectItem();
                    vo.setCheck(false);
                    vo.setValue(item.getPayment_id());
                    vo.setLabel(item.getPayment_name());
                    selectItems.add(vo);
                });
        return ResultVo.success("查询成功", selectItems);

    }

//    //获取标题下拉
//    @GetMapping("/selecTitletList")
//    public ResultVo selecTitletList(){
//        List<OrderInfo> list = orderService.list();
//        //封装返回值
//        List<SelectItem> selectItems=new ArrayList<>();
//        Optional.ofNullable(list).orElse(new ArrayList<>())
//                .forEach(item->{
//                    SelectItem vo=new SelectItem();
//                    vo.setCheck(false);
//                    vo.setValue(item.getTopic_id());
//                    vo.setLabel(item.getTopic_titile());
//                    selectItems.add(vo);
//                });
//        return ResultVo.success("查询成功",selectItems);
//
//    }

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
