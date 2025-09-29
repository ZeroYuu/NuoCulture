<template>
    <div class="contain">

        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <el-button @click="batchDelete" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
        </div>



        <el-skeleton :rows="2" animated :loading="loading">
            <div class="cards initial-visible">
                <div class="type">
                    傩文化资讯
                </div>
                <el-empty description="暂时没有数据" v-if="StarCulList.length === 0" :image-size="50" style="width: 100%;" />
                <div class="card " v-for="item in StarCulList">

                    <div style="display: flex;justify-content: space-between;width: 100%;">
                        <div class="select ">
                            <el-checkbox-group v-model="selectedItems">
                                <el-checkbox :value="item.star_id" :key="item.star_id"
                                    @change="handleSelectChange(item.star_id, $event)"><br>
                                </el-checkbox>
                            </el-checkbox-group>
                        </div>
                        <el-image :src="item.cul_cover" alt="" fit="cover" />
                        <div class="contentcolumn" @click="navigateToDetail(item.cul_id, 'cul')">
                            <div class="top">
                                <el-text class="title">{{ item.cul_title }}</el-text>
                                <!-- <el-text class="time">{{ item.cul_time }}</el-text> -->
                            </div>
                            <div class="text-clamp" v-ignore-images="item.cul_content"></div>
                        </div>
                        <el-button link style="font-size: 20px;margin-left: 20px;" @click="deleteBtn(item.star_id)"
                            color="#493131" plain>删除</el-button>

                    </div>
                </div>


            </div>
        </el-skeleton>

        <el-skeleton :rows="2" animated :loading="loading">
            <div class="cards initial-visible">
                <div class="type">
                    研学活动
                </div>
                <el-empty description="暂时没有数据" v-if="StarActList.length === 0" :image-size="50" style="width: 100%;" />
                <div class="card " v-for="item in StarActList">

                    <div style="display: flex;justify-content: space-between;width: 100%;">
                        <div class="select ">
                            <el-checkbox-group v-model="selectedItems">
                                <el-checkbox :value="item.star_id" :key="item.star_id"
                                    @change="handleSelectChange(item.star_id, $event)"><br>
                                </el-checkbox>
                            </el-checkbox-group>
                        </div>
                        <el-image :src="item.actv_img_url" alt="" fit="cover" />
                        <div class="contentcolumn" @click="navigateToDetail(item.actv_id, 'actv')">
                            <div class="top">
                                <el-text class="title">{{ item.actv_name }}</el-text>
                                <!-- <el-text class="time">{{ item.start_time }}&nbsp;至&nbsp;{{ item.end_time }}</el-text> -->

                            </div>
                            <div class="text-clamp" v-ignore-images="item.actv_content"></div>
                            <el-text class="pub">负责人:{{ item.actv_leader }}</el-text>
                        </div>

                        <el-button link style="font-size: 20px;margin-left: 20px;" @click="deleteBtn(item.star_id)"
                            color="#493131" plain>删除</el-button>
                    </div>

                </div>
            </div>
        </el-skeleton>

        <el-skeleton :rows="2" animated :loading="loading">
            <div class="cards initial-visible">
                <div class="type">
                    话题讨论
                </div>
                <el-empty description="暂时没有数据" v-if="StarTopicList.length === 0" :image-size="50"
                    style="width: 100%;" />
                <div class="card" v-for="item in StarTopicList">
                    <div style="display: flex;justify-content: space-between;width: 100%;">
                        <div class="select ">
                            <el-checkbox-group v-model="selectedItems">
                                <el-checkbox :value="item.star_id" :key="item.star_id"
                                    @change="handleSelectChange(item.star_id, $event)"><br>
                                </el-checkbox>
                            </el-checkbox-group>
                        </div>
                        <el-image :src="item.img_path_url" alt="" fit="cover" />
                        <div class="contentcolumn" @click="navigateToDetail(item.topic_id, 'topic')">

                            <div class="top">
                                <el-text class="title">{{ item.topic_titile }}</el-text>
                                <!-- <el-text class="time">{{ item.start_time }}&nbsp;至&nbsp;{{ item.end_time }}</el-text> -->

                            </div>
                            <div class="text-clamp" v-ignore-images="item.topic_content"></div>
                            <el-text class="pub" v-if="accountMap[item.usr_id] != undefined">发布人:{{
                                accountMap[item.usr_id].usr_name }}</el-text>

                        </div>
                        <el-button link style="font-size: 20px;margin-left: 20px;" @click="deleteBtn(item.star_id)"
                            color="#493131" plain>删除</el-button>

                    </div>

                </div>
            </div>
        </el-skeleton>

        <el-skeleton :rows="2" animated :loading="loading">
            <div class="cards initial-visible">
                <div class="type">
                    周边商品
                </div>
                <el-empty description="暂时没有数据" v-if="StarGoodLsist.length === 0" :image-size="50"
                    style="width: 100%;" />
                <div style="display: flex;justify-content: flex-start;align-items: center;width: 100%;">
                    <div class="card" v-for="item in StarGoodLsist"
                        style="width: 33.3%;gap: 20px;box-sizing: border-box;">

                        <div class="content">
                            <div class="select ">
                                <el-checkbox-group v-model="selectedItems">
                                    <el-checkbox :value="item.star_id" :key="item.star_id"
                                        @change="handleSelectChange(item.star_id, $event)"><br>
                                    </el-checkbox>
                                </el-checkbox-group>
                            </div>
                            <el-image :src="item.img_path_url" alt="" fit="cover"
                                @click="navigateToDetail(item.goods_id, 'goods')">
                            </el-image>
                            <div class="title" @click="navigateToDetail(item.goods_id, 'goods')">
                                <el-text style=" font-size: 20px;">{{
                                    item.goods_name }}</el-text>
                            </div>

                            <div class="price">
                                <el-text style="color: #ff6600;font-size: 20px">
                                    ￥{{ item.goods_price }}
                                </el-text>
                                <el-button link style="font-size: 20px;margin-left: 20px;"
                                    @click="deleteBtn(item.star_id)" color="#493131" plain>删除</el-button>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </el-skeleton>

    </div>

</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, reactive } from 'vue';
import { getStarGoodsApi } from '@/api/frontend/goods/index';
import { getStarCulApi } from '@/api/frontend/culture/index';
import { getStarActvApi } from '@/api/frontend/active/index';
import { getStarTopicApi } from '@/api/frontend/topic/index';
import { getAccountApi, allDelStarApi, delStarApi } from '@/api/frontend/usrinfo/index';

const search = () => {
    getList();
}


// 单个删除
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
            let res = await delStarApi(id);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                getList()
                // 从选中列表中移除
                // selectedItems.value = selectedItems.value.filter(item => item !== id);
            }
        })
        .catch(() => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
        })
}

// 批量删除
const batchDelete = () => {
    console.log(selectedItems.value);
    ElMessageBox.confirm(
        `是否确定删除选中的${selectedItems.value.length}个商品?`,
        '系统警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {//点击确定
            const ids = selectedItems.value.map(id => id);
            console.log(ids);
            let res = await allDelStarApi(ids);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                selectedItems.value = [] // 清除选择
                getList()
            }
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '已取消批量删除',
            })
        });
}
const isIndeterminate = ref(false)

// 全选/取消全选
const handleSelectAll = (val: boolean) => {
    if (val) {
        selectedItems.value = [
            ...StarCulList.value.map((item: { star_id: any }) => item.star_id),
            ...StarActList.value.map((item: { star_id: any }) => item.star_id),
            ...StarTopicList.value.map((item: { star_id: any }) => item.star_id),
            ...StarGoodLsist.value.map((item: { star_id: any }) => item.star_id)
        ];
        isIndeterminate.value = false;
    } else {
        selectedItems.value = [];
    }
}

// 单个选择变化
const handleSelectChange = (id: string, checked: boolean) => {
    if (checked) {
        if (!selectedItems.value.includes(id)) {
            selectedItems.value.push(id);
        }
    } else {
        const index = selectedItems.value.indexOf(id);
        if (index > -1) {
            selectedItems.value.splice(index, 1);
        }
    }
    // 更新全选和半选状态
    updateSelectAll();
}
// 更新全选和部分选中状态
const updateSelectAll = () => {
    const totalItems = [
        ...StarCulList.value,
        ...StarActList.value,
        ...StarTopicList.value,
        ...StarGoodLsist.value
    ].length;

    selectAll.value = selectedItems.value.length === totalItems && totalItems > 0;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < totalItems;
}



const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,

    goods_type_id: '',
    goods_status: '1',
    goods_content: '',

})

const loading = ref(true)

// const goodsTypeList: any = ref([]);
// // 文化
// const getGoodsType = async () => {
//     const res = await getGoodsTypeApi();
//     goodsTypeList.value = res.data;
//     console.log("goodsTypeList", goodsTypeList.value);
//     return goodsTypeList.value;
// }

import { userStore } from '@/store/user/index'
const user = userStore()
const selectedItems = ref<any[]>([]);
const selectAll = ref(false);

const StarCulList: any = ref([]);
const StarActList: any = ref([]);
const StarTopicList: any = ref([]);
const StarGoodLsist: any = ref([]);
const getList = async () => {
    //发送请求
    let resCul = await getStarCulApi(user.usr_id)
    let resActv = await getStarActvApi(user.usr_id)
    let restopic = await getStarTopicApi(user.usr_id)
    let resGoods = await getStarGoodsApi(user.usr_id)
    if (restopic.code == 200 && resGoods.code == 200 && resCul.code == 200 && resActv.code == 200) {

        // console.log(res)
        //设置表格数据
        StarCulList.value = resCul.data;
        StarActList.value = resActv.data;
        StarTopicList.value = restopic.data;
        StarGoodLsist.value = resGoods.data;


        console.log("StarCulList", resCul.data);
        console.log("StarActList", StarActList.value);
        console.log("StarTopicList", StarTopicList.value);
        console.log("StarGoodLsist", StarGoodLsist.value);

        for (let i = 0; i < StarCulList.value.length - 1; i++) {

            getAccount(restopic.data[i].usr_id)
        }



        loading.value = false;

    }
}

const account: any = ref([{}]);
const accountMap: any = ref({});
const getAccount = async (id: string) => {
    //发送请求
    let res = await getAccountApi(id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        account.value = res.data;
        accountMap.value[id] = res.data;
        console.log("accountMap", accountMap.value);

        loading.value = false;
    }
}
const addModel = reactive({
    order_id: '',

})

onMounted(() => {

    getList();

    // document.querySelector('.el-scrollbar__wrap').addEventListener('scroll', handleScroll);

    // 标记首屏可见的卡片
    setTimeout(() => {
        const cards = document.querySelectorAll('.cards');

        const viewportHeight = window.innerHeight - 20;

        cards.forEach(cards => {
            const rect = cards.getBoundingClientRect();
            if (rect.top > viewportHeight) {
                cards.classList.remove('initial-visible');
            }
        });

    }, 300);

});

import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();



onUnmounted(() => {

});

const navigateToDetail = (id: string, tag: string) => {
    console.log(id);
    if (tag == 'cul') {
        router.push({ name: 'frontculturedetial', params: { id } });
    } else if (tag == 'actv') {
        router.push({ name: 'frontactivedetial', params: { id } });
    }
    else if (tag == 'topic') {
        router.push({ name: 'fronttopicdetial', params: { id } });
    }
    else if (tag == 'goods') {
        router.push({ name: 'frontgoodsdetial', params: { id } });
    }

};

</script>
<style scoped>
.a {
    position: absolute;
    top: 75px;
    right: 500px;
    transform: translate(0, -50%);
    background-color: rgba(73, 49, 49, 0.5);
    height: 40px;
    border-radius: 40px;
    transition: all 0.4s ease;
    display: flex;
    backdrop-filter: blur(5px);
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
    margin: 20px !important;
    position: relative;

    .batch-actions {
        margin-bottom: 10px;
        display: flex;
        align-items: center;
        gap: 20px;
        /* padding: 10px 0; */
        /* width: 100%; */
        border-radius: 4px;
        --el-border: #1d4350 solid 1px;
        position: relative;
        /* justify-content: space-between; */
        /* margin: 0 20px; */
    }

    .el-breadcrumb {
        margin-bottom: 20px;
    }

    .type {
        display: flex;
        align-items: center;
        font-size: 23px;
        font-weight: 500;
    }
}


@keyframes slide-fade-in {
    from {
        opacity: 0;
        box-shadow: none;
        transform: scale(.6) translateY(15vh);
        transition: transform 1s ease,
    }
}

.cards:not(.initial-visible) {
    animation: slide-fade-in both;
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    animation-timeline: view();
    /* 定义动画时间范围 */
    animation-range: contain 0% contain 50%;
}

.cards {
    display: flex;
    margin-top: 20px;
    opacity: 1;
    border-radius: 10px;

    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    /* width: 100%; */
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    align-items: flex-start;
    /* align-items: center; */
    flex-direction: column;
    padding: 20px;

}

.top {
    opacity: 1;
    transform: none;
    display: flex;
    align-items: center;
    --el-border: #1d4350 solid 1px;


}

.card {
    opacity: 1;
    transform: none;
    padding: 10px;

    .select {
        /* opacity: 1;
            transform: none; */
        display: flex;
        align-items: center;
        --el-border: #1d4350 solid 1px;
    }



    /* animation: slide-fade-in both; */
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    /* animation-timeline: view(); */
    /* 定义动画时间范围 */
    /* animation-range: contain 0% contain 20%; */


    .el-image {
        width: 20%;
        /* height: 100%; */
        margin-right: 15px;
    }

    .contentcolumn {
        display: flex;
        width: 90%;
        /* height: 100%; */
        flex-direction: column;
        align-content: flex-start !important;
        justify-content: center;
        cursor: pointer;



        .top {
            display: flex;
            justify-content: space-between;
            height: 20%;


            .title {
                font-size: 25px;
                color: black;
                font-weight: 500;
                margin-top: 20px;
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
                overflow: hidden;
                text-overflow: ellipsis;

            }

            .time {
                font-size: 15px;
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

        .pub {
            text-align: right;
            width: 100%;
            margin-right: 20px;
            margin-top: 10px;
        }

    }

    .content {
        display: flex;
        /* width: 100%; */
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;
        position: relative;

        .select {
            /* opacity: 1;
            transform: none; */
            display: flex;
            align-items: center;
            --el-border: #1d4350 solid 1px;
            position: absolute;
            left: 130px;
        }

        .el-image {
            width: 150px;
            /* height: 100%; */
            /* margin-right: 15px; */
            cursor: pointer;
        }

        .price {
            width: 150px;
            justify-content: space-between;
            display: flex;
        }

        .title {
            cursor: pointer;
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
    align-items: center;




}
</style>