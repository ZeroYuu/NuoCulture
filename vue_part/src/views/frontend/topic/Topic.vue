<template>
    <div class="contain">
        <bread-crumb></bread-crumb>
        <div class="typetitle">
            <el-button @click="changeAll" color="#493131b0" plain>全部</el-button>
            <div v-for="item in topicTypeList" :key="item" @click="changeType(item.topic_type_id)">
                <el-button v-if="topicTypeList !== undefined" color="#493131b0" plain>{{ item.topic_type_name
                    }}</el-button>
            </div>

            <el-button @click="addBtn" color="#493131b0" class="add" size="large">发布话题</el-button>
            <div class="a">
                <input type="text" class="b" v-model="searchParam.topic_content">
                <div class="c" @click="search">
                    <el-icon>
                        <Search />
                    </el-icon>
                </div>
            </div>
        </div>

        <el-empty description="暂时没有数据" v-if="topicList.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards">
                <div class="card initial-visible" v-for="item in topicList" @click="navigateToDetail(item.topic_id)">
                    <el-image :src="item.img_path_url" alt="" fit="cover" />


                    <el-text class="content">{{ item.topic_titile }}</el-text>

                </div>
            </div>

        </el-skeleton>

        <!-- 分页
            page-sizes 每页个数
            -->
        <div class="page">
            <el-pagination :page-sizes="[5, 10, 15, 20]" background :page-size="searchParam.pageSize"
                :current-page="searchParam.curPage" layout="->,total, sizes, prev, pager, next, jumper"
                :total="searchParam.total" @size-change="pageSizeChange" @current-change="currentPageChange" />
        </div>
    </div>


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
import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

import { getListApi, getTopicTypeApi, addtopicApi,addViewApi } from '@/api/frontend/topic/index';
import { uploadApi, getSelectTypeApi } from '@/api/backend/Topic/Content/index';

import { onMounted, reactive, ref, watch } from 'vue';


import RichTextEditor from '@/components/RichTextEditor.vue'
import SelectChecked from "@/components/SelectChecked.vue"
import uploadAvater from "@/components/uploadAvater.vue"
import UsrDialog from "@/components/UsrDialog.vue"
import useDialog from '@/hooks/useDialog';

import { userStore } from '@/store/user/index'
const user = userStore()
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
    topic_status: '',
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


            res = await addtopicApi(addModel)

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
//添加按钮
const addBtn = () => {
    addModel.topic_id = '';
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











const searchParam = reactive({
    curPage: 1,
    pageSize: 5,
    total: 1,

    topic_type_id: '',
    topic_status: '1',
    topic_content: '',
})

const search = () => {
    searchParam.curPage = 1;
    getList();

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
const showEmpty = ref(false)
const loading = ref(true)

const topicTypeList: any = ref([]);
// 文化
const getTopicType = async () => {
    const res = await getTopicTypeApi();
    topicTypeList.value = res.data;
    console.log("topicTypeList", topicTypeList.value);
    return topicTypeList.value;
}
const topicList: any = ref([{}]);


const getList = async () => {
    //发送请求
    let res = await getListApi(searchParam)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        topicList.value = res.data.records;
        console.log(topicList.value);
        searchParam.total = res.data.total;
        loading.value = false;

    }
}

const changeAll = () => {
    searchParam.topic_type_id = '';
    searchParam.curPage = 1;
    getList();
}
const changeType = (e: any) => {
    searchParam.topic_content = '',
        searchParam.topic_type_id = e;
    searchParam.curPage = 1;
    getList();
}

watch(topicList, () => {
    showEmpty.value = topicList.value.length === 0;
    console.log(showEmpty.value);
});

onMounted(() => {
    getTopicType();
    getSelectType();
    getList();

    // 标记首屏可见的卡片
    setTimeout(() => {
        const cards = document.querySelectorAll('.card');
        const viewportHeight = window.innerHeight;

        cards.forEach(card => {
            const rect = card.getBoundingClientRect();
            if (rect.top > viewportHeight) {
                card.classList.remove('initial-visible');
            }
        });
    }, 300);

});

import { useRouter } from 'vue-router';
import { ElMessage, type FormInstance } from 'element-plus';

const router = useRouter();

const navigateToDetail = async (id: string) => {
    let res = await addViewApi(id);
    if (res.code == 200) {
        console.log(id);
        router.push({ name: 'fronttopicdetial', params: { id } });
    }
};

</script>
<style scoped>
.add {
    position: absolute;
    /* top: -5px; */
    /* left:120px; */
    top: 25px;
    right: 10px;
}

.a {
    position: absolute;
    /* top: 10px; */
    /* left:220px; */
    top: 47px;
    right: 120px;
    transform: translate(0, -50%);
    background-color: #493131b0;
    height: 40px;
    border-radius: 40px;
    transition: .4s;
    display: flex;

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
    ;
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
    margin: 50px 50px;
    position: relative;


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

.cards {
    display: flex;
    /* margin-top: 20px; */
    flex-wrap: wrap;
    gap: 50px;
    margin: 40px 0;
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
    width: 22%;
    box-sizing: border-box;


    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;

    height: 320px;
    /* overflow: hidden; */
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    justify-content: center;

    padding: 10px;




    .el-image {
        width: 100%;
        height: 80%;
    }

    .content {
        width: 100%;
        /* height: 15%; */
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        text-overflow: ellipsis;

        font-size: 20px;
        color: black;
        font-weight: 500;


    }

}

.card:not(.initial-visible) {
    animation: slide-fade-in both;
    /* 使用浏览器的视图时间线,允许动画根据视口的变化进行同步 */
    animation-timeline: view();
    /* 定义动画时间范围 */
    animation-range: contain 0% contain 50%;
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