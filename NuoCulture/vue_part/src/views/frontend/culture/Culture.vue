<template>
    <div class="contain">
        <bread-crumb></bread-crumb>
        <div class="typetitle">
            <el-button @click="changeAll" color="#493131b0" plain>全部</el-button>
            <div v-for="item in cultureTypeList" :key="item" @click="changeType(item.cul_type_id)">
                <el-button v-if="cultureTypeList !== undefined" color="#493131b0" plain>
                    {{ item.cul_type_name }}
                </el-button>
            </div>
            <div class="a">
                <input type="text" class="b" v-model="searchParam.cul_content">
                <div class="c" @click="search">
                    <el-icon>
                        <Search />
                    </el-icon>
                </div>
            </div>
        </div>
        <el-empty description="暂时没有数据" v-if="showEmpty" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards" v-for="item in cultureList">
                <div class="card initial-visible" @click="navigateToDetail(item.cul_id)">
                    <el-image :src="item.cul_cover" alt="" fit="cover" />
                    <div class="content">
                        <div class="top">
                            <el-text class="title">{{ item.cul_title }}</el-text>
                            <el-text class="time">{{ item.cul_time }}</el-text>

                        </div>
                        <div class="text-clamp" v-ignore-images="item.cul_content"></div>
                    </div>
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

import { getListApi, getCultureTypeApi, } from '@/api/frontend/culture/index';
import { onMounted, reactive, ref, watch } from 'vue';


const search = () => {
    getList();
}


const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    // cul_id:'',
    cul_type_id: '',
    cul_shelves: '1',
    cul_content: '',
})

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

const cultureTypeList: any = ref([]);
// 文化
const getcultureType = async () => {
    const res = await getCultureTypeApi();
    cultureTypeList.value = res.data;
    console.log("cultureTypeList", cultureTypeList.value);
    return cultureTypeList.value;
}
const cultureList: any = ref([{}]);

const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        cultureList.value = res.data.records;
        console.log(cultureList.value);
        searchParam.total = res.data.total;
        loading.value = false;

    }
}

const changeAll = () => {
    searchParam.cul_type_id = '';
    searchParam.curPage = 1;
    getList();
}
const changeType = (e: any) => {
    searchParam.cul_type_id = e;
    searchParam.curPage = 1;
    searchParam.cul_content = '';
    getList();
}

watch(cultureList, () => {
    showEmpty.value = cultureList.value.length === 0;
    console.log(showEmpty.value);
});

onMounted(() => {
    getcultureType();
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

import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToDetail = (id: string) => {
    console.log(id);
    router.push({ name: 'frontculturedetial', params: { id } });
};


</script>
<style scoped>
.a {
    position: absolute;
    top: 47px;
    /* right: 120px; */
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
    padding: 8px;
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
    
}

.b {
    border: none;
    background: none;
    /* 取消选中搜索框时的外边框 */
    outline: none;
    width: 0;
    padding: 0;
    transition: .4s;
    /* line-height: 40px; */
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
    from{
        opacity: 0;
        box-shadow: none;
        transform: scale(.6) translateY(15vh);
    }
}

.cards {
    display: flex;
    margin-top: 20px;
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

    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    width: 100%;
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px;

    .el-image {
        width: 10%;
        height: 100%;
        margin-right: 15px;
    }

    .content {
        display: flex;
        width: 90%;
        /* height: 100%; */
        flex-direction: column;
        align-content: flex-start !important;


        .top {
            display: flex;
            justify-content: space-between;
            height: 20%;

            .title {
                font-size: 28px;
                color: black;
                font-weight: 500;
                margin-top: 10px;


            }

            .time {
                font-size: 20px;
                color: #999;
            }

        }

        .text-clamp {
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-box-align: start;
            -webkit-line-clamp: 3;
            overflow: hidden;
            text-overflow: ellipsis;
            justify-content: space-between;
            flex-wrap: wrap;
            word-break: break-all;
            /* 防止单词溢出 */
            /* height: auto; */
            text-align: left;
        }



    }

}

.card:not(.initial-visible) {
    animation: slide-fade-in both;
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    animation-timeline: view();
    /* 定义动画时间范围 */
    animation-range: contain 0% contain 50%;
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