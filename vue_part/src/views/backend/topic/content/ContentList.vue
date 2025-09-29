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
                        <el-form-item label="话题类型">
                            <SelectChecked :options="optionsType" @change="selected" ref="selectRef"
                                v-model="searchParam.topic_type_id" filterable></SelectChecked>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="标题">
                            <el-input v-model="searchParam.topic_titile" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="发布人账号">
                            <el-input v-model="searchParam.usr_account" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="发布人昵称">
                            <el-input v-model="searchParam.usr_name" clearable />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="审核状态">
                            <SelectChecked :options="optionsStatus" @change="selected" ref="selectRef"
                                v-model="searchParam.topic_status" filterable />
                        </el-form-item>
                    </el-col>

                    <el-col :span="6" :offset="0">
                        <el-form-item label="审核备注">
                            <el-input v-model="searchParam.topic_remark" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="浏览量">
                            <number-range v-model="formData.numberRange" :valueRange="[0, 10000]"
                                @change="getserchViews" >
                            </number-range>
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
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="topic_titile" label="话题标题" :show-overflow-tooltip="true" />
            <el-table-column prop="topic_type_id" label="话题类型">
                <template #default="scope">
                    <el-text>{{ typeNameMap[scope.row.topic_type_id] ? typeNameMap[scope.row.topic_type_id] :
                        "--" }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="主图">
                <template #default="scope">
                    <el-image style="width: 50px; height: 50px" :src="srcMap[scope.row.topic_id][0]" :zoom-rate="1.2"
                        :max-scale="7" :min-scale="0.2" v-if="srcMap[scope.row.topic_id] !== undefined"
                        :preview-src-list="srcMap[scope.row.topic_id]" show-progress :initial-index="4" fit="cover"
                        lazy />
                </template>
            </el-table-column>
            <el-table-column prop="topic_view" label="浏览量" />
            <el-table-column label="发布账号">
                <template #default="scope">
                    <el-text v-if="accountMap[scope.row.usr_id] !== undefined">
                        {{ accountMap[scope.row.usr_id][0].usr_account ? accountMap[scope.row.usr_id][0].usr_account :
                            "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="发布昵称">
                <template #default="scope">
                    <el-text v-if="accountMap[scope.row.usr_id] !== undefined">
                        {{ accountMap[scope.row.usr_id][0].usr_name ? accountMap[scope.row.usr_id][0].usr_name :
                        "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column prop="topic_content" label="文化内容">
                <template #default="scope">
                    <el-button @click="previewContent(scope.row.topic_content)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="是否推荐">
                <template #default="scope">
                    {{ scope.row.topic_recommend ? "是" : "否" }}
                </template>
            </el-table-column>
            <el-table-column prop="topic_status" label="审核状态">
                <template #default="scope">
                    <el-text v-if="scope.row.topic_status == 0">待审核</el-text>
                    <el-text v-else-if="scope.row.topic_status == 1">审核通过</el-text>
                    <el-text v-else="scope.row.topic_status==-1">审核不通过</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="topic_remark" label="审核备注" />
            <el-table-column label="操作" min-width="150" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.topic_id)">删除</el-button>
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
                    <el-col :span="12" :offset="0" v-show="shouldShow">
                        <el-form-item label="标题" prop="topic_titile">
                            <el-input v-model="addModel.topic_titile" clearable style="width: 220px;"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0" v-show="shouldShow">
                        <el-form-item label="话题类型" prop="topic_type_id">
                            <SelectChecked :options="optionsType" @change="selected" ref="selectRef"
                                v-model="addModel.topic_type_id" style="width: 220px;"></SelectChecked>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0" v-show="shouldShow">
                        <el-form-item label="发布者" prop="usr_id">
                            <SelectChecked :options="optionsUsr" @change="selected" ref="selectRef"
                                v-model="addModel.usr_id" style="width: 220px;"></SelectChecked>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0" v-show="shouldShow">
                        <el-form-item label="主图" prop="img_path">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="5" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0"  v-show="!shouldShow">
                        <el-form-item label="审核状态" prop="topic_status">
                            <SelectChecked :options="statusoptions" @change="selected" ref="selectRef"
                                v-model="addModel.topic_status" filterable style="width: 220px;"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="审核备注" prop="topic_remark">
                            <el-input v-model="addModel.topic_remark" clearable style="width: 220px;"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0" >
                        <el-form-item label="是否推荐" prop="topic_recommend">
                            <el-switch v-model="addModel.topic_recommend"
                                @change="recommendChange(addModel.topic_recommend)" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24" :offset="0" v-show="shouldShow">
                        <el-form-item label="话题内容" prop="topic_content">
                            <RichTextEditor @update="update" :content="addModel.topic_content">
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
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import { addApi, getListApi, editApi, deleteApi, getSelectUsrApi, getAccountApi, getSelectTypeApi, allDeleteApi, getTypeNameApi, uploadApi, getImagesApi } from '@/api/backend/Topic/Content/index'

import SelectChecked from "@/components/SelectChecked.vue"
import uploadAvater from "@/components/uploadAvater.vue"
import RichTextEditor from '@/components/RichTextEditor.vue'
import NumberRange from '@/components/NumberRange.vue'



// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

//控制显示
const shouldShow = computed(() => {
    return !(tags.value == '1' && !UsrArray.includes(addModel.usr_id));
});

const statusoptions = ref([
    { value: 0, label: '待审核' },
    { value: 1, label: '审核通过' },
    { value: -1, label: '审核不通过' }
])


//上传图片墙
const srcList: any = ref(['']);
const srcMap: any = ref<Record<string, string>>({});
// 表格 照片墙
const getImages = async (id: string) => {
    const res = await getImagesApi(id);
    srcList.value = res.data;
    srcMap.value[id] = srcList.value;
    console.log(srcMap.value[id][0]);
    return srcMap.value[id];

}

// 表格 用于存储用户账户信息的映射
const accountMap = ref<Record<string, string>>({});

// 表格 获取单个用户账户信息
const getAccount = async (usr_id: string) => {
    let res = await getAccountApi(usr_id);
    if (!accountMap.value[usr_id]) {

        accountMap.value[usr_id] = res.data;
        console.log('获取用户账户信息成功:', accountMap.value[usr_id][0].usr_account);
        return accountMap.value[usr_id];
    }

}

//浏览量数组
const formData = reactive({
    numberRange: [''],
    // minValue:1,
    // maxValue:1000,

})

const getserchViews = (value: Array<string>) => {
    searchParam.topic_view = value.join(',');
    // console.log(searchParam.usr_bday);
}

//表单 更新富文本编辑器内容
const update = (content: string) => {
    addModel.topic_content = content
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

const optionsUsr = ref([])

const optionsStatus = ref([
    { value: 0, label: '待审核' },
    { value: 1, label: '审核通过' },
    { value: -1, label: '审核不通过' }
])

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
        // console.log("后台数据："+optionsType.value.values)
    }

}

let UsrArray: any = []
//下拉数据 后台获取数据
const getSelectUsr = async () => {
    let res = await getSelectUsrApi();
    if (res.code == 200) {
        optionsUsr.value = res.data
        for (let i = 0; i < res.data.length; i++) {
            UsrArray.push(res.data[i].value)
        }
        console.log("后台数据：", UsrArray)
    }

}



//搜索 绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,

    topic_titile: '',
    topic_type_id: '',
    topic_view: '',
    usr_account: '',
    usr_name: '',
    topic_status: '',
    topic_remark:'',

})


//搜索按钮
const searchBtn = () => {
    getList();
}

//重置按钮
const resetBtn = () => {
    searchParam.curPage = 1;
    searchParam.topic_titile = '';
    searchParam.topic_type_id = '',
    searchParam.usr_account = '';
    searchParam.usr_name = '';
    searchParam.topic_status = '',
    searchParam.topic_remark = '';
    formData.numberRange = [''],
    // formData.maxValue =0,
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
    if (UsrArray.includes(row.usr_id)) {
        dialog.height = 600;
    } else {
        dialog.height = 200;
    }
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row);

        // getImages(row.topic_id);
        //头像回显

        //头像回显

        for (let i = 0; i < srcMap.value[row.topic_id].length; i++) {
            console.log(srcMap.value[row.topic_id][i])
            MyPicUploadRef.value.fileList.push({
                'url': srcMap.value[row.topic_id][i]
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
            const ids = multipleSelection.value.map((item: { topic_id: any; }) => item.topic_id);
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
    addModel.topic_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '增加';
    dialog.height = 600;

    //清空表单
    addRef.value?.resetFields();
    addModel.topic_status = '1';
}



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    topic_id: '',
    topic_titile: '',
    topic_type_id: '',
    topic_content: '',
    usr_id: '',
    topic_recommend: false,
    topic_status: '',
    img_path: '',
    topic_remark:'',
})

//弹窗推荐改变
const recommendChange = (status: any) => {
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
                for (let i = 0; i < MyPicUploadRef.value.fileList.length; i++) {
                    let path = MyPicUploadRef.value.fileList[i].raw;
                    console.log(path);
                    FormDatas.append("file", path);
                }
                console.log(FormDatas.get('file'));
                let uploadMsg = await uploadApi(FormDatas);
                console.log(uploadMsg.data);
                if (uploadMsg) {
                    addModel.img_path = uploadMsg.data;
                }
            }
            if (tags.value == "0") {
                //添加

                res = await addApi(addModel)

            } else {
                //修改
                res = await editApi(addModel)

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
//省份自定义验证规则
const aduit = (rule: any, value: any, callback: any) => {
    if (tags.value == "1" && addModel.topic_status == '-1') {
        if (addModel.topic_remark == "" || addModel.topic_remark == null) {
            console.log("不通过原因不能为空")
            rules.topic_remark[0].required = true;
            return callback(new Error('不通过原因不能为空'));
        } else {
            rules.topic_remark[0].required = false;
            callback()
            console.log("验证成功")
        }
    }
    else {
            rules.topic_remark[0].required = false;
            callback()
            console.log("验证成功")
        }
};
//表单验证
const rules = reactive({
    topic_titile: [
        { required: true, message: '请输入话题标题', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    topic_type_id: [
        { required: true, message: '请选择话题类型', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    usr_id: [
        { required: true, message: '请选择发布人', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    topic_content: [
        { required: true, message: '请输入话题内容', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    img_path: [
        { required: true, validator: validList, trigger: 'change', message: '请选择图片' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    topic_remark: [
        { required: false, trigger: 'change', message: '请输入不通过原因', validator: aduit }
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
    getSelectUsr();
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                await getTypeName(row.topic_type_id);
                await getAccount(row.usr_id);
                await getImages(row.topic_id);
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
.el-form{
    width: 100%;
}
// .searchCard .el-input{
//     width: 220px;
// }
</style>