<template>
    <div class="footer">

        <el-menu mode="horizontal" :default-active="defaultActive" router :ellipsis="false">
            <template v-for="(menu,index) in menuList" :key="menu.path">
                <el-menu-item :index="menu.path">
                    <template #title>{{ menu.meta.title }}</template>
                </el-menu-item>
                <div v-if="index!==menuList.length-1" style="color: white;"> | </div>
            </template>
        </el-menu>
        <p>© 2025 傩文化数字平台 版权所有</p>
    </div>
</template>

<script setup lang="ts">
import { computed, reactive } from "vue";
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
import { useRoute } from "vue-router";
const route = useRoute();

//获取路由对象
const defaultActive = computed(() => {
    const { path } = route;
    console.log("path:" + path.split('/')[1]);
    return '/'+path.split('/')[1];
});

</script>

<style scoped>
.footer {
    text-align: center;
    background-image: url(@/assets/images/footer.png);
    /* justify-content: space-evenly; */
    background-size: cover;


    border-top: 1px solid #ddd;

    .el-menu {
        width: 100%;
        max-width: 100%;
        padding: 0px;
        display: flex;
        --el-menu-active-color: #e9e7ef !important;
        --el-menu-hover-bg-color:none;
        --el-menu-hover-text-color:#e9e7ef;
        --el-menu-bg-color: none;
        --el-menu-text-color: #e9e7ef;
        font-family: HANYIGUFENG;
        background-image: url();

        /* 水平居中 */
        align-items: center;

        justify-content: center;
        /* 垂直居中（如果需要） */




        .el-menu-item {
            font-size: 20px;
            color: rgb(255, 255, 255);
            padding: 0 30px;
        }

    }
    p{
        color: white;
        line-height: 30px;

    }
}
</style>