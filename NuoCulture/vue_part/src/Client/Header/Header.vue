<template>
    <el-menu mode="horizontal" :default-active="defaultActive" router :ellipsis="false" >
        <div class="logo">
            <img src="@/assets/images/nuo.png" height="50px">
            <el-text class="text">
                傩文化数字平台
            </el-text>
        </div>

        <template v-for="menu in menuList" :key="menu.path">
            <el-menu-item :index="menu.path">
                <template #title>{{ menu.meta.title }}</template>
            </el-menu-item>
        </template>
    </el-menu>


</template>

<script setup lang="ts">
import { computed,reactive} from "vue";
let menuList = reactive([
    {
        path: '/home',
        name: 'Home',
        component: 'home',
        meta: {
            title: "首页",
            roles: ["front:home"],
        }
    },
    {
        path: "/culture",
        name: "culture",
        component: "/culture/frontculture",
        meta: {
            title: "傩文化资讯",
            roles: ["front:culture"],
        }
    },
    {
        path: "/active",
        name: "active",
        component: "/active/frontactive",
        meta: {
            title: "研学活动",
            roles: ["front:active"],
        }
    },
    {
        path: "/topic",
        name: "topic",
        component: "/active/frontactive",
        meta: {
            title: "话题讨论",
            roles: ["front:topic"],
        }
    },
    {
        path: "/goods",
        name: "goods",
        component: "/goods/frontgoods",
        meta: {
            title: "傩文化周边",
            roles: ["front:goods"],
        }
    },
    {
        path: "/usrinfo",
        name: "usrinfo",
        component: "/usrinfo/usrinfo",
        meta: {
            title: "个人中心",
            roles: ["front:usrinfo"],
        }
    },
])

//引入路由
import { useRoute,} from "vue-router";
const route = useRoute();

//获取路由对象
const defaultActive = computed(() => {
    const { path } = route;
    console.log("path:" + path.split('/')[1]);
    return '/'+path.split('/')[1];
});


</script>

<style scoped>
.el-menu {
    width: 100%;
    max-width: 100%;
    padding: 0px;
    display: flex;
    --el-menu-hover-bg-color: #464046b4;
    --el-menu-hover-text-color: #e9e7ef;
    --el-menu-text-color: white;
    --el-menu-active-color: #e9e7ef !important;
    font-family: HANYIGUFENG;
    background-image: linear-gradient(to right, #1d4350, #a43931);
    /* justify-content: space-evenly; */


    /* 水平居中 */
    align-items: center;
    /* 垂直居中（如果需要） */
    height: 100px;

    /* 设置容器高度，以便子元素垂直居中 */


    .logo {
        display: flex;
        align-items: center;

        img {
            margin-left: 50px;
            margin-right: 10px;
        }

        .text {
            font-size: 30px;
            font-weight: bold;
            color: rgb(255, 255, 255);
            /* width: 250px; */

        }
    }

    .el-menu-item {
        font-size: 28px;
        color: rgb(255, 255, 255);
        padding: 0 30px;
    }

    .el-menu-item:nth-child(2) {
        margin-left: auto;
    }

    .el-menu-item.is-active {
        border-bottom: 3px solid var(--el-menu-active-color);
        background-color:#493131;
    }
    .el-menu-item.is-active:focus {
        background-color: #493131;
    }

}
</style>