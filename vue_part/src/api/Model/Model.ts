//定义用户数据类型
export type UserAccount = {
    usr_id: string,
    usr_account: string,
    usr_name: string,
    usr_psd: string,
    usr_phone: string,
    usr_email: string,
    usr_bday: string,
    usr_avater: string,
    is_admin: string,
}

export type UserListParam = {
    curPage:number,
    pageSize:number,
    usr_account:string,
    usr_name: string,
    usr_email:string,
    usr_phone: string,
    is_admin: string,
    usr_bday: string,
    // total:number
}