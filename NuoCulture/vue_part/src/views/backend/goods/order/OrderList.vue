<template>
    <div class="search">
        <!-- 搜索框 -->
        <div class="seaBtn">
            <el-button color="#493131b0" :icon="Search" @click="searchBtn">搜索</el-button>
            <el-button :icon="Refresh" @click="resetBtn" plain>重置</el-button>
        </div>
        <div class="searchCard">
            <el-form :inline="true" :model="searchParam" class="forme" size="default" label-position="right"
                label-width="auto">
                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="省">
                            <el-select v-model="selectedProvinceCode" placeholder="请选择省份" @change="handleProvinceChange"
                                filterable>
                                <el-option v-for="province in provinces" :key="province.value" :label="province.label"
                                    :value="province.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="市">
                            <el-select v-model="selectedCityCode" placeholder="请选择城市" @change="handleCityChange"
                                filterable>
                                <el-option v-for="city in filteredCities" :key="city.value" :label="city.label"
                                    :value="city.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="区">
                            <el-select v-model="selectedDistrictCode" placeholder="请选择区" @change="handleDistrictChange"
                                filterable>
                                <el-option v-for="district in filteredDistricts" :key="district.value"
                                    :label="district.label" :value="district.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="详细地址">
                            <el-input v-model="searchParam.addr_detail" clearable />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="收货人">
                            <el-input v-model="searchParam.addr_name" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="收货电话">
                            <el-input v-model="searchParam.addr_phone" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="下单账号">
                            <el-input v-model="searchParam.usr_id" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="支付方式">
                            <SelectChecked :options="paymentoptions" @change="selected" ref="selectRef"
                                v-model="searchParam.payment_id" filterable />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="订单编号">
                            <el-input v-model="searchParam.order_number" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="物流单号">
                            <el-input v-model="searchParam.order_track_num" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="订单状态">
                            <SelectChecked :options="statusoptions" @change="selected" ref="selectRef"
                                v-model="searchParam.order_status" filterable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="备注">
                            <el-input v-model="searchParam.order_remark" clearable />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8" :offset="-3">
                        <el-form-item label="下单时间" prop="usr_bday">
                            <el-date-picker v-model="searchData.order_time" type="daterange" start-placeholder="开始日期"
                                end-placeholder="结束日期" :default-time="defaultTime" @change="getserchData"
                                value-format="YYYY-MM-DD" />
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
        </div>
    </div>

    <!-- 表格数据 -->
    <div class="table">
        <!-- <div class="taBtn">
            <el-button color="brown" :icon="EditPen" plain @click="addBtn">新增</el-button>
            <el-button type="danger" :icon="Delete" plain @click="allDeleteBtn">批量删除</el-button>
        </div> -->
        <el-table ref="tableList" :data="tableData" stripe
            :header-cell-style="{ background: ' #493131e6', color: 'white', 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange"
            :show-overflow-tooltip="true">
            <el-table-column type="selection" width="55" />
            <el-table-column label="订单编号">
                <template #default="scope">
                    {{ scope.row.order_number ? scope.row.order_number : "--" }}
                </template>
            </el-table-column>
            <el-table-column prop="usr_account" label="下单账户">
                <template #default="scope">
                    <el-text>
                        {{ accountMap[scope.row.usr_id] ? accountMap[scope.row.usr_id] : "--" }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="收货人">
                <template #default="scope">
                    <el-text v-if="addressMap[scope.row.addr_id] !== undefined">
                        {{ addressMap[scope.row.addr_id][0].addr_name ? addressMap[scope.row.addr_id][0].addr_name :
                            "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="收货电话">
                <template #default="scope">
                    <el-text v-if="addressMap[scope.row.addr_id] !== undefined">
                        {{ addressMap[scope.row.addr_id][0].addr_phone ? addressMap[scope.row.addr_id][0].addr_phone :
                            "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="地址">
                <template #default="scope">
                    <el-text v-if="addressMap[scope.row.addr_id] !== undefined">
                        {{ tableAddr(addressMap[scope.row.addr_id]) }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="下单时间">
                <template #default="scope">
                    <el-text>
                        {{ scope.row.order_time ? scope.row.order_time : "--" }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="备注">
                <template #default="scope">
                    <el-text>
                        {{ scope.row.order_remark ? scope.row.order_remark : "--" }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="物流单号">
                <template #default="scope">
                    {{ scope.row.order_track_num ? scope.row.order_track_num : "--" }}
                </template>
            </el-table-column>
            <el-table-column label="订单状态">
                <template #default="scope">
                    <el-text v-if="scope.row.order_status == 0">待支付</el-text>
                    <el-text v-else-if="scope.row.order_status == 1">待发货</el-text>
                    <el-text v-else-if="scope.row.order_status == -1">支付失败</el-text>
                    <el-text v-else-if="scope.row.order_status == 2">待收货</el-text>
                    <el-text v-else-if="scope.row.order_status == -2">取消订单</el-text>
                    <el-text v-else-if="scope.row.order_status == 3">已收货</el-text>
                    <el-text v-else="scope.row.order_status==4">评价完成</el-text>
                </template>
            </el-table-column>
            <el-table-column label="商品评价">
                <template #default="scope">
                    <el-text v-if="goodsReviewMap[scope.row.order_id] !== undefined">
                        {{ goodsReviewMap[scope.row.order_id][0].goods_review_content
                            ? goodsReviewMap[scope.row.order_id][0].goods_review_content : "--"
                        }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="支付方式">
                <template #default="scope">
                    <el-text v-if="paymentMap[scope.row.payment_id] !== undefined">
                        {{ paymentMap[scope.row.payment_id] ? paymentMap[scope.row.payment_id] : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="deliverBtn(scope.row)"
                        v-if="scope.row.order_status == 1" style="margin-left: 3px;">
                        发货
                    </el-button>
                    <el-button link type="primary" size="small" @click="checkBtn(scope.row)"
                        style="margin-left: 3px;">查看明细</el-button>
                    <el-button link type="primary" size="small" @click="remarkBtn(scope.row)"
                        style="margin-left: 3px;">备注</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 富文本编辑框预览 -->
        <el-dialog title="内容" v-model="fromVisible" width="80%" style="max-height: 80%;overflow: auto;" append-to-body>
            <div>
                <div v-html="dialogcontent"></div>
            </div>
        </el-dialog>


        <!-- 分页
            page-sizes 每页个数
            -->
        <div class="page">
            <el-pagination :page-sizes="[5, 10, 15, 20]" background :page-size="searchParam.pageSize"
                :current-page="searchParam.curPage" layout="->,total, sizes, prev, pager, next, jumper"
                :total="searchParam.total" @size-change="pageSizeChange" @current-change="currentPageChange" />
        </div>
        <!-- 操作 -->
    </div>


    <!-- 抽屉 -->
    <el-drawer v-model="table" title="订单明细" direction="btt" size="80%" append-to-bodyappend-to-body>
        <el-table ref="DrawerList" :data="DrawerData" stripe
            :header-cell-style="{ background: ' #493131b0', color: 'white', 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange"
            :show-overflow-tooltip="true">
            <!-- <el-table-column type="selection" width="55" /> -->
            <el-table-column label="商品名称">
                <template #default="scope">
                    <el-text v-if="goodsMap[scope.row.goods_id] !== undefined">
                        {{ goodsMap[scope.row.goods_id][0].goods_name ? goodsMap[scope.row.goods_id][0].goods_name :
                            "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column prop="goods_id" label="商品图片">
                <template #default="scope">
                    <el-image style="width: 50px; height: 50px"
                        :src="srcMap[goodsMap[scope.row.goods_id][0].goods_id][0]" :zoom-rate="1.2" :max-scale="7"
                        :min-scale="0.2"
                        v-if="goodsMap[scope.row.goods_id] !== undefined && srcMap[goodsMap[scope.row.goods_id][0].goods_id] !== undefined"
                        :preview-src-list="srcMap[goodsMap[scope.row.goods_id][0].goods_id]" show-progress
                        :initial-index="4" fit="cover" lazy />
                </template>
            </el-table-column>
            <el-table-column label="单价">
                <template #default="scope">
                    <el-text v-if="goodsMap[scope.row.goods_id] !== undefined">
                        {{ goodsMap[scope.row.goods_id][0].goods_price ?
                            goodsMap[scope.row.goods_id][0].goods_price :
                            "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="数量">
                <template #default="scope">
                    <el-text>
                        {{ scope.row.goods_count ? scope.row.goods_count : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="支付总价">
                <template #default="scope">
                    <el-text>
                        {{ scope.row.order_price ? scope.row.order_price : "--" }}
                    </el-text>
                </template>
            </el-table-column>
        </el-table>
    </el-drawer>


    <!-- <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
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
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="所属用户" prop="usr_id">
                            <SelectChecked :options="options" @change="selected" ref="selectRef"
                                v-model="addModel.usr_id"></SelectChecked>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="头像" prop="usr_avater">
                            <uploadAvater size="120px" :limit="1" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="是否推荐" prop="cul_recommend">
                            <el-switch v-model="addModel.cul_recommend"
                                @change="recommendChange(addModel.cul_recommend)" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="生日" prop="usr_bday">
                            <el-date-picker v-model="addModel.usr_bday" type="date" placeholder="选择日期" clearable />
                        </el-form-item>
                    </el-col>
                    <el-row>
                        <el-col :span="24" :offset="0">
                            <el-form-item label="文化内容" prop="cul_content">
                                <RichTextEditor @update="update" :content="addModel.cul_content">
                                </RichTextEditor>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-row>
            </el-form>
        </template>
    </UsrDialog> -->
    <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                <el-row v-if="tags == '1'">
                    <el-col :span="24" :offset="0">
                        <el-form-item label="快递单号" prop="order_track_num">
                            <el-input v-model="addModel.order_track_num" clearable />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="tags == '2'">
                    <el-col :span="24" :offset="0">
                        <el-form-item label="备注" prop="order_remark">
                            <el-input v-model="addModel.order_remark" style="width: 300px" :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea" placeholder="输入评价" clearable/>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </template>
    </UsrDialog>
</template>

<script setup lang="ts">
import { Delete, Search, EditPen, Refresh } from '@element-plus/icons-vue'
import UsrDialog from "@/components/UsrDialog.vue"
import { nextTick, onMounted, reactive, ref, watch, computed } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import {
    deliverApi, getListApi, remarkApi, deleteApi, getSelectPaymentApi,
    allDeleteApi, getAccountApi, getAddrApi, uploadApi, getPaymentApi, getGoodsApi, getImagesApi, getGoodsReviewApi
} from '@/api/backend/Goods/Order/index'

import { regionData, CodeToText } from 'element-china-area-data'
import SelectChecked from "@/components/SelectChecked.vue"
import uploadAvater from "@/components/uploadAvater.vue"
import RichTextEditor from '@/components/RichTextEditor.vue'
import DetailDrawer from '@/components/DetailDrawer.vue';
// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

const goodsReviewMap: any = ref({});
const getGoodsReview = async (id: string) => {
    let res = await getGoodsReviewApi(id);
    if (res.code == 200) {
        console.log(res.data)
        if (res.data.length > 0) {
            goodsReviewMap.value[id] = res.data;
            return goodsReviewMap.value[id]
        }
    }

}

const goodsMap: any = ref({});
const getGoods = async (id: string) => {
    let res = await getGoodsApi(id);
    if (res.code == 200) {
        goodsMap.value[id] = res.data
        console.log(goodsMap.value[id])
        await getImages(goodsMap.value[id][0].goods_id);

        return goodsMap.value[id]
    }

}
//上传图片墙
const srcList: any = ref(['']);
const srcMap: any = ref({});
// 表格 照片墙
const getImages = async (id: string) => {
    const res = await getImagesApi(id);
    srcList.value = res.data;
    srcMap.value[id] = srcList.value;
    console.log(srcMap.value[id]);

    return srcMap.value[id];

}


//抽屉
const table = ref(false)


const statusoptions = ref([
    { value: 0, label: '待支付' },
    { value: 1, label: '待发货' },
    { value: -1, label: '支付失败' },
    { value: 2, label: '待收货' },
    { value: -2, label: '取消订单' },
    { value: 3, label: '已收货' },
    { value: 4, label: '评价完成 ' },
])


//表单 更新富文本编辑器内容
const update = (content: string) => {
    addModel.cul_content = content
    // console.log(addModel.cul_content)
};

//设置显示内容弹窗默认值
const fromVisible = ref(false)

//设置弹窗显示内容
const dialogcontent = ref("")

//表格 显示富文本编辑框
const previewContent = (content: string) => {
    dialogcontent.value = content;
    fromVisible.value = true;
    // console.log(fromVisible.value)

}



//判断是新增还是编辑，0新增 1编辑
const tags = ref("")

const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()



//下拉数据
const paymentoptions = ref([])

//下拉选择 勾选的数据
const selected = (value: string | number) => {
    // addModel.usr_id = value;
    console.log(value);
}

//下拉数据 后台获取数据
const getPaymentSelect = async () => {
    let res = await getSelectPaymentApi();
    if (res.code == 200) {
        paymentoptions.value = res.data
        // console.log("后台数据："+res.data)
    }

}




//搜索 绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    // usr_account: '',
    // usr_name: '',
    addr_name: '',
    addr_phone: '',
    addr_province: '',
    addr_city: '',
    addr_country: '',
    addr_detail: '',
    usr_id: '',
    payment_id: '',
    order_number: '',
    order_time: '',
    order_remark: '',
    order_track_num: '',
    order_status: '',



})


//搜索 日期选择
const defaultTime = ref<[Date, Date]>([
    new Date(2000, 1, 1, 0, 0, 0),
    new Date(2000, 2, 1, 23, 59, 59),
])

//搜索 绑定日期
const searchData = reactive({
    order_time: [''],
})

//搜索 获取选择的日期
const getserchData = (value: Array<string>) => {
    searchParam.order_time = value.join(',');
    // console.log(searchParam.usr_bday);
}

//搜索按钮
const searchBtn = () => {
    getList();
}

//重置按钮
const resetBtn = () => {
    searchParam.curPage = 1;
    searchParam.addr_phone = '';
    searchParam.addr_name = '',
        searchParam.addr_detail = '',
        searchParam.addr_province = '',
        searchParam.addr_city = '',
        searchParam.addr_country = '',
        selectedProvinceCode.value = '';
    selectedCityCode.value = '';
    selectedDistrictCode.value = '';
    searchParam.usr_id = '',
        searchParam.payment_id = '',
        searchParam.order_number = '',
        searchParam.order_time = '',
        searchParam.order_remark = '',
        searchParam.order_track_num = '',
        searchParam.order_status = '',
        searchData.order_time = [''],
        getList();
}





//表格数据
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])

// 表格 用于存储用户账户信息的映射
const accountMap = ref<Record<string, string>>({});

// 表格 获取单个用户账户信息
const getAccount = async (usr_id: string) => {
    if (!accountMap.value[usr_id]) {
        const res = await getAccountApi(usr_id);
        accountMap.value[usr_id] = res.data;
        console.log('获取用户账户信息成功:', res.data);
    }
    return accountMap.value[usr_id];
}

//表格 用于存储地址信息的映射
const addressMap = ref<Record<string, string>>({});

// 表格 后端获取的code变为text
const getAddress = async (id: string) => {
    if (!addressMap.value[id]) {
        const res = await getAddrApi(id);
        addressMap.value[id] = res.data;
        console.log("地址信息:", addressMap.value[id]);
    }
    return addressMap.value[id];
}
const tableAddr = (id: string) => {
    // console.log(id[0].addr_province)

    return CodeToText[id[0].addr_province] +
        CodeToText[id[0].addr_city] +
        CodeToText[id[0].addr_country] +
        id[0].addr_detail;



}

const paymentMap = ref<Record<string, string>>({});


const getPayment = async (usr_id: string) => {
    if (!paymentMap.value[usr_id]) {
        const res = await getPaymentApi(usr_id);
        paymentMap.value[usr_id] = res.data;
        console.log('获取支付方式信息成功:', res.data);
    }
    return paymentMap.value[usr_id];
}


//表格 获取省份列表数据  
const selectedOptions: any = ref([])

//表格 更改省市县联动
const handleChange = () => {
    addModel.addr_province = selectedOptions.value[0];
    addModel.addr_city = selectedOptions.value[1];
    addModel.addr_country = selectedOptions.value[2];
    console.log(selectedOptions.value)
}




//表格 多选框
const multipleSelection: any = ref<[]>([])

//表格 选择项变化
const handleSelectionChange = (val: any) => {
    multipleSelection.value = val
    console.log(multipleSelection.value)
}



//发货
const deliverBtn = (row: any) => {
    console.log(row);
    tags.value = "1";
    //显示弹窗
    dialog.visible = true;
    dialog.title = '发货';
    dialog.height = 300;
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);

    })

    //清空表单
    addRef.value?.resetFields();
}

//发货
const remarkBtn = (row: any) => {
    console.log(row);
    tags.value = "2";
    //显示弹窗
    dialog.visible = true;
    dialog.title = '备注';
    dialog.height = 300;
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);

    })

    //清空表单
    addRef.value?.resetFields();
}

const DrawerList = ref<InstanceType<any>>()
const DrawerData = ref<any>([])
//查看
const checkBtn = (id: any) => {

    table.value = true;
    let arr = [tableMap.value[id.order_id]]; //对象转化为数组
    console.log(arr)
    DrawerData.value = arr;

    // searchParam.total = res.data.total
}

//查看明细

//表格高度
const tableHeight = ref(0)
const tableMap = ref<Record<string, string>>({});
//表格 获取数据
const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        tableData.value = res.data.records;

        //设置总页数
        searchParam.total = res.data.total
    }
    console.log("getList:", res.data.records)
}

//批量删除按钮
const allDeleteBtn = async () => {
    if (multipleSelection.value.length === 0) return
    console.log(multipleSelection.value)
    ElMessageBox.confirm(
        `确定要删除选中的 ${multipleSelection.value.length} 项吗?`,
        '系统警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {//点击确定
            const ids = multipleSelection.value.map((item: { usr_id: any; }) => item.usr_id);
            console.log(ids);
            let res = await allDeleteApi(ids);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                tableList.value?.clearSelection() // 清除选择
                getList()
            }
        })
        .catch((error) => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
            console.error(error);
        })
}



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    order_id: '',
    order_status: '',
    order_number: '',
    order_remark: '',
    order_appraise: '',
    goods_id: '',
    order_price: '',
    order_time: '',

    addr_id: '',
    addr_name: '',
    addr_phone: '',
    addr_province: 1,
    addr_city: 1,
    addr_country: 1,
    addr_detail: '',
    usr_id: '',
    usr_avater: '',
    is_admin: '',
    cul_content: '',
    order_track_num: '',
})
//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;

            if (tags.value == "1") {
                res = await deliverApi(addModel)
                console.log(res)
            } else {
                //修改
                res = await remarkApi(addModel)
                console.log(res)
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

//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();

}


//图片验证
const validList = (rule: any, value: any, callback: any) => {
    console.log(MyPicUploadRef.value?.fileList.length);
    if (MyPicUploadRef.value?.fileList.length === 0) {
        callback(new Error('请选择图片'));
    } else {
        callback();
    }
};


//省份自定义验证规则
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
    order_track_num: [
        { required: true, trigger: 'change', message: '请输入快递单号' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],

})






// 绑定省市区选择的数据
const selectedProvinceCode = ref('');
const selectedCityCode = ref('');
const selectedDistrictCode = ref('');

// 原始省份数据
const provinces = computed(() => regionData.map(province => ({
    value: province.value,
    label: province.label,
})));
// 根据选中的省份过滤城市
const filteredCities = computed(() => {
    if (!selectedProvinceCode.value) return [{ value: '', label: '请先选择省份' }];

    // 当选择了省份时，返回对应省份下的所有市
    return regionData
        .find(province => province.value === selectedProvinceCode.value)!
        .children.map(city => ({
            value: city.value,
            label: city.label,
        }));
});

// 根据选中的城市过滤区
const filteredDistricts = computed(() => {
    if (!selectedCityCode.value) return [{ value: '', label: '请先选择省份和城市' }];
    // 当选择了城市时，返回对应城市下的所有区县
    return regionData
        .find(province => province.value === selectedProvinceCode.value)!
        .children.find(city => city.value === selectedCityCode.value)!
        .children.map(district => ({
            value: district.value,
            label: district.label,
        }));
});

// 处理省份选择变化
const handleProvinceChange = (value: string) => {
    selectedCityCode.value = '';
    selectedDistrictCode.value = '';
    const province = regionData.find(p => p.value === value);
    if (province) {
        searchParam.addr_province = province.value;
    }
};
// 处理城市选择变化
const handleCityChange = (value: string) => {
    selectedDistrictCode.value = '';
    const city = regionData
        .find(province => province.value === selectedProvinceCode.value)!
        .children.find(c => c.value === value);
    if (city) {
        searchParam.addr_city = city.value;
    }

};

// 处理区选择变化
const handleDistrictChange = (value: string) => {
    const district = regionData
        .find(province => province.value === selectedProvinceCode.value)!
        .children.find(city => city.value === selectedCityCode.value)!
        .children.find(d => d.value === value);

    if (district) {
        searchParam.addr_country = district.value;
    }
};


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


//预处理
onMounted(() => {
    nextTick(() => {
        tableHeight.value = window.outerHeight - 620;
    })
    getList()

    getPaymentSelect()
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                tableMap.value[row.order_id] = row; // 初始化映射关系

                // console.log("row",row);
                await getAccount(row.usr_id);
                await getAddress(row.addr_id);
                // await getAddressText(row.addr_city);
                // await getAddressText(row.addr_country);
                await getPayment(row.payment_id);
                await getGoods(row.goods_id);
                await getGoodsReview(row.order_id);


            }
        }
    }, { immediate: true })
})

</script>

<style lang="scss" scoped>
//修改分页器激活颜色
// :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
//     background-color: #815908; //修改默认的背景色
// }

//解决层级穿透
::v-deep(.el-table__cell) {
    position: static !important; // 解决el-image 和 el-table冲突层级冲突问题
}

//解决预览过大
::v-deep(.el-image-viewer__img) {
    width: 50% !important;
    height: auto !important;
}
</style>