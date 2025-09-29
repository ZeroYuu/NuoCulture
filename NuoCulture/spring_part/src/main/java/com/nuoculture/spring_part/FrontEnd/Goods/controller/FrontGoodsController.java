package com.nuoculture.spring_part.FrontEnd.Goods.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.CultureInfo;
import com.nuoculture.spring_part.BackEnd.Culture.Message.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderService;
import com.nuoculture.spring_part.BackEnd.Goods.Type.service.GoodsService;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.service.ContentService;
import com.nuoculture.spring_part.BackEnd.Topic.Review.service.ReviewService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.service.UserService;
import com.nuoculture.spring_part.FrontEnd.Goods.entity.*;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontCartService;
import com.nuoculture.spring_part.FrontEnd.Goods.service.FrontGoodsService;
import com.nuoculture.spring_part.FrontEnd.Goods.service.GoodsReviewService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/frontgoods")
public class FrontGoodsController {

    @Autowired
    FrontGoodsService frontGoodsService;

    @Value("${file.staticPath}")
    private String staticPath;

    @Autowired
    FrontCartService frontCartService;


    @PostMapping("/addCart")
    public ResultVo addTopic(@RequestBody CartInfo cartInfo) {
        System.out.println(cartInfo);
        cartInfo.setCart_status(0);
        boolean flag = false;
        QueryWrapper<CartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", cartInfo.getGoods_id()).eq("usr_id", cartInfo.getUsr_id()).eq("goods_color_id", cartInfo.getGoods_color_id()).eq("cart_status", 0);
        CartInfo temp = frontCartService.getOne(queryWrapper);
        if (temp != null) {
            temp.setGoods_count(cartInfo.getGoods_count() + temp.getGoods_count());
            flag = frontCartService.updateById(temp);
        } else {
            flag = frontCartService.save(cartInfo);
        }

        if (flag) {
            return ResultVo.success("加购成功");
        }
        return ResultVo.error("加购失败");
    }


    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @Autowired
    ContentService contentService;

    @PostMapping("/addOrder/{derictPay}")
    public ResultVo addOrder(@RequestBody OrderInfo orderInfo, @PathVariable("derictPay") boolean derictPay) {
        System.out.println(orderInfo+ "" + derictPay);
        boolean flag=false;
        int id=0;

        if (orderInfo.getOrder_id() != 0) {
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderInfo.getOrder_id());
            OrderInfo temp = orderService.getOne(queryWrapper);
            temp.setPayment_id(orderInfo.getPayment_id());
            temp.setOrder_status(1);
            flag = orderService.updateById(temp);
            id = orderInfo.getOrder_id();
//            System.out.println(id);
        } else {
            if (!derictPay) {
                QueryWrapper<CartInfo> orderWrapper = new QueryWrapper<>();
                orderWrapper.eq("goods_id", orderInfo.getGoods_id())
                        .eq("goods_color_id", orderInfo.getGoods_color_id())
                        .eq("usr_id", orderInfo.getUsr_id())
                        .eq("cart_status", 0);
                CartInfo result = frontCartService.getOne(orderWrapper);
                result.setCart_status(1);
                frontCartService.updateById(result);

            }

            orderInfo.setOrder_status(1);
            orderInfo.setOrder_time(new Date());
            //雪花算法
            String orderNumber = IdUtil.getSnowflake(1, 1).nextIdStr();//或者可以用IdUtil.simpleUUID();
            orderInfo.setOrder_number(orderNumber);


            flag = orderService.save(orderInfo);
            //借用获得id
            id = contentService.getId();

            //判断积分

        }
        if (orderInfo.getPayment_id() == 3) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("usr_id", orderInfo.getUsr_id());
            UserInfo temp = userService.getOne(queryWrapper);
//            System.out.println(temp.getUsr_points());
            if (temp != null) {
                if ((temp.getUsr_points() / 100) < orderInfo.getOrder_price()) {
                    QueryWrapper<OrderInfo> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("order_id", id);
                    OrderInfo temp1 = orderService.getOne(queryWrapper1);

                    temp1.setOrder_status(0);
                    orderService.updateById(temp1);
                    return ResultVo.error("积分不够，请更换支付方式", 201,temp1.getOrder_id());
                } else {
                    temp.setUsr_points((int) (temp.getUsr_points() - orderInfo.getOrder_price() * 100));
                    userService.updateById(temp);
                }
            }
        }


        if (flag) {
            return ResultVo.success("购买成功");
        }
        return ResultVo.error("购买失败");
    }

    @GetMapping("/StarGoods/{id}/{uid}/{star}")
    public ResultVo StarGoods(@PathVariable("id") int id, @PathVariable("uid") int uid, @PathVariable("star") boolean star) {
        System.out.println(id + " " + uid);
        int temp = frontGoodsService.StarGoods(id, uid, star);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {
            return ResultVo.success("收藏成功");
        } else if (temp == 2) {
            return ResultVo.success("取消收藏");
        }
        return ResultVo.error("收藏失败");
    }

    @GetMapping("/StarCart/{id}/{uid}/{cid}")
    public ResultVo StarCart(@PathVariable("id") int id, @PathVariable("uid") int uid, @PathVariable("cid") int cid) {
        System.out.println(id + " " + uid);
        int temp = frontGoodsService.StarCart(id, uid, cid);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {
            return ResultVo.success("收藏成功");
        }
        return ResultVo.error("收藏失败");
    }

    @GetMapping("/getStarGoods/{id}")
    public ResultVo getStarGoods(@PathVariable("id") int id) {
        List<UserStarInfo> account = frontGoodsService.getStarGoods(id);
        System.out.println(account);
        if (account.isEmpty()) {
            return ResultVo.success("", account);
        }
        Set<Integer> GoodsIds = account.stream().map(UserStarInfo::getGoods_id).collect(Collectors.toSet());

        List<GoodsInfo> infos = frontGoodsService.listByIds(GoodsIds);

        IntStream.range(0, infos.size()) // 假设infos和account列表长度相同
                .forEach(i -> {
                    GoodsInfo info = infos.get(i); // 获取infos列表中的元素
                    UserStarInfo userStarInfo = account.get(i); // 获取account列表中的对应元素
                    info.setStar_id(userStarInfo.getStar_heart_id()); // 设置star_id
                });


        for (Integer goodsId : GoodsIds) {
            String url = frontGoodsService.getImgurl(goodsId);
            for (GoodsInfo goodsInfo : infos) {
                if (goodsInfo.getGoods_id() == (goodsId)) {  // 假设GoodsInfo有getGoodsId方法
                    goodsInfo.setImg_path_url(url);
                    break;  // 找到匹配的goodsInfo后跳出内层循环
                }
            }
        }


        if (account != null) {
            return ResultVo.success("", infos);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getStar/{id}/{uid}")
    public ResultVo getStar(@PathVariable("id") int id, @PathVariable("uid") int uid) {
        Boolean account = frontGoodsService.getStar(id, uid);
        System.out.println(account);

        return ResultVo.success("", account);

    }

    @Autowired
    GoodsReviewService goodsReviewService;

    //删除评论
    @DeleteMapping("/delReview/{id}")
    public ResultVo delReview(@PathVariable("id") int id) {
        boolean temp = goodsReviewService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //删除购物车商品
    @DeleteMapping("/delCart/{id}")
    public ResultVo delCart(@PathVariable("id") int id) {
        boolean temp = frontCartService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除购物车商品
    @DeleteMapping("/allDelCart/{id}")
    public ResultVo allDelete(@PathVariable("id") int[] Id) {
        System.out.println(Arrays.toString(Id));
        boolean temp = false;
//        int[] ids = Arrays.asList(Id).stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = frontCartService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }


    //删除订单
    @DeleteMapping("/delOrder/{id}")
    public ResultVo delOrder(@PathVariable("id") int id) {
        boolean temp = orderService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除订单
    @DeleteMapping("/allDelOrder/{id}")
    public ResultVo allDelOrder(@PathVariable("id") int[] Id) {
        System.out.println(Arrays.toString(Id));
        boolean temp = false;
//        int[] ids = Arrays.asList(Id).stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = orderService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //批量收藏购物车商品
    @GetMapping("/allStarCart/{id}/{uid}/{cid}")
    public ResultVo allStarCart(@PathVariable("id") int[] Id, @PathVariable("uid") int uid, @PathVariable("cid") int[] cid) {
        System.out.println(Arrays.toString(Id));
        int temp = 0;
//        int[] ids = Arrays.asList(Id).stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            System.out.println(cid[i]);
            temp = frontGoodsService.StarCart(Id[i], uid, cid[i]);
        }

        if (temp == 1) {
            return ResultVo.success("批量收藏成功");
        }
        return ResultVo.error("批量收藏失败");
    }


    @PutMapping
    public ResultVo updateColor(@RequestBody CartInfo cartInfo) {
        System.out.println("cartInfo" + cartInfo);

        boolean flag = false;
        // 查询是否存在相同商品、颜色、用户的购物车记录
        QueryWrapper<CartInfo> existQuery = new QueryWrapper<>();
        existQuery.eq("goods_id", cartInfo.getGoods_id()).eq("usr_id", cartInfo.getUsr_id()).eq("goods_color_id", cartInfo.getGoods_color_id()).eq("cart_status", 0);
        // 使用last("limit 1")确保只返回一条记录
        CartInfo existCart = frontCartService.getOne(existQuery.last("limit 1"));

        // 查询当前要更新的购物车记录
        QueryWrapper<CartInfo> currentQuery = new QueryWrapper<>();
        currentQuery.eq("cart_id", cartInfo.getCart_id());
        CartInfo currentCart = frontCartService.getOne(currentQuery.last("limit 1"));

        if (existCart != null) {
            // 合并数量
            existCart.setGoods_count(cartInfo.getGoods_count() + existCart.getGoods_count());
            flag = frontCartService.updateById(existCart);
            // 删除原来的记录
            if (currentCart != null) {
                frontCartService.removeById(currentCart);
            }
        } else {
            // 更新当前记录
            if (currentCart != null) {
                currentCart.setGoods_count(cartInfo.getGoods_count());
                currentCart.setGoods_color_id(cartInfo.getGoods_color_id());
                flag = frontCartService.updateById(currentCart);
            }
        }

        if (flag) {
            return ResultVo.success("更新成功", flag ? existCart != null ? existCart : currentCart : null);
        }
        return ResultVo.error("更新失败!");
    }

    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(PageParam param) {
        System.out.println("param" + param);
        //构建分页对象

        IPage<GoodsInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());


        if (StringUtils.isNotEmpty(param.getGoods_type_id())) {
            frontGoodsService.getTypeGoods(page, param.getGoods_type_id(), param.getGoods_content());
        } else {
            frontGoodsService.getGoods(page, param.getGoods_content());
        }

        System.out.println(page);
        if (page != null) {
            return ResultVo.success("查询成功", page);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/getCartList/{id}")
    public ResultVo getCartList(@PathVariable("id") int id, PageParam param) {
        System.out.println("param" + param);
        //构建分页对象

        List<CartInfo> list = frontGoodsService.getCartList(id, param.getGoods_content());
        System.out.println(list);
        if (list != null) {
            return ResultVo.success("查询成功", list);
        }
        return ResultVo.error("查询失败!");
    }


    //获取活动信息
    @GetMapping("/getRecmdList")
    public ResultVo getRecmdList() {
        List<GoodsInfo> recmd = frontGoodsService.getRecmdList();
        System.out.println(recmd);
        if (recmd != null) {
            return ResultVo.success("", recmd);
        }
        return ResultVo.error("查询失败!");
    }

    //获取活动信息
    @GetMapping("/getGoodsType")
    public ResultVo getGoodsType() {
        List<GoodsTypeInfo> type = frontGoodsService.getType();
        System.out.println(type);
        if (type != null) {
            return ResultVo.success("", type);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getImages/{id}")
    public ResultVo getImages(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = frontGoodsService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getDetail/{gid}")
    public ResultVo getDetail(@PathVariable("gid") int gid) {
        List<GoodsInfo> detail = frontGoodsService.getDetailApi(gid);
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getCartDetail/{gid}/{gcid}")
    public ResultVo getCartDetail(@PathVariable("gid") int gid, @PathVariable("gcid") int gcid) {
        List<GoodsInfo> detail = frontGoodsService.getDetailApi(gid);
        String url = frontGoodsService.getImgurl(gid);
        String color = frontGoodsService.getColor(gcid);
        for (GoodsInfo goodsInfo : detail) {
            goodsInfo.setImg_path_url(url);
            goodsInfo.setGoods_color_name(color);
            goodsInfo.setGoods_color_id(gcid);
        }
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getReview/{id}")
    public ResultVo getReview(@PathVariable("id") int id) {
        List<GoodsReviewInfo> review = frontGoodsService.getReview(id);
        System.out.println(review);
        if (review != null) {
            return ResultVo.success("", review);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getAccount/{id}")
    public ResultVo getAccount(@PathVariable("id") int id) {
        List<UserInfo> account = frontGoodsService.getAccount(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getGoodsTypeName/{id}")
    public ResultVo getGoodsTypeName(@PathVariable("id") int id) {
        List<GoodsTypeInfo> name = frontGoodsService.getGoodsTypeName(id);
        System.out.println(name);
        if (name != null) {
            return ResultVo.success("", name);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/selectTypeList/{id}")
    public ResultVo selectTypeList(@PathVariable("id") int id) {
        List<GoodsColorInfo> list = frontGoodsService.getColorList(id);
//        System.out.println(list);
        //封装返回值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(item -> {
            SelectItem vo = new SelectItem();
            vo.setCheck(false);
            vo.setValue(item.getGoods_color_id());
            vo.setLabel(item.getGoods_color_name());
            selectItems.add(vo);
        });
        return ResultVo.success("查询成功", selectItems);

    }


}
