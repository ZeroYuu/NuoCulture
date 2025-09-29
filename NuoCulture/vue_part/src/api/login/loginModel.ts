//定义用户数据类型
export type Login = {

    usr_account: string,
    usr_psd: string,
    code: string,
}

export type Register = {
    usr_id: string,
    usr_account: string,
    usr_name: string,
    usr_psd: string,
    usr_phone: string,
    usr_email: string,
    usr_bday: string,
    usr_avater: string,
    is_admin: string,
}

