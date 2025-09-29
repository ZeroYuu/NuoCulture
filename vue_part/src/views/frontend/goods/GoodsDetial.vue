<template>
    <div class="contain">
        <bread-crumb></bread-crumb>
        <div class="hoverpart">
            <div class="cart" @click="cart">
                <el-icon :size="50" color="#e9e7ef">
                    <ShoppingCart />
                </el-icon>
            </div>
        </div>
        <el-empty description="暂时没有数据" v-if="detail.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div style="display: flex;">


                <div class="card" :model="detail" v-if="detail !== undefined">
                    <div class="top">
                        <div class="goodsImg">
                            <ImageZoom :src="partImg" class="imgB">
                                <el-image :src="partImg" />
                            </ImageZoom>
                            <div class="imgS">

                                <el-image v-for="item, index in srcList" :key="item" :src="item" fit="cover"
                                    @click="changeImg(srcList[index])" />
                            </div>
                        </div>




                        <div class="intro">
                            <el-text class="name">{{ detail.goods_name }}</el-text>
                            <el-text class="partOne" v-if="GoodsTypeName[0] !== undefined">类别:{{
                                GoodsTypeName[0].goods_type_name
                            }}</el-text>
                            <el-text class="partOne">售价:<el-text style="color: #ff6600;">￥{{ detail.goods_price
                                    }}</el-text></el-text>
                            <el-text class="partOne">库存:{{ detail.goods_inventory }}</el-text>
                            <el-form :model="addModel">
                                <el-form-item label="颜色" class="partTwo">
                                    <SelectChecked :options="options" @change="selected" ref="selectRef"
                                        v-model="addModel.goods_color_id">
                                    </SelectChecked>
                                </el-form-item>
                                <el-form-item label="数量" class="partTwo">
                                    <el-input-number v-model="addModel.goods_count" :min="1"
                                        :max="detail.goods_inventory"></el-input-number>
                                </el-form-item>
                            </el-form>
                            <div style="display: flex;align-items: center;">
                                <el-button color="#4e3929" plain @click="buyBtn()"
                                    style="width: 150px;font-size: 16px;">直接购买</el-button>
                                <el-button color="#4e3929" plain @click="cartBtn()"
                                    style="width: 150px;font-size: 16px;">加入购物车</el-button>
                                <el-icon :size="35" style="margin-left: 10px;">
                                    <component :is="showNew ? 'StarFilled' : 'Star'" @click="Star" />
                                </el-icon>
                            </div>

                        </div>

                    </div>


                    <el-tabs :module="detail">
                        <el-tab-pane label="商品详情" v-html="detail.goods_intro" class="text-clamp"></el-tab-pane>
                        <el-tab-pane label="用户评论" class="review">
                            <el-text style="width: 100%; text-align: left;display: flex;padding: 10px 0;">共{{
                                reviewList.length }}条评论</el-text>
                            <div class="review-list" v-for="(item, index) in reviewList" :key="index">
                                <div class="avatar" v-if="accountMap[item.usr_id] !== undefined">
                                    <el-avatar :src="accountMap[item.usr_id][0].usr_avater" alt="" />
                                </div>
                                <div class="review_content" v-if="accountMap[item.usr_id] !== undefined">
                                    <el-text class="title">{{ accountMap[item.usr_id][0].usr_name }}</el-text>
                                    <div v-html="item.goods_review_content"></div>
                                    <div class="delete">
                                        <el-text>{{ item.goods_review_time }}</el-text>
                                        <el-button link color="#4e3929" plain @click="deleteBtn(item.goods_review_id)"
                                            v-if="user.usr_id == accountMap[item.usr_id][0].usr_id">删除</el-button>
                                    </div>
                                </div>

                            </div>

                        </el-tab-pane>
                    </el-tabs>
                </div>
                <div class="recmd">
                    <el-text style="padding-top: 10px;font-weight:500;color: #493131;font-size: 20px;">推荐商品</el-text>
                    <div v-for="item in recmdgoodsList" @click="navigateToDetail(item.goods_id)" class="part">
                        <el-image :src="item.img_path_url" alt="" fit="cover" />
                        <el-text class="content">{{ item.goods_name }}</el-text>
                        <el-text class="price">￥{{ item.goods_price }}</el-text>

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
import { useRouter } from 'vue-router';

const router = useRouter();

import { JwtToken } from '@/api/frontend/usrinfo/index';
const showNew = ref(false)

const Star = async () => {


    try {
        await JwtToken();
        // 发送请求
        showNew.value = !showNew.value
        let res = await StarGoodsApi(props.id, user.usr_id, showNew.value)
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

const navigateToDetail = (id: string) => {
    console.log(id);
    router.push({ name: 'frontgoodsdetial', params: { id } }).then(() => {
        window.location.reload();
    });
};

const cart = () => {
    router.push({ name: 'cart' });
}


import BreadCrumb from '@/Client/Header/BreadCrumb.vue';
import SelectChecked from "@/components/SelectChecked.vue"
import ImageZoom from "@/components/ImageZoom.vue"
import {
    getDetailApi, getImagesApi, addCartApi, getAccountApi, getReviewApi, delReviewApi,
    getGoodsTypeNameApi, selectTypeListApi, getRecmdListApi, StarGoodsApi, getStarApi
} from '@/api/frontend/goods/index';
import { onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { userStore } from '@/store/user/index'
const user = userStore();




const showEmpty = ref(false)
const loading = ref(true)

const recmdgoodsList: any = ref([{}]);


const getRecmdList = async () => {
    //发送请求
    let res = await getRecmdListApi()
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        recmdgoodsList.value = res.data;
        console.log(recmdgoodsList.value);
        loading.value = false;

    }
}



const detail: any = ref([{}]);
const getDetail = async () => {
    //发送请求
    let res = await getDetailApi(props.id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        detail.value = res.data[0];
        // getAccount(detail.value.usr_id)
        console.log("detail", detail.value);

        loading.value = false;
    }
}



const GoodsTypeName: any = ref([]);
// 文化
const getGoodsTypeName = async () => {
    const res = await getGoodsTypeNameApi(props.id);
    GoodsTypeName.value = res.data;
    console.log("GoodsTypeName", GoodsTypeName.value);
    return GoodsTypeName.value;
}



//上传图片墙
const srcList: any = ref(['']);
const getImages = async () => {
    const res = await getImagesApi(props.id);
    srcList.value = res.data;
    console.log(srcList.value);
    partImg.value = srcList.value[0]

    return srcList.value;
}
const partImg = ref('')

const changeImg = (src: string) => {
    partImg.value = src;
    return partImg.value;
}

//下拉数据
const options = ref([])
const color = ref('')

//下拉选择 勾选的数据
const selected = (value: string | number) => {

    console.log(value);
}

//下拉数据 后台获取数据
const getSelectType = async (id: string) => {
    let res = await selectTypeListApi(id);
    if (res.code == 200) {
        options.value = res.data;
        addModel.goods_color_id = res.data[0].value;


        // console.log("后台数据："+res.data)
    }

}



//评论
const reviewList: any = ref(['']);
const getReview = async () => {
    const res = await getReviewApi(props.id);
    reviewList.value = res.data;
    for (let i = 0; i < reviewList.value.length; i++) {
        getAccount(reviewList.value[i].usr_id)
    }
    console.log("reviewList", reviewList.value);
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
    console.log("accountMap", accountMap.value);
    return accountMap.value;
}



const addModel = reactive({
    cart_id: '',
    goods_id: '',
    goods_count: 1,
    usr_id: '',
    goods_color_id: '',

})

//加购
const cartBtn = async () => {
    try {
        await JwtToken();
        // 发送请求
        addModel.cart_id = '';
        addModel.goods_id = props.id
        addModel.usr_id = user.usr_id

        let res = null;
        res = await addCartApi(addModel)

        if (res.code == 200) {

            ElMessage.success(res.msg)
            getDetail()
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}

import { goodsStore } from '@/store/GoodsConfirm/index';
const goods = goodsStore();
//购买
const buyBtn = async () => {
    try {
        await JwtToken();
        // 发送请求
        goods.clearGoods();
        if (detail) {
            const selectedColor = options.value.find(item => item.value === addModel.goods_color_id);
            const goodsData = {
                goods_name: detail.value.goods_name,
                goods_color_name: selectedColor ? selectedColor.label : '',
                goods_color_id: addModel.goods_color_id,
                goods_price: detail.value.goods_price,
                goods_count: addModel.goods_count,
                goods_url: srcList.value[0],
                goods_inventory: detail.value.goods_inventory,
                goods_id: props.id,
                direct_pay: true,
            };
            console.log(goodsData);
            goods.addGoods(goodsData);
        }
        // 传递到订单页面
        router.push({
            name: 'frontgoodsorder'
        });
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}

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
                getReview();
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
    getGoodsTypeName();
    getSelectType(props.id);
    getRecmdList();
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
    width: 70%;
    height: 100%;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;

    .top {
        display: flex;
        margin-left: 50px;

        .goodsImg {

            .imgB {
                width: 400px;
                height: 400px;


            }

            .imgS {
                display: flex;


                .el-image {
                    width: 70px;
                    height: 70px;
                    margin-right: 10px;
                    border: 1px solid #ebeef5;
                    border-radius: 5px;

                }

            }

        }



        .intro {
            display: flex;
            justify-content: space-between;
            height: 20%;
            flex-direction: column;
            width: 100%;
            --el-font-size-base: 25px;
            padding: 20px 0;
            padding-left: 20px;

            .name {
                font-size: 30px;
                color: rgb(65, 22, 22);
                font-weight: 600;
                text-align: left;
                width: 100%;
                padding: 13px;
            }

            .partOne {
                color: #412222;
                text-align: left;
                width: 100%;
                padding: 15px;
            }

            .partTwo {
                padding: 7px 0;
                --el-text-color-regular: #412222;
                -el-form-label-font-size: 25px
            }

        }

    }


    .text-clamp {
        display: flex;
        flex-direction: column;
        text-align: left;
        text-indent: 2em;
        padding-bottom: 10px;


    }

    .text-clamp:deep(img) {
        width: 100%;
        /* height: 200px; */
    }


    .review {

        .el-button {
            display: flex;
            margin-top: 20px;
        }


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

    .el-carousel {
        width: 40%;
        height: 100%;

        .img {
            width: 100%;
            height: 100%;
        }
    }

}

.hoverpart {
    position: fixed;
    /* 固定定位 */
    right: -20px;
    /* 右侧距离 */
    width: 50px;
    /* 初始宽度 */
    height: 50px;
    overflow: hidden;
    transition: width 0.3s ease;
    transition: .4s;
    /* 平滑过渡效果 */



    .cart {

        /* 鼠标悬停时显示指针 */
        background-color: #4931318e;
        border-radius: 50%;
        cursor: pointer;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: 100%;
        height: 100%;
        transition: .4s;

        .el-icon {
            width: 30px;
            height: 30px;
            transition: .4s;
        }
    }
}



.hoverpart:hover {
    width: 80px;
    height: 80px;
    padding-right: 40px;
    transition: .4s;

    .cart {
        background-color: #493131;
        border-radius: 50%;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        cursor: pointer;
        width: 100%;
        height: 100%;
        transition: .4s;


        .el-icon {
            width: 80px;
            height: 80px;
            transition: .4s;
        }
    }
}

.recmd {
    border-radius: 10px;
    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    width: 20%;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    margin-left: 30px;
    height: min-content;

    .el-image {

        padding: 10px;
        width: inherit;

    }

    .part {
        /* height: 260px; */

        border-bottom: 3px solid #e4e7ed;
        padding: 10px;

        .content {
            width: 100%;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;
            overflow: hidden;
            text-overflow: ellipsis;

            font-size: 20px;
            color: black;
            font-weight: 500;

        }

        .price {
            width: 100%;
            font-size: 18px;
            color: #c84a41;
            font-weight: 500;
            display: flex;
            justify-content: flex-end;
            margin-bottom: 10px;
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