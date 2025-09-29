import http from "@/http";
import { type GoodsType,type TypeListParam } from "@/api/backend/Goods/GoodsType/TopicTypeModel";
//新增
export const addApi = (param: GoodsType) => {
    // console.log(param)
    return http.post("/api/godstype", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/godstype/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/godstype/allDel/${id}`)
}

//编辑
export const editApi = (param: GoodsType) => {
    // console.log(param)
    return http.put("/api/godstype", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/godstype/upload", param)
}


//获取角色列表
export const getListApi = (param: TypeListParam)=>{
    return http.get("/api/godstype/getList",param)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/godstype/getImages/${id}`)
}

//获取角色下拉
export const getCodeSelectApi = () => {
    return http.get("/api/godstype/getCodeSelectList")
}

export const getNameSelectApi = () => {
    return http.get("/api/godstype/getNameSelectList")
}