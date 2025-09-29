//定义用户数据类型
export type GoodsType = {
    goods_type_id: string,
    goods_type_name:string,
    goods_type_encode: string,
    goods_type_remark:string,
}

export type TypeListParam = {
    curPage:number,
    pageSize:number,

    goods_type_name:string,
    goods_type_encode: string,
    goods_type_remark:string,

    // total:number
}