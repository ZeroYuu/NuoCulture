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
                    <el-col :span="8" :offset="0">
                        <el-form-item label="活动名称">
                            <SelectChecked :options="options" @change="selected" ref="selectRef"
                                v-model="searchParam.actv_id" filterable />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
    </div>

    <!-- 表格数据 -->
    <div class="table">
        <div class="taBtn">
            <el-button color="#493131e6" :icon="EditPen" plain @click="addBtn">新增</el-button>
            <el-button type="danger" :icon="Delete" plain @click="allDeleteBtn">批量删除</el-button>
        </div>
        <el-table ref="tableList" :data="tableData" stripe
            :header-cell-style="{ background: ' #493131e6', color: 'white', 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange"
            :show-overflow-tooltip="true">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="actv_name" label="活动名称">
                <template #default="scope">
                    <el-text v-if="actvMap[scope.row.actv_id] !== undefined">
                        {{ actvMap[scope.row.actv_id][0].actv_name || '加载中...' }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="主图">
                <template #default="scope">
                    <el-image style="width: 50px; height: 50px" :src="srcMap[actvMap[scope.row.actv_id][0].actv_id][0]" :zoom-rate="1.2"
                        :max-scale="7" :min-scale="0.2"  v-if="actvMap[scope.row.actv_id] !== undefined && srcMap[actvMap[scope.row.actv_id][0].actv_id] !== undefined"
                        :preview-src-list="srcMap[actvMap[scope.row.actv_id][0].actv_id]" show-progress :initial-index="4" fit="cover"
                        lazy />
                </template>
            </el-table-column>
            <el-table-column label="活动地址">
                <template #default="scope">
                    <el-text v-if="actvMap[scope.row.actv_id] !== undefined">
                        {{ addressMap[actvMap[scope.row.actv_id][0].actv_region] ? addressMap[actvMap[scope.row.actv_id][0].actv_region] : "--" }}
                    </el-text>

                </template>
            </el-table-column>
            <el-table-column label="开始时间">
                <template #default="scope">
                    <el-text v-if="actvMap[scope.row.actv_id] !== undefined">
                        {{ actvMap[scope.row.actv_id][0].start_time ? actvMap[scope.row.actv_id][0].start_time : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="结束时间">
                <template #default="scope">
                    <el-text v-if="actvMap[scope.row.actv_id] !== undefined">
                        {{ actvMap[scope.row.actv_id][0].end_time ? actvMap[scope.row.actv_id][0].end_time : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="精彩瞬间">
                <template #default="scope">
                    <el-button @click="previewContent(scope.row.mom_content)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.mom_id)">删除</el-button>
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

    <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="活动名称" prop="actv_id">
                            <SelectChecked :options="options" @change="selected" ref="selectRef"
                                v-model="addModel.actv_id" style="width: 220px;"></SelectChecked>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24" :offset="0">
                        <el-form-item label="精彩瞬间" prop="mom_content">
                            <RichTextEditor @update="update" :content="addModel.mom_content">
                            </RichTextEditor>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </template>
    </UsrDialog>
</template>

<script setup lang="ts">
import { Delete, Search, EditPen, Refresh, SortUp } from '@element-plus/icons-vue'
import UsrDialog from "@/components/UsrDialog.vue"
import { nextTick, onMounted, reactive, ref, watch, computed } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import { addApi, getListApi, editApi, deleteApi, getAddTextApi, allDeleteApi, getImagesApi, getActvApi, getSelectApi } from '@/api/backend/Active/Moment/index'
import SelectChecked from "@/components/SelectChecked.vue"
import RichTextEditor from '@/components/RichTextEditor.vue'
// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

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


//表单 更新富文本编辑器内容
const update = (content: string) => {
    addModel.mom_content = content
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



//下拉数据
const options = ref([])

//下拉选择 勾选的数据
const selected = (value: string | number) => {
    // addModel.usr_id = value;
    console.log(value);
}

//下拉数据 后台获取数据
const getSelect = async () => {
    let res = await getSelectApi();
    if (res.code == 200) {
        options.value = res.data
        // console.log("后台数据："+options.value)
    }

}


//搜索 绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    actv_id: '',

})


//搜索按钮
const searchBtn = () => {
    getList();
}

//重置按钮
const resetBtn = () => {
    searchParam.curPage = 1;
    searchParam.actv_id = '';
    getList();
}





//表格数据
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])
// const tableList: any = ref([])
// const tableData: any = ref([])


//表格 用于存储地址信息的映射
const addressMap: any = ref({});

let area = "";
// 表格 后端获取的code变为text
const getAddressText = async (region: string, detail: string) => {
    area = ""
    const codeArray = region.split(",");
    area +=
        codeArray[0] +
        "/" +
        codeArray[1] +
        "/" +
        codeArray[2] + "/" + detail;

    addressMap.value[region] = area;
    return addressMap.value[region];
}

const actvMap: any = ref({});
const getActv = async (id: string) => {
    let res = await getActvApi(id);
    if (res.code == 200) {
        actvMap.value[id] = res.data
        console.log(actvMap.value[id])
        await getAddressText(actvMap.value[id][0].actv_region, actvMap.value[id][0].actv_addr);
        await getImages(actvMap.value[id][0].actv_id)

        return actvMap.value[id]
    }

}


//表格 多选框
const multipleSelection: any = ref<[]>([])

//表格 选择项变化
const handleSelectionChange = (val: any) => {
    multipleSelection.value = val
    console.log(multipleSelection.value)
}

//表格 编辑
const editBtn = (row: any) => {
    console.log(row);
    tags.value = "1";
    //显示弹窗
    dialog.visible = true;
    dialog.title = '编辑';
    dialog.height = 500;
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);
    })
    //清空表单
    addRef.value?.resetFields();
}

//表格 删除
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

//表格高度
const tableHeight = ref(0)

//表格 获取数据
const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        tableData.value = res.data.records
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
            const ids = multipleSelection.value.map((item: { mom_id: any; }) => item.mom_id);
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
//添加按钮
const addBtn = () => {
    addModel.mom_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '增加';
    dialog.height = 500;
    //清空表单
    addRef.value?.resetFields();
}



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    mom_id: '',
    actv_id: '',
    mom_content: '',
})
//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;

            if (tags.value == "0") {
                //添加

                res = await addApi(addModel)

                // res = await addApi(addModel)
            } else {
                //修改
                res = await editApi(addModel)
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



//表单验证
const rules = reactive({
    mom_content: [
        { required: true, trigger: 'change', message: '请输入地址' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_id: [
        { required: true, message: '请输入地址', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],

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


//预处理
onMounted(() => {
    nextTick(() => {
        tableHeight.value = window.outerHeight - 620;
    })
    getList()
    getSelect()
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                await getActv(row.actv_id)

                // console.log("watch");

            }
        }
    }, { immediate: true });
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
.el-form{
    width: 100%;
}
.el-input{
    width: 220px;
}
</style>