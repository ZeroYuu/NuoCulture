//定义用户数据类型
export type CultureMoment = {
    mom_id: string,
    mom_content: string,
    actv_id: string,
}

export type MomentListParam = {
    curPage:number,
    pageSize:number,
    actv_id:string,
    // actv_addr: string,
    // actv_leader:string,
    // actv_phone: string,
    // start_time: string,
    // end_time: string,

    // total:number
}