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
                        <el-form-item label="账号">
                            <el-input v-model="searchParam.usr_account" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="昵称">
                            <el-input v-model="searchParam.usr_name" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="邮箱">
                            <el-input v-model="searchParam.usr_email" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="电话">
                            <el-input v-model="searchParam.usr_phone" clearable />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="生日">
                            <el-date-picker v-model="searchData.usr_bday" type="daterange" start-placeholder="开始出生日期"
                                end-placeholder="结束出生日期" :default-time="defaultTime" @change="getserchData"
                                value-format="YYYY-MM-DD" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="6">
                        <el-form-item label="角色">
                            <!-- <el-input v-model="searchParam.is_admin" clearable /> -->
                            <el-select v-model="searchParam.is_admin" placeholder="请选择" style="width: 220px">
                                <el-option v-for="item in options" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
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
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange" :show-overflow-tooltip="true">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="usr_account" label="账号" />
            <el-table-column prop="usr_name" label="昵称" />
            <el-table-column label="邮箱">
                <template #default="scope">
                    {{ scope.row.usr_email ? scope.row.usr_email : "--" }}
                </template>
            </el-table-column>
            <el-table-column label="头像">
                <template #default="scope">
                    <el-avatar :src="scope.row.usr_avater" icon="Avatar" style="font-size: 30px"/>
                </template>
            </el-table-column>
            <el-table-column label="手机号">
                <template #default="scope">
                    {{ scope.row.usr_phone ? scope.row.usr_phone : "--" }}
                </template>
            </el-table-column>
            <el-table-column prop="usr_bday" label="生日">
                <template #default="scope">
                    {{ scope.row.usr_bday ? scope.row.usr_bday : "--" }}
                </template>
            </el-table-column>
            <el-table-column prop="is_admin" label="所属角色" :formatter="stateFormat" />
            <el-table-column label="操作" min-width="180" width="180" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.usr_id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
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
            <el-form ref="addRef" label-width="auto" :model="addModel" :rules="rules" :inline="true">
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="账号" prop="usr_account">
                            <el-input v-model="addModel.usr_account" typt="text" autocapitalize="off" clearable/>
                        </el-form-item :offset="0">
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="昵称" prop="usr_name">
                            <el-input v-model="addModel.usr_name" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="生日" prop="usr_bday">
                            <el-date-picker v-model="addModel.usr_bday" type="date" placeholder="选择日期" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="角色" prop="is_admin">
                            <el-select v-model="addModel.is_admin" placeholder="请选择" style="width: 220px">
                                <el-option v-for="item in options" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item :offset="0">
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="邮箱" prop="usr_email">
                            <el-input v-model="addModel.usr_email" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="电话" prop="usr_phone">
                            <el-input v-model="addModel.usr_phone" clearable/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="头像" prop="usr_avater">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="1" ref="MyPicUploadRef" />
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
import { nextTick, onMounted, reactive, ref } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import { addApi, getListApi, editApi, deleteApi, uploadApi, allDeleteApi } from '@/api/backend/User/Account'
import uploadAvater from "@/components/uploadAvater.vue"

// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()
const { dialog } = useDialog()
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])
//角色选择
const options = [
    {
        value: 0,
        label: '用户',
    },
    {
        value: 1,
        label: '管理员',
    },
]
const stateFormat = (row: any) => {
    if (row.is_admin == "1") {
        return "管理员"
    }
    else if (row.is_admin == "0") {
        return "用户"
    }

}


//日期选择
const defaultTime = ref<[Date, Date]>([
    new Date(2000, 1, 1, 0, 0, 0),
    new Date(2000, 2, 1, 23, 59, 59),
])

//表单ref属性
const addRef = ref<FormInstance>()
//表单绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    usr_account: '',
    usr_name: '',
    usr_email: '',
    usr_phone: '',
    is_admin: '',
    usr_bday: '',
    total: 1,
})

const searchData = reactive({
    usr_bday: [''],
})

//添加增加表单的对象
const addModel = reactive({
    usr_id: '',
    usr_account: '',
    usr_name: '',
    usr_psd: '',
    usr_phone: '',
    usr_email: '',
    usr_bday: '',
    usr_avater: '',
    is_admin: '',
})
//搜索
const searchBtn = () => {
    getList();
}

// const value = ref('')
const getserchData = (value: Array<string>) => {
    searchParam.usr_bday = value.join(',');
    console.log(searchParam.usr_bday);
}

//重置
const resetBtn = () => {
    searchParam.usr_account = '';
    searchParam.curPage = 1;
    searchParam.usr_name = '',
        searchParam.usr_email = '',
        searchParam.usr_phone = '',
        searchParam.is_admin = '',
        searchParam.usr_bday = '',
        searchData.usr_bday = [''],
        getList();
}
//提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;
            //带头像
            addModel.usr_avater = ''
            if (MyPicUploadRef.value?.fileList.length) {
                //上传用户头像涉及的变量
                let FormDatas = new FormData(); //上传给后端的变量
                // let avater = MyPicUploadRef.value.fileList[0].raw;
                let path = MyPicUploadRef.value.fileList[0].raw;
                console.log(path);
                FormDatas.append("file", path);
                console.log(FormDatas.get('file'));
                let uploadMsg = await uploadApi(FormDatas);
                console.log(uploadMsg.data);
                if (uploadMsg) {
                    addModel.usr_avater = uploadMsg.data;
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
    MyPicUploadRef.value.fileList=[];

    addRef.value?.resetFields();

}

//判断是新增还是编辑，0新增 1编辑
const tags = ref("")


//添加用户
const addBtn = () => {
    addModel.usr_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '增加';
    //清空表单
    addRef.value?.resetFields();
}

//编辑
const editBtn = (row: any) => {
    console.log(row);
    tags.value = "1";
    //显示弹窗
    dialog.visible = true;
    dialog.title = '编辑';
    //回显数据
    nextTick(() => {
        //复制数据
        Object.assign(addModel, row)
        //头像回显
        if (addModel.usr_avater) {
            MyPicUploadRef.value.fileList.push({
                'url': addModel.usr_avater
            })
        }
    })
    //清空表单
    addRef.value?.resetFields();
}

//删除
const deleteBtn = (usr_id: string) => {
    console.log(usr_id)
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
            let res = await deleteApi(usr_id);
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

//多选框
const multipleSelection: any = ref<[]>([])
// 选择项变化
const handleSelectionChange = (val: any) => {
    multipleSelection.value = val
    console.log(multipleSelection.value)
}

//批量删除
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



//表单验证
const rules = reactive({
    usr_account: [
        { required: true, message: '请输入账号', trigger: 'blur' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    usr_name: [
        { required: true, message: '请输入名字', trigger: 'blur' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    is_admin: [
        {required: true, message: '请选择角色', trigger: 'blur' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
})


//表格数据
// const tableList: any = ref([])
// const tableData: any = ref([])

//表格高度
const tableHeight = ref(0)
//获取表格数据
const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        console.log(res)
        //设置表格数据
        tableData.value = res.data.records
        //设置总页数
        searchParam.total = res.data.total
    }
    console.log(tableData.value)
}


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

onMounted(() => {
    nextTick(() => {
        tableHeight.value = window.outerHeight - 620;
    })

    getList()

})

</script>

<style lang="scss" scoped>
//修改分页器激活颜色
// :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
//     background-color: #815908; //修改默认的背景色
// }
.el-form{
    width: 100%;
}
.el-input{
    width: 220px;
}
</style>