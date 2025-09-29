import http from "@/http";
import { type TopicType,type TypeListParam } from "@/api/backend/Topic/TopicType/TopicTypeModel";
//新增
export const addApi = (param: TopicType) => {
    // console.log(param)
    return http.post("/api/tpctype", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/tpctype/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/tpctype/allDel/${id}`)
}

//编辑
export const editApi = (param: TopicType) => {
    // console.log(param)
    return http.put("/api/tpctype", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/tpctype/upload", param)
}


//获取角色列表
export const getListApi = (param: TypeListParam)=>{
    return http.get("/api/tpctype/getList",param)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/tpctype/getImages/${id}`)
}

//获取角色下拉
export const getCodeSelectApi = () => {
    return http.get("/api/tpctype/getCodeSelectList")
}

export const getNameSelectApi = () => {
    return http.get("/api/tpctype/getNameSelectList")
}