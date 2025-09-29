package com.nuoculture.spring_part.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo {
    private String msg;
    private Integer code;
    private Object data;

    public static ResultVo vo(String msg,Integer code,Object data){
        return new ResultVo(msg,code,data);
    }

    //成功
    public static  ResultVo success(){
        return new ResultVo("请求成功！",StatusCode.SUCCESS_CODE,null);
    }
    public static  ResultVo success(String msg){
        return new ResultVo(msg,StatusCode.SUCCESS_CODE,null);
    }
    public static  ResultVo success(String msg,Object data){
        return new ResultVo(msg,StatusCode.SUCCESS_CODE,data);
    }
    public static  ResultVo success(String msg,Integer code,Object data){
        return new ResultVo(msg,code,data);
    }

    //失败
    public static  ResultVo error(){
        return new ResultVo("请求失败！",StatusCode.ERROE_CODE,null);
    }
    public static  ResultVo error(String msg){
        return new ResultVo(msg,StatusCode.ERROE_CODE,null);
    }
    public static  ResultVo error(String msg,Object data){
        return new ResultVo(msg,StatusCode.ERROE_CODE,data);
    }
    public static  ResultVo error(String msg,Integer code,Object data){
        return new ResultVo(msg,code,data);
    }


    //没登陆
    public static  ResultVo nologin(){
        return new ResultVo("未登录，请先登录！",StatusCode.NO_LOGIN,null);
    }
    public static  ResultVo nologin(String msg){
        return new ResultVo(msg,StatusCode.NO_LOGIN,null);
    }
    public static  ResultVo nologin(String msg,Object data){
        return new ResultVo(msg,StatusCode.NO_LOGIN,data);
    }
    public static  ResultVo nologin(String msg,Integer code,Object data){
        return new ResultVo(msg,code,data);
    }

}
