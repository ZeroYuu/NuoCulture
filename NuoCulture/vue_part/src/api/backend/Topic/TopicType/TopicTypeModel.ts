//定义用户数据类型
export type TopicType = {
    topic_type_id: string,
    topic_type_name:string,
    topic_type_encode: string,
    topic_type_remark:string,
}

export type TypeListParam = {
    curPage:number,
    pageSize:number,

    topic_type_name:string,
    topic_type_encode: string,
    topic_type_remark:string,

    // total:number
}