//定义用户数据类型
export type UserAddress = {
    addr_id: string,
    addr_name: string,
    addr_phone: string,
    addr_province: number,
    addr_city: number,
    addr_country: number,
    addr_detail: string,
    usr_id: string,
}

export type AddressListParam = {
    curPage:number,
    pageSize:number,
    // usr_account:string,
    addr_name: string,
    addr_phone: string,
    addr_province: number,
    addr_city: number,
    addr_country: number,
    addr_detail: string,
    usr_id: string,
    // usr_name: string,
    // total:number
}