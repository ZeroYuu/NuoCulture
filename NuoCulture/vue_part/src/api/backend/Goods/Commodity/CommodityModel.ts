//定义用户数据类型
export type GoodsCommdity = {
    goods_id: string,
    goods_name: string,
    goods_type_id: string,
    goods_price: string,
    goods_inventory: string,
    goods_shelves: boolean,
    goods_intro:string,
    img_path: string,
    goods_color_name:string,
}

export type CommdityListParam = {
    curPage:number,
    pageSize:number,

    goods_name:string,
    goods_type_id: string,
    goods_price:string,
    goods_inventory:string,
    goods_color_name:string,
    // total:number
}