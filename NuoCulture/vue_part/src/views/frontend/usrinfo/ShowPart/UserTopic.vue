<template>
    <div class="contain">
        <div class="batch-actions">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" :indeterminate="isIndeterminate">全选</el-checkbox>
            <el-button @click="batchDelete" :disabled="selectedItems.length === 0" color="#493131b0"
                plain>批量删除</el-button>
            <el-button @click="addBtn" color="#493131b0" class="add" size="large">发布话题</el-button>
        </div>
        <el-empty description="暂时没有数据" v-if="showEmpty" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards initial-visible" v-for="item in usrTopicList">
                <div class="select ">
                    <el-checkbox-group v-model="selectedItems">
                        <el-checkbox :value="item.topic_id" :key="item.topic_id"
                            @change="handleSelectChange(item.topic_id, $event)"><br>
                        </el-checkbox>
                    </el-checkbox-group>
                </div>
                <div class="card " @click="navigateToDetail(item.topic_id, item.topic_status)">

                    <el-image :src="item.img_path_url" alt="" fit="cover" />
                    <!-- </div>
                <div class="card"> -->
                    <div class="content">
                        <div class="top">
                            <el-text class="title">{{ item.topic_titile }}</el-text>
                            <!-- <el-text class="time">{{ item.start_time }}&nbsp;至&nbsp;{{ item.end_time }}</el-text> -->

                        </div>
                        <div class="text-clamp" v-ignore-images="item.topic_content"></div>
                        <el-text class="pub">负责人:{{ user.usr_name }}</el-text>
                    </div>
                </div>


                <div class="right ">
                    <div style="text-align: left;width: 100%;font-size: 20px;margin: 10px 0;">
                        <el-text style="font-size: 20px;">审核状态:</el-text>

                        <el-text v-if="item.topic_status == 0" style="font-size: 20px;">审核中</el-text>
                        <el-text v-if="item.topic_status == 1" style="font-size: 20px;">审核成功</el-text>
                        <el-text v-if="item.topic_status == -1" style="font-size: 20px;">审核失败</el-text>
                    </div>

                    <div style="text-align: left;width: 100%;font-size: 20px;margin: 10px 0;">
                        <el-button link style="font-size: 20px;" @click="datail(item)" color="#493131"
                            plain>查看详情</el-button>
                        <el-button link style="font-size: 20px;" @click="editBtn(item)" color="#493131"
                            v-if="item.join_status != 0" plain>重新发布</el-button>
                        <el-button link style="font-size: 20px;" @click="deleteBtn(item.topic_id)" color="#493131"
                            plain>删除</el-button>

                    </div>



                </div>
            </div>
        </el-skeleton>
    </div>
    <el-dialog title="查看详情" width="80%" style="max-height: 80%;overflow: auto;" append-to-body v-model="isvisible">
        <el-row v-if="tags === '0'">
            <el-col :span="24" :offset="0" style="margin: 10px 0;">
                <el-text style="font-size: 20px">
                    审核备注：{{ addModel.topic_remark ? addModel.topic_remark : "无备注" }}
                </el-text>
            </el-col>
        </el-row>
    </el-dialog>
    <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="标题" prop="topic_titile">
                            <el-input v-model="addModel.topic_titile" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="话题类型" prop="topic_type_id">
                            <SelectChecked :options="optionsType" @change="selected" ref="selectRef"
                                v-model="addModel.topic_type_id"></SelectChecked>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="主图" prop="img_path">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="5" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24" :offset="0">
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
import { userStore } from '@/store/user/index'
const user = userStore()
import { addtopicApi, delTopicApi, allDelTopicApi, addViewApi } from '@/api/frontend/topic/index';
import { getUsrTopicApi } from '@/api/frontend/usrinfo/index';
import { nextTick, onMounted, reactive, ref, watch } from 'vue';

import { editApi } from '@/api/frontend/topic/index';
import { uploadApi, getSelectTypeApi, getImagesApi } from '@/api/backend/Topic/Content/index';
import RichTextEditor from '@/components/RichTextEditor.vue'
import SelectChecked from "@/components/SelectChecked.vue"
import uploadAvater from "@/components/uploadAvater.vue"
import UsrDialog from "@/components/UsrDialog.vue"
import useDialog from '@/hooks/useDialog';


const selectAll = ref(false);
const isIndeterminate = ref(false)
const selectedItems = ref<any[]>([]);
// 全选/取消全选
const handleSelectAll = (val: boolean) => {
    if (val) {
        selectedItems.value = usrTopicList.value.map((item: { topic_id: any; }) => item.topic_id);
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
    selectAll.value = selectedItems.value.length === usrTopicList.value.length;
    isIndeterminate.value = selectedItems.value.length > 0 && selectedItems.value.length < usrTopicList.value.length;
};


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
            let res = await allDelTopicApi(ids);
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



//弹窗
const { dialog } = useDialog()
//弹窗 ref属性
const addRef = ref<FormInstance>()
const addModel = reactive({
    topic_id: '',
    topic_titile: '',
    topic_type_id: '',
    topic_content: '',
    usr_id: user.usr_id,
    topic_recommend: false,
    topic_status: '0',
    img_path: '',
    topic_remark: '',
})

//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();
    MyPicUploadRef.value.fileList = [];
}
const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()
//表单 更新富文本编辑器内容
const update = (content: string) => {
    addModel.topic_content = content
    // console.log(addModel.cul_content)

};

const tags = ref("")
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

                res = await addtopicApi(addModel)

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


//上传图片墙
const srcList: any = ref(['']);
const srcMap: any = ref<Record<string, string>>({});
// 表格 照片墙
const getImages = async (id: string) => {
    const res = await getImagesApi(id);
    srcList.value = res.data;
    srcMap.value[id] = srcList.value;
    console.log(srcMap.value[id]);
    return srcMap.value[id];

}


// 编辑
const editBtn = (row: any) => {
    console.log(row);
    tags.value = "1";
    //显示弹窗
    dialog.visible = true;
    dialog.title = '重新发布';
    dialog.height = 600;

    //回显数据
    nextTick(async () => {
        //复制数据
        Object.assign(addModel, row);

        await getImages(row.topic_id);

        // 头像回显
        if (srcMap.value[row.topic_id]) {
            console.log(srcMap.value[row.topic_id].length);
            for (let i = 0; i < srcMap.value[row.topic_id].length; i++) {
                console.log(srcMap.value[row.topic_id][i]);
                MyPicUploadRef.value.fileList.push({
                    'url': srcMap.value[row.topic_id][i]
                });
            }
        }
    })

    //清空表单
    addRef.value?.resetFields();
}

const addBtn = () => {
    addModel.topic_id = '';
    tags.value = "0";
    dialog.visible = true;
    dialog.title = '发布话题';
    dialog.height = 600;

    //清空表单
    addRef.value?.resetFields();
    addModel.topic_status = '1';
}



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
        // console.log("后台数据："+optionsType.value.values)
    }

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
})
















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
            let res = await delTopicApi(id);
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

//设置显示内容弹窗默认值
const isvisible = ref(false)

//表格 显示富文本编辑框
const datail = (item: any) => {
    tags.value = "0";
    isvisible.value = true;
    //回显数据
    nextTick(async () => {
        //复制数据
        Object.assign(addModel, item);


    })
};

const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,
    // cul_id:'',
})

const showEmpty = ref(false)
const loading = ref(true)

const usrTopicList: any = ref([]);
const getList = async () => {
    //发送请求
    let res = await getUsrTopicApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        usrTopicList.value = res.data;
        console.log(usrTopicList.value);
        searchParam.total = res.data.total;
        loading.value = false;

    }
}




watch(usrTopicList, () => {
    showEmpty.value = usrTopicList.value.length === 0;
    console.log(showEmpty.value);
});

onMounted(() => {
    getList();
    getSelectType();

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

const navigateToDetail = async (id: string, status: number) => {
    let res = await addViewApi(id);
    console.log(id);
    if (status == 1)
        router.push({ name: 'fronttopicdetial', params: { id } });
};

</script>
<style scoped>
.add {
    position: absolute;
    /* top: -5px; */
    /* left:120px; */
    /* top: 25px; */
    right: 10px;
}

.right {
    /* opacity: 1;
    transform: none; */
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
    margin: 20px;
    position: relative;

    .batch-actions {
        margin-bottom: 10px;
        display: flex;
        align-items: center;
        gap: 20px;
        padding: 10px 0;
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
    /* opacity: 1;
    transform: none; */
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