import { defineStore } from 'pinia'

//定义一个store
export const userStore = defineStore('userStore', {
    state: () => { //定义
        return {
            usr_id: '',
            usr_name: '',
            token: '',
            is_admin: '',
        }
    },
    getters: {  //获取 
        getUsrId(state) {
            return state.usr_id
        },
        getUsrName(state) {
            return state.usr_name
        },
        getToken(state) {
            return state.token
        },
        getAdmin(state) {
            return state.is_admin
        }
    },
    actions: {  //操作
        setUserId(userId: string) {
            this.usr_id = userId
        },
        setUsrName(UsrName: string) {
            this.usr_name = UsrName
        },
        setToken(token: string) {
            this.token = token
        },
        setAdmin(token: string) {
            this.is_admin = token
        },
    },
    persist: {
        key: 'userStore', //存储名称
        storage: sessionStorage, // 存储方式
        paths: ['usr_id', 'usr_name','token','is_admin'], //指定 state 中哪些数据需要被持久化。[] 表示不持久化任何状态，undefined 或 null 表示持久化整个 state
    },
})