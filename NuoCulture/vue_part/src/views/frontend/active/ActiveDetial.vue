<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <el-empty description="暂时没有数据" v-if="detail.length === 0" />
        <div class="cards">
            <div class="card">
                <el-carousel :interval="5000" type="card" height="200px">
                    <el-carousel-item v-for="item in srcList" :key="item">
                        <el-image :src="item" fit="cover" class="img" />
                    </el-carousel-item>
                </el-carousel>

                <el-skeleton :rows="5" animated :loading="loading">

                    <el-descriptions title="活动信息" direction="vertical" :column="3" size="large" border :module="detail">
                        <template #extra>
                            <div style="display: flex;align-items: center;margin-top: 25px;">
                                <el-icon :size="35" style="margin-right: 10px;">
                                    <component :is="showNew ? 'StarFilled' : 'Star'" @click="Star" />
                                </el-icon>
                                <el-button color="#4e3929" @click="addBtn">我要报名</el-button>

                            </div>

                        </template>
                        <el-descriptions-item label="活动名称">{{ detail.actv_name }}</el-descriptions-item>
                        <el-descriptions-item label="负责人">{{ detail.actv_leader }}</el-descriptions-item>
                        <el-descriptions-item label="联系电话">{{ detail.actv_phone }}</el-descriptions-item>
                        <el-descriptions-item label="地址">
                            {{ detail.actv_region.split(',').join('/') + '/' + detail.actv_addr }}
                        </el-descriptions-item>
                        <el-descriptions-item label="开始时间">{{ detail.start_time }}</el-descriptions-item>
                        <el-descriptions-item label="结束时间">{{ detail.end_time }}</el-descriptions-item>
                    </el-descriptions>

                    <el-tabs :module="detail">
                        <el-tab-pane label="活动详情" v-html="detail.actv_content" class="text-clamp"></el-tab-pane>
                        <el-tab-pane :label="'精彩瞬间' + (index + 1)" v-for="(item, index) in momentList"
                            v-html="item.mom_content" class="text-clamp"></el-tab-pane>
                    </el-tabs>

                </el-skeleton>
            </div>
        </div>

        <UsrDialog :visible="dialog.visible" :title="dialog.title" :width="dialog.width" :height="dialog.height"
            @onClose="onClose" @onConfirm="commit" >
            <template v-slot:content>
                <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
                    <el-row>
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

    </div>

</template>
<script setup lang="ts">
import { defineProps, onMounted, reactive, ref } from 'vue';

const props = defineProps<{ id: string }>();

// 使用 props.id 来获取传递过来的 id
console.log('Detail ID:', props.id);
import { userStore } from '@/store/user/index'
const user = userStore()
import { useRouter } from 'vue-router';
const router = useRouter();
//收藏

const showNew = ref(false)

import { JwtToken } from '@/api/frontend/usrinfo/index';
const Star = async () => {

    try {
        await JwtToken();
        // 发送请求
        showNew.value = !showNew.value
        let res = await StarActvApi(props.id, user.usr_id, showNew.value)
        if (res.code == 200) {
            console.log(res.msg);
            ElMessage.success(res.msg)
            // getStar()
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}


const getStar = async () => {
    if (user.usr_id != '') {
        let res = await getStarApi(props.id, user.usr_id)
        if (res.code == 200) {
            console.log(res.data);
            showNew.value = res.data;
        }
    }
}



import BreadCrumb from '@/Client/Header/BreadCrumb.vue';

import { getDetailApi, getImagesApi, getMomentApi, addApi, getStarApi, StarActvApi } from '@/api/frontend/active/index';

const showEmpty = ref(false)
const loading = ref(true)

const detail: any = ref([{}]);
const getDetail = async () => {
    //发送请求
    let res = await getDetailApi(props.id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        detail.value = res.data[0];
        console.log(detail.value);
        loading.value = false;
    }
}



//上传图片墙
const momentList: any = ref(['']);
// 表格 照片墙
const getMoment = async () => {
    const res = await getMomentApi(props.id);
    momentList.value = res.data;
    console.log(momentList.value);
    return momentList.value;
}

//上传图片墙
const srcList: any = ref(['']);
// 表格 照片墙
const getImages = async () => {
    const res = await getImagesApi(props.id);
    srcList.value = res.data;
    console.log(srcList.value);
    return srcList.value;
}

import useDialog from '@/hooks/useDialog';
import UsrDialog from "@/components/UsrDialog.vue"
import { ElMessage, type FormInstance } from 'element-plus';
const { dialog } = useDialog()
const addRef = ref<FormInstance>()

//弹窗 增加表单的对象
const addModel = reactive({
    join_id: '',
    usr_id: '',
    actv_id: '',
    join_reason: '',
    join_status: '0',

})
//弹窗 关闭
const onClose = () => {
    dialog.visible = false;
    addRef.value?.resetFields();

}
//添加按钮
const addBtn = async() => {
    try {
        await JwtToken();
        // 发送请求
        addModel.join_id = '';
        dialog.visible = true;
        dialog.title = '活动申请';
        dialog.height = 150;
        addModel.actv_id = props.id
        addModel.usr_id=user.usr_id;
        addModel.join_status = '0';
        //清空表单
        addRef.value?.resetFields();
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }

}

const commit = () => {
    console.log("useCommit")
    addRef.value?.validate(async (valid) => {
        if (valid) {
            console.log("表单验证通过")
            let res = null;


            res = await addApi(addModel)

            if (res.code == 200) {
                // console.log(res)
                ElMessage.success(res.msg)
                //刷新页面
                getDetail()
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

onMounted(() => {
    getDetail();
    getImages();
    getMoment();
    getStar();
});





</script>
<style scoped>
.contain {
    width: auto;
    margin: 50px 50px;


    .el-breadcrumb {
        margin-bottom: 20px;
    }
}





.card {
    border-radius: 10px;
    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    /* width: 100%; */
    height: 100%;
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    flex-direction: column;
    padding: 40px;

    .el-carousel {
        width: 100%;
        height: 100%;

        .img {
            width: 100%;
            height: 100%;
        }
    }

    .el-descriptions {
        margin-top: 50px;
        --el-text-color-primary: #4e3929;
        width: 100%;
        /* --el-descriptions-item-bordered-label-background:#1d4350; */

    }

    .el-tabs {
        margin-top: 30px;

        .text-clamp {
            display: flex;
            flex-direction: column;
            text-align: left;
            text-indent: 2em;

        }

        .text-clamp:deep(img) {
            max-width: 80%;
            /* height: 200px; */
        }
    }

}
</style>