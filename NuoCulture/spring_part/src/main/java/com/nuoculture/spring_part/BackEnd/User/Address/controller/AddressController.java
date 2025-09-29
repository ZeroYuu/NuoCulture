package com.nuoculture.spring_part.BackEnd.User.Address.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.service.UserService;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.AddressParam;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.SelectItem;
import com.nuoculture.spring_part.BackEnd.User.Address.entity.UserAddress;
import com.nuoculture.spring_part.BackEnd.User.Address.service.AddressService;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/addr")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;


    @GetMapping("/getList")
    public ResultVo getList(AddressParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<UserAddress> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        //收货人姓名模糊查询
        if (StringUtils.isNotEmpty(param.getAddr_name())) {
            wrapper.lambda().like(UserAddress::getAddr_name, param.getAddr_name());
        }
        //收货人电话模糊查询
        if (StringUtils.isNotEmpty(param.getAddr_phone())) {
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(UserAddress::getAddr_phone, param.getAddr_phone());
        }
        //用户id查询
        if (StringUtils.isNotEmpty(param.getUsr_id())) {
            wrapper.lambda().eq(UserAddress::getUsr_id, param.getUsr_id());
        }
        //省份查询
        if (StringUtils.isNotEmpty(param.getAddr_province())) {
            wrapper.lambda().like(UserAddress::getAddr_province, param.getAddr_province());
        }
        //城市查询
        if (StringUtils.isNotEmpty(param.getAddr_city())) {
            wrapper.lambda().like(UserAddress::getAddr_city, param.getAddr_city());
        }
        //区查询
        if (StringUtils.isNotEmpty(param.getAddr_country())) {
            wrapper.lambda().like(UserAddress::getAddr_country, param.getAddr_country());
        }
        //详细地址查询
        if (StringUtils.isNotEmpty(param.getAddr_detail())) {
            wrapper.lambda().like(UserAddress::getAddr_detail, param.getAddr_detail());
        }


        IPage<UserAddress> pages = addressService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getUsr/{usr_id}")
    public ResultVo getUsr(@PathVariable("usr_id") int usr_id) {
        String account = addressService.getAccount(usr_id);
        System.out.println(account);

        return ResultVo.success(account);

    }

    @GetMapping("/getAddrText/{id}")
    public ResultVo getAddrText(@PathVariable("id") int id) {
        String account = addressService.getAddrText(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success(account);
        }
        return ResultVo.success("");
    }

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

    //删除
    @DeleteMapping("{addr_id}")
    public ResultVo delete(@PathVariable("addr_id") int addrId) {
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
        boolean temp = addressService.removeById(addrId);
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
            temp = addressService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody UserAddress userAddress) {
        System.out.println("useEdit");
        System.out.println(userAddress);
        boolean temp = addressService.updateById(userAddress);
        if (temp) {
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
    }

    @GetMapping("/selectList")
    //获取用户下拉数据
    public ResultVo selectList() {
        List<UserInfo> list = userService.list();
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
        return ResultVo.success("查询成功", selectItems);

    }

//    @GetMapping("/getMapList/{Pid}")
//    public List<pcdMessage> getMapList(@PathVariable("Pid")Integer Pid){
//        return pcdService.getMapListByPid(Pid);
//    }
//


}
