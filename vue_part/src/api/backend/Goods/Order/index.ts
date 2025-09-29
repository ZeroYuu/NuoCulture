import http from "@/http";
import { type GoodsOrder,type OrderListParam } from "@/api/backend/Goods/Order/OrderModel";

//新增
export const addApi = (param: GoodsOrder) => {
    // console.log(param)
    return http.post("/api/ord", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/ord/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/ord/allDel/${id}`)
}

//编辑
export const deliverApi = (param: GoodsOrder) => {
    // console.log(param)
    return http.put("/api/ord/deliver", param)
}
export const remarkApi = (param: GoodsOrder) => {
    // console.log(param)
    return http.put("/api/ord/remark", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/ord/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/ord/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: OrderListParam)=>{
    return http.get("/api/ord/getList",param)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/ord/getAccount/${id}`)
}
//获取id的用户信息
export const getPaymentApi = (id: string) => {
    return http.get(`/api/ord/getPayment/${id}`)
}
//获取id的用户信息
export const getGoodsApi = (id: string) => {
    return http.get(`/api/ord/getGoods/${id}`)
}
export const getGoodsReviewApi = (id: string) => {
    return http.get(`/api/ord/getGoodsReview/${id}`)
}
//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/ord/getImages/${id}`)
}

//获取地址名称
export const getAddrApi = (id: string) => {
    return http.get(`/api/ord/getAddr/${id}`)
}

//获取角色下拉
export const getSelectPaymentApi = () => {
    return http.get("/api/ord/selectPaymentList")
}
