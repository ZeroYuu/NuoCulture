//定义用户数据类型
export type EditPsd = {
    usr_psd: string,
    new_psd: string,
    confirm_psd: string,
    usr_id: string,
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
export type ReviewGoods = {
    goods_review_id: string,
    goods_review_content: string,
    order_id:string,
}
