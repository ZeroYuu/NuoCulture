//定义用户数据类型
export type TopicReview = {
    topic_review_id: string,
    topic_id: string,
    usr_id: string,
    topic_review_content:string,
    topic_review_time:string,
}

export type ReviewListParam = {
    curPage:number,
    pageSize:number,

    topic_id:string,
    usr_id:string,
    topic_review_time:string,
    usr_account:string,
    usr_name:string,



    // total:number
}