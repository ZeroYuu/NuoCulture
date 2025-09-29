import { defineStore } from 'pinia'

//定义一个store
export const useMenuStore = defineStore('Menu', {
    state: () => {  //定义容器
        return{
            collapse:false
        }
    },
    getters: {  //获取数据
        getCollapse(state){
            return state.collapse
        }
    },
    actions: {   //赋值或操作容器
        setCollapse(collapse:boolean){
            this.collapse =collapse
        }
    },
})