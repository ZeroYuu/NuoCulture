<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <el-skeleton :rows="5" animated :loading="loading">


            <div class="cards initial-visible">
                <el-empty description="暂时没有数据" v-if="addrList.length === 0" style="width: 70%;" />

                <div class="cardtop">
                    <div class="top">
                        确认收货地址
                        <div>
                            <el-button link @click="addBtn" color="#493131" plain
                                style=" font-size: 18px">使用新地址</el-button>
                            <el-button link @click="manageAddr" color="#493131" plain
                                style=" font-size: 18px">管理地址</el-button>

                        </div>


                    </div>

                    <div class="content">
                        <el-empty description="暂时没有数据" v-if="addrList.length === 0" style="width: 40%;" />
                        <template v-for="(item, index) in addrList" :key="index">

                            <template v-if="showAllAddr || index < 3">
                                <div class="address" :class="{ 'visited': item.isVisited }" @click="addressClick(item)">
                                    <el-icon :size="30" color="#1d4350">
                                        <component :is="item.showAddr ? 'LocationFilled' : 'Location'"
                                            :class="{ 'visitedicon': item.isVisited }" />
                                    </el-icon>
                                    <div class="detailAddr">
                                        <el-text>
                                            {{ CodeToText[item.addr_province] }}&nbsp;{{ CodeToText[item.addr_city]
                                            }}&nbsp;{{
                                                CodeToText[item.addr_district] }}
                                        </el-text>
                                        <el-text>
                                            {{ item.addr_detail }}
                                        </el-text>
                                        <el-text>

                                            {{ item.addr_name }}&nbsp;{{ item.addr_phone }}
                                        </el-text>
                                        <div class='edit' @click="editBtn(item)">编辑</div>
                                    </div>
                                </div>
                            </template>
                        </template>
                    </div>
                    <div class="bottom" @click="toggleShowAllAddr">
                        <el-text>{{ showAllAddrText }}</el-text>
                        <el-icon>
                            <component :is="showAllAddr ? 'ArrowUp' : 'ArrowDown'" />
                        </el-icon>

                    </div>

                </div>

            </div>

            <div class="cards initial-visible">
                <!-- <el-empty description="暂时没有数据" v-if="goodsList.length === 0" style="width: 70%;" /> -->
                <div class="top">
                    确认订单信息

                </div>
                <div style="width:900px;z-index: 99;position: absolute;top: 60px;" ref="barRef">
                    <el-row>
                        <el-col :span="8">
                            宝贝
                        </el-col>
                        <el-col :span="5">
                            商品属性
                        </el-col>
                        <el-col :span="3" style="margin-left: 30px;">
                            原价
                        </el-col>
                        <el-col :span="3" style="margin-left: 30px;">
                            数量
                        </el-col>
                        <el-col :span="3" style="margin-left: 15px;">
                            小计
                        </el-col>
                    </el-row>
                </div>
                <div class="cardbottom" v-for="item in goodsList">

                    <el-image :src="item.goods_url" alt="" fit="cover" />
                    <div class="content">

                        <div class="title">
                            <el-text style=" font-size: 20px;">{{
                                item.goods_name }}</el-text>
                        </div>

                        <div class="color">
                            <el-text style="font-size: 20px">
                                颜色分类：{{ item.goods_color_name }}
                            </el-text>
                        </div>
                        <div class="price">
                            <el-text style="font-size: 20px">
                                ￥{{ item.goods_price }}
                            </el-text>
                        </div>

                        <div class="number">
                            <el-input-number v-model="item.goods_count" :min="1"
                                :max="item.goods_inventory"></el-input-number>
                        </div>
                        <div class="allprice">
                            <el-text style="color: #ff6600;font-size: 20px">
                                ￥{{ item.goods_price * item.goods_count }}
                            </el-text>
                        </div>
                    </div>

                </div>

            </div>

        </el-skeleton>
        <div class="Settlement" ref="settlementRef">
            <div class="settletop">
                <div style="display: flex;gap: 10px;">
                    <el-text>付款详情</el-text>
                    <el-text class="small">共<el-text>{{ goodsList.length }}</el-text>件商品</el-text>
                </div>

                <div class="money">
                    <img src="@/assets/images/money1.png" alt="">:{{ account.usr_points }}
                    <el-text class="small">={{ account.usr_points / 100 }}</el-text>
                </div>

            </div>
            <div class="total-price">
                <el-text>商品总价</el-text>
                <el-text>
                    ￥{{ calculateTotalPrice().toFixed(2) }}
                </el-text>
            </div>
            <div class="payment">
                <el-text>付款方式</el-text>
                <div style="display:flex;gap: 20px;width: inherit;">
                    <img src="@/assets/images/zfb.png" alt="" fit="cover" :class="{ 'visited': isVisitedZfb }"
                        @click="markAsVisited('zfb')" />
                    <img src="@/assets/images/vx.jpg" alt="" fit="cover" :class="{ 'visited': isVisitedVx }"
                        @click="markAsVisited('vx')" />
                    <el-text class="points" :class="{ 'visited': isVisitedJf }" @click="markAsVisited('jf')">
                        积分
                        <el-text class="small">￥{{ account.usr_points / 100 }}</el-text>
                    </el-text>
                </div>
            </div>
            <div class="remark">
                <el-text>备注</el-text>
                <div style="display:flex;gap: 20px;width: inherit;">
                    <el-input v-model="orderModel.order_remark" style="width: 100%" :autosize="{ minRows: 2 }"
                        type="textarea" placeholder="在此输入备注" />
                </div>
            </div>
            <el-divider />
            <div class="bottom">
                <div class="actualpayment">
                    <el-text style="font-size: 16px;">实付款</el-text>
                    <el-text style="color: #ff6600;font-size: 20px;font-weight: 600;" v-if="isVisitedJf == true">
                        ￥0.00
                    </el-text>
                    <el-text style="color: #ff6600;font-size: 20px;font-weight: 600;" v-else>
                        ￥{{ calculateTotalPrice().toFixed(2) }}
                    </el-text>
                </div>
                <div>
                    <el-tooltip class="box-item" effect="dark" content="返回购物车" placement="top">
                        <el-button @click="backCart" class="back" plain>返回</el-button>
                    </el-tooltip>
                    <el-button type="primary" style="margin-left: 10px;font-size: 20px;"
                        @click="submitOrder">提交订单</el-button>
                </div>
            </div>


        </div>

    </div>

    <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="省/市/区" prop="address">
                            <el-cascader placeholder="请选择" :options="regionData" filterable v-model="selectedOptions"
                                @change="handleChange" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="详细地址" prop="addr_detail">
                            <el-input v-model="addModel.addr_detail" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="收货人" prop="addr_name">
                            <el-input v-model="addModel.addr_name" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="收货人电话" prop="addr_phone">
                            <el-input v-model="addModel.addr_phone" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </template>
    </UsrDialog>

</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, reactive, computed, nextTick } from 'vue';
import BreadCrumb from '@/Client/Header/BreadCrumb.vue';
import SelectChecked from "@/components/SelectChecked.vue"
import { addOrderApi } from '@/api/frontend/goods/index'

import { getUsrAddrApi, getAccountApi, addAddrApi, editAddrApi } from '@/api/frontend/usrinfo/index'
import { useRoute } from 'vue-router';
import { regionData, CodeToText } from 'element-china-area-data'
import { goodsStore } from '@/store/GoodsConfirm/index';

import UsrDialog from "@/components/UsrDialog.vue"
import useDialog from '@/hooks/useDialog';


const route = useRoute()
const manageAddr = () => {
    router.push({ name: 'usraddress' });
}
const backCart = () => {
    router.push({ name: 'frontgoodscart' });
}


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



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()
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

//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();
    addModel.addr_id = '';
    selectedOptions.value = [];
    // handleChange();

}
const tags = ref("")
//编辑
const editBtn = (row: any) => {
    console.log(row);
    //显示弹窗
    tags.value = "1";
    dialog.visible = true;
    dialog.title = '编辑';
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);
        selectedOptions.value[0] = addModel.addr_province.toString();
        selectedOptions.value[1] = addModel.addr_city.toString();
        if (addModel.addr_country != 0) {
            selectedOptions.value[2] = addModel.addr_country.toString();
        }

        // handleChange();
    })
    //清空表单
    addRef.value?.resetFields();
}


const addBtn = () => {
    addModel.addr_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '发布话题';
    dialog.height = 300;

    //清空表单
    addRef.value?.resetFields();
}

const options2 = ref([])
//勾选的数据
const selected2 = (value: string) => {
    addModel.usr_id = value;
    console.log(value);
}
const selectedOptions: any = ref([])
const handleChange = () => {
    addModel.addr_province = selectedOptions.value[0];
    addModel.addr_city = selectedOptions.value[1];
    addModel.addr_country = selectedOptions.value[2];
    console.log("handleChange", selectedOptions.value)

}


//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid: any) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;


            if (tags.value == "0") {
                //添加

                res = await addAddrApi(addModel)

            } else {
                //修改
                res = await editAddrApi(addModel)

            }


            if (res.code == 200) {
                // console.log(res)
                ElMessage.success(res.msg)
                //刷新页面
                getList()
                //关闭弹窗
                onClose()
            }
        } else {
            console.log('表单验证不通过')
        }
    })
}



//提交订单
const orderModel = reactive({
    order_id: '',
    usr_id: '',
    addr_id: '',
    order_remark: '',
    goods_id: '',
    goods_color_id: '',
    goods_count: 0,
    payment_id: 0,
    order_price: 0,
    order_status: 0,
})

const submitOrder = async () => {
    console.log("submitOrder")
    // 
    orderModel.usr_id = user.usr_id;
    let res;
    let count = 0;
    for (let i = 0; i < goodsStoreInstance.goodsList.length; i++) {

        orderModel.goods_id = goodsStoreInstance.goodsList[i].goods_id;
        orderModel.goods_color_id = goodsStoreInstance.goodsList[i].goods_color_id;
        orderModel.goods_count = goodsStoreInstance.goodsList[i].goods_count;
        if (orderModel.payment_id == 0) {
            ElMessage.error("请选择支付方式");
        } else {


            res = await addOrderApi(orderModel, goodsStoreInstance.goodsList[0].direct_pay)

            if (res.code == 200) {
                count++;
            }
            if (res.code == 201) {
                // ElMessage.error("积分不够，请更换支付方式")
                ElMessage.error(res.msg)
                console.log(res);
                orderModel.order_id = res.data
            }
        }

    }

    if (count == goodsStoreInstance.goodsList.length) {
        ElMessage.success(res.msg)
        //刷新页面
        router.push({ name: 'frontgoodscart' });
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


// 购物车数据
const goodsStoreInstance = goodsStore();
const goodsList = computed(() => goodsStoreInstance.goodsList);

//展示地址
const showAllAddr = ref(false);
const showAllAddrText = ref("显示全部地址");
const toggleShowAllAddr = () => {
    showAllAddr.value = !showAllAddr.value;
    console.log(showAllAddrText.value);
    if (showAllAddr.value) {
        showAllAddrText.value = "隐藏多余地址";
    } else {
        showAllAddrText.value = "显示全部地址";
    }
};



//点击效果
const addressClick = (item: typeof addrList[0]) => {

    // 重置所有地址的isActive状态
    addrList.value.forEach((addr: { isVisited: boolean; showAddr: boolean }) => {
        addr.isVisited = false;
        addr.showAddr = false; // 重置showAddr
    });
    // 设置点击的地址为激活状态
    item.isVisited = true;
    item.showAddr = true;
    orderModel.addr_id = item.addr_id;

    console.log(item)

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


import { userStore } from '@/store/user/index'
const user = userStore()
const addrList: any = ref([]);

const getList = async () => {
    //发送请求
    let res = await getUsrAddrApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        addrList.value = res.data.map((item: any, index: number) => ({
            ...item,
            showAddr: index === 0 ? true : false,
            isVisited: index === 0 ? true : false
        }));
        console.log("addrList", addrList.value);
        searchParam.total = res.data.total;
        orderModel.addr_id = res.data[0].addr_id;
        orderModel.order_id = '';
        loading.value = false;

    }
}


onMounted(() => {

    getList();
    getAccount();
    document.querySelector('.el-scrollbar__wrap').addEventListener('scroll', handleScroll);

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
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus';

const router = useRouter();
const settlementRef = ref<HTMLElement | null>(null);
const barRef = ref<HTMLElement | null>(null);

// 处理滚动事件
const handleScroll = () => {
    if (settlementRef.value) {
        const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
        // console.log(scrollableContainer.scrollTop);
        if (scrollableContainer.scrollTop > '180') {
            settlementRef.value.style.top = '10px';
            settlementRef.value.style.position = 'fixed';
            settlementRef.value.style.right = '50px';
            // settlementRef.value.style.width = '28%';
        } else {
            settlementRef.value.style.position = 'absolute';
            settlementRef.value.style.top = '40px';
            settlementRef.value.style.right = '0';
            // settlementRef.value.style.width = '30%';
        }
    }
    if (barRef.value) {
        const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
        // console.log(scrollableContainer.scrollTop);
        if (scrollableContainer.scrollTop > '540') {
            barRef.value.style.top = '10px';
            barRef.value.style.position = 'fixed';
            barRef.value.style.backgroundColor = '#49313130';
            barRef.value.style.color = '#1d4350';
            barRef.value.style.backdropFilter = 'blur(13px)';
            barRef.value.style.height = '50px';
            barRef.value.style.paddingTop = '20px';

            // barRef.value.style.width = '28%';
        } else {
            barRef.value.style.position = 'absolute';
            barRef.value.style.backgroundColor = 'transparent';
            barRef.value.style.color = 'black';
            barRef.value.style.backdropFilter = 'blur(0px)';
            barRef.value.style.top = '60px';
            barRef.value.style.paddingTop = '0px';
            // barRef.value.style.right = '0';
            // barRef.value.style.width = '30%';
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



// 计算总价
const calculateTotalPrice = () => {
    let total = 0;
    goodsList.value.forEach((item: any) => {
        if (item) {
            total += item.goods_price * item.goods_count;
        }
    });
    orderModel.order_price = total;

    return total;
};

const validCascader = (rule: any, value: any, callback: any) => {
    console.log(selectedOptions.value.length);
    if (selectedOptions.value.length === 0) {
        callback(new Error('请输入地址'));
    } else {
        callback();
    }

};


//表单验证
const rules = reactive({
    address: [
        { required: true, validator: validCascader, trigger: 'change', message: '请输入地址' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    addr_detail: [
        { required: true, message: '请输入地址', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    addr_name: [
        { required: true, message: '请输入收货人', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    addr_phone: [
        { required: true, message: '请输入收货人电话', trigger: 'change' },
        { min: 11, max: 11, message: '电话号码必须是11位数字', trigger: 'blur' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    usr_id: [
        { required: true, message: '请选择所属账户', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
})

</script>
<style scoped>
.a {
    position: absolute;
    top: 75px;
    /* left:250px; */
    right: 500px;
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

    .batch-actions {
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 20px;
        padding: 10px;
        border-radius: 4px;
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
    width: 63%;
    position: relative;

}

.cardtop {
    opacity: 1;
    transform: none;
    padding: 10px;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    flex-direction: column;
    width: 100%;
    box-sizing: border-box;

    .el-image {
        width: 10%;
        height: 80%;
        margin-right: 15px;
    }

    .top {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: inherit;
        color: #493131;
        font-size: 18px;
    }

    .content {
        display: flex;
        /* width:100% ; */
        /* height: 100%; */
        flex-direction: row;
        justify-content: flex-start;
        gap: 20px;
        margin: 20px 0;
        flex-wrap: wrap;



        .address {
            display: flex;
            width: 31%;
            padding: 20px;
            border-radius: 10px;
            border: #e9e7ef 1px solid;
            box-sizing: border-box;
            cursor: pointer;
            overflow: hidden;

            .edit {
                display: none;
            }

        }

        .address:hover {
            border: #ff6600 1px solid;
            position: relative;

            .edit {
                position: absolute;
                display: block;
                bottom: 0;
                right: 0;
                background-color: #d2d0d8a1;
                font-size: 13px;
                padding: 2px 5px;
                border-radius: 10px 0 0 0;
            }

            .edit:hover {

                color: #ff6600;
            }
        }

        .visited {
            border: #ff6600 1px solid;
        }

        .visitedicon {
            color: #ff6600;
        }

        .detailAddr {
            display: flex;
            flex-direction: column;

            .el-text {
                font-size: 18px;
                text-align: left;
                width: 100%;
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
                overflow: hidden;
                text-overflow: ellipsis;
            }


        }
    }

    .bottom {
        display: flex;
        align-items: center;
        /* width: inherit; */
        cursor: pointer;
    }

    .bottom:hover {
        color: #ff6600;

        .el-text {
            color: #ff6600;
        }
    }
}

.cardbottom {
    opacity: 1;
    transform: none;
    padding: 10px;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    /* flex-direction: column; */
    width: 100%;
    margin-top: 60px;

    .el-image {
        width: 10%;
        height: 80%;
        margin-right: 15px;
    }

    .top {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: inherit;
        color: #493131;
        font-size: 18px;
    }

    .content {
        display: flex;
        width: inherit;
        /* height: 100%; */
        flex-direction: row;
        justify-content: space-between;
        align-items: center;

        .title {
            width: 160px;
            /* padding: 10px; */
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .color {
            width: 240px;
            /* padding: 10px; */
        }

        .price {
            width: 120px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .allprice {
            width: 120px;
            /* text-align: center; */
            /* padding: 10px; */
        }

        .number {
            /* width: 100px; */
            /* text-align: center; */
            /* padding: 10px; */
        }

    }

    .bottom {
        display: flex;
        /* width: inherit; */
    }
}


.Settlement {
    /* animation: slide-fade-in both; */
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    /* animation-timeline: view(); */
    /* 定义动画时间范围 */
    /* animation-range: contain 0% contain 30%; */
    border-radius: 10px;
    position: absolute;
    right: 0px;
    top: 40px;
    z-index: 99;
    /* transition: all 0.3s ease; */

    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;


    width: 420px;
    margin-left: 50px;
    display: flex;
    flex-direction: column;

    .el-text {
        padding-top: 10px;
        font-weight: 500;
        color: #493131;
        font-size: 20px;

    }

    .settletop {
        display: flex;
        flex-direction: row;
        width: inherit;
        gap: 10px;
        align-items: center;
        justify-content: space-between;

        .money {
            display: flex;
            align-items: center;
            font-size: 20px;

            img {
                height: 30px;
                width: 30px;
                margin-right: 5px;
            }

            .el-text {
                padding: 0;
            }

        }


        .small {
            font-size: 16px;
            font-weight: 300;
        }

    }


    .payment {
        width: inherit;
        /* margin-bottom: 20px; */
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 20px;

        /* min-height: 200px; */

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

    .remark {
        width: inherit;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 20px;

        .el-text {
            width: 100%;
            text-align: left;
        }
    }


    .bottom {
        display: flex;
        align-items: center;
        width: inherit;
        /* border-top: 1px solid #eee; */
        justify-content: space-between;

        .actualpayment {
            display: flex;
            flex-direction: column;

            .el-text {
                width: 100%;
                text-align: left;
            }
        }

        .back {
            margin-left: 10px;
            font-size: 16px;
            border: 1px solid #f7f7f7;
        }

        .back:hover {
            margin-left: 10px;
            font-size: 16px;
            background-color: #f7f7f7;
        }

        .el-button {
            height: 48px;
            line-height: 48px;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
        }
    }



    .total-price {
        display: flex;
        justify-content: space-between;
        width: 100%;
        padding: 10px 0;
        /* border-top: 1px solid #eee; */
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