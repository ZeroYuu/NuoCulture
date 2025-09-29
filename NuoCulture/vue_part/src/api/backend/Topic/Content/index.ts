import http from "@/http";
import { type TopicContent,type ContentListParam } from "@/api/backend/Topic/Content/ContentModel";

//新增
export const addApi = (param: TopicContent) => {
    // console.log(param)
    return http.post("/api/cont", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/cont/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/cont/allDel/${id}`)
}

//编辑
export const editApi = (param: TopicContent) => {
    // console.log(param)
    return http.put("/api/cont", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/cont/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/cont/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: ContentListParam)=>{
    return http.get("/api/cont/getList",param)
}

export const getImagesApi = (id: string) => {
    return http.get(`/api/cont/getImages/${id}`)
}

//获取文化类型的名称
export const getTypeNameApi = (id: string) => {
    return http.get(`/api/cont/getTypeName/${id}`)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/cont/getAccount/${id}`)
}

//获取类型下拉
export const getSelectTypeApi = () => {
    return http.get("/api/cont/selectTypeList")
}
//获取标题下拉
export const getSelectTitleApi = () => {
    return http.get("/api/cont/selecTitletList")
}
//获取用户下拉
export const getSelectUsrApi = () => {
    return http.get("/api/cont/selectUsrList")
}