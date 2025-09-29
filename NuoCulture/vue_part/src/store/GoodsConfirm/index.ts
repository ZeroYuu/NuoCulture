// src/store/GoodsConfirm/index.ts
import { defineStore } from 'pinia';

export const goodsStore = defineStore('goodsStore', {
    state: () => ({
        goodsList: [] as {
            goods_id: string;
            goods_name: string;
            goods_color_name: string;
            goods_color_id: string;
            goods_price: number;
            goods_count: number;
            goods_url: string;
            goods_inventory: number;
            direct_pay: boolean;
        }[],
    }),
    actions: {
        addGoods(data: {
            goods_id: string; goods_name: string; goods_color_name: string;goods_color_id: string;
            goods_price: number; goods_count: number; goods_url: string; goods_inventory: number;direct_pay: boolean;
        }) {
            this.goodsList.push(data);
        },
        clearGoods() {
            this.goodsList = [];
        },
    },
    persist: {
        key: 'goodsStore',
        storage: sessionStorage,
        paths: ['goodsList'], // 指定需要持久化的状态路径
    },
});