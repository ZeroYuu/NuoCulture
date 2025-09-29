import http from "@/http";
import { type PageParam ,type TopicReview} from "@/api/frontend/topic/PageModel";
import { type TopicContent,type ContentListParam } from "@/api/backend/Topic/Content/ContentModel";

//新增
export const addReviewApi = (param: TopicReview) => {
    return http.post("/api/fronttopic/addReview", param)
}
export const addtopicApi = (param: TopicContent) => {
    return http.post("/api/fronttopic/addTopic", param)
}


//添加浏览量
export const addViewApi = (id:string) => {
    return http.put(`/api/fronttopic/addView/${id}`)
}


//删除评论
export const delReviewApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/fronttopic/delReview/${id}`)
}


export const delTopicApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/fronttopic/delTopic/${id}`)
}
//批量删除
export const allDelTopicApi = (id: Array<String>) => {
    // console.log(param)
    return http.delete(`/api/fronttopic/allDelTopic/${id}`)
}

//编辑
export const editApi = (param: TopicContent) => {
    // console.log(param)
    return http.put("/api/fronttopic", param)
}




export const getStarApi = (id: string,uid: string) => {
    return http.get(`/api/fronttopic/getStar/${id}/${uid}`)
}

export const StarTopicApi = (id: string,uid:string,star:boolean) => {
    return http.get(`/api/fronttopic/StarTopic/${id}/${uid}/${star}`)
}

export const getStarTopicApi = (id: string) => {
    return http.get(`/api/fronttopic/getStarTopic/${id}`)
}



//获取角色列表
export const getListApi = (param: PageParam)=>{
    return http.get("/api/fronttopic/getList",param)
}

//获取id的用户信息
export const getDetailApi = (id: string) => {
    return http.get(`/api/fronttopic/getDetail/${id}`)
}
export const getImagesApi = (id: string) => {
    return http.get(`/api/fronttopic/getImages/${id}`)
}
export const getReviewApi = (id: string) => {
    return http.get(`/api/fronttopic/getReview/${id}`)
}
export const getAccountApi = (id: string) => {
    return http.get(`/api/fronttopic/getAccount/${id}`)
}

export const getTopicTypeApi = () => {
    return http.get("/api/fronttopic/getTopicType")
}
