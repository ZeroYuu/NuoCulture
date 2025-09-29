<template>
    <div class="contain">

        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <el-button @click="batchDelete" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
            <el-button @click="addBtn" color="#493131b0" class="add" size="large">添加地址</el-button>
        </div>

        <el-empty description="暂时没有数据" v-if="usrAddrList.length === 0" style="width: 70%;" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards initial-visible" v-for="item in usrAddrList">
                <div class="select ">
                    <el-checkbox-group v-model="selectedItems">
                        <el-checkbox :value="item.addr_id" :key="item.addr_id"
                            @change="handleSelectChange(item.addr_id, $event)"><br>
                        </el-checkbox>
                    </el-checkbox-group>
                </div>

                <div class="card">
                    <div style="display: flex;justify-content: space-around;align-items: center;width: 100%;">

                        <div class="content">

                            <div class="allprice">
                                <el-text style="font-size: 20px">
                                    {{ item.addr_name }}
                                </el-text>
                            </div>
                            <div class="allprice">
                                <el-text style="font-size: 20px">
                                    {{ item.addr_phone }}
                                </el-text>
                            </div>
                            <div class="title">
                                <el-text style="font-size: 20px">
                                    {{ CodeToText[item.addr_province] }}
                                </el-text>

                            </div>

                            <div class="price">
                                <el-text style="font-size: 20px">
                                    {{ CodeToText[item.addr_city] }}
                                </el-text>
                            </div>

                            <div class="number">
                                <el-text style="font-size: 20px">
                                    {{ CodeToText[item.addr_country] }}
                                </el-text>
                            </div>

                            <div class="remark">
                                <el-text style="font-size: 20px">
                                    {{ item.addr_detail }}
                                </el-text>
                            </div>



                            <div class="delete">

                                <el-button link @click="editBtn(item)" color="#493131" plain>编辑</el-button>

                                <el-button link @click="deleteBtn(item.addr_id)" color="#493131" plain>删除</el-button>

                            </div>

                        </div>
                    </div>
                </div>

            </div>

        </el-skeleton>

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
import { onMounted, onUnmounted, ref, reactive, nextTick } from 'vue';

// import { allDeleteApi, deleteApi, getDetailApi, getCartDetailApi, selectTypeListApi } from '@/api/frontend/goods/index';
import { getUsrAddrApi, editAddrApi,addAddrApi,delAddrApi,alldelAddrApi } from '@/api/frontend/usrinfo/index';

import UsrDialog from "@/components/UsrDialog.vue"
import useDialog from '@/hooks/useDialog';
import { regionData, CodeToText } from 'element-china-area-data'
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
    selectedOptions.value = [];

}
const tags = ref("")
//编辑
const editBtn = (row: any) => {
    console.log(row);
    //显示弹窗
    tags.value = "1";
    dialog.visible = true;
    dialog.height = 300;
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
            let res = await delAddrApi(id);
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
            let res = await alldelAddrApi(ids);
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
        selectedItems.value = usrAddrList.value.map((item: { addr_id: any; }) => item.addr_id);
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
    selectAll.value = selectedItems.value.length === usrAddrList.value.length;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < usrAddrList.value.length;
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
const usrAddrList: any = ref({});
// const usrOrderMap: any = ref({});
const selectedItems = ref<any[]>([]);
const selectAll = ref(false);

const getList = async () => {
    //发送请求
    let res = await getUsrAddrApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        usrAddrList.value = res.data;
        // usrAddrList.addr_province=CodeToText[usrAddrList.addr_province]
        console.log("usrAddrList", usrAddrList.value);
        searchParam.total = res.data.total;

        // console.log("usrOrderMap", usrOrderMap.value);
        loading.value = false;

    }
}

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
.add{
    position: absolute;
    /* top: -5px; */
    /* left:120px; */
    /* top: 25px; */
    right: 10px;
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
        /* justify-content: space-between; */
        /* margin: 0 20px; */
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

.select {
    /* opacity: 1;
    transform: none; */
    display: flex;
    align-items: center;
    --el-border: #1d4350 solid 1px;
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