<template>
    <div class="logo">
        <img :src="MenuLogo" alt="" class="img">
        <span v-if="show" class="title">{{ title }}</span>
    </div>
</template>

<script setup lang="ts">
import MenuLogo from "@/assets/images/logo.png"
import { watch, ref } from "vue";
const title = ref('傩文化管理平台')

//菜单栏展开与关闭
import { useMenuStore } from '@/store/menu/index';
const store = useMenuStore();

//展示与关闭时的延时
const show = ref(true)
watch(
    () => store.getCollapse,
    (collapse: boolean) => {
        if (!collapse) {
            setTimeout(() => {
                show.value = !collapse
            },300)
        } else {
            show.value = !collapse
        }
    })

</script>

<style lang="scss" scoped>
.logo {
    display: flex;
    width: 100%;
    height: 60px;
    background-color: #69615b;
    cursor: pointer;
    text-align: center;
    align-items: center;
    padding-left:7px;

    .img {
        width: 50px;
        height: 50px;
    }

    .title {
        color: white;
        margin: 0;
        font-size: 17px;
        font-weight: bold;
        line-height: 60px;
        padding: 0 10px;
    }
}
</style>