<template>
    <menu-top></menu-top>
    <el-menu :default-active="defaultActive" class="el-menu-vertical" background-color="#b7a091" router
        :collapse="isCollapse" @open="handleOpen" @close="handleClose">
        <menu-item :menuList="menuList"></menu-item>
    </el-menu>

</template>

<script setup lang="ts">
import { computed, reactive } from "vue";
//引入menuItem组件
import MenuItem from "@/Administrator/menu/MenuItem.vue";
import MenuTop from "@/Administrator/menu/MenuTop.vue";

//引入路由
import { useRoute } from "vue-router";
const route = useRoute();

//获取路由对象
const defaultActive = computed(() => {
    const { path } = route;
    console.log("path:" + path);
    return path;
});

// import { ref } from 'vue'
//定义响应数据
// const isCollapse = ref(true)

//引入MenuStore
import { useMenuStore } from '@/store/menu/index';
const store = useMenuStore();
const isCollapse = computed(() => {
    return store.collapse;
})
//展示
const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
};
//关闭
const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}

//定义一个数组
let menuList = reactive([
    {
        path: "/console",
        name: "console",
        component: "Layout",
        meta: {
            title: "控制台",
            roles: ["sys:console"],
        },
    },
    {
        path: "/user",
        name: "user",
        component: "Layout",
        meta: {
            title: "用户", //账号管理 用户地址
            icon: "User",
            roles: ["sys:user"],
        },
        children: [
            {
                path: "/accountList",
                name: "accountList",
                component: "/user/Account/AccountList",
                meta: {
                    title: "账户管理",
                    icon: "Coordinate",
                    roles: ["sys:account"],
                },
            },
            {
                path: "/AddressList",
                name: "AddressList",
                component: "/user/address/AddressList",
                meta: {
                    title: "用户地址",
                    icon: "Location",
                    roles: ["sys:address"],
                },
            }
        ]
    },
    {
        path: "/beculture",
        name: "beculture",
        component: "Layout",
        meta: {
            title: "傩文化", //首页轮播图 文化类型 文化资讯
            icon: "Picture",
            roles: ["sys:beculture"],
        },
        children: [
            {
                path: "/carouselList",
                name: "carouselList",
                component: "/culture/carousel/CarouselList",
                meta: {
                    title: "首页轮播图",
                    icon: "Film",
                    roles: ["sys:carouselList"],
                },
            },
            {
                path: "/culturetypeList",
                name: "culturetypeList",
                component: "/culture/culturetype/CultureTypeList",
                meta: {
                    title: "傩文化类型",
                    icon: "MessageBox",
                    roles: ["sys:culturetypeList"],
                },
            },
            {
                path: "/messageList",
                name: "messageList",
                component: "/culture/message/MessageList",
                meta: {
                    title: "傩文化资讯",
                    icon: "Message",
                    roles: ["sys:message"],
                },
            }
        ]
    },
    {
        path: "/beactive",
        name: "beactive",
        component: "Layout",
        meta: {
            title: "研学活动", //活动发布 精彩瞬间 活动审核
            icon: "Guide",
            roles: ["sys:beactive"],
        },
        children: [
            {
                path: "/publishList",
                name: "publishList",
                component: "/active/publish/PublishList",
                meta: {
                    title: "活动发布",
                    icon: "Medal",
                    roles: ["sys:publishList"],
                },
            },
            {
                path: "/momentList",
                name: "momentList",
                component: "/active/moment/MomentList",
                meta: {
                    title: "精彩瞬间",
                    icon: "Camera",
                    roles: ["sys:momentList"],
                },
            },
            {
                path: "/auditList",
                name: "auditList",
                component: "/active/audit/AuditList",
                meta: {
                    title: "活动审核",
                    icon: "TakeawayBox",
                    roles: ["sys:auditList"],
                },
            },
        ]
    },
    {
        path: "/betopic",
        name: "betopic",
        component: "Layout",
        meta: {
            title: "话题讨论", //话题类型 话题管理 话题评论
            icon: "Dessert",
            roles: ["sys:betopic"],
        },
        children: [
            {
                path: "/topictypeList",
                name: "topictypeList",
                component: "/topic/topictype/TopicTypeList",
                meta: {
                    title: "话题类型",
                    icon: "Position",
                    roles: ["sys:topictypeList"],
                },
            },
            {
                path: "/contentList",
                name: "contentList",
                component: "/topic/content/ContentList",
                meta: {
                    title: "内容管理",
                    icon: "ChatSquare",
                    roles: ["sys:contentList"],
                },
            },
            {
                path: "/reviewList",
                name: "reviewList",
                component: "/topic/review/ReviewList",
                meta: {
                    title: "话题评论",
                    icon: "Postcard",
                    roles: ["sys:reviewList"],
                },
            },
        ]
    },
    {
        path: "/begoods",
        name: "begoods",
        component: "Layout",
        meta: {
            title: "文化周边", //商品类型 周边商品 周边订单
            icon: "Goods",
            roles: ["sys:begoods"],
        },
        children: [
            {
                path: "/goodstypeList",
                name: "goodstypeList",
                component: "/goods/goodstype/GoodsTypeList",
                meta: {
                    title: "商品类型",
                    icon: "Coin",
                    roles: ["sys:goodstypeList"],
                },
            },
            {
                path: "/commodityList",
                name: "commodityList",
                component: "/goods/commodity/commodityList",
                meta: {
                    title: "周边商品",
                    icon: "ShoppingTrolley",
                    roles: ["sys:commodityList"],
                },
            },
            {
                path: "/orderList",
                name: "orderList",
                component: "/goods/order/OrderList",
                meta: {
                    title: "周边订单",
                    icon: "Wallet",
                    roles: ["sys:orderList"],
                },
            },
        ]
    },
])
</script>

<style lang="scss" scoped>
.el-menu {
    border-right: none;
    --el-menu-active-color:#c1f9eb;
    --el-menu-text-color: rgb(252, 251, 251);

    // .el-menu-item {
    //     // --el-menu-active-color: #e9e7ef;
    //     --el-menu-text-color: white;
    // }
}

.el-menu-vertical:not(.el-menu--collapse) {
    width: 220px;
}

:deep(.el-sub-menu .el-menu-item) {
    background: #a18c80;
}
:deep(.el-sub-menu .el-menu-item:hover) {
    background: var(--el-menu-hover-bg-color);
}
</style>