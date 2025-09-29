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
                        <el-form-item label="文化类型">
                            <SelectChecked :options="optionsType" @change="selected" ref="selectRef"
                                v-model="searchParam.cul_type_id" filterable style="width: 220px;"></SelectChecked>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="标题">
                            <el-input v-model="searchParam.cul_title" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="发布者">
                            <el-input v-model="searchParam.cul_pub" clearable />
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
            :header-cell-style="{ background: '#493131e6', color: 'white', 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="cul_title" label="标题" :show-overflow-tooltip="true" />
            <el-table-column prop="cul_type_id" label="文化类型">
                <template #default="scope">
                    <el-text>{{ typeNameMap[scope.row.cul_type_id] || '加载中...' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="封面">
                <template #default="scope">
                    <el-image :src="scope.row.cul_cover" :preview-src-list="[scope.row.cul_cover]"
                        style="width:50px; height:50px;" :zoom-rate="1.2" :max-scale="5" :min-scale="0.2" />
                </template>
            </el-table-column>
            <el-table-column prop="cul_pub" label="发布者" />
            <el-table-column label="上架状态">
                <template #default="scope">
                    {{ scope.row.cul_shelves ? "上架" : "下架" }}
                </template>
            </el-table-column>
            <el-table-column label="是否推荐">
                <template #default="scope">
                    {{ scope.row.cul_recommend ? "是" : "否" }}
                </template>
            </el-table-column>
            <el-table-column prop="cul_content" label="文化内容">
                <template #default="scope">
                    <el-button @click="previewContent(scope.row.cul_content)">查看内容</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="180" width="180" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.cul_id)">删除</el-button>
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
                    <el-col :span="12" :offset="0">
                        <el-form-item label="标题" prop="cul_title">
                            <el-input v-model="addModel.cul_title" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="文化类型" prop="cul_type_id">
                            <SelectChecked :options="optionsType" @change="selected" ref="selectRef"
                                v-model="addModel.cul_type_id" style="width: 220px;"></SelectChecked>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="发布者" prop="cul_pub">
                            <el-input v-model="addModel.cul_pub" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="封面" prop="cul_cover">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="1" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="上架状态" prop="cul_shelves">
                            <el-switch v-model="addModel.cul_shelves" @change="shelvesChange(addModel.cul_shelves)" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="是否推荐" prop="cul_recommend">
                            <el-switch v-model="addModel.cul_recommend"
                                @change="recommendChange(addModel.cul_recommend)" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24" :offset="0">
                        <el-form-item label="文化内容" prop="cul_content">
                            <RichTextEditor @update="update" :content="addModel.cul_content">
                            </RichTextEditor>
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
import { nextTick, onMounted, reactive, ref, watch } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import { addApi, getListApi, editApi, deleteApi, getSelectTitleApi, getSelectTypeApi, allDeleteApi, getTypeNameApi, uploadApi } from '@/api/backend/Culture/Message/index'

import SelectChecked from "@/components/SelectChecked.vue"
import uploadAvater from "@/components/uploadAvater.vue"
import RichTextEditor from '@/components/RichTextEditor.vue'

// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

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
    console.log(fromVisible.value)

}


//判断是新增还是编辑，0新增 1编辑
const tags = ref("")

const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()

//下拉数据
const optionsType = ref([])

//下拉选择 勾选的数据
const selected = (value: string | number) => {
    // addModel.usr_id = value;
    console.log(value);
}

//下拉数据 后台获取数据
const getSelectType = async () => {
    let res = await getSelectTypeApi();
    if (res.code == 200) {
        optionsType.value = res.data
        // console.log("后台数据："+res.data)
    }

}



//搜索 绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,

    cul_title: '',
    cul_type_id: '',
    cul_pub: '',

})


//搜索按钮
const searchBtn = () => {
    getList();
}

//重置按钮
const resetBtn = () => {
    searchParam.curPage = 1;
    searchParam.cul_title = '';
    searchParam.cul_type_id = '',
        searchParam.cul_pub = '',

        getList();
}





//表格数据
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])
// const tableList: any = ref([])
// const tableData: any = ref([])

// 表格 用于存储用户账户信息的映射
const typeNameMap = ref<Record<string, string>>({});


const getTypeName = async (id: string) => {
    if (!typeNameMap.value[id]) {
        const res = await getTypeNameApi(id);
        typeNameMap.value[id] = res.msg;
        console.log('获取信息成功:', res.msg);
    }
    return typeNameMap.value[id];
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
    dialog.height = 600;
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);

        //头像回显
        if (addModel.cul_cover) {
            MyPicUploadRef.value.fileList.push({
                'url': addModel.cul_cover
            })
        }
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
    console.log("getList:", tableData.value)
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
            const ids = multipleSelection.value.map((item: { cul_id: any; }) => item.cul_id);
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
    addModel.cul_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '增加';
    dialog.height = 600;

    //清空表单
    addRef.value?.resetFields();
}



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    cul_id: '',
    cul_title: '',
    cul_type_id: '',
    cul_pub: '',
    cul_content: '',
    cul_shelves: true,
    cul_recommend: false,
    cul_cover: '',
})

//弹窗推荐改变
const recommendChange = (status: any) => {
    console.log(status)
}
const shelvesChange = (status: any) => {
    console.log(status)
}

//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid: any) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;
            //带头像
            if (MyPicUploadRef.value?.fileList.length) {
                //上传用户头像涉及的变量
                let FormDatas = new FormData(); //上传给后端的变量
                // let avater = MyPicUploadRef.value.fileList[0].raw;
                let path = MyPicUploadRef.value.fileList[0].raw;
                FormDatas.append("file", path);
                console.log(FormDatas.get('file'));
                let uploadMsg = await uploadApi(FormDatas);
                console.log(uploadMsg.data);
                if (uploadMsg) {
                    addModel.cul_cover = uploadMsg.data;
                }
            }
            if (tags.value == "0") {
                //添加

                // for (const [key, val] of Object.entries(addModel)) {
                //     formData.append(key, val as any)
                // }

                res = await addApi(addModel)

                // res = await addApi(addModel)
            } else {
                //修改
                res = await editApi(addModel)
                // console.log(res)
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
    MyPicUploadRef.value.fileList = [];
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

//表单验证
const rules = reactive({
    cul_title: [
        { required: true, message: '请输入资讯标题', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    cul_type_id: [
        { required: true, message: '请选择资讯类型', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    cul_pub: [
        { required: true, message: '请输入发布者', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    cul_cover: [
        { required: true, message: '请选择封面', trigger: 'change', validator: validList}
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    cul_content: [
        { required: true, message: '请输入内容', trigger: 'change'}
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
    getSelectType();
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                await getTypeName(row.cul_type_id);
            }
        }
    }, { immediate: true })
})



</script>

<style lang="scss" scoped>
//修改分页器激活颜色
// :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
//     background-color: #493131b0; //修改默认的背景色
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