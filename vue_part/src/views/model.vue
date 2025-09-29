<template>
    <div class="search">
        <!-- 搜索框 -->
        <div class="seaBtn">
            <el-button color="rgb(105, 82, 52)" :icon="Search" @click="searchBtn">搜索</el-button>
            <el-button :icon="Refresh" @click="resetBtn" plain>重置</el-button>
        </div>
        <div class="searchCard">
            <el-form :inline="true" :model="searchParam" class="forme" size="default" label-position="right"
                label-width="auto">
                <el-row>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="省">
                            <el-select v-model="selectedProvinceCode" placeholder="请选择省份" @change="handleProvinceChange"
                                style="width: 180px;" filterable>
                                <el-option v-for="province in provinces" :key="province.value" :label="province.label"
                                    :value="province.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="市">
                            <el-select v-model="selectedCityCode" placeholder="请选择城市" @change="handleCityChange"
                                style="width: 180px;" filterable>
                                <el-option v-for="city in filteredCities" :key="city.value" :label="city.label"
                                    :value="city.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="区">
                            <el-select v-model="selectedDistrictCode" placeholder="请选择区" @change="handleDistrictChange"
                                style="width: 180px;" filterable>
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
                        <el-form-item label="生日">
                            <!-- <el-input v-model="searchParam.addr_name" clearable /> -->
                            <el-date-picker v-model="searchData.usr_bday" type="daterange" start-placeholder="开始出生日期"
                                end-placeholder="结束出生日期" :default-time="defaultTime" @change="getserchData"
                                value-format="YYYY-MM-DD" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="收货电话">
                            <el-input v-model="searchParam.addr_phone" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="所属用户">
                            <SelectChecked :options="options" @change="selected" ref="selectRef"
                                v-model="searchParam.usr_id"></SelectChecked>
                        </el-form-item>
                        <el-form-item label="角色">
                            <el-select v-model="searchParam" placeholder="请选择" style="width: 210px">
                                <el-option v-for="item in roleoptions" :key="item.value" :label="item.label"
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
            <el-button color="brown" :icon="EditPen" plain @click="addBtn">新增</el-button>
            <el-button type="danger" :icon="Delete" plain @click="allDeleteBtn">批量删除</el-button>
        </div>
        <el-table ref="tableList" :data="tableData" stripe
            :header-cell-style="{ background: ' rgb(105, 82, 52)', color: 'white', 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="usr_account" label="所属账户">
                <template #default="scope">
                    <el-text>{{ accountMap[scope.row.usr_id] || '' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="省">
                <template #default="scope">
                    <el-text>{{ addressMap[scope.row.addr_province] || '' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="市">
                <template #default="scope">
                    <el-text>{{ addressMap[scope.row.addr_city] || '' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="区">
                <template #default="scope">
                    <el-text>{{ addressMap[scope.row.addr_country] || '' }}</el-text>
                </template>
            </el-table-column>
            <el-table-column label="封面">
                <template #default="scope">
                    <el-image :src="scope.row.hp_path" :preview-src-list="[scope.row.hp_path]"
                        style="width:50px; height:40px;" :zoom-rate="1.2" :max-scale="5" :min-scale="0.2" />
                </template>
            </el-table-column>
            <el-table-column label="头像">
                <template #default="scope">
                    <el-avatar :src="scope.row.usr_avater" icon="Avatar" style="font-size: 30px" />
                </template>
            </el-table-column>
            <el-table-column label="邮箱">
                <template #default="scope">
                    {{ scope.row.usr_email ? scope.row.usr_email : "--" }}
                </template>
            </el-table-column>
            <el-table-column prop="addr_name" label="收货人" />
            <el-table-column prop="cul_content" label="文化内容">
                <template #default="scope">
                    <el-button @click="previewContent(scope.row.cul_content)">查看内容</el-button>
                </template>
            </el-table-column>
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
            <el-table-column prop="addr_phone" label="收货电话" />
            <el-table-column prop="addr_phone" label="所属角色" :stateFormat="stateFormat" />
            <el-table-column label="操作" min-width="180" width="220" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.usr_id)">删除</el-button>
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
                            <!-- <el-input v-model="addModel.addr_detail" /> -->
                            <SelectChecked :options="options" @change="selected" ref="selectRef"
                                v-model="addModel.usr_id"></SelectChecked>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <el-form-item label="头像" prop="usr_avater">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
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
    </UsrDialog>
</template>

<script setup lang="ts">
import { Delete, Search, EditPen, Refresh } from '@element-plus/icons-vue'
import UsrDialog from "@/components/UsrDialog.vue"
import { nextTick, onMounted, reactive, ref, watch, computed } from "vue";
import useDialog from '@/hooks/useDialog';
import { type FormInstance, ElMessage, ElMessageBox } from 'element-plus'
import { addApi, getListApi, editApi, deleteApi, getSelectApi, allDeleteApi, getAccountApi, getAddTextApi, uploadApi } from '@/api/Model/index'

import { regionData } from 'element-china-area-data'
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
    // console.log(fromVisible.value)

}


//判断是新增还是编辑，0新增 1编辑
const tags = ref("")

const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()



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
    usr_bday: [''],
})

//搜索 获取选择的日期
const getserchData = (value: Array<string>) => {
    searchParam.usr_bday = value.join(',');
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
        getList();
}





//表格数据
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])
// const tableList: any = ref([])
// const tableData: any = ref([])

// 表格 用于存储用户账户信息的映射
const accountMap = ref<Record<string, string>>({});

// 表格 获取单个用户账户信息
const getAccount = async (usr_id: string) => {
    if (!accountMap.value[usr_id]) {
        const res = await getAccountApi(usr_id);
        accountMap.value[usr_id] = res.msg;
        console.log('获取用户账户信息成功:', res.msg);
    }
    return accountMap.value[usr_id];
}

//表格 角色转换
const stateFormat = (row: any) => {
    if (row.is_admin == "1") {
        return "管理员"
    }
    else if (row.is_admin == "0") {
        return "用户"
    }
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

//表格 用于存储地址信息的映射
const addressMap = ref<Record<string, string>>({});

// 表格 后端获取的code变为text
const getAddressText = async (id: string) => {
    if (!addressMap.value[id]) {
        const res = await getAddTextApi(id);
        addressMap.value[id] = res.msg;
        console.log(res.msg);
    }
    return addressMap.value[id];
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

        //头像回显
        if (addModel.usr_avater) {
            MyPicUploadRef.value.fileList.push({
                'url': addModel.usr_avater
            })
        }

        //省市县联动回显
        selectedOptions.value[0] = addModel.addr_province.toString();
        selectedOptions.value[1] = addModel.addr_city.toString();
        selectedOptions.value[2] = addModel.addr_country.toString();
        handleChange();
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
//添加按钮
const addBtn = () => {
    addModel.addr_id = '';
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
})
//弹窗 提交
const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
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
    addRef.value?.resetFields();
    MyPicUploadRef.value.fileList = [];
    selectedOptions.value = [];
    handleChange();

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
        { required: true, message: '请输入收货人电话', trigger: 'change' }
        // { min: 3, max: 5, message: '', trigger: 'blur' },
    ],
    usr_id: [
        { required: true, message: '请选择所属账户', trigger: 'change' }
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
    getSelect()
    // 当表格数据加载完成后，获取所有用户的账户信息
    watch(() => tableData.value, async (newData) => {
        if (newData && newData.length > 0) {
            for (const row of newData) {
                await getAccount(row.usr_id);
                await getAddressText(row.addr_province);
                await getAddressText(row.addr_city);
                await getAddressText(row.addr_country);

            }
        }
    }, { immediate: true })
})

</script>

<style lang="scss" scoped>
//修改分页器激活颜色
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
    background-color: #815908; //修改默认的背景色
}

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