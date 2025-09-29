<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <el-empty description="暂时没有数据" v-if="detail.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="card" :model="detail" v-if="detail !== undefined">
                <el-carousel height="400px" direction="vertical" type="card">
                    <el-carousel-item v-for="item in srcList" :key="item">
                        <el-image :src="item" fit="cover" class="img" />
                    </el-carousel-item>
                </el-carousel>

                <div class="content" v-if="accountMap[detail.usr_id] !== undefined">
                    <div class="top">
                        <el-text class="title">{{ detail.topic_titile }}</el-text>

                        <div class="time">
                            <el-text>
                                浏览量:{{ detail.topic_view }}&nbsp;&nbsp;{{ detail.topic_time }}
                            </el-text>
                        </div>

                    </div>
                    <div class="text-clamp" v-html="detail.topic_content"></div>
                    <el-text class="pub">发布者：{{ accountMap[detail.usr_id][0].usr_name }}</el-text>

                    <div class="review">
                        <el-input v-model="addModel.topic_review_content" style="width: 100%" :autosize="{ minRows: 4 }"
                            type="textarea" placeholder="请文明发言" />
                        <div style="display: flex;align-items: center;margin-top: 25px;">
                            <el-button color="#4e3929" plain @click="publishBtn">发表</el-button>
                            <el-icon :size="35" style="margin-left: 10px;">
                                <component :is="showNew ? 'StarFilled' : 'Star'" @click="Star" />
                            </el-icon>
                        </div>




                        <el-text style="width: 100%; text-align: left;display: flex;padding-top: 20px;">共{{
                            reviewList.length }}条评论</el-text>
                        <div class="review-list" v-for="(item, index) in reviewList" :key="index">
                            <div class="avatar" v-if="accountMap[item.usr_id] !== undefined">
                                <el-avatar :src="accountMap[item.usr_id][0].usr_avater" alt="" />
                            </div>
                            <div class="review_content" v-if="accountMap[item.usr_id] !== undefined">
                                <el-text class="title">{{ accountMap[item.usr_id][0].usr_name }}</el-text>
                                <div v-html="item.topic_review_content" style=".text-clamp:deep(img) {width: 100%;}">
                                </div>
                                <div class="delete">
                                    <el-text>{{ item.topic_review_time }}</el-text>
                                    <el-button color="#4e3929" plain @click="deleteBtn(item.topic_review_id)" link
                                        v-if="user.usr_id == accountMap[item.usr_id][0].usr_id">删除</el-button>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

        </el-skeleton>

    </div>

</template>

<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps<{ id: string }>();

// 使用 props.id 来获取传递过来的 id
console.log('Detail ID:', props.id);
import { userStore } from '@/store/user/index'
const user = userStore()
import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

import {
    getDetailApi, getImagesApi, getReviewApi,
    addReviewApi, delReviewApi, getAccountApi, StarTopicApi, getStarApi
} from '@/api/frontend/topic/index';
import { onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const showNew = ref(false)
import { JwtToken } from '@/api/frontend/usrinfo/index';
import { useRouter } from 'vue-router';
const router = useRouter();
const Star = async () => {
    try {
        await JwtToken();
        // 发送请求
        showNew.value = !showNew.value
        let res = await StarTopicApi(props.id, user.usr_id, showNew.value)
        if (res.code == 200) {
            console.log(res.msg);
            ElMessage.success(res.msg)
            // getStar()
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}
const getStar = async () => {
    if (user.usr_id != '') {

        let res = await getStarApi(props.id, user.usr_id)
        if (res.code == 200) {
            console.log(res.data);
            showNew.value = res.data;
        }
    }

}


const showEmpty = ref(false)
const loading = ref(true)

const detail: any = ref([{}]);
const getDetail = async () => {
    //发送请求
    let res = await getDetailApi(props.id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        detail.value = res.data[0];
        getAccount(detail.value.usr_id)
        console.log(detail.value);
        loading.value = false;
    }
}

//上传图片墙
const srcList: any = ref(['']);
const getImages = async () => {
    const res = await getImagesApi(props.id);
    srcList.value = res.data;
    console.log(srcList.value);
    return srcList.value;
}

//评论
const reviewList: any = ref(['']);
const getReview = async () => {
    const res = await getReviewApi(props.id);
    reviewList.value = res.data;
    for (let i = 0; i < reviewList.value.length; i++) {
        getAccount(reviewList.value[i].usr_id)
    }
    console.log(reviewList.value);
    return reviewList.value;
}

//用户
const accountList: any = ref([]);
const accountMap: any = ref({});
const getAccount = async (id: string) => {
    if (id == null) return ('')
    const res = await getAccountApi(id);
    accountList.value = res.data;
    accountMap.value[id] = accountList.value;
    console.log(accountMap.value);
    return accountMap.value;
}



const addModel = reactive({
    topic_review_id: '',
    topic_id: '',
    usr_id: '',
    topic_review_content: '',

})

const publishBtn = async () => {

    try {
        await JwtToken();
        // 发送请求
        if (addModel.topic_review_content != '') {

            addModel.topic_review_id = '';
            addModel.topic_id = props.id
            addModel.usr_id = user.usr_id;

            let res = null;
            res = await addReviewApi(addModel)
            addModel.topic_review_content = ''
            if (res.code == 200) {

                ElMessage.success(res.msg)
                getReview()


            }
        } else {
            ElMessage.error('评论不能为空')
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}

//表格 删除
const deleteBtn = (id: string) => {
    console.log(id)
    ElMessageBox.confirm(
        '是否确定删除?',
        '系统警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {//点击确定
            let res = await delReviewApi(id);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                getReview()
            }
        })
        .catch(() => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
        })
}

onMounted(() => {
    getDetail();
    getImages();
    getReview();
    getStar();
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
    /* width: 100%; */
    height: 100%;
    background-color: #ffffff;
    display: flex;
    /* justify-content: center; */


    .content {
        display: flex;
        width: 60%;
        height: 100%;
        flex-direction: column;
        /* padding: 0 30px; */
        justify-content: space-between;
        padding-left: 2%;



        .top {
            display: flex;
            justify-content: space-between;
            height: 20%;
            flex-direction: column;

            .title {
                font-size: 28px;
                color: black;
                font-weight: 500;
                text-align: left;
                width: 100%;
            }

            .time {
                font-size: 20px;
                color: #999;
                text-align: right;
                width: 100%;
            }

        }

        .text-clamp {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: left;
            text-indent: 2em;
            border-bottom: 2px solid #ccc;
            padding-bottom: 10px;



        }

        .text-clamp:deep(img) {
            max-width: 80%;
            /* height: 200px; */
        }

        .pub {
            text-align: right;
            width: 100%;
            padding-top: 20px;
        }


        .review {
            margin-top: 30px;


            .review-list {
                display: flex;

                padding: 20px 10px;

                .avatar {

                    display: flex;
                    align-items: center;
                    padding-right: 10px;

                    img {

                        width: 50px;
                        height: 50px;
                    }
                }

                .review_content {
                    display: flex;
                    flex-direction: column;
                    width: 100%;
                    text-align: left;
                    padding: 5px;

                    .title {
                        text-align: left;
                        width: 100%;
                        padding-bottom: 5px;
                    }

                    .delete {
                        display: flex;
                        justify-content: space-between;
                        padding-top: 5px;
                    }
                }

            }
        }


    }

    .el-carousel {
        width: 40%;
        height: 100%;

        .img {
            width: 100%;
            height: 100%;
        }
    }

}

.page {
    height: 50px;
    line-height: 50px;
    position: relative;
    bottom: 0;
}

.typetitle {
    display: flex;
    gap: 1rem;
}
</style>