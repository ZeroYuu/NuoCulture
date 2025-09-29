//定义用户数据类型
export type CultureMessage = {
    cul_id: string,
    cul_title: string,
    cul_type_id: string,
    cul_pub: string,
    cul_content: string,
    cul_shelves: boolean,
    cul_recommend: boolean,
    cul_cover: string
}

export type MessageListParam = {
    curPage:number,
    pageSize:number,
    cul_title:string,
    cul_type_id: string,
    cul_pub:string,
    // total:number
}