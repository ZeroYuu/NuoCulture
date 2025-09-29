import http from "@/http";
import { type Login,type Register } from "@/api/login/loginModel";

//新增
export const addApi = (param: Register) => {
    // console.log(param)
    return http.post("/api/login", param)
}


//验证码
export const getImageApi = () => {
    return http.post("/api/login/getImage")
}

//登录
export const loginApi = (param: Login) => {
    return http.post("/api/login/login",param)
}