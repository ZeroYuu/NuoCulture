import http from "@/http";
import { type CultureAudit,type AuditListParam } from "@/api/backend/Active/Audit/AuditModel";
//新增
export const addApi = (param: CultureAudit) => {
    // console.log(param)
    return http.post("/api/adu", param)
}

//删除
export const deleteApi = (id:string) => {
    // console.log(param)
    return http.delete(`/api/adu/${id}`)
}

//批量删除
export const allDeleteApi = (id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/adu/allDel/${id}`)
}

//编辑
export const editApi = (param: CultureAudit) => {
    // console.log(param)
    return http.put("/api/adu", param)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/adu/upload", param)
}


//获取角色列表
export const getListApi = (param: AuditListParam)=>{
    return http.get("/api/adu/getList",param)
}

//获取id的用户信息
export const getImagesApi = (id: string) => {
    return http.get(`/api/adu/getImages/${id}`)
}

export const getActvApi = (id: string) => {
    return http.get(`/api/adu/getActv/${id}`)
}

//获取id的用户信息
export const getAccountApi = (id: string) => {
    return http.get(`/api/adu/getAccount/${id}`)
}

//获取地址名称
export const getAddTextApi = (id: string) => {
    return http.get(`/api/adu/getAddrText/${id}`)
}

//获取角色下拉
export const getActvSelectApi = () => {
    return http.get("/api/adu/actvSelectList")
}

export const getUsrSelectApi = () => {
    return http.get("/api/adu/usrSelectList")
}