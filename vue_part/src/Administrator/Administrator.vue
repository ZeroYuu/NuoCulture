<template>
    <el-container class="administrator">
        <el-aside width="auto" class="aside">
            <el-scrollbar>
                <menu-bar></menu-bar>
            </el-scrollbar>
        </el-aside>
        <el-container>
            <el-header class="header">
                <Header></Header>
            </el-header>
            <el-scrollbar>
                <el-main class="main">
                    <router-view></router-view>
                </el-main>
            </el-scrollbar>
        </el-container>
    </el-container>
</template>

<script setup lang="ts">
import MenuBar from "@/Administrator/menu/MenuBar.vue"

//引入header组件
import Header from "@/Administrator/header/Header.vue"

</script>

<style lang="scss" scoped>
.administrator {
    height: 100%;

    .aside {
        // background-color:#3c4149;
        background-color: #b7a091;
        // background-image: linear-gradient(to bottom, #1d4350, #a43931);
    }

    .header {
        // background-color: rgb(254, 239, 219);
        background-color: #f2e7e5;
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
    }

    .main {
        background-color: white;
    }
}

.scrollbar-demo-item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
}

</style>