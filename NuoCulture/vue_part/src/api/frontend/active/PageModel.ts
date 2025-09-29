//定义用户数据类型

export type PageParam = {
    curPage:number,
    pageSize:number,
    // cul_id:string,
}
export type JoinActive={
    join_id:string,
    usr_id:string,
    actv_id:string,
    join_reason:string,
    join_status:string,
}