package com.nuoculture.spring_part.BackEnd.User.Account.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.BackEnd.User.Account.service.UserService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserParam;
import com.nuoculture.spring_part.utils.ResultVo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/usr")
public class UserController {

    @Autowired
    UserService userService;
    Date startDate = new Date();
    Date endDate = new Date();

    @Value("${file.staticPath}")
    private String staticPath;
//
//    @Autowired
//    GoodsTypeMapper userMapper;

//    @GetMapping("/")
//    public String hello(){
//        return "cyy";
//    }
//@GetMapping("/getUserList1")
//public List<GoodsTypeInfo> s(){
//    return userMapper.selectList(null);
//}


    @GetMapping("/getList")
    public ResultVo getList(UserParam param) {
        System.out.println("param" + param);
        //构建分页对象
        IPage<UserInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());
        //构造查询条件
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();

        //账号
        if (StringUtils.isNotEmpty(param.getUsr_account())) {
//            wrapper.lambda().orderByDesc(GoodsTypeInfo::getUsr_id);
            wrapper.lambda().like(UserInfo::getUsr_account, param.getUsr_account());
        }
        //角色
        if (StringUtils.isNotEmpty(param.getIs_admin())) {
            wrapper.lambda().eq(UserInfo::getIs_admin, param.getIs_admin());
        }

        //邮箱
        if (StringUtils.isNotEmpty(param.getUsr_email())) {
            wrapper.lambda().like(UserInfo::getUsr_email, param.getUsr_email());
        }
        //昵称
        if (StringUtils.isNotEmpty(param.getUsr_name())) {
            wrapper.lambda().like(UserInfo::getUsr_name, param.getUsr_name());

        }
        //电话
        if (StringUtils.isNotEmpty(param.getUsr_phone())) {
            wrapper.lambda().like(UserInfo::getUsr_phone, param.getUsr_phone());
        }
        //生日
        if (StringUtils.isNotEmpty(param.getUsr_bday())) {
            String[] Date = param.getUsr_bday().split(",");

//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
            System.out.println(Date[0]);
            System.out.println(Date[1]);
//            try {
//                startDate = format.parse(Date[0]);
//                endDate = format.parse(Date[1]);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            System.out.println(startDate);
//            System.out.println(endDate);

            wrapper.lambda().between(UserInfo::getUsr_bday, Date[0], Date[1]);
        }
        IPage<UserInfo> pages = userService.page(page, wrapper);

        if (pages != null) {
            return ResultVo.success("查询成功", pages);
        }
        return ResultVo.error("查询失败!");
    }

    //增加用户
    @PostMapping
    public ResultVo add(@RequestBody UserInfo userInfo) {
        System.out.println(userInfo);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getUsr_account, userInfo.getUsr_account());
        UserInfo user = userService.getOne(wrapper);
        if (user != null) {
            return ResultVo.error("账号已存在");
        }

        boolean temp = userService.save(userInfo);
        if (temp) {
            return ResultVo.success("添加成功");
        }
        return ResultVo.error("添加失败");
    }

    //删除
    @DeleteMapping("{usr_id}")
    public ResultVo delete(@PathVariable("usr_id") int usrId) {
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
        boolean temp = userService.removeById(usrId);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除
    @DeleteMapping("/allDel/{usr_id}")
    public ResultVo allDelete(@PathVariable("usr_id") int[] usrId) {
//        GoodsTypeInfo user=new GoodsTypeInfo();
//        user=userService.getById(usrId);
//        System.out.println(user);
//        user.setUsr_status(99);
//        boolean temp = userService.updateById(user);
        boolean temp = false;
        for (int i = 0; i < usrId.length; i++) {
            System.out.println(usrId[i]);
            temp = userService.removeById(usrId[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody UserInfo userInfo) {
        System.out.println("useEdit");
        System.out.println(userInfo);
        boolean temp = userService.updateById(userInfo);
        if (temp) {
            return ResultVo.success("编辑成功");
        }
        return ResultVo.error("编辑失败");
    }

    //上传
    @PostMapping("/uploadAvatar")
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



}
