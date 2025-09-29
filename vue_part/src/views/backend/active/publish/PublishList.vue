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
                    <el-col :span="7" :offset="0">
                        <el-form-item label="活动名称">
                            <el-input v-model="searchParam.actv_name" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="7" :offset="0">
                        <el-form-item label="活动地址">
                            <el-input v-model="searchParam.actv_region" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="10" :offset="0">
                        <el-form-item label="开始时间">
                            <el-date-picker v-model="searchData.start_time" type="daterange" start-placeholder="开始日期"
                                end-placeholder="结束日期" :default-time="defaultTime" @change="getStarserchData"
                                value-format="YYYY-MM-DD" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="7" :offset="0">
                        <el-form-item label="负责人">
                            <el-input v-model="searchParam.actv_leader" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="7" :offset="0">
                        <el-form-item label="负责人电话">
                            <el-input v-model="searchParam.actv_phone" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="10" :offset="0">
                        <el-form-item label="结束时间">
                            <el-date-picker v-model="searchData.end_time" type="daterange" start-placeholder="开始日期"
                                end-placeholder="结束日期" :default-time="defaultTime" @change="getEndserchData"
                                value-format="YYYY-MM-DD" />
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
            <el-table-column prop="actv_name" label="活动名称" />
            <el-table-column label="主图">
                <template #default="scope">
                    <el-image style="width: 50px; height: 50px" :src="srcMap[scope.row.actv_id][0]" :zoom-rate="1.2"
                        :max-scale="7" :min-scale="0.2" v-if="srcMap[scope.row.actv_id] !== undefined"
                        :preview-src-list="srcMap[scope.row.actv_id]" show-progress :initial-index="4" fit="cover"
                        lazy />
                </template>
            </el-table-column>
            <el-table-column label="活动地址">
                <template #default="scope">
                    <el-text>{{ addressMap[scope.row.actv_region] || '加载中...' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="开始时间">
                <template #default="scope" >
                    {{ scope.row.start_time ? scope.row.start_time : "--" }}
                </template>
            </el-table-column>
            <el-table-column label="结束时间">
                <template #default="scope">
                    {{ scope.row.end_time ? scope.row.end_time : "--" }}
                </template>
            </el-table-column>
            <el-table-column label="活动内容">
                <template #default="scope">
                    <el-button @click="previewContent(scope.row.actv_content)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="是否推荐">
                <template #default="scope">
                    {{ scope.row.actv_recommend ? "是" : "否" }}
                </template>
            </el-table-column>
            <el-table-column prop="actv_leader" label="负责人" />
            <el-table-column prop="actv_phone" label="负责人电话" />
            <el-table-column label="操作" min-width="150" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.actv_id)">删除</el-button>
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
                        <el-form-item label="活动名称" prop="actv_name">
                            <el-input v-model="addModel.actv_name" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="省/市/区" prop="addr_region">
                            <el-cascader placeholder="请选择" :options="regionData" filterable v-model="selectedOptions"
                                @change="handleChange" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="详细地址" prop="actv_addr">
                            <el-input v-model="addModel.actv_addr" clearable/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="负责人" prop="actv_leader">
                            <el-input v-model="addModel.actv_leader" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="负责人电话" prop="actv_phone">
                            <el-input v-model="addModel.actv_phone" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="主图" prop="actv_img">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="5" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="开始时间" prop="start_time">
                            <el-date-picker v-model="addModel.start_time" type="datetime" placeholder="选择日期" clearable
                                format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="结束时间" prop="end_time">
                            <el-date-picker v-model="addModel.end_time" type="datetime" placeholder="选择日期" clearable
                                format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="是否推荐" prop="actv_recommend">
                            <el-switch v-model="addModel.actv_recommend" />
                        </el-form-item>
                    </el-col>
                    <el-row>
                        <el-col :span="24" :offset="0">
                            <el-form-item label="文化内容" prop="actv_content">
                                <RichTextEditor @update="update" :content="addModel.actv_content">
                                </RichTextEditor>
                            </el-form-item>
                        </el-col>
                    </el-row>
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
import { addApi, getListApi, editApi, deleteApi, getAddTextApi, allDeleteApi, getImagesApi, uploadApi } from '@/api/backend/Active/Publish/index'

import { regionData, CodeToText, TextToCode } from 'element-china-area-data'
import uploadAvater from "@/components/uploadAvater.vue"
import RichTextEditor from '@/components/RichTextEditor.vue'
import Console from '../../Console.vue';
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
    console.log(srcMap.value[id][0]);
    return srcMap.value[id];

}


//表单 更新富文本编辑器内容
const update = (content: string) => {
    addModel.actv_content = content
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




//搜索 绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    actv_name: '',
    actv_region: '',
    actv_addr: '',
    actv_leader: '',
    actv_phone: '',
    actv_content: '',
    start_time: '',
    end_time: '',
    actv_recommend: '',

})

//搜索 角色选择
const roleoptions = [
    {
        value: '0',
        label: '用户',
    },
    {
        value: '1',
        label: '管理员',
    },
]

//搜索 日期选择
const defaultTime = ref<[Date, Date]>([
    new Date(2000, 1, 1, 0, 0, 0),
    new Date(2000, 2, 1, 23, 59, 59),
])

//搜索 绑定日期
const searchData = reactive({
    start_time: [''],
    end_time: [''],
})

//搜索 获取选择的日期
const getStarserchData = (value: Array<string>) => {
    searchParam.start_time = value.join(',');
    console.log(searchParam.start_time);
}

const getEndserchData = (value: Array<string>) => {
    searchParam.end_time = value.join(',');
    console.log(searchParam.end_time);
}

//搜索按钮
const searchBtn = () => {
    getList();
}

//重置按钮
const resetBtn = () => {
    searchParam.curPage = 1;

    searchParam.actv_name = '';
    searchParam.actv_region = '',
        searchParam.actv_leader = '',
        searchParam.actv_phone = '',
        searchData.start_time = [''],
        searchData.end_time = [''],
        searchParam.start_time = '',
        searchParam.end_time = '',
        getList();
}





//表格数据
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])
// const tableList: any = ref([])
// const tableData: any = ref([])


//表格 获取省份列表数据  
const selectedOptions: any = ref([])

//表格 更改省市县联动
const handleChange = () => {

    addModel.actv_region = CodeToText[selectedOptions.value[0]] + "," +
        CodeToText[selectedOptions.value[1]] + "," + CodeToText[selectedOptions.value[2]];

    console.log(addModel.actv_region)
}

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

        const codeArray = addModel.actv_region.split(",");
        console.log(codeArray)
        console.log(codeArray[1])
        console.log(TextToCode["广东省"].惠州市.博罗县)

        selectedOptions.value[0] = TextToCode[codeArray[0]].code;
        selectedOptions.value[1] = TextToCode[codeArray[0]][codeArray[1]].code;
        selectedOptions.value[2] = TextToCode[codeArray[0]][codeArray[1]][codeArray[2]].code;
        console.log(selectedOptions.value)
        // handleChange();

        //省市县联动回显
        // selectedOptions.value[0] = addModel.addr_province.toString();
        // selectedOptions.value[1] = addModel.addr_city.toString();
        // selectedOptions.value[2] = addModel.addr_country.toString();
        // handleChange();

        getImages(row.actv_id);
        console.log(row.actv_id);

        //头像回显

        for (let i = 0; i < srcMap.value[row.actv_id].length; i++) {
            console.log(srcMap.value[row.actv_id][i])
            MyPicUploadRef.value.fileList.push({
                'url': srcMap.value[row.actv_id][i]
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
            const ids = multipleSelection.value.map((item: { actv_id: any; }) => item.actv_id);
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
    addModel.actv_id = '';
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
    actv_id: '',
    actv_name: '',
    actv_addr: '',
    actv_region: '',
    actv_leader: '',
    actv_phone: '',
    actv_content: '',
    start_time: '',
    end_time: '',
    actv_img: '',
    actv_recommend: false,
})
//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;
            //带头像
            console.log(MyPicUploadRef.value?.fileList);
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
                    addModel.actv_img = uploadMsg.data;
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
    addRef.value?.resetFields();
    MyPicUploadRef.value.fileList = [];
    selectedOptions.value = [];

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
    addr_region: [
        { required: true, validator: validCascader, trigger: 'change', message: '请选择地址' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_img: [
        { required: true, validator: validList, trigger: 'change', message: '请选择图片' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_addr: [
        { required: true, message: '请输入地址', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_name: [
        { required: true, message: '请输入活动名称', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_phone: [
        { required: true, message: '请输入负责人电话', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_leader: [
        { required: true, message: '请输入负责人', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    start_time: [
        { required: true, message: '请选开始时间', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    end_time: [
        { required: true, message: '请选择结束时间', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    actv_content: [
        { required: true, message: '请输入资讯内容', trigger: 'change' }
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
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                await getAddressText(row.actv_region, row.actv_addr);
                await getImages(row.actv_id)
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