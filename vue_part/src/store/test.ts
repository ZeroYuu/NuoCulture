import { defineStore } from 'pinia'
export const usetestStore = defineStore('test', {
    state: () => {  //定义容器
        return{
            count:1
        }
    },
    getters: {  //获取数据
        getCount(state){
            return state.count*2
        }
    },
    actions: {   //赋值或操作容器
        setCount(count:number){
            this.count =count
        }
    },
})