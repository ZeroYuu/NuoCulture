package com.nuoculture.spring_part.Login.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.Login.entity.LoginParam;
import com.nuoculture.spring_part.Login.entity.LoginVo;
import com.nuoculture.spring_part.Login.service.LoginService;
import com.nuoculture.spring_part.jwt.JwtUtils;
import com.nuoculture.spring_part.utils.ResultVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    LoginService loginService;

    @Autowired
    JwtUtils jwtUtils;


    //注册
//    @PostMapping
//    public ResultVo add(@RequestBody SysUser sysUser){
//        sysUser.setCreateTime(new Date());
//        //boolean temp = sysUserService.save(sysUser);
//        sysUserService.saveUser(sysUser);
//       // if(temp){
//            return ResultVo.success("新增成功！");
//        //}
//       // return ResultVo.error("新增失败！");
//    }

//    //重置密码
//    @PostMapping("/resetPassword")
//    public ResultVo resetPassword(@RequestBody SysUser sysUser){
//        //条件构造器
//        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
//        wrapper.lambda().eq(UserInfo::getUserId,sysUser.getUserId())
//                .set(UserInfo::getPassword,"666666");
//        boolean temp = sysUserService.update(wrapper);
//        if(temp){
//            return ResultVo.success("密码重置成功！！");
//        }
//        return  ResultVo.error("密码重置失败！！");
//    }

    //获取图片验证码
    @PostMapping("/getImage")
    public ResultVo getImage(HttpServletRequest request){
        //获取Session对象
        HttpSession session = request.getSession();
        //生成验证码
        String text = defaultKaptcha.createText();
        //存储在Session中
        session.setAttribute("code",text);
        System.out.println(session.getAttribute("code"));
        //生成图片
        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            //写入内存中
            ImageIO.write(image,"jpg",outputStream);
            //转换为Base64
            Base64.Encoder encoder = Base64.getEncoder();
            String base64 = encoder.encodeToString(outputStream.toByteArray());
            String str = "data:image/jpeg;base64,"+base64.replaceAll("\r\n","");

            return ResultVo.success("验证码获取成功！",str);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //登录
    @PostMapping("/login")
    public ResultVo login(HttpServletRequest request,@RequestBody LoginParam param){
        //获取前端传递过来的验证码
        String code1 = param.getCode();
        System.out.println(code1);
        //获取Session
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("code"));
        String code2 = (String)session.getAttribute("code");
        //判断session中有无验证码
        if(StringUtils.isEmpty(code2)){
            return ResultVo.error("验证码过期！");
        }
        //两个验证码是否相同
        if(!code2.equals(code1)){
            return ResultVo.error("验证码不正确！");
        }

        //查询条件
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getUsr_account,param.getUsr_account())
                .eq(UserInfo::getUsr_psd,param.getUsr_psd());
        UserInfo user = loginService.getOne(wrapper);
        if(user == null){
            return ResultVo.error("账号或密码不正确！");
        }
        //检查当天是否已登录
        boolean alreadyLoggedInToday = loginService.checkLoginToday(user.getUsr_id());

        //返回用户信息和token
        LoginVo vo = new LoginVo();
        vo.setUsr_id(user.getUsr_id());
        vo.setUsr_name(user.getUsr_name());
        vo.setIs_admin(user.getIs_admin());
        //生成token
        vo.setToken(jwtUtils.createToken(String.valueOf(user.getUsr_id()), user.getUsr_name()));

        if (!alreadyLoggedInToday) {
            //增加积分并更新最后登录时间
            loginService.updateLoginPointsAndTime(user.getUsr_id());
            return ResultVo.success("登录成功！今日首次登录，积分已增加！", vo);
        }
        return ResultVo.success("登录成功！", vo);
    }





}
