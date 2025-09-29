import http from "@/http";
import { type CultureCarouseL,type CarouseListParam } from "@/api/backend/Culture/Carouse/CarouseModel";

//新增
export const addApi = (param: CultureCarouseL) => {
    // console.log(param)
    return http.post("/api/cult", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/cult/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/cult/allDel/${id}`)
}

//编辑
export const editApi = (param: CultureCarouseL) => {
    // console.log(param)
    return http.put("/api/cult", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/cult/upload", param)
}

//获取角色列表
export const getListApi = (param: CarouseListParam)=>{
    return http.get("/api/cult/getList",param)
}


//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/cult/getAddrText/${id}`)
}

//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/cult/selectList")
}
