//定义用户数据类型
export type CultureAudit = {
    join_id: string,
    usr_id: string,
    actv_id: string,
    join_reason: string,
    join_remark: string,
    join_status: string,
}

export type AuditListParam = {
    curPage:number,
    pageSize:number,
    usr_name:string,
    usr_account:string,
    actv_id: string,
    join_reason:string,
    join_remark: string,
    join_status: string,
    

    // total:number
}