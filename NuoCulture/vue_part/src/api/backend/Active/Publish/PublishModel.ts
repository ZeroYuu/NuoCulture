//定义用户数据类型
export type ActivePublish = {
    actv_id: string,
    actv_name: string,
    actv_addr: string,
    actv_region:string,
    actv_leader: string,
    actv_phone: string,
    actv_content: string,
    start_time: string,
    end_time: string,
    actv_img: string,
    actv_recommend: boolean,
}

export type PublishListParam = {
    curPage:number,
    pageSize:number,
    actv_name:string,
    actv_addr: string,
    actv_leader:string,
    actv_phone: string,
    start_time: string,
    end_time: string,
    // total:number
}

export type deleteImg = {
    url:string,
}