<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <div class="a">
            <input type="text" class="b" v-model="searchParam.goods_content">
            <div class="c" @click="search">
                <el-icon>
                    <Search />
                </el-icon>
            </div>
        </div>
        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <el-button @click="allDeleteBtn" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
            <el-button @click="allStarBtn" color="#493131b0" plain>移入收藏</el-button>
        </div>

        <el-empty description="暂时没有数据" v-if="cartList.length === 0" style="width: 70%;" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards initial-visible" v-for="item in cartList">
                <div class="card " v-if="detailMap[item.cart_id] !== undefined && cartMap[item.cart_id] !== undefined">
                    <el-checkbox-group v-model="selectedItems">
                        <el-checkbox :value="item.cart_id" :key="item.cart_id"
                            @change="handleSelectChange(item.cart_id, $event)"><br>
                        </el-checkbox>
                    </el-checkbox-group>
                    <el-image :src="detailMap[item.cart_id].img_path_url" alt="" fit="cover" />
                    <div class="content">

                        <div class="title">
                            <el-text @click="navigateToDetail(item.goods_id)" style=" font-size: 20px;">{{
                                detailMap[item.cart_id].goods_name }}</el-text>
                        </div>

                        <div class="color">
                            <el-text style="font-size: 20px" @click="updateColor(detailMap[item.cart_id], item)">
                                颜色分类：{{ detailMap[item.cart_id].goods_color_name }}
                                <div class='ribbon1'>修改</div>
                            </el-text>
                        </div>
                        <div class="price">
                            <el-text style="color: #ff6600;font-size: 20px">
                                ￥{{ detailMap[item.cart_id].goods_price }}
                            </el-text>
                        </div>

                        <div class="number">
                            <el-input-number v-model="cartMap[item.cart_id].goods_count" :min="1"
                                :max="detail.goods_inventory"></el-input-number>
                        </div>

                        <div class="delete">

                            <el-button link @click="deleteBtn(item.cart_id)" color="#493131" plain
                                style=" font-size: 20px;margin-bottom: 5px;">删除</el-button>
                            <el-button link @click="Star(item)" color="#493131" plain
                                style=" font-size: 20px;margin-top: 5px;">移入收藏</el-button>

                        </div>

                    </div>

                </div>

            </div>

        </el-skeleton>
        <div class="Settlement" ref="settlementRef">
            <el-text style="padding-top: 10px;font-weight:500;color: #493131;font-size: 20px;">结算明细</el-text>
            <el-empty description="请选择商品" v-if="selectedItems.length === 0" :image-size="80" />
            <div v-else style="width: 100%;">
                <el-scrollbar max-height="700px">
                    <div class="selected-items">
                        <div v-for="id in selectedItems" :key="id" class="selected-item">
                            <el-image :src="detailMap[id]?.img_path_url" fit="cover"
                                style="width: 60px; height: 60px; margin-right: 10px;" />
                            <!-- <div>
                                <el-text>{{ detailMap[id]?.goods_name }}</el-text>

                            </div> -->
                        </div>
                    </div>
                </el-scrollbar>
            </div>
            <div class="total-price">
                <el-text style="font-weight:500;color: #493131;font-size: 20px;">合计：</el-text>
                <el-text style="font-weight:500;color: #ff6600;font-size: 20px;">
                    ￥{{ calculateTotalPrice().toFixed(2) }}
                </el-text>
            </div>
            <el-button style="font-size: 20px; margin-top: 20px;" @click="addOrder()" color="#493131"
                :disabled="selectedItems.length === 0">
                结算({{ selectedItems.length }})
            </el-button>
        </div>

    </div>
    <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <div style="padding: 20px; display: flex; gap: 20px;">

                <el-image :src="colorModel.img_path_url" style="width: 120px; height: 120px;" fit="cover" />
                <div
                    style="display: flex; flex-direction: column; justify-content: center;justify-content: space-around;width: 100%;">
                    <el-text style="color: #ff6600; font-size: 20px; font-weight: bold;text-align: left;width: 100%;">
                        ￥{{ colorModel.goods_price }}
                    </el-text>
                    <el-divider />
                    <div style="display: flex;gap: 20px;text-align: center;">
                        <el-text style="font-size: 16px; margin-bottom: 10px; display: block;">颜色分类</el-text>
                        <SelectChecked :options="options" @selected="selected" v-model="addModel.goods_color_id" />
                    </div>
                </div>
            </div>
        </template>
    </UsrDialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, reactive, nextTick } from 'vue';
import BreadCrumb from '@/Client/Header/BreadCrumb.vue';
import SelectChecked from "@/components/SelectChecked.vue"
import {
    allDeleteCartApi, delCartApi, getCartListApi, getCartDetailApi,
    selectTypeListApi, StarCartApi, updateColorApi,allStarCartApi
} from '@/api/frontend/goods/index';

const Star = async (item: any) => {

    ElMessageBox.confirm(
        '是否收藏该商品?',
        '系统警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {//点击确定
            let res = await StarCartApi(item.goods_id, user.usr_id,item.cart_id)

            if (res.code == 200) {
                console.log(res.msg);
                ElMessage.success(res.msg)
                getList()

                // getStar()
            }

        })
        .catch(() => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
        })


}

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
            let res = await delCartApi(id);
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

// 批量收藏
const allStarBtn = () => {
    if (selectedItems.value.length === 0) return
    ElMessageBox.confirm(
        `是否确定收藏选中的${selectedItems.value.length}个商品?`,
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
            const gids=ids.map(id => detailMap.value[id].goods_id);
            console.log(gids);
            let res = await allStarCartApi(gids, user.usr_id,ids);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                selectedItems.value = [] // 清除选择
                getList()
            }
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '已取消批量收藏',
            })
        });
}

const allDeleteBtn = async () => {
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
            let res = await allDeleteCartApi(ids);
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
        selectedItems.value = cartList.value.map((item: { cart_id: any; }) => item.cart_id);
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
    selectAll.value = selectedItems.value.length === cartList.value.length;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < cartList.value.length;
};


import UsrDialog from "@/components/UsrDialog.vue"
import useDialog from '@/hooks/useDialog';

//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()

//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();
}



//弹窗 提交
const commit = async () => {

    console.log(addModel)
    let res = await updateColorApi(addModel);
    if (res.code == 200) {
        ElMessage.success(res.msg);
        getList();
        onClose();
    }
}

//添加按钮
const updateColor = (item: any, cartitem: any) => {
    dialog.visible = true;
    dialog.title = '修改颜色';
    dialog.width = 700;
    dialog.height = 300;

    nextTick(() => {
        //复制数据
        console.log(item)


        colorModel.goods_price = item.goods_price;
        colorModel.img_path_url = item.img_path_url;
        addModel.goods_color_id = item.goods_color_id;
        addModel.goods_id = item.goods_id;
        addModel.cart_id = cartitem.cart_id;
        addModel.goods_count = cartitem.goods_count;





        getSelectType(item);

    })

}

//下拉数据
const options = ref([])

//下拉选择 勾选的数据
const selected = (value: string) => {
    // addModel.goods_color_id = value;
    console.log(value);
}

//下拉数据 后台获取数据
const getSelectType = async (item: any) => {
    let res = await selectTypeListApi(item.goods_id);
    if (res.code == 200) {
        options.value = res.data

        console.log("后台数据：" + options.value)
    }

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
const cartList: any = ref([{}]);
const cartMap: any = ref({});
const selectedItems = ref<any[]>([]);
const selectAll = ref(false);
import { JwtToken } from '@/api/frontend/usrinfo/index';

const getList = async () => {
    //发送请求
    try {
        await JwtToken();
        // 发送请求
        let res = await getCartListApi(searchParam, user.usr_id)
        if (res.code == 200) {
            // console.log(res)
            //设置表格数据
            cartList.value = res.data
            console.log("cartList", cartList.value);
            searchParam.total = res.data.total;


            for (let i = 0; i < cartList.value.length; i++) {
                cartMap.value[cartList.value[i].cart_id] = cartList.value[i];


                getDetail(cartList.value[i].goods_id, cartList.value[i].goods_color_id, cartList.value[i].cart_id);

            }
            console.log("detailMap", detailMap.value);
            console.log("cartMap", cartMap.value);
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
const addModel = reactive({
    goods_id: '',
    usr_id: user.usr_id,
    goods_color_id: '',

    cart_id: '',
    goods_count: 0,


})

const colorModel = reactive({
    img_path_url: '',

    goods_price: '',

})

// watch(goodsList, () => {
//     showEmpty.value = goodsList.value.length === 0;
//     console.log(showEmpty.value);
// });

onMounted(() => {

    getList();

    document.querySelector('.el-scrollbar__wrap').addEventListener('scroll', handleScroll);


    // 标记首屏可见的卡片
    setTimeout(() => {
        const cards = document.querySelectorAll('.cards');
        const viewportHeight = window.innerHeight;

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

import { goodsStore } from '@/store/GoodsConfirm/index';
import Console from '@/views/backend/Console.vue';
const goods = goodsStore();
const addOrder = () => {
    if (selectedItems.value.length === 0) {
        ElMessage.warning('请选择要结算的商品');
        return;
    }

    goods.clearGoods();
    selectedItems.value.forEach(id => {
        const cartItem = cartMap.value[id];
        const detailItem = detailMap.value[id];
        if (cartItem && detailItem) {
            const goodsData = {
                goods_name: detailItem.goods_name,
                goods_color_name: detailItem.goods_color_name,
                goods_color_id: cartItem.goods_color_id,
                goods_price: detailItem.goods_price,
                goods_count: cartItem.goods_count,
                goods_url: detailItem.img_path_url,
                goods_inventory: detailItem.goods_inventory,
                goods_id: cartItem.goods_id,
                direct_pay: false,
            };
            goods.addGoods(goodsData);
        }
    });

    // 传递到订单页面
    router.push({
        name: 'frontgoodsorder'
    });
}


// 计算总价
const calculateTotalPrice = () => {
    return selectedItems.value.reduce((total, id) => {
        const item = cartMap.value[id];
        const detail = detailMap.value[id];
        if (item && detail) {
            return total + (item.goods_count * detail.goods_price);
        }
        return total;
    }, 0);
};


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
    width: 65%;
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;

    margin: 30px 0;

}

.card {
    opacity: 1;
    transform: none;
    padding: 15px;
    display: flex;
    justify-content: center;
    align-items: center;



    .el-image {
        width: 10%;
        height: 80%;
        margin-right: 15px;
    }

    .content {
        display: flex;
        width: 100%;
        /* height: 100%; */
        flex-direction: row;
        justify-content: space-around;

        /* align-content: flex-start !important; */
        .title,
        .color,
        .number,
        .price,
        .delete {
            /* margin: auto; */
            padding: 10px;
            display: flex;
            align-items: center;
            position: relative;
            /* width: 120px; */
        }

        .delete {
            display: flex;
            flex-direction: column;
        }

        .title {
            cursor: pointer;
        }

        .color {
            .ribbon1 {
                position: absolute;
                width: 0;
                color: transparent;
            }
        }

        .color:hover {
            border: #ff6600 dashed 1px;
            padding: 9px;
            cursor: pointer;

            .ribbon1 {
                /* line-height: 5px; */
                text-align: center;
                transform: rotate(42deg);
                position: absolute;
                /* padding: 8px 0; */
                right: -22px;
                top: 5px;
                width: 80px;
                background: #ff6600;
                color: white;
                box-shadow: 0 5px 5px rgba(0, 0, 0, 0.1);
                letter-spacing: 1px;
                font-size: 12px;
            }

            .ribbon1:before,
            .ribbon1:after {
                content: "";
                border-top: 4px solid #4e7c7d;
                border-left: 4px solid transparent;
                border-right: 4px solid transparent;
                position: absolute;
                bottom: -4px;
            }

            .ribbon1:before {
                left: 0;
            }

            .ribbon1:after {
                right: 0;
            }
        }
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
    z-index: 99;



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