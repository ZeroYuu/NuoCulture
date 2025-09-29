import http from "@/http";
import { type ActivePublish,type PublishListParam } from "@/api/backend/Active/Publish/PublishModel";

//新增
export const addApi = (param: ActivePublish) => {
    // console.log(param)
    return http.post("/api/pub", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/pub/${id}`)
}

export const delImgApi = (url: string) => {
    return http.delete(`/api/pub/delImg/${url}`)
}


//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/pub/allDel/${id}`)
}

//编辑
export const editApi = (param: ActivePublish) => {
    // console.log(param)
    return http.put("/api/pub", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/pub/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/pub/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: PublishListParam)=>{
    return http.get("/api/pub/getList",param)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/pub/getImages/${id}`)
}

//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/pub/getAddrText/${id}`)
}

//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/pub/selectList")
}
