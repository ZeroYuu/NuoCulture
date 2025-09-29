//定义用户数据类型
export type CultureType = {
    cul_type_id: string,
    cul_type_path: string,
    cul_type_name: string,
    cul_type_remark: string,
}

export type TypeListParam = {
    curPage:number,
    pageSize:number,
    cul_type_name: string,
    cul_type_remark: string,
    // total:number
}