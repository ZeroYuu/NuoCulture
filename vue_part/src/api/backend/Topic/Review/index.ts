import http from "@/http";
import { type TopicReview,type ReviewListParam } from "@/api/backend/Topic/Review/ReviewModel";
//新增
export const addApi = (param: TopicReview) => {
    // console.log(param)
    return http.post("/api/rev", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/rev/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/rev/allDel/${id}`)
}

//编辑
export const editApi = (param: TopicReview) => {
    // console.log(param)
    return http.put("/api/rev", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/rev/upload", param)
}

//富文本上传
export const uploadRichTextApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/rev/uploadRichText", param)
}

//获取角色列表
export const getListApi = (param: ReviewListParam)=>{
    return http.get("/api/rev/getList",param)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/cont/getAccount/${id}`)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/rev/getImages/${id}`)
}

export const getTopicApi = (id: string) => {
    return http.get(`/api/rev/getTopic/${id}`)
}


//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/rev/getAddrText/${id}`)
}

//获取角色下拉
export const getSelectApi = () => {
    return http.get("/api/rev/selectList")
}
