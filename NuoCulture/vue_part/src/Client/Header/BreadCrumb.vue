<template>
    <el-breadcrumb separator="/">
        <!-- <el-breadcrumb-item to="/console">控制台</el-breadcrumb-item> -->
        <el-breadcrumb-item v-for="item in tabs" :to="{ path: item.path}">{{ item.meta.title }}</el-breadcrumb-item>
    </el-breadcrumb>
</template>

<script setup lang="ts">

//引入路由
import { ref, watch, type Ref } from 'vue';
import { useRoute, type RouteLocationMatched } from 'vue-router';

//获取路由
const route = useRoute();

//定义数组 保存数据
const tabs: Ref<RouteLocationMatched[]> = ref([])

//获取面包屑数据
const getBreadCrumb = () => {
    let matched = route.matched.filter((item) => item.meta && item.meta.title)

    //获取第一个元素
    const one = matched[0]

    //判断是否时首页
    if (one.path == '/home') {
        matched = [];
        // tabs.value = matched;
    }
    tabs.value = matched;
}

getBreadCrumb()

//监听路由变化
watch(
    () => route.path,
    () => getBreadCrumb()
)

</script>

<style scoped>
.el-breadcrumb {
    font-size: 23px;
    color: black;
    font-weight: 400;
    padding-bottom: 10px;
}
</style>