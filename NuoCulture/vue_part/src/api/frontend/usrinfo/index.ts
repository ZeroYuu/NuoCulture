import http from "@/http";
import {type EditPsd,type ReviewGoods } from "@/api/frontend/usrinfo/UserInfoModel";
import { type UserAddress} from "@/api/backend/User/Address/AddressModel";
import { type UserAccount } from "@/api/backend/User/Account/UserModel";
import { type OrderGoods } from "@/api/frontend/goods/PageModel";

//新增
export const addAddrApi = (param: UserAddress) => {
    // console.log(param)
    return http.post("/api/usrinfo", param)
}
export const ReviewApi = (param: ReviewGoods) => {
    // console.log(param)
    return http.post("/api/usrinfo/review", param)
}

export const delAddrApi = (id: string) => {
    // console.log(param)
    return http.delete(`/api/usrinfo/delAddr/${id}`)
}
export const alldelAddrApi = (id: Array<String>) => {
    // console.log(param)
    return http.delete(`/api/usrinfo/alldelAddr/${id}`)
}

//收藏删除
export const delStarApi = (id: string) => {
    // console.log(param)
    return http.put(`/api/usrinfo/delStar/${id}`)
}
//收藏全删
export const allDelStarApi = (id: Array<String>) => {
    // console.log(param)
    return http.put(`/api/usrinfo/allDelStar/${id}`)
}

//编辑
export const editAddrApi = (param: UserAddress) => {
    // console.log(param)
    return http.put("/api/usrinfo/editAddr", param)
}
export const checkGoodsApi = (param: OrderGoods) => {
    // console.log(param)
    return http.put("/api/usrinfo/checkGoods", param)
}



//编辑用户信息
export const editUsrInfoApi = (param: UserAccount) => {
    // console.log(param)
    return http.put("/api/usrinfo/editUsrInfo", param)
}
//修改密码
export const editPsdApi = (param: EditPsd) => {
    // console.log(param)
    return http.put("/api/usrinfo/editPsd", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/usrinfo/uploadAvatar", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/usrinfo/uploadRichText", param)
}

//评价
export const getReviewApi = (id: string)=>{
    return http.get(`/api/usrinfo/getReview/${id}`)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/usrinfo/getAccount/${id}`)
}

//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/usrinfo/getAddrText/${id}`)
}
//获取地址名称
export const JwtToken = () => {
    return http.get("/api/usrinfo")
}




export const getUsrActvApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrActv/${id}`)
}
export const getUsrTopicApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrTopic/${id}`)
}
export const getUsrOrdApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrOrd/${id}`)
}
export const getUsrStarApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrStar/${id}`)
}
export const getUsrHeartApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrHeart/${id}`)
}
export const getUsrAddrApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrAddr/${id}`)
}
export const getOrderAddrApi = (id: string) => {
    return http.get(`/api/usrinfo/getOrderAddr/${id}`)
}
export const getUsrInfoApi = (id: string) => {
    return http.get(`/api/usrinfo/getUsrInfo/${id}`)
}




//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/usrinfo/selectList")
}
