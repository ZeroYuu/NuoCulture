import http from "@/http";
import { type CultureMessage,type MessageListParam } from "@/api/backend/Culture/Message/MessageModel";

//新增
export const addApi = (param: CultureMessage) => {
    // console.log(param)
    return http.post("/api/msg", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/msg/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/msg/allDel/${id}`)
}

//编辑
export const editApi = (param: CultureMessage) => {
    // console.log(param)
    return http.put("/api/msg", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/msg/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/msg/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: MessageListParam)=>{
    return http.get("/api/msg/getList",param)
}

//获取文化类型的名称
export const getTypeNameApi = (id: string) => {
    return http.get(`/api/msg/getTypeName/${id}`)
}

//获取类型下拉
export const getSelectTypeApi = () => {
    return http.get("/api/msg/selectTypeList")
}
//获取标题下拉
export const getSelectTitleApi = () => {
    return http.get("/api/msg/selecTitletList")
}