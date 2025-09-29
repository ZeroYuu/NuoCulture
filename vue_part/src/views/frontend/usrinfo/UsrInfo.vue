<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <el-empty description="暂时没有数据" v-if="detail.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div style="display: flex;">

                <div class="card" :model="detail" v-if="detail !== undefined">
                    <div class="top">
                        <div class="usrinfo">
                            <el-avatar :src="detail.usr_avater" class="avater" icon="Avatar" style="font-size: 150px" />
                            <div style="display: flex;flex-direction: column; margin: 0 50px;">
                                <el-text class="name">{{ detail.usr_name }}</el-text>

                                <el-text class="account">{{ detail.usr_account }}</el-text>

                                <el-text class="money">
                                    <img src="@/assets/images/money1.png" alt="">
                                    <el-text>:{{ detail.usr_points }}积分</el-text>
                                </el-text>
                            </div>
                        </div>
                        <div class="icon">
                            <el-icon @click="navigateToDetail" :size="50" color="#1d4350"><ShoppingCart /></el-icon>
                            <el-icon :size="50" @click="star" color="#1d4350">
                                <component :is="showStar ? 'Star' : 'StarFilled'" />
                            </el-icon>
                            <el-icon :size="50" @click="address" color="#1d4350">
                                <component :is="showAddress ? 'Location' : 'LocationFilled'" />
                            </el-icon>
                            <el-dropdown>


                                <el-icon :size="50" color="#1d4350">
                                    <component :is="showSetting ? 'Setting' : 'Tools'" />
                                </el-icon>


                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="usreditinfo">修改信息</el-dropdown-item>
                                        <el-dropdown-item @click="usreditpsd">修改密码</el-dropdown-item>
                                        <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                    </div>





                    <el-menu mode="horizontal" :default-active="infoDefaultActive" router :ellipsis="false">
                        <template v-for="menu in menuList" :key="menu.path">
                            <el-menu-item :index="menu.path">
                                <template #title>{{ menu.meta.title }}</template>
                            </el-menu-item>
                        </template>
                    </el-menu>

                    <div class="main">
                        <router-view></router-view>
                    </div>


                </div>

            </div>
        </el-skeleton>

    </div>

</template>

<script setup lang="ts">
import { computed } from 'vue';

//引入路由
import { useRoute } from "vue-router";
const route = useRoute();

//获取路由对象
const infoDefaultActive = computed(() => {
    const { path } = route;
    if (path.includes(menuList[0].path) || path.includes(menuList[1].path) || path.includes(menuList[2].path)) {
        showAddress.value = true;
        showStar.value = true;
        showSetting.value = true;
    } else {
        if (path == "/usrinfo/usrstar") {
            showStar.value = false;
        }
        else if (path == "/usrinfo/usraddress") {
            showAddress.value = false;
        } else {
            showSetting.value = false;
        }
    }
    console.log("path:" + path.split('/')[1] + path.split('/')[2]);
    return '/' + path.split('/')[1] + '/' + path.split('/')[2];

});
let menuList = reactive([
    {
        path: "/usrinfo/usractive",
        name: "usractive",
        // component: () => import("@/views/frontend/usrinfo/ShowPart/UserActive.vue"),
        meta: {
            title: "参与活动",
            roles: ["front:usractive"],
        }
    },
    {
        path: "/usrinfo/usertopic",
        name: "usertopic",
        // component: () => import("@/views/frontend/usrinfo/ShowPart/UserTopic.vue"),
        meta: {
            title: "发布话题",
            roles: ["front:usrcart"],
        }
    },
    {
        path: "/usrinfo/usrorder",
        name: "usrorder",
        // component: () => import("@/views/frontend/usrinfo/ShowPart/UserOrder.vue"),
        meta: {
            title: "我的订单",
            roles: ["front:usrcart"],
        }
    }
])





import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

import { getAccountApi, JwtToken } from '@/api/frontend/usrinfo/index';
import { onMounted, reactive, ref, watch } from 'vue';

import { userStore } from '@/store/user/index'
const user = userStore()

const navigateToDetail = () => {

    router.push({ name: 'cart' });
};
const showEmpty = ref(false)
const loading = ref(true)


const detail: any = ref([{}]);
const getDetail = async () => {

    try {
        await JwtToken();
        // 发送请求
        let res = await getAccountApi(user.usr_id);
        if (res.code == 200) {
            // 如果请求成功，设置表格数据
            detail.value = res.data;
            console.log(detail.value);
            loading.value = false;
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }



}


import { useRouter } from 'vue-router';

const router = useRouter();

const showStar = ref(true)
const showAddress = ref(true)
const showSetting = ref(true)
const star = () => {
    showStar.value = false;
    showAddress.value = true;
    showSetting.value = true;
    router.push({ name: 'usrstar' });
}
const address = () => {
    showAddress.value = false;
    showStar.value = true;
    showSetting.value = true;
    router.push({ name: 'usraddress' });
}
const usreditinfo = () => {
    showSetting.value = false;
    showAddress.value = true;
    showStar.value = true;
    router.push({ name: 'usreditinfo' });
}
const usreditpsd = () => {
    showSetting.value = false;
    showAddress.value = true;
    showStar.value = true;
    router.push({ name: 'usreditpsd' });
}
const exit = () => {
    sessionStorage.clear();
    window.location.href = '/login';
}


onMounted(() => {
    getDetail();
});

</script>
<style scoped>
.contain {
    width: auto;
    margin: 50px 50px;


    .el-breadcrumb {
        margin-bottom: 20px;
    }
}

.card {
    border-radius: 10px;
    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    width: 100%;
    height: 100%;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;

    .top {
        display: flex;
        margin-left: 50px;
        justify-content: space-between;

        .usrinfo {
            display: flex;

            .avater {
                width: 200px;
                height: 200px;
            }

            .name {
                font-size: 60px;
                color: #493131;
                text-align: left;
                width: 100%;
            }

            .account {
                font-size: 30px;
                text-align: left;
                width: 100%;
            }

            .money {
                font-size: 20px;
                text-align: left;
                width: 100%;
                display: flex;
                align-items: center;
                margin-top: 10px;

                img {
                    width: 30px;
                    height: 30px;
                    margin-right: 10px;
                }

                .el-text {
                    font-size: 20px;
                }
            }
        }


        .icon {
            margin-right: 30px;
            display: flex;
            align-items: center;

            .el-icon {
                padding: 0 10px;
            }
        }
    }

    .el-menu {
        width: 100%;
        max-width: 100%;
        padding: 0px;
        display: flex;
        --el-menu-hover-bg-color: #464046b4;
        --el-menu-hover-text-color: #e9e7ef;
        --el-menu-text-color: #1d4350;
        --el-menu-active-color: #1d4350 !important;
        font-size: 30px;
        /* justify-content: space-evenly; */
        margin-top: 20px;


        /* 水平居中 */
        align-items: center;
        /* 垂直居中（如果需要） */
        height: 60px;

        .el-menu-item.is-active:focus {
            background-color: transparent;
        }
    }

}
</style>