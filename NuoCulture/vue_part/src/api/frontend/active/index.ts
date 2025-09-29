import http from "@/http";
import { type JoinActive,type PageParam } from "@/api/frontend/active/PageModel";

//新增
export const addApi = (param: JoinActive) => {
    return http.post("/api/frontactv", param)
}

//删除
export const deleteApi = (id: string) => {
    // console.log(param)
    return http.delete(`/api/frontactv/${id}`)
}

//批量删除
export const allDeleteApi = (id: Array<String>) => {
    // console.log(param)
    return http.delete(`/api/frontactv/allDel/${id}`)
}

//取消报名
export const cancelApi = (id: string) => {
    // console.log(param)
    return http.put(`/api/frontactv/cancel/${id}`)
}



//获取角色列表
export const getListApi = (param: PageParam)=>{
    return http.get("/api/frontactv/getList",param)
}


export const getStarApi = (id: string,uid: string) => {
    return http.get(`/api/frontactv/getStar/${id}/${uid}`)
}

export const StarActvApi = (id: string,uid:string,star:boolean) => {
    return http.get(`/api/frontactv/StarActv/${id}/${uid}/${star}`)
}
export const getStarActvApi = (id: string) => {
    return http.get(`/api/frontactv/getStarActv/${id}`)
}


//获取id的用户信息
export const getDetailApi = (id: string) => {
    return http.get(`/api/frontactv/getDetail/${id}`)
}
export const getMomentApi = (id: string) => {
    return http.get(`/api/frontactv/getMoment/${id}`)
}
export const getImagesApi = (id: string) => {
    return http.get(`/api/frontactv/getImages/${id}`)
}

export const getCultureTypeApi = () => {
    return http.get("/api/frontactv/getCultureType")
}
