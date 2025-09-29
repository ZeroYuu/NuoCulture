//定义用户数据类型

export type PageParam = {
    curPage:number,
    pageSize:number,
    // cul_id:string,
    goods_type_id:string,
    goods_status:string,

    // total:number
}

export type CartGoods = {
    cart_id:string,
    goods_id:string,
    goods_count:number,
    usr_id:string,
    goods_color_id:string,

}
export type OrderGoods = {
    order_id: string,
    usr_id: string,
    addr_id:string,
    order_remark: string,
    goods_id: string,
    goods_color_id: string,
    goods_count: number,
    payment_id: number,
    order_price: number,
    order_status: number,
}