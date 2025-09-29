//定义用户数据类型

export type PageParam = {
    curPage:number,
    pageSize:number,
    // cul_id:string,
    topic_type_id:string,
    topic_status:string,
    // total:number
}
export type TopicReview={
    topic_review_id:string,
    topic_id:string,
    usr_id:string,
    topic_review_content:string,
}