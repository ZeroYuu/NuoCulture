import { type RouteRecordRaw, createRouter, createWebHistory } from "vue-router";
import Test from '@/components/HelloWorld.vue'
import HR from '@/components/HelloWorld.vue'
import Administrator from '@/Administrator/Administrator.vue'
import Client from "@/Client/Client.vue";

import { h, resolveComponent } from "vue";

//定义一个数组
const routes: Array<RouteRecordRaw> = [
    {
        path: '/test',
        name: 'Test',
        component: Test
    },
    {
        path: '/hr',
        name: 'HR',
        component: HR
    },

    {
        path: '/login',
        name: 'login',
        component: () => import("@/views/login/Login.vue"),
    },
    {
        path: '/register',
        name: 'register',
        component: () => import("@/views/login/Register.vue"),
    },
    {
        path: '/administrator',
        name: 'Administrator',
        component: Administrator,
        redirect: '/console',
        children: [
            {
                path: "/console",
                name: "console",
                component: () => import("@/views/backend/Console.vue"),
                meta: {
                    title: "控制台",
                    icon: "House",
                },
            }
        ]
    },
    {
        path: "/user",
        name: "user",
        component: Administrator,
        meta: {
            title: "用户", //账号管理 用户地址
            icon: "User",
            roles: ["sys:user"],
        },
        children: [
            {
                path: "/accountList",
                name: "accountList",
                component: () => import("@/views/backend/user/account/AccountList.vue"),
                meta: {
                    title: "账户管理",
                    icon: "Coordinate",
                    roles: ["sys:account"],
                },
            },
            {
                path: "/AddressList",
                name: "AddressList",
                component: () => import("@/views/backend/user/address/AddressList.vue"),
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
        component: Administrator,
        meta: {
            title: "傩文化", //首页轮播图 文化类型 文化资讯 
            icon: "Picture",
            roles: ["sys:beculture"],
        },
        children: [
            {
                path: "/carouselList",
                name: "carouselList",
                component: () => import("@/views/backend/culture/carousel/CarouselList.vue"),
                meta: {
                    title: "首页轮播图",
                    icon: "Film",
                    roles: ["sys:carouselList"],
                },
            },
            {
                path: "/culturetypeList",
                name: "culturetypeList",
                component: () => import("@/views/backend/culture/culturetype/CultureTypeList.vue"),
                meta: {
                    title: "傩文化类型",
                    icon: "MessageBox",
                    roles: ["sys:culturetypeList"],
                },
            },
            {
                path: "/messageList",
                name: "messageList",
                component: () => import("@/views/backend/culture/message/MessageList.vue"),
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
        component: Administrator,
        meta: {
            title: "研学活动", //活动发布 精彩瞬间 活动审核
            icon: "Guide",
            roles: ["sys:beactive"],
        },
        children: [
            {
                path: "/publishList",
                name: "publishList",
                component: () => import("@/views/backend/active/publish/PublishList.vue"),
                meta: {
                    title: "活动发布",
                    icon: "Medal",
                    roles: ["sys:publishList"],
                },
            },
            {
                path: "/momentList",
                name: "momentList",
                component: () => import("@/views/backend/active/moment/MomentList.vue"),
                meta: {
                    title: "精彩瞬间",
                    icon: "Camera",
                    roles: ["sys:momentList"],
                },
            },
            {
                path: "/auditList",
                name: "auditList",
                component: () => import("@/views/backend/active/audit/AuditList.vue"),
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
        component: Administrator,
        meta: {
            title: "话题讨论", //话题类型 话题管理 话题评论
            icon: "Dessert",
            roles: ["sys:betopic"],
        },
        children: [
            {
                path: "/topictypeList",
                name: "topictypeList",
                component: () => import("@/views/backend/topic/topictype/TopicTypeList.vue"),
                meta: {
                    title: "话题类型",
                    icon: "Position",
                    roles: ["sys:topictypeList"],
                },
            },
            {
                path: "/contentList",
                name: "contentList",
                component: () => import("@/views/backend/topic/content/ContentList.vue"),
                meta: {
                    title: "内容管理",
                    icon: "ChatSquare",
                    roles: ["sys:contentList"],
                },
            },
            {
                path: "/reviewList",
                name: "reviewList",
                component: () => import("@/views/backend/topic/review/ReviewList.vue"),
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
        component: Administrator,
        meta: {
            title: "文化周边", //商品类型 周边商品 周边订单
            icon: "Goods",
            roles: ["sys:begoods"],
        },
        children: [
            {
                path: "/goodstypeList",
                name: "goodstypeList",
                component: () => import("@/views/backend/goods/goodstype/GoodsTypeList.vue"),
                meta: {
                    title: "商品类型",
                    icon: "Coin",
                    roles: ["sys:goodstypeList"],
                },
            },
            {
                path: "/commodityList",
                name: "commodityList",
                component: () => import("@/views/backend/goods/commodityList/commodityList.vue"),
                meta: {
                    title: "周边商品",
                    icon: "ShoppingTrolley",
                    roles: ["sys:commodityList"],
                },
            },
            {
                path: "/orderList",
                name: "orderList",
                component: () => import("@/views/backend/goods/order/OrderList.vue"),
                meta: {
                    title: "周边订单",
                    icon: "Wallet",
                    roles: ["sys:orderList"],
                },
            },
        ]
    },




    {
        path: '/client',
        name: 'client',
        component: Client,
        redirect: '/home',
        children: [
            {
                path: "/home",
                name: "home",
                component: () => import("@/views/frontend/home/Home.vue"),
                meta: {
                    title: "首页",
                },
            },
            {
                path: "/frontculture",
                name: "frontculture",
                component: { render: () => h(resolveComponent("router-view")) },
                redirect: '/culture',
                meta: {
                    title: "傩文化资讯",
                    roles: ["front:culture"],
                },
                children: [
                    {
                        path: "/culture",
                        name: "culture",
                        component: () => import("@/views/frontend/culture/Culture.vue"),
                    },
                    {
                        path: "/culture/detial/:id",
                        name: "frontculturedetial",
                        component: () => import("@/views/frontend/culture/CultureDetail.vue"),
                        props: true,
                        meta: {
                            title: "资讯详情",
                            roles: ["front:frontculturedetial"],
                        }
                    },
                ]
            },
            {
                path: "/frontactive",
                name: "frontactive",
                component: { render: () => h(resolveComponent("router-view")) },
                redirect: '/active',
                // component: () => import("@/views/frontend/active/Active.vue"),
                meta: {
                    title: "研学活动",
                    roles: ["front:active"],
                },
                children: [
                    {
                        path: "/active",
                        name: "active",
                        component: () => import("@/views/frontend/active/Active.vue"),

                    },
                    {
                        path: "/active/detial/:id",
                        name: "frontactivedetial",
                        component: () => import("@/views/frontend/active/ActiveDetial.vue"),
                        props: true,
                        meta: {
                            title: "活动详情",
                            roles: ["front:frontactivedetial"],
                        }
                    },
                ]
            },
            {
                path: "/fronttopic",
                name: "fronttopic",
                component: { render: () => h(resolveComponent("router-view")) },
                redirect: '/topic',
                // component: () => import("@/views/frontend/topic/Topic.vue"),
                meta: {
                    title: "话题讨论",
                    roles: ["front:topic"],
                },
                children: [
                    {
                        path: "/topic",
                        name: "topic",
                        component: () => import("@/views/frontend/topic/Topic.vue"),
                    },
                    {
                        path: "/topic/detial/:id",
                        name: "fronttopicdetial",
                        component: () => import("@/views/frontend/topic/TopicDetial.vue"),
                        props: true,
                        meta: {
                            title: "话题详情",
                            roles: ["front:fronttopicdetial"],
                        }
                    },
                ]
            },
            {
                path: "/frontgoods",
                name: "frontgoods",
                component: { render: () => h(resolveComponent("router-view")) },
                redirect: '/goods',
                // component: () => import("@/views/frontend/goods/Goods.vue"),
                meta: {
                    title: "傩文化周边",
                    roles: ["front:goods"],
                },
                children: [
                    {
                        path: "/goods",
                        name: "goods",
                        component: () => import("@/views/frontend/goods/Goods.vue"),
                    },
                    {
                        path: "/goods/detial/:id",
                        name: "frontgoodsdetial",
                        component: () => import("@/views/frontend/goods/GoodsDetial.vue"),
                        props: true,
                        meta: {
                            title: "周边详情",
                            roles: ["front:frontgoodsdetial"],
                        }
                    },
                    {
                        path: "/goods/cart",
                        name: "cart",
                        // component: () => import("@/views/frontend/goods/GoodsCart.vue"),
                        component: { render: () => h(resolveComponent("router-view")) },
                        redirect: '/goods/cart',
                        meta: {
                            title: "购物车",
                            roles: ["front:cart"],
                        }, 
                        children: [
                            {
                                path: "/goods/cart",
                                name: "frontgoodscart",
                                component: () => import("@/views/frontend/goods/GoodsCart.vue"),
                            },
                            {
                                path: "/goods/cart/order",
                                name: "frontgoodsorder",
                                component: () => import("@/views/frontend/goods/GoodsOrder.vue"),
                                meta: {
                                    title: "订单详情",
                                    roles: ["front:frontgoodsdetial"],
                                },
                                props: true
                            },]
                    },
                ]
            },
            {
                path: "/usrinfo",
                name: "usrinfo",
                component: () => import("@/views/frontend/usrinfo/UsrInfo.vue"),
                redirect: "/usrinfo/usractive",
                meta: {
                    title: "个人中心",
                    roles: ["front:usrinfo"],
                },
                children: [
                    {
                        path: "/usrinfo/usractive",
                        name: "/usractive",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserActive.vue"),
                        meta: {
                            title: "参与活动",
                            roles: ["front:usractive"],
                        }
                    },
                    {
                        path: "/usrinfo/usraddress",
                        name: "usraddress",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserAddress.vue"),
                        meta: {
                            title: "我的地址",
                            roles: ["front:usraddress"],
                        }
                    },

                    {
                        path: "/usrinfo/usrorder",
                        name: "usrorder",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserOrder.vue"),
                        meta: {
                            title: "我的订单",
                            roles: ["front:usrcart"],
                        }
                    },
                    {
                        path: "/usrinfo/usrstar",
                        name: "usrstar",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserStar.vue"),
                        meta: {
                            title: "我的收藏",
                            roles: ["front:usrcart"],
                        }
                    },
                    {
                        path: "/usrinfo/usertopic",
                        name: "usertopic",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserTopic.vue"),
                        meta: {
                            title: "发布话题",
                            roles: ["front:usrcart"],
                        }
                    },
                    {
                        path: "/usrinfo/usreditinfo",
                        name: "usreditinfo",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserEditInfo.vue"),
                        meta: {
                            title: "基本信息",
                            roles: ["front:usrcart"],
                        }
                    },
                    {
                        path: "/usrinfo/usreditpsd",
                        name: "usreditpsd",
                        component: () => import("@/views/frontend/usrinfo/ShowPart/UserEditPsd.vue"),
                        meta: {
                            title: "密码修改",
                            roles: ["front:usrcart"],
                        }
                    },
                ]
            },


        ]
    },


]

//创建路由对象
const router = createRouter({
    history: createWebHistory(),
    routes
})

//导出
export default router