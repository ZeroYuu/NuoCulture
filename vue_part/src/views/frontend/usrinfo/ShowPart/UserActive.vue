<template>
    <div class="contain">
        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <el-button @click="batchDelete" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
        </div>
        <el-empty description="暂时没有数据" v-if="showEmpty" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards initial-visible" v-for="item in usrActvList">
                <div class="select ">
                    <el-checkbox-group v-model="selectedItems">
                        <el-checkbox :value="item.join_id" :key="item.join_id"
                            @change="handleSelectChange(item.join_id, $event)" :disabled="item.join_status == 0"><br>
                        </el-checkbox>
                    </el-checkbox-group>
                </div>
                <div class="card" @click="navigateToDetail(item.actv_id)">
                    <el-image :src="item.actv_img_url" alt="" fit="cover" />
                    <div class="content">
                        <div class="top">
                            <el-text class="title">{{ item.actv_name }}</el-text>
                            <!-- <el-text class="time">{{ item.start_time }}&nbsp;至&nbsp;{{ item.end_time }}</el-text> -->

                        </div>
                        <div class="text-clamp" v-ignore-images="item.actv_content"></div>
                        <el-text class="pub">负责人:{{ item.actv_leader }}</el-text>
                    </div>
                </div>


                <div class="right">
                    <div style="text-align: left;width: 100%;font-size: 20px;margin: 10px 0;">
                        <el-text style="font-size: 20px;">审核状态:</el-text>

                        <el-text v-if="item.join_status == 0" style="font-size: 20px;">审核中</el-text>
                        <el-text v-if="item.join_status == 1" style="font-size: 20px;">审核成功</el-text>
                        <el-text v-if="item.join_status == -1" style="font-size: 20px;">审核失败</el-text>
                        <el-text v-if="item.join_status == 2" style="font-size: 20px;">取消报名</el-text>
                    </div>

                    <div style="text-align: left;width: 100%;font-size: 20px;margin: 10px 0;">
                        <el-button link style="font-size: 20px;" @click="datail(item)" color="#493131"
                            plain>查看详情</el-button>
                        <el-button link style="font-size: 20px;" @click="cancelBtn(item.join_id)" color="#493131"
                            v-if="item.join_status == 1 || item.join_status == 0" plain>取消报名</el-button>
                        <el-button link style="font-size: 20px;" @click="ResignBtn(item)" color="#493131"
                            v-if="item.join_status == -1 || item.join_status == 2" plain>重新报名</el-button>
                        <el-button link style="font-size: 20px;" @click="deleteBtn(item.join_id)" color="#493131"
                            v-if="item.join_status != 0" plain>删除</el-button>

                    </div>

                </div>

            </div>

        </el-skeleton>
        <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
            @onClose="onClose" @onConfirm="commit" :isReadOnly="tags === '0'">
            <template v-slot:content>
                <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                    <el-row v-if="tags === '1'">
                        <el-col :span="24" :offset="0">
                            <el-form-item label="申请原因" prop="join_reason">
                                <el-input v-model="addModel.join_reason" clearable style="width: 700px" autosize
                                    type="textarea" />
                            </el-form-item>
                        </el-col>
                    </el-row>

                </el-form>
            </template>
        </UsrDialog>


        <el-dialog title="查看详情" width="80%" style="max-height: 80%;overflow: auto;" append-to-body v-model="isvisible">
            <el-row v-if="tags === '0'">
                <el-col :span="24" :offset="0" style="margin: 10px 0;">
                    <el-text style="font-size: 20px">
                        参加原因：{{ addModel.join_reason }}
                    </el-text>
                </el-col>
                <el-col :span="24" :offset="0" style="margin: 10px 0;">
                    <el-text style="font-size: 20px">
                        审核备注：{{ addModel.join_remark }}
                    </el-text>
                </el-col>
            </el-row>
        </el-dialog>

    </div>

</template>

<script setup lang="ts">
import { userStore } from '@/store/user/index'
const user = userStore()

import { getUsrActvApi } from '@/api/frontend/usrinfo/index';
import { nextTick, onMounted, reactive, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus';
import { addApi, deleteApi, allDeleteApi, cancelApi } from '@/api/frontend/active/index';
const router = useRouter();
import useDialog from '@/hooks/useDialog';
import UsrDialog from "@/components/UsrDialog.vue"
import type { id } from 'element-plus/es/locales.mjs';

const tags = ref("")
const isvisible = ref(false)
const selectAll = ref(false);
const isIndeterminate = ref(false)
const selectedItems = ref<any[]>([]);
// 全选/取消全选
const handleSelectAll = (val: boolean) => {
    if (val) {
        // 过滤出所有未被禁用的项目，并映射它们的join_id到selectedItems
        selectedItems.value = usrActvList.value
            .filter((item: { join_status: number; }) => item.join_status !== 0) // 假设join_status为0表示复选框被禁用
            .map((item: { join_id: any; }) => item.join_id);
        isIndeterminate.value = false; // 如果全选，则不是半选状态
    } else {
        // 取消全选，清空selectedItems
        selectedItems.value = [];
        isIndeterminate.value = false; // 取消全选后，也不是半选状态
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
    selectAll.value = selectedItems.value.length === usrActvList.value.length;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < usrActvList.value.length;
};


// 批量删除
const batchDelete = () => {
    console.log(selectedItems.value)
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
            let res = await allDeleteApi(ids);
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
            let res = await deleteApi(id);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                getList()
            }
        })
        .catch(() => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
        })
}
const cancelBtn = (id: string) => {
    console.log(id)
    ElMessageBox.confirm(
        '是否确定取消?',
        '系统警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {//点击确定
            let res = await cancelApi(id);
            if (res.code == 200) {
                ElMessage.success(res.msg)
                getList()
            }
        })
        .catch(() => {//点击取消
            ElMessage({
                type: 'info',
                message: '已取消该操作',
            })
        })
}


const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    // cul_id:'',
})

const showEmpty = ref(false)
const loading = ref(true)

const usrActvList: any = ref([]);
const getList = async () => {
    //发送请求
    let res = await getUsrActvApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        usrActvList.value = res.data;
        console.log(usrActvList.value);
        searchParam.total = res.data.total;
        loading.value = false;

    }
}




watch(usrActvList, () => {
    showEmpty.value = usrActvList.value.length === 0;
    console.log(showEmpty.value);
});

onMounted(() => {
    getList();

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



const navigateToDetail = (id: string) => {
    console.log(id);
    router.push({ name: 'frontactivedetial', params: { id } });
};

const { dialog } = useDialog()
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    join_id: '',
    usr_id: '',
    actv_id: '',
    join_reason: '',
    join_status: '0',
    join_remark: '',

})
//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();

}

// 更新previewdetail方法
const datail = (item: any) => {
    tags.value = "0";
    isvisible.value = true;
    //回显数据
    nextTick(async () => {
        //复制数据
        Object.assign(addModel, item);


    })
};



//添加按钮
const ResignBtn = async (item: any) => {

    // 发送请求
    tags.value = "1";
    dialog.visible = true;
    dialog.title = '活动申请';
    dialog.height = 150;
    addModel.actv_id = item.actv_id;
    addModel.usr_id = user.usr_id;

    //回显数据
    nextTick(async () => {
        //复制数据
        Object.assign(addModel, item);
        addModel.join_id = '';
        addModel.join_status = '0';

    })

    //清空表单
    addRef.value?.resetFields();
}

const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid: any) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;


            res = await addApi(addModel)

            if (res.code == 200) {
                // console.log(res)
                ElMessage.success(res.msg)
                //刷新列表
                location.reload()


                //关闭弹窗
                onClose()
            }
        } else {
            console.log('表单验证不通过')
        }
    })
}
const rules = reactive({
    // address: [
    //     { required: true, validator: validCascader, trigger: 'change', message: '请输入地址' }
    //     // { min: 3, max: 5, message: '', trigger: 'blur' },
    // ],
    join_reason: [
        { required: true, message: '请输入原因', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],

})


</script>
<style scoped>
.right {
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
    /* height: 120px; */
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px 20px;


    width: 30%;
    margin-left: 50px;
    display: flex;
    flex-direction: column;

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
        padding: 10px 0 !important;
        border-radius: 4px;
        --el-border: #1d4350 solid 1px;
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

.select {
    /* opacity: 1;
    transform: none; */
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
    justify-content: center;
    align-items: center;
    padding: 10px;
    cursor: pointer;

    .el-image {
        width: 30%;
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