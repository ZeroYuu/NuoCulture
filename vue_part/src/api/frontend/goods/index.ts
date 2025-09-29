import http from "@/http";
import { type PageParam, type CartGoods,type OrderGoods } from "@/api/frontend/goods/PageModel";
//新增购物车
export const addCartApi = (param: CartGoods) => {
    // console.log(param)
    return http.post("/api/frontgoods/addCart", param)
}
//新增订单
export const addOrderApi = (param: OrderGoods,derictPay:boolean) => {
    // console.log(param)
    return http.post(`/api/frontgoods/addOrder/${derictPay}`, param)
}

//删除评论
export const delReviewApi = (id: string) => {
    // console.log(param)
    return http.delete(`/api/frontgoods/delReview/${id}`)
}
//购物车删除
export const delCartApi = (id: string) => {
    // console.log(param)
    return http.delete(`/api/frontgoods/delCart/${id}`)
}
//购物车全删
export const allDeleteCartApi = (id: Array<String>) => {
    // console.log(param)
    return http.delete(`/api/frontgoods/allDelCart/${id}`)
}

//订单删除
export const delOrderApi = (id: string) => {
    // console.log(param)
    return http.delete(`/api/frontgoods/delOrder/${id}`)
}
//订单全删
export const allDelOrderApi = (id: Array<String>) => {
    // console.log(param)
    return http.delete(`/api/frontgoods/allDelOrder/${id}`)
}





export const updateColorApi = (param: CartGoods) => {
    return http.put("/api/frontgoods", param)
}

export const getStarGoodsApi = (id: string) => {
    return http.get(`/api/frontgoods/getStarGoods/${id}`)
}

export const getStarApi = (id: string, uid: string) => {
    return http.get(`/api/frontgoods/getStar/${id}/${uid}`)
}
//购物车全收藏
export const allStarCartApi = (id: Array<String>, uid: string,cid: Array<String>) => {
    // console.log(param)
    return http.get(`/api/frontgoods/allStarCart/${id}/${uid}/${cid}`)
}

export const StarGoodsApi = (id: string, uid: string, star: boolean) => {
    return http.get(`/api/frontgoods/StarGoods/${id}/${uid}/${star}`)
}
export const StarCartApi = (id: string, uid: string,cid: string) => {
    return http.get(`/api/frontgoods/StarCart/${id}/${uid}/${cid}`)
}



//获取角色列表
export const getListApi = (param: PageParam) => {
    return http.get("/api/frontgoods/getList", param)
}

export const getCartListApi = (param: PageParam, id: string) => {
    return http.get(`/api/frontgoods/getCartList/${id}`, param)
}

export const getRecmdListApi = () => {
    return http.get("/api/frontgoods/getRecmdList")
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/frontgoods/getAccount/${id}`)
}
export const getCartDetailApi = (gid: string, gcid: string) => {
    return http.get(`/api/frontgoods/getCartDetail/${gid}/${gcid}`)
}

export const getDetailApi = (gid: string) => {
    return http.get(`/api/frontgoods/getDetail/${gid}`)
}
export const getImagesApi = (id: string) => {
    return http.get(`/api/frontgoods/getImages/${id}`)
}
export const getReviewApi = (id: string) => {
    return http.get(`/api/frontgoods/getReview/${id}`)
}
export const getGoodsTypeNameApi = (id: string) => {
    return http.get(`/api/frontgoods/getGoodsTypeName/${id}`)
}

export const getGoodsTypeApi = () => {
    return http.get("/api/frontgoods/getGoodsType")
}

export const selectTypeListApi = (id: string) => {
    return http.get(`/api/frontgoods/selectTypeList/${id}`)
}
