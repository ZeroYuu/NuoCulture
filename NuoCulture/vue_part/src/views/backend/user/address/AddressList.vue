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
                            <!-- <el-date-picker v-model="searchData.usr_bday" type="daterange" start-placeholder="开始出生日期"
                                end-placeholder="结束出生日期" :default-time="defaultTime" @change="getserchData"
                                value-format="YYYY-MM-DD" /> -->
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="收货电话">
                            <el-input v-model="searchParam.addr_phone" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="0">
                        <el-form-item label="所属用户">
                            <SelectChecked :options="options1" @change="selected1" ref="selectRef"
                                v-model="searchParam.usr_id" filterable></SelectChecked>

                            <!-- <el-select v-model="searchParam" placeholder="请选择" style="width: 210px">
                                <el-option v-for="item in options" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select> -->
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
            <el-table-column prop="usr_account" label="所属用户">
                <template #default="scope">
                    <el-text>
                        {{ accountMap[scope.row.usr_id] ? accountMap[scope.row.usr_id] : "该用户已注销" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="省">
                <template #default="scope">
                    <el-text>
                        {{ addressMap[scope.row.addr_province] ? addressMap[scope.row.addr_province] : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="市">
                <template #default="scope">
                    <el-text>
                        {{ addressMap[scope.row.addr_city] ? addressMap[scope.row.addr_city] : "--" }}
                    </el-text>
                </template>
            </el-table-column>
            <el-table-column label="区">
                <template #default="scope">
                    <el-text>
                        {{ addressMap[scope.row.addr_country] ? addressMap[scope.row.addr_country] : "--" }}
                    </el-text>
                </template>
            </el-table-column>

            <!-- <el-table-column label="头像">
                <template #default="scope">
                    <el-avatar :src="scope.row.usr_avater" />
                </template>
</el-table-column> -->
            <el-table-column prop="addr_detail" label="详细地址" />
            <el-table-column prop="addr_name" label="收货人" />
            <el-table-column prop="addr_phone" label="收货电话" />
            <el-table-column label="操作" min-width="180" width="180" align="center">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editBtn(scope.row)">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small" @click="deleteBtn(scope.row.addr_id)">删除</el-button>
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
                            <el-input v-model="addModel.addr_detail" clearable/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="收货人" prop="addr_name">
                            <el-input v-model="addModel.addr_name" clearable/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="收货人电话" prop="addr_phone">
                            <el-input v-model="addModel.addr_phone" clearable/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="所属用户" prop="usr_id">
                            <!-- <el-input v-model="addModel.addr_detail" /> -->
                            <SelectChecked :options="options2" @change="selected2" ref="selectRef"
                                v-model="addModel.usr_id" filterable style="width: 220px;"></SelectChecked>

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
import { addApi, getListApi, editApi, deleteApi, getSelectApi, allDeleteApi, getAccountApi, getAddTextApi } from '@/api/backend/User/Address/index'
import { regionData } from 'element-china-area-data'
import SelectChecked from "@/components/SelectChecked.vue"
// 单文件引入
// import avatar from '@/assets/image/avatar.png'
// 多文件引入 介绍用法
// const avatar = getAssetsFile('avatar.png')

// export const getAssetsFile = (urlName: string) => {
//     // `../assets/image/${urlName}`替换为自己项目路径
//     return new URL(`../api/usr/${urlName}`, import.meta.url).href;
// }

const { dialog } = useDialog()
const tableList = ref<InstanceType<any>>()
const tableData = ref<any>([])

//下拉数据
const options1 = ref([])
const options2 = ref([])

// 用于存储用户账户信息的映射
const accountMap = ref<Record<string, string>>({});

// 获取单个用户账户信息
const getAccount = async (usr_id: string) => {
    if (!accountMap.value[usr_id]) {
        const res = await getAccountApi(usr_id);
        accountMap.value[usr_id] = res.msg;
        console.log('获取用户账户信息成功:', res.msg);
    }
    return accountMap.value[usr_id];
}


//勾选的数据
const selected1 = (value: string) => {
    searchParam.usr_id = value;
    console.log(value);
}
//勾选的数据
const selected2 = (value: string) => {
    addModel.usr_id = value;
    console.log(value);
}

//后台获取数据
const getSelect = async () => {
    let res = await getSelectApi();
    if (res.code == 200) {
        options1.value = res.data
        options2.value = res.data
        // console.log("后台数据："+res.data)
    }

}


//表单ref属性
const addRef = ref<FormInstance>()
//表单绑定的对象
const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    // usr_account: '',
    // usr_name: '',
    addr_name: '',
    addr_phone: '',
    addr_province: 0,
    addr_city: 0,
    addr_country: 0,
    addr_detail: '',
    usr_id: '',
    total: 1,
})
//获取列表数据  

const selectedOptions: any = ref([])
const handleChange = () => {
    addModel.addr_province = selectedOptions.value[0];
    addModel.addr_city = selectedOptions.value[1];
    addModel.addr_country = selectedOptions.value[2];
    console.log("handleChange", selectedOptions.value)

}


//添加增加表单的对象
const addModel = reactive({
    addr_id: '',
    addr_name: '',
    addr_phone: '',
    addr_province: 1,
    addr_city: 1,
    addr_country: 1,
    addr_detail: '',
    usr_id: '',
})
//搜索
const searchBtn = () => {
    getList();
}

// 用于存储用户账户信息的映射
const addressMap = ref<Record<string, string>>({});

// code变为text
const getAddressText = async (id: string) => {
    if (!addressMap.value[id]) {
        const res = await getAddTextApi(id);
        addressMap.value[id] = res.msg;
        console.log(res.msg);
    }
    return addressMap.value[id];
}

//重置
const resetBtn = () => {
    searchParam.addr_phone = '';
    searchParam.curPage = 1;
    searchParam.addr_name = '',
        searchParam.addr_detail = '',
        searchParam.addr_province = 0,
        searchParam.addr_city = 0,
        searchParam.addr_country = 0,
        selectedProvinceCode.value = '';
    selectedCityCode.value = '';
    selectedDistrictCode.value = '';
    searchParam.usr_id = '',

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
            // if (MyPicUploadRef.value?.fileList.length) {
            //     //上传用户头像涉及的变量
            //     let FormDatas = new FormData(); //上传给后端的变量
            //     // let avater = MyPicUploadRef.value.fileList[0].raw;
            //     let path = MyPicUploadRef.value.fileList[0].raw;
            //     FormDatas.append("file", path);
            //     console.log(FormDatas.get('file'));
            //     let uploadMsg = await uploadApi(FormDatas);
            //     console.log(uploadMsg.data);
            //     if (uploadMsg) {
            //         addModel.usr_avater = uploadMsg.data;
            //     }

            // }
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

//判断是新增还是编辑，0新增 1编辑
const tags = ref("")


//添加用户
const addBtn = () => {
    addModel.addr_id = '';
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

//删除
const deleteBtn = (addr_id: string) => {
    console.log(addr_id)
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
            let res = await deleteApi(addr_id);
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
            const ids = multipleSelection.value.map((item: { addr_id: any; }) => item.addr_id);
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


const onClose = () => {

    dialog.visible = false;
    addRef.value?.resetFields();
    addModel.addr_id = '';
    selectedOptions.value = [];
    handleChange();

}

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
        // console.log(res)
        //设置表格数据
        tableData.value = res.data.records
        //设置总页数
        searchParam.total = res.data.total
    }
    console.log("getList:", tableData.value)
}
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
// :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
//     background-color: #815908; //修改默认的背景色
// }
.el-form{
    width: 100%;
}
.el-input {
    width: 220px;
}
</style>