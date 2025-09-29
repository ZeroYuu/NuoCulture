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


        <div class="typetitle">

            <el-button @click="changeAll" color="#493131b0" plain>全部</el-button>
            <div v-for="item in goodsTypeList" :key="item" @click="changeType(item.goods_type_id)">
                <el-button v-if="goodsTypeList !== undefined" color="#493131b0" plain>{{ item.goods_type_name }}</el-button>
            </div>
            <div class="a">
                <input type="text" class="b" v-model="searchParam.goods_content">
                <div class="c" @click="search">
                    <el-icon>
                        <Search />
                    </el-icon>
                </div>
            </div>
        </div>

        <el-empty description="暂时没有数据" v-if="goodsList.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards">
                <div class="card initial-visible" v-for="item in goodsList" @click="navigateToDetail(item.goods_id)">
                    <el-image :src="item.img_path_url" alt="" fit="cover" />


                    <el-text class="content">{{ item.goods_name }}</el-text>
                    <el-text class="price">￥{{ item.goods_price }}</el-text>

                </div>
            </div>

        </el-skeleton>

        <!-- 分页
            page-sizes 每页个数
            -->
        <div class="page">
            <el-pagination :page-sizes="[5, 10, 15, 20]" background :page-size="searchParam.pageSize"
                :current-page="searchParam.curPage" layout="->,total, sizes, prev, pager, next, jumper"
                :total="searchParam.total" @size-change="pageSizeChange" @current-change="currentPageChange" />
        </div>
    </div>

</template>

<script setup lang="ts">
import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

import { getListApi, getGoodsTypeApi } from '@/api/frontend/goods/index';
import { onMounted, reactive, ref, watch } from 'vue';

const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,

    goods_type_id: '',
    goods_status: '1',
    goods_content: '',

})
const search = () => {
    getList();
}
//当前页改变时出发
const currentPageChange = (val: any) => {
    searchParam.curPage = val
    getList();
}

//页大小改变时出发
const pageSizeChange = (val: any) => {
    searchParam.pageSize = val
    getList();
}
const showEmpty = ref(false)
const loading = ref(true)

const goodsTypeList: any = ref([]);
// 文化
const getGoodsType = async () => {
    const res = await getGoodsTypeApi();
    goodsTypeList.value = res.data;
    console.log("goodsTypeList", goodsTypeList.value);
    return goodsTypeList.value;
}
const goodsList: any = ref([{}]);


const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        goodsList.value = res.data.records;
        console.log(goodsList.value);
        searchParam.total = res.data.total;
        loading.value = false;

    }
}

const changeAll = () => {
    searchParam.goods_type_id = '';
    searchParam.curPage = 1;
    getList();
}
const changeType = (e: any) => {
    searchParam.goods_content= '',
    searchParam.goods_type_id = e;
    searchParam.curPage = 1;
    getList();
}

watch(goodsList, () => {
    showEmpty.value = goodsList.value.length === 0;
    console.log(showEmpty.value);
});

onMounted(() => {
    getGoodsType();

    getList();


    // 标记首屏可见的卡片
    setTimeout(() => {
        const cards = document.querySelectorAll('.card');
        const viewportHeight = window.innerHeight;
        
        cards.forEach(card => {
            const rect = card.getBoundingClientRect();
            if (rect.top > viewportHeight) {
                card.classList.remove('initial-visible');
            }
        });
    }, 300);
});

const cart = () => {
    router.push({ name: 'cart' });
}

import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToDetail = (id: string) => {
    console.log(id);
    router.push({ name: 'frontgoodsdetial', params: { id } });
};

</script>
<style scoped>

.a {
    position: absolute;
    top: 10px;
    /* left:150px; */
    right: 0;
    transform: translate(0, -50%);
    background-color: #493131b0;
    height: 40px;
    border-radius: 40px;
    transition: .4s;
    display: flex;

}

.a:hover {
    min-width: auto;
    padding: 10px;
}

.a:hover .b {

    width: 200px;
    padding: 0 6px;

}

.a:hover .c {
    background-color: #493131;
    /* width: 40px; */
    font-size: 20px;
    cursor: pointer;
    ;
}

.b {
    border: none;
    background: none;
    /* 取消选中搜索框时的外边框 */
    outline: none;
    width: 0;
    padding: 0;
    transition: .4s;
    line-height: 40px;
    font-size: 20px;
    color: #e9e7ef;
}

.c {
    color: #e9e7ef;
    float: right;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: .4s;
    /* font-size: 20px; */
}
.contain {
    width: auto;
    margin: 50px 50px;
    position: relative;


    .el-breadcrumb {
        margin-bottom: 20px;
    }
}


@keyframes slide-fade-in {
    from {
        opacity: 0;
        box-shadow: none;
        transform: scale(.6) translateY(15vh);
        /* transition: transform 1s ease, */
    }
}

.cards {
    display: flex;
    /* margin-top: 20px; */
    flex-wrap: wrap;
    gap: 50px;
    margin: 40px 0;
}

.card:not(.initial-visible) {
    animation: slide-fade-in both;
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    animation-timeline: view();
    /* 定义动画时间范围 */
    animation-range: contain 0% contain 50%;

}

.card {
    opacity: 1;
    transform: none;
    /* animation: slide-fade-in both; */
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    /* animation-timeline: view(); */
    /* 定义动画时间范围 */
    /* animation-range: contain 0% contain 50%; */
    border-radius: 10px;
    width:22%;
    box-sizing: border-box;


    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;

    height: 320px;
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    justify-content: center;

    padding: 10px;


    .el-image {
        width: 100%;
        height: 80%;
    }

    .content {
        width: 100%;
        height: 15%;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
        overflow: hidden;
        text-overflow: ellipsis;

        font-size: 28px;
        color: black;
        font-weight: 500;

    }

    .price {
        width: 100%;
        height: 15%;
        font-size: 20px;
        color: #c84a41;
        font-weight: 500;
        display: flex;
        justify-content: flex-end;
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
    align-items: center;




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
        width:100%;
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
</style>