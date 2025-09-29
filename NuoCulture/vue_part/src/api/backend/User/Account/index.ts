import http from "@/http";
import { type UserAccount,type UserListParam } from "@/api/backend/User/Account/UserModel";

//新增
export const addApi = (param: UserAccount) => {
    // console.log(param)
    return http.post("/api/usr", param)
}
//获取角色列表
export const getListApi = (param: UserListParam)=>{
    return http.get("/api/usr/getList",param)
}

//编辑
export const editApi = (param: UserAccount) => {
    // console.log(param)
    return http.put("/api/usr", param)
}

//删除
export const deleteApi = (usr_id:string) => {
    // console.log(param)
    return http.delete(`/api/usr/${usr_id}`)
}

//批量删除
export const allDeleteApi = (usr_id:Array<String>) => {
    // console.log(param)
    return http.delete(`/api/usr/allDel/${usr_id}`)
}

//上传
export const uploadApi = (param: FormData) => {
    console.log(param.get("file"))
    return http.upload("/api/usr/uploadAvatar", param)
}

