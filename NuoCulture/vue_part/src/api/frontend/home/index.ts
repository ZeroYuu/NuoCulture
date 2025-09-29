import http from "@/http";
// import { type CultureCarouseL,type CarouseListParam } from "@/api/backend/Culture/Carouse/CarouseModel";


//获取角色列表
// export const getListApi = (param: CarouseListParam)=>{
//     return http.get("/api/cult/getList",param)
// }

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/fronthome/getUsr/${id}`)
}
export const getImagesApi = () => {
    return http.get("/api/fronthome/getImages")
}
export const getCultureApi = () => {
    return http.get("/api/fronthome/getCulture")
}
export const getActiveApi = () => {
    return http.get("/api/fronthome/getActive")
}
export const getTopicApi = () => {
    return http.get("/api/fronthome/getTopic")
}
export const getGoodsApi = () => {
    return http.get("/api/fronthome/getGoods")
}