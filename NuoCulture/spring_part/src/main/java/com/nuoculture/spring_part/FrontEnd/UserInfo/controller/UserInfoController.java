package com.nuoculture.spring_part.FrontEnd.UserInfo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Publish.entity.ActiveInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.BackEnd.User.Address.service.AddressService;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.CartInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.GoodsReviewInfo;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontGoodsService;
import com.nuoculture.spring_part.FrontEnd.Goods.service.GoodsReviewService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.*;
import com.nuoculture.spring_part.FrontEnd.UserInfo.service.UserInfoService;
import com.nuoculture.spring_part.jwt.JwtInterceptor;
import com.nuoculture.spring_part.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/usrinfo")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @Value("${file.staticPath}")
    private String staticPath;

    @Autowired
    AddressService addressService;

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody UserAddress userAddress) {
        System.out.println(userAddress);
        boolean temp = addressService.save(userAddress);
        if (temp) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    @Autowired
    GoodsReviewService goodsReviewService;
    @PostMapping("/review")
    public ResultVo review(@RequestBody GoodsReviewInfo goodsReviewInfo) {
        System.out.println(goodsReviewInfo);
        goodsReviewInfo.setGoods_review_time(new Date());
        boolean temp = goodsReviewService.save(goodsReviewInfo);

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", goodsReviewInfo.getOrder_id());
        OrderInfo result = orderService.getOne(queryWrapper);
        result.setOrder_status(4);
        boolean flag = orderService.updateById(result);
        if (temp) {
            return ResultVo.success("评价成功");
        }
        return ResultVo.error("评价失败");
    }




    //修改
    @PutMapping("/editAddr")
    public ResultVo editAddr(@RequestBody UserAddress userAddress) {
        System.out.println("useEdit");
        System.out.println(userAddress);
        boolean temp = addressService.updateById(userAddress);
        if (temp) {
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
    }

    @Autowired
    OrderService orderService;
    //确认收货
    @PutMapping("/checkGoods")
    public ResultVo checkGoods(@RequestBody OrderInfo orderInfo) {
        System.out.println(orderInfo);
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getOrder_id());
        OrderInfo temp = orderService.getOne(queryWrapper);

        temp.setOrder_status(3);
        boolean flag = orderService.updateById(temp);
        if (flag) {
            return ResultVo.success("收货成功");
        }
        return ResultVo.error("收货失败");
    }

    @Autowired
    UserInfoService userService;

    //修改
    @PutMapping("/editUsrInfo")
    public ResultVo editUsrInfo(@RequestBody UserInfo userInfo) {
        System.out.println("useEdit");
        System.out.println(userInfo);
        userService.updateUserInfo(userInfo);
//        if (temp) {
//            return ResultVo.success("编辑成功");
//        }
        return ResultVo.success("编辑成功");
    }


    //修改
    @PutMapping("/editPsd")
    public ResultVo editPsd(@RequestBody PsdParam param) {
        System.out.println("useEdit");
        System.out.println(param);
        //查询用户
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getUsr_id, param.getUsr_id());
        UserInfo user = userService.getOne(wrapper);
        System.out.println(user);
        //判断密码是否正确
        if (!user.getUsr_psd().equals(param.getUsr_psd())) {
            return ResultVo.error("原密码不正确！");
        }
        if (user.getUsr_psd().equals(param.getNew_psd())) {
            return ResultVo.error("原密码与新密码一致！");
        }
        //修改密码
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(UserInfo::getUsr_id, param.getUsr_id())
                .set(UserInfo::getUsr_psd, param.getUsr_psd());
        if (userService.update(updateWrapper)) {
            return ResultVo.success("修改密码成功！");
        }
        return ResultVo.error("修改密码失败！");
    }


    //上传
    @PostMapping("/uploadAvatar")
    public ResultVo uploadAvatar(@RequestParam(value = "file", required = false) MultipartFile file) {
        boolean tags;
        String preservePath = "";
        if (file == null) {
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
            String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
            preservePath = staticPath + "img/avatars/" + fileName;
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
    public Map<String, Object> uploadRichText(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        boolean tags;
        String preservePath = "";
        Map<String, Object> map = new HashMap<>();

        System.out.println("file:" + file);
        // 1. 确保目录存在
        File avatarDir = new File("img/msg");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();

        }
        System.out.println("文件夹路径: " + avatarDir.getAbsolutePath());
        // 2. 获取安全的文件名（避免覆盖或路径攻击）
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = avatarDir.getAbsolutePath() + "/" + fileName;
        preservePath = staticPath + "img/msg/" + fileName;
        System.out.println(preservePath);
        // 3. 保存文件
        file.transferTo(new File(filePath));

        map.put("errno", 0);
        map.put("data", CollUtil.newArrayList(Dict.create().set("url", preservePath)));


        return map;
    }


    //删除订单
    @DeleteMapping("/delAddr/{id}")
    public ResultVo delAddr(@PathVariable("id") int id) {
        boolean temp = addressService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除订单
    @DeleteMapping("/alldelAddr/{id}")
    public ResultVo alldelAddr(@PathVariable("id") int[] Id) {
        System.out.println(Arrays.toString(Id));
        boolean temp = false;
//        int[] ids = Arrays.asList(Id).stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = addressService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }


    @PutMapping("/delStar/{id}")
    public ResultVo delStar(@PathVariable("id") int id) {
        System.out.println(id);
        int temp = userInfoService.cancelStar(id);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {

            return ResultVo.success("取消收藏");
        }
        return ResultVo.error("取消失败");
    }

    @PutMapping("/allDelStar/{id}")
    public ResultVo allDelStar(@PathVariable("id") int[] Id) {
        System.out.println(Id);
        int temp=0;
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = userInfoService.cancelStar(Id[i]);
        }

//        System.out.println(getStarCul(uid));
        if (temp == 1) {

            return ResultVo.success("批量取消收藏");
        }
        return ResultVo.error("批量取消失败");
    }

    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(ModelParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<UserInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        //收货人姓名模糊查询
//        if(StringUtils.isNotEmpty(param.getAddr_name())){
//            wrapper.lambda().like(UserInfo::getAddr_name,param.getAddr_name());
//        }
//        //收货人电话模糊查询
//        if(StringUtils.isNotEmpty(param.getAddr_phone())){
////            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
//            wrapper.lambda().like(UserInfo::getAddr_phone,param.getAddr_phone());
//        }


        IPage<UserInfo> pages = userInfoService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    @JwtInterceptor.JwtToken
    @GetMapping
    public ResultVo JwtToken(){
        return ResultVo.success();
    }

    @GetMapping("/getAccount/{id}")
    public ResultVo getAccount(@PathVariable("id") int id) {
        UserInfo account = userInfoService.getById(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @Autowired
    FrontGoodsService frontGoodsService;
    @GetMapping("/getReview/{id}")
    public ResultVo getReview(@PathVariable("id") int id) {
        List<GoodsReviewInfo> account = frontGoodsService.getReview(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsrActv/{id}")
    public ResultVo getUsrActv(@PathVariable("id") int id) {
        List<ActiveInfo> account = userInfoService.getUsrActv(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsrTopic/{id}")
    public ResultVo getUsrTopic(@PathVariable("id") int id) {
        List<TopicInfo> detail = userInfoService.getUsrTopic(id);

        for (TopicInfo topicInfo : detail) {
            String url = userInfoService.getImgurl(topicInfo.getTopic_id());
            topicInfo.setImg_path_url(url); // 假设GoodsInfo类有setImgPathUrl方法假设GoodsInfo类有setGoodsColorName方法
        }

        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsrCart/{id}")
    public ResultVo getUsrCart(@PathVariable("id") int id) {
        List<CartInfo> account = userInfoService.getUsrCart(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsrOrd/{id}")
    public ResultVo getUsrOrd(@PathVariable("id") int id) {
        List<OrderInfo> account = userInfoService.getUsrOrd(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }



    @GetMapping("/getUsrAddr/{id}")
    public ResultVo getUsrAddr(@PathVariable("id") int id) {
        List<UserAddress> account = userInfoService.getUsrAddr(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/getOrderAddr/{id}")
    public ResultVo getOrderAddr(@PathVariable("id") int id) {
        UserAddress account = addressService.getById(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsrInfo/{id}")
    public ResultVo getUsrInfo(@PathVariable("id") int id) {
        List<UserInfo> account = userInfoService.getUsrInfo(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    //获取用户下拉数据
//    @GetMapping("/selectList")
//    //获取用户下拉数据
//    public ResultVo selectList(){
//        List<UserInfo> list = userInfoService.list();
//        //封装返回值
//        List<SelectItem> selectItems=new ArrayList<>();
//        Optional.ofNullable(list).orElse(new ArrayList<>())
//                .forEach(item->{
//                    SelectItem vo=new SelectItem();
//                    vo.setCheck(false);
//                    vo.setValue(item.getUsr_id());
//                    vo.setLabel(item.getAddr_phone());
//                    selectItems.add(vo);
//                });
//        return ResultVo.success("查询成功",selectItems);
//
//    }


}
