//定义用户数据类型
export type GoodsOrder = {
    order_id: string,
    order_number: string,
    usr_id: string,
    addr_id: string,
    order_remark: string,
    order_track_num: string,
    order_appraise: string,
    goods_id: string,
    order_price: string,
    order_status: string,
    order_time: string,
}

export type OrderListParam = {
    curPage: number,
    pageSize: number,

    addr_name: string,
    addr_phone: string,
    addr_province: string,
    addr_city: string,
    addr_country: string,
    addr_detail: string,
    usr_id: string,
    payment_id: string,
    order_number: string,
    order_time: string,
    order_remark: string,
    order_track_num: string,
    order_status: string,

    // total:number
}