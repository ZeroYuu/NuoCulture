<template>
    <el-calendar class="calendar">
        <template #date-cell="{ data }">
            <p :class="{ 'is-selected': data.isSelected, 'is-logged-in': isLoggedInDate(data.day) }">
                {{ data.day.split('-').slice(1).join('-') }}<br />
                {{ isLoggedInDate(data.day) ? '已登录' : '' }}
                {{ data.isSelected ? '✔️' : '' }}
            </p>

        </template>
    </el-calendar>

    <el-card class="info" shadow="never">
        <template #header>用户信息</template>
        <div class="content">

            <el-avatar :src="accountList.usr_avater" :size="130" fit="cover" />
            <div class="content_rigth">
                <el-text class="name"> {{ getUsrName }}</el-text>
                <el-tag type="success">管理员</el-tag>
            </div>

        </div>
        <div class="wel">
            <el-text >欢迎您的到来！</el-text>
        </div>
        

    </el-card>


</template>

<style>
.is-selected {
    color: #1989fa;
}
</style>

<script setup lang="ts">

import { getAccountApi } from '@/api/frontend/usrinfo/index';
import { computed, onMounted, ref } from 'vue'
import { userStore } from '@/store/user/index'
const user = userStore()
const getUsrName = computed(() => {
    return user.getUsrName
})

//用户
const accountList: any = ref([]);
const getAccount = async (id: string) => {
    if (id == null) return ('')
    const res = await getAccountApi(id);
    accountList.value = res.data;
    return accountList.value;
}
const loginDates = ref(JSON.parse(localStorage.getItem('loginDates')) || []);
const saveLoginDates = () => {
    localStorage.setItem('loginDates', JSON.stringify(loginDates.value));
};
// 检查给定日期是否存在于登录日期数组中
const isLoggedInDate = (date: string) => loginDates.value.includes(date);
const getTodayDate = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

onMounted(() => {
    getAccount(user.getUsrId)
    if (!loginDates.value.includes(getTodayDate())) {
        loginDates.value.push(getTodayDate());
        saveLoginDates(); // 保存更新后的loginDates到localStorage
    }
})
</script>

<style lang="scss" scoped>
.info {
    .content {
        display: flex;
        border-bottom: 1px solid #e4e7ed;
        width: 100%;
        padding-bottom: 20px;
        height: 100%;

        --el-text-color-primary: #493131;

        .content_rigth {
            display: flex;
            padding-left: 20px;
            flex-direction: column;
        }

        .name {
            font-size: 30px;
            font-weight: 600px;
            margin: 10px;

        }
    }

    .wel {
        margin-top: 10px;
        line-height: 30px;
        height: 30px;
    }
}
</style>