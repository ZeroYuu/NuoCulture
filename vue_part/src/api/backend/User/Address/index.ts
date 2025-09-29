import http from "@/http";
import { type UserAddress, type AddressListParam } from "@/api/backend/User/Address/AddressModel";

//新增
export const addApi = (param: UserAddress) => {
    console.log("addApi:"+param)
    return http.post("/api/addr", param)
}
//获取角色列表
export const getListApi = (param: AddressListParam) => {
    return http.get("/api/addr/getList", param)
}

//获取账号列表
export const getAccountApi = (usr_id: string) => {
    return http.get(`/api/addr/getUsr/${usr_id}`)
}

//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/addr/getAddrText/${id}`)
}

//编辑
export const editApi = (param: UserAddress) => {
    // console.log(param)
    return http.put("/api/addr", param)
}

//删除
export const deleteApi = (addr_id: string) => {
    // console.log(param)
    return http.delete(`/api/addr/${addr_id}`)
}

//角色下拉
export const getSelectApi = () => {
    return http.get("/api/addr/selectList")
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/addr/allDel/${id}`)
}
