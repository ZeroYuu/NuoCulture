<template>
    <div class="contain">

        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <div style="width: 1100px;position: absolute;z-index: 99;margin-left: 80px;" ref="settlementRef">
                <el-row>
                    <el-col :span="5" style="margin-left: 45px;">
                        宝贝
                    </el-col>
                    <el-col :span="1" style="margin-left: 30px;">
                        单价
                    </el-col>
                    <el-col :span="2" style="margin-left: 30px;">
                        数量
                    </el-col>
                    <el-col :span="4" style="margin-left: -10px;">
                        备注
                    </el-col>
                    <el-col :span="2" style="margin-left: 20px;">
                        实付款
                    </el-col>
                    <el-col :span="2" style="margin-left: 40px;">
                        交易状态
                    </el-col>
                    <el-col :span="3" style="margin-left: 60px;">
                        交易操作
                    </el-col>
                </el-row>
            </div>
            <el-button @click="batchDelete" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
        </div>

        <el-empty description="暂时没有数据" v-if="usrOrderList.length === 0" style="width: 70%;" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards initial-visible" v-for="item in usrOrderList">


                <div class="card" v-if="detailMap[item.order_id] !== undefined">
                    <div style="padding-bottom: 20px;display: flex;justify-content: space-between;width: 100%;">

                        <div class="top">
                            <el-checkbox-group v-model="selectedItems">
                                <el-checkbox :value="item.order_id" :key="item.order_id"
                                    @change="handleSelectChange(item.order_id, $event)"><br>
                                </el-checkbox>
                            </el-checkbox-group>
                            &nbsp;
                            {{ formatOrderTime(item.order_time) }}&nbsp;&nbsp;&nbsp;&nbsp;
                            订单号：{{ item.order_number }}&nbsp;&nbsp;&nbsp;&nbsp;
                            <div v-if="item.order_status == 2 || 3 || 4">
                                物流号：{{ item.order_track_num }}
                            </div>

                        </div>
                        <div style="margin-right: 20px;">
                            <el-icon @click="deleteBtn(item.order_id)" :size="30">
                                <Delete />
                            </el-icon>
                        </div>

                    </div>
                    <div style="display: flex;justify-content: space-around;align-items: center;width: 100%;">
                        <el-image :src="detailMap[item.order_id].img_path_url" alt="" fit="cover" />
                        <div class="content">

                            <div class="title">
                                <div class="name">
                                    <el-text @click="navigateToDetail(item.goods_id)" style=" font-size: 20px;">{{
                                        detailMap[item.order_id].goods_name }}</el-text>
                                </div>


                                <div class="color">
                                    <el-text style="font-size: 16px;color: #9e9e9e;">
                                        颜色分类：{{ detailMap[item.order_id].goods_color_name }}
                                    </el-text>
                                </div>
                            </div>

                            <div class="price">
                                <el-text style="color: #ff6600;font-size: 20px">
                                    ￥{{ detailMap[item.order_id].goods_price }}
                                </el-text>
                            </div>

                            <div class="number">
                                <el-text style="font-size: 20px">
                                    {{ item.goods_count }}
                                </el-text>
                            </div>

                            <div class="remark">
                                <el-text style="font-size: 20px">
                                    {{ item.order_remark ? item.order_remark : "无备注" }}
                                </el-text>
                            </div>

                            <div class="allprice">
                                <el-text style="color: #ff6600;font-size: 20px">
                                    ￥{{ item.order_price }}
                                </el-text>
                            </div>


                            <div class="status">

                                <el-text v-if="item.order_status == 0" style="font-size: 20px;">待支付</el-text>
                                <el-text v-if="item.order_status == 1" style="font-size: 20px;">待发货</el-text>
                                <el-text v-if="item.order_status == 2" style="font-size: 20px;">待收货</el-text>
                                <el-text v-if="item.order_status == 3" style="font-size: 20px;">已收货</el-text>
                                <el-text v-if="item.order_status == 4" style="font-size: 20px;">评价完成</el-text>
                                <el-text v-if="item.order_status == -1" style="font-size: 20px;">支付失败</el-text>
                                <el-text v-if="item.order_status == -2" style="font-size: 20px;">取消订单 </el-text>
                            </div>


                            <div class="delete">

                                <el-button link @click="dtrackBtn(item)" color="#493131" plain
                                    v-if="item.order_status == 2 || item.order_status == 3 || item.order_status == 4">
                                    查看物流
                                </el-button>
                                <el-button link @click="detailBtn(item)" color="#493131" plain>查看详情</el-button>

                                <el-button link @click="BuyBtn(item)" color="#493131" plain
                                    v-if="item.order_status == 0">去支付</el-button>


                            </div>
                            <div class="delete">
                                <el-button @click="navigateToDetail(item.goods_id)" color="#493131"
                                    plain>再次购买</el-button>

                                <el-button @click="checkGoods(item)" color="#493131" plain
                                    v-if="item.order_status == 2">确认收货</el-button>

                                <el-button @click="ReviewBtn(item)" color="#493131" plain
                                    v-if="item.order_status == 3">评价</el-button>



                            </div>

                        </div>
                    </div>
                </div>

            </div>

        </el-skeleton>

    </div>

    <el-dialog :title="title" width="80%" style="max-height: 80%;overflow: auto;" append-to-body v-model="isvisible">
        <el-row v-if="tags === '0'">
            <el-col :span="24" :offset="0" style="margin: 10px 0;">
                <el-empty description="暂时没有数据" v-if="Model.order_track_num === ''" />
                <el-text style="font-size: 20px" v-else>
                    物流单号：{{ Model.order_track_num }}
                </el-text>
            </el-col>
        </el-row>
        <el-row v-if="tags === '1'">
            <el-col :span="24" :offset="0">
                <el-descriptions :column="3" border v-if="detailMap[orderModel.order_id] != undefined">
                    <el-descriptions-item label="订单号">{{ orderModel.order_number }}</el-descriptions-item>
                    <el-descriptions-item label="收货人">{{ addModel.addr_name }}</el-descriptions-item>
                    <el-descriptions-item label="地址">
                        {{ CodeToText[addModel.addr_province] }}/{{ CodeToText[addModel.addr_city] }}
                        /{{ CodeToText[addModel.addr_country] }}/{{ addModel.addr_detail }}</el-descriptions-item>
                    <el-descriptions-item label="商品名称">{{ detailMap[orderModel.order_id].goods_name
                    }}</el-descriptions-item>
                    <el-descriptions-item label="商品颜色">{{ detailMap[orderModel.order_id].goods_color_name
                    }}</el-descriptions-item>
                    <el-descriptions-item label="商品数量">{{ orderModel.goods_count }}</el-descriptions-item>
                    <el-descriptions-item label="支付方式">
                        {{ orderModel.payment_id === 1 ? '支付宝' : (orderModel.payment_id === 2 ? '微信' :
                            (orderModel.payment_id === 3 ? '积分' : '未知支付方式')) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="订单价格">{{ orderModel.order_price }}</el-descriptions-item>
                    <el-descriptions-item label="订单时间">{{ orderModel.order_time }}</el-descriptions-item>
                    <el-descriptions-item label="订单状态">{{ orderStatusText }}</el-descriptions-item>
                    <el-descriptions-item label="物流单号">{{ orderModel.order_track_num }}</el-descriptions-item>
                    <el-descriptions-item label="订单备注">{{ orderModel.order_remark }}</el-descriptions-item>
                    <el-descriptions-item label="商品评价">{{ reviewModel.goods_review_content }}</el-descriptions-item>
                </el-descriptions>
            </el-col>
        </el-row>
        <el-row v-if="tags === '2'">
            <div class="pay">
                <img src="@/assets/images/zfb.png" alt="" fit="cover" :class="{ 'visited': isVisitedZfb }"
                    @click="markAsVisited('zfb')" />
                <img src="@/assets/images/vx.jpg" alt="" fit="cover" :class="{ 'visited': isVisitedVx }"
                    @click="markAsVisited('vx')" />
                <el-text class="points" :class="{ 'visited': isVisitedJf }" @click="markAsVisited('jf')">
                    积分
                    <el-text class="small">￥{{ account.usr_points / 100 }}</el-text>
                </el-text>
            </div>


        </el-row>
        <el-row v-if="tags === '3'">
            <el-form ref="addRef" label-width="auto" :model="reviewModel" :inline="true" :rules="rules">
                <el-col :span="24" :offset="0" style="width: 100%;">
                    <el-form-item label="评价" prop="goods_review_content" >
                        <el-input v-model="reviewModel.goods_review_content" style="width: 300px" :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea" placeholder="输入评价" />

                    </el-form-item>
                </el-col>
            </el-form>


        </el-row>
        <el-row v-if="tags === '2' || tags === '3'">
            <div class="dialog-footer">
                <el-button color="#493131e6" @click="commit" plain>
                    确定
                </el-button>
                <el-button @click="onClose">取消</el-button>
            </div>
        </el-row>
    </el-dialog>


</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, reactive, nextTick, computed } from 'vue';

import { delOrderApi, getCartDetailApi, allDelOrderApi, addOrderApi } from '@/api/frontend/goods/index';
import { getUsrOrdApi, getUsrAddrApi, getAccountApi, checkGoodsApi, getReviewApi, ReviewApi } from '@/api/frontend/usrinfo/index';
import { CodeToText } from 'element-china-area-data'
const tags = ref("")
const title = ref("")
const addRef = ref<FormInstance>()
const isvisible = ref(false)
const Model = reactive({
    order_track_num: '',
})
const usrAddrList: any = ref({});
const reviewModel = reactive({
    goods_review_id: '',
    goods_review_content: '',
    order_id: '',
})


const isVisitedZfb = ref(false);
const isVisitedVx = ref(false);
const isVisitedJf = ref(false);
const markAsVisited = (type: string) => {
    if (type === 'zfb') {
        isVisitedZfb.value = true;
        isVisitedVx.value = false;
        isVisitedJf.value = false;
        orderModel.payment_id = 1;
    } else if (type === 'vx') {
        isVisitedVx.value = true;
        isVisitedZfb.value = false;
        isVisitedJf.value = false;
        orderModel.payment_id = 2;
    } else if (type === 'jf') {
        isVisitedJf.value = true;
        isVisitedVx.value = false;
        isVisitedZfb.value = false;
        orderModel.payment_id = 3;
    }
    console.log(isVisitedZfb.value, isVisitedVx.value, isVisitedJf.value)

}


const onClose = () => {
    isvisible.value = false;
    orderModel.payment_id == 0

}
const commit = async () => {
    if (tags.value == '2') {

        console.log("submitOrder")
        // 
        orderModel.usr_id = user.usr_id;
        let res;
        let count = 0;

        if (orderModel.payment_id == 0) {
            ElMessage.error("请选择支付方式");
        } else {

            console.log(orderModel)
            res = await addOrderApi(orderModel, true)

            if (res.code == 200) {
                ElMessage.success(res.msg)
                //刷新页面
                getList()
                //关闭弹窗
                onClose()
            }
            if (res.code == 201) {
                // ElMessage.error("积分不够，请更换支付方式")
                ElMessage.error(res.msg)
                console.log(res);
                orderModel.order_id = res.data
            }

        }

    }
    if (tags.value == '3') {
        let res = await ReviewApi(reviewModel)
        if (res.code == 200) {
            ElMessage.success(res.msg)
            getList()
            onClose()
        }
    }

}

const account: any = ref([{}]);
const getAccount = async () => {
    //发送请求
    let res = await getAccountApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        account.value = res.data;
        console.log(account.value);

        loading.value = false;
    }
}


// 计算属性：根据 orderModel.order_status 返回订单状态文本
const orderStatusText = computed(() => {
    switch (orderModel.order_status) {
        case 0: return '待支付';
        case 1: return '待发货';
        case 2: return '待收货';
        case 3: return '已收货';
        case 4: return '评价完成';
        case -1: return '支付失败';
        case -2: return '取消订单';
        default: return '未知状态';
    }
});

//物流按钮
const dtrackBtn = async (item: any) => {

    // 发送请求
    tags.value = "0";
    title.value = "物流信息";
    isvisible.value = true;
    Model.order_track_num = item.order_track_num;

}

//提交订单
const orderModel = reactive({
    order_id: '',
    order_number: '',
    usr_id: '',
    addr_id: '',
    order_remark: '',
    order_track_num: '',

    goods_id: '',
    goods_color_id: '',
    goods_count: 0,
    payment_id: 0,
    order_price: 0,
    order_time: '',
    order_status: 0,
})

//查看详情
const selectedOptions: any = ref([])
const addModel = reactive({
    addr_id: '',
    addr_name: '',
    addr_phone: '',
    addr_province: 1,
    addr_city: 1,
    addr_country: 1,
    addr_detail: '',
    usr_id: ''
})
const detailBtn = (item: any) => {

    // 发送请求
    tags.value = "1";
    title.value = "查看详情";
    isvisible.value = true;
    nextTick(async () => {
        Object.assign(orderModel, item);
        let res = await getUsrAddrApi(item.addr_id)
        let res2 = await getReviewApi(item.order_id)

        if (res.code == 200 || res2.code == 200) {

            usrAddrList.value = res.data;
            reviewModel.goods_review_content = res2.data[0].goods_review_content;
            Object.assign(addModel, usrAddrList.value[0]);
        }
        // addModel.addr_province = selectedOptions.value[0];
        // addModel.addr_city = selectedOptions.value[1];
        // addModel.addr_country = selectedOptions.value[2];
    })


}

//支付按钮
const BuyBtn = async (item: any) => {

    // 发送请求
    tags.value = "2";
    title.value = "支付方式";
    isvisible.value = true;
    Object.assign(orderModel, item);


}
//确认收货
const checkGoods = async (item: any) => {

    Object.assign(orderModel, item);
    let res = await checkGoodsApi(orderModel)
    if (res.code == 200) {
        ElMessage.success(res.msg)
        getList()
    }
}
//评价
const ReviewBtn = async (item: any) => {

    tags.value = "3";
    title.value = "添加评价";
    isvisible.value = true;
    Object.assign(reviewModel, item);

}

const formatOrderTime = (timeStr: string | number | Date) => {
    const date = new Date(timeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1并补零
    const day = String(date.getDate()).padStart(2, '0'); // 日期补零
    return `${year}-${month}-${day}`;
};

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
            let res = await delOrderApi(id);
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
            let res = await allDelOrderApi(ids);
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
        selectedItems.value = usrOrderList.value.map((item: { order_id: any; }) => item.order_id);
        isIndeterminate.value = false
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

const updateSelectAll = () => {
    selectAll.value = selectedItems.value.length === usrOrderList.value.length;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < usrOrderList.value.length;
};




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
const usrOrderList: any = ref({});
const usrOrderMap: any = ref({});
const selectedItems = ref<any[]>([]);
const selectAll = ref(false);

const getList = async () => {
    //发送请求
    let res = await getUsrOrdApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        usrOrderList.value = res.data
        console.log("usrOrderList", usrOrderList.value);
        searchParam.total = res.data.total;


        for (let i = 0; i < usrOrderList.value.length; i++) {
            usrOrderMap.value[usrOrderList.value[i].order_id] = usrOrderList.value[i];


            getDetail(usrOrderList.value[i].goods_id, usrOrderList.value[i].goods_color_id, usrOrderList.value[i].order_id);

        }
        console.log("detailMap", detailMap.value);
        console.log("usrOrderMap", usrOrderMap.value);
        loading.value = false;

    }
}
const detail: any = ref([{}]);
const detailMap: any = ref({});
const getDetail = async (gid: string, gcid: string, index: string) => {
    //发送请求
    let res = await getCartDetailApi(gid, gcid)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        detail.value = res.data[0];

        detailMap.value[index] = detail.value;

        loading.value = false;
    }
}


onMounted(() => {

    getList();
    getAccount();
    document.querySelector('.el-scrollbar__wrap').addEventListener('scroll', handleScroll);

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
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus';

const router = useRouter();
const settlementRef = ref<HTMLElement | null>(null);

// 处理滚动事件
const handleScroll = () => {
    if (settlementRef.value) {
        const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
        // console.log(scrollableContainer.scrollTop);
        if (scrollableContainer.scrollTop > '515') {
            // settlementRef.value.style.top = '10px';
            settlementRef.value.style.position = 'fixed';

            settlementRef.value.style.backgroundColor = '#49313130';
            settlementRef.value.style.color = '#1d4350';
            settlementRef.value.style.backdropFilter = 'blur(13px)';
            settlementRef.value.style.height = '50px';
            settlementRef.value.style.paddingTop = '20px';

            // settlementRef.value.style.width = '28%';
        } else {
            settlementRef.value.style.position = 'absolute';
            settlementRef.value.style.backgroundColor = 'transparent';
            settlementRef.value.style.color = 'black';
            settlementRef.value.style.backdropFilter = 'blur(0px)';
            settlementRef.value.style.top = '10px';
            settlementRef.value.style.paddingTop = '0px';
            // settlementRef.value.style.right = '0';
            // settlementRef.value.style.width = '30%';
        }
    }
};


onUnmounted(() => {
    const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
    console.log(scrollableContainer);
    if (scrollableContainer) {
        scrollableContainer.addEventListener('scroll', handleScroll);
    }
});

const navigateToDetail = (id: string) => {
    console.log(id);
    router.push({ name: 'frontgoodsdetial', params: { id } });
};


const rules = reactive({
    goods_review_content: [
        { required: true, trigger: 'change', message: '请输入评价' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
})
</script>
<style scoped>
.pay {
    display: flex;
    gap: 20px;
    width: inherit;
    justify-content: center;

    .el-text {
        width: inherit;
        text-align: left;
    }

    img {
        width: 150px;
        height: 80px;
        border-radius: 10px;
        border: #e9e7ef solid 1px;
    }

    .points {
        border-radius: 10px;
        border: #e9e7ef solid 1px;
        padding: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        width: 100%;

        .small {
            font-size: 16px;
            font-weight: 300;
            color: #5f5e63;
            padding-top: 5px;
        }
    }

    .visited {
        border: #ff6600 1px solid;
    }

}

.dialog-footer {
    height: 50px;
    display: flex;
    width: 100%;
    justify-content: flex-end;
    align-items: center;

}

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
        padding: 10px 0;
        /* width: 100%; */
        border-radius: 4px;
        --el-border: #1d4350 solid 1px;
        position: relative;
        justify-content: space-between;
        margin: 0 20px;
    }

    .el-breadcrumb {
        margin-bottom: 20px;
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
    transform: none;
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
    /* animation: slide-fade-in both; */
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    /* animation-timeline: view(); */
    /* 定义动画时间范围 */
    /* animation-range: contain 0% contain 20%; */
    border-radius: 10px;

    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    width: 100%;
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    align-items: flex-start;
    /* align-items: center; */
    flex-direction: column;
    padding: 20px;

    .el-image {
        width: 10%;
        height: 80%;
        margin-right: 15px;
    }

    .content {
        display: flex;
        width: 100%;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;

        .title {
            width: 200px;
            /* padding: 10px; */
            display: flex;
            flex-direction: column;
            align-items: flex-start;


        }

        .price {
            width: 120px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .allprice {
            width: 140px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .number {
            width: 80px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .remark {
            width: 170px;
            /* height: auto; */
            /* text-align: center; */
            /* margin: 10px; */
            /* white-space: nowrap; */
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .status {
            width: 120px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .delete {
            width: 140px;
            display: flex;
            flex-direction: column;
            align-items: center;
            /* justify-content: center; */
            /* padding: 10px; */

            .el-button {
                /* width: 100px; */
                margin: 2px 0;
                padding: 8px 15px;
                font-size: 18px;
            }
        }

        .name,
        .color {
            cursor: pointer;
            /* width: 100%; */
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;
            overflow: hidden;
            text-overflow: ellipsis;
        }

    }

}

.Settlement {
    animation: slide-fade-in both;
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    animation-timeline: view();
    /* 定义动画时间范围 */
    animation-range: contain 0% contain 30%;
    border-radius: 10px;
    position: absolute;
    right: 0px;
    top: 40px;
    /* transition: all 0.3s ease; */

    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px 20px;


    width: 420px;
    margin-left: 50px;
    display: flex;
    flex-direction: column;



    .selected-items {
        width: 100%;
        margin-bottom: 10px;
        display: flex;
        min-height: 200px;


    }

    .selected-item {
        /* display: flex;
        align-items: center; */
        padding: 10px;
        /* border-bottom: 1px solid #eee; */
    }

    .total-price {
        display: flex;
        justify-content: space-between;
        width: 100%;
        padding: 10px 0;
        border-top: 1px solid #eee;
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