import http from "@/http";
import { type CultureType,type TypeListParam } from "@/api/backend/Culture/CultureType/CultureTypeModel";

//新增
export const addApi = (param: CultureType) => {
    // console.log(param)
    return http.post("/api/cultype", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/cultype/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/cultype/allDel/${id}`)
}

//编辑
export const editApi = (param: CultureType) => {
    // console.log(param)
    return http.put("/api/cultype", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/cultype/upload", param)
}

//获取角色列表
export const getListApi = (param: TypeListParam)=>{
    return http.get("/api/cultype/getList",param)
}

//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/cultype/getAddrText/${id}`)
}

//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/cultype/selectList")
}
