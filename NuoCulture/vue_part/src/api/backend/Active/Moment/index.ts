import http from "@/http";
import { type CultureMoment,type MomentListParam } from "@/api/backend/Active/Moment/MomentModel";
//新增
export const addApi = (param: CultureMoment) => {
    // console.log(param)
    return http.post("/api/mom", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/mom/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/mom/allDel/${id}`)
}

//编辑
export const editApi = (param: CultureMoment) => {
    // console.log(param)
    return http.put("/api/mom", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/mom/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/mom/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: MomentListParam)=>{
    return http.get("/api/mom/getList",param)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/mom/getImages/${id}`)
}

export const getActvApi = (id: string) => {
    return http.get(`/api/mom/getActv/${id}`)
}



//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/mom/getAddrText/${id}`)
}

//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/mom/selectList")
}
