//定义用户数据类型
export type TopicContent = {
    topic_id: string,
    topic_titile: string,
    topic_type_id: string,
    topic_content: string,
    usr_id: string,
    topic_recommend: boolean,
    topic_status:string,
    img_path: string,
    topic_remark:string,
}

export type ContentListParam = {
    curPage:number,
    pageSize:number,

    topic_titile:string,
    topic_type_id: string,
    topic_view:string,
    usr_name:string,
    usr_account:string,
    topic_status:string,
    topic_remark:string,
    // total:number
}