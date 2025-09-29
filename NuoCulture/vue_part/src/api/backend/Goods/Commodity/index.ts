import http from "@/http";
import { type GoodsCommdity,type CommdityListParam } from "@/api/backend/Goods/Commodity/CommodityModel";

//新增
export const addApi = (param: GoodsCommdity) => {
    // console.log(param)
    return http.post("/api/comd", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/comd/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/comd/allDel/${id}`)
}

//编辑
export const editApi = (param: GoodsCommdity) => {
    // console.log(param)
    return http.put("/api/comd", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/comd/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/comd/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: CommdityListParam)=>{
    return http.get("/api/comd/getList",param)
}

export const getImagesApi = (id: string) => {
    return http.get(`/api/comd/getImages/${id}`)
}

export const getColorApi = (id: string) => {
    return http.get(`/api/comd/getColor/${id}`)
}

//获取文化类型的名称
export const getTypeNameApi = (id: string) => {
    return http.get(`/api/comd/getTypeName/${id}`)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/comd/getAccount/${id}`)
}

//获取类型下拉
export const getSelectTypeApi = () => {
    return http.get("/api/comd/selectTypeList")
}
//获取标题下拉
export const getSelectTitleApi = () => {
    return http.get("/api/comd/selecTitletList")
}
