<template>
    <el-scrollbar class="fontscrollbar">
        <el-container class="client">
            <el-header class="header">
                <Header></Header>
            </el-header>
            <el-main class="main">
                <!-- <bread-crumb></bread-crumb> -->
                <router-view></router-view>
            </el-main>
            <el-footer class="footer">
                <Footer></Footer>
            </el-footer>
        </el-container>
    </el-scrollbar>
</template>

<script setup lang="ts">

//引入header组件
import Header from "@/Client/Header/Header.vue"
import Footer from "@/Client/Footder/Footer.vue";
// import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

</script>

<style lang="scss" scoped>
.client {
    width: 100%;
    min-height: 100vh;
    height: 100%;

    background-image: url("@/assets/images/bg.png");
    /* 背景图垂直、水平均居中 */
    background-position: center center;
    /* 背景图不平铺 */
    background-repeat: no-repeat;
    /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
    background-attachment: fixed;
    /* 让背景图基于容器大小伸缩 */
    background-size: 100% 100%;


    .header {
        display: flex;
        align-items: center;
        width: 100%;
        flex-wrap: wrap;
        height: 20%;

        padding: 0;
    }

    .main {
        padding: 0;
        height: 100%;
        overflow:unset;
    }

    .footer {
        // height
        padding:10px 0;
        // line-height: 100px;

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