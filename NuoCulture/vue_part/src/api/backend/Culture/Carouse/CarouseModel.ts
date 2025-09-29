//定义用户数据类型
export type CultureCarouseL = {
    hp_id: string,
    hp_path: string,
    hp_title: string,
    hp_remark: string,
}

export type CarouseListParam = {
    curPage:number,
    pageSize:number,
    hp_title: string,
    hp_remark: string,
    // total:number
}