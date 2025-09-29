import http from "@/http";
import { type PageParam } from "@/api/frontend/culture/PageModel";


//获取角色列表
export const getListApi = (param: PageParam)=>{
    return http.get("/api/frontcult/getList",param)
}


export const getStarCulApi = (id: string) => {
    return http.get(`/api/frontcult/getStarCul/${id}`)
}
export const getStarApi = (id: string,uid: string) => {
    return http.get(`/api/frontcult/getStar/${id}/${uid}`)
}

export const StarCulApi = (id: string,uid:string,star:boolean) => {
    return http.get(`/api/frontcult/StarCul/${id}/${uid}/${star}`)
}




//获取id的用户信息
export const getDetailApi = (id: string) => {
    return http.get(`/api/frontcult/getDetail/${id}`)
}
export const getImagesApi = () => {
    return http.get("/api/frontcult/getImages")
}

export const getCultureTypeApi = () => {
    return http.get("/api/frontcult/getCultureType")
}
