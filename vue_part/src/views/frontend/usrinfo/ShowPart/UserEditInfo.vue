<template>
    <div class="contain">
        <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
            <el-row>
                <el-col :span="12" :offset="0">
                    <el-form-item label="昵称" prop="usr_name" >
                        <el-input v-model="addModel.usr_name" clearable  style="width: 220px;"/>
                    </el-form-item>
                </el-col>
                <el-col :span="12" :offset="0">
                    <el-form-item label="生日" prop="usr_bday">
                        <el-date-picker v-model="addModel.usr_bday" type="date" placeholder="选择日期" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12" :offset="0">
                    <el-form-item label="邮箱" prop="usr_email">
                        <el-input v-model="addModel.usr_email" clearable  style="width: 220px;"/>
                    </el-form-item>
                </el-col>
                <el-col :span="12" :offset="0">
                    <el-form-item label="电话" prop="usr_phone">
                        <el-input v-model="addModel.usr_phone" clearable style="width: 220px;"/>
                    </el-form-item>
                </el-col>

            </el-row>
            <el-row>
                <el-col :span="10" :offset="0">
                    <el-form-item label="头像" prop="usr_avater">
                        <!-- <el-input  typt="text" autocapitalize="off" /> -->
                        <uploadAvater size="120px" :limit="1" ref="MyPicUploadRef" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <!-- <template #footer> -->
        <div class="dialog-footer">
            <el-button color="#493131e6" @click="commit" plain>
                确定
            </el-button>
        </div>
        <!-- </template> -->
    </div>
</template>

<script setup lang="ts">

import { getAccountApi, uploadApi, editUsrInfoApi } from '@/api/frontend/usrinfo/index';
import { userStore } from '@/store/user/index'
import { onMounted, reactive, ref } from 'vue';
import uploadAvater from "@/components/frontAvater.vue"
import { ElMessage } from 'element-plus';
const user = userStore();
const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()

const usrinfo: any = ref([{}]);
const getUsrInfo = async () => {
    //发送请求
    let res = await getAccountApi(user.usr_id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        usrinfo.value = res.data;
        console.log("usrinfo", usrinfo.value);


        //复制数据
        Object.assign(addModel, usrinfo.value)
        //头像回显
        if (addModel.usr_avater) {
            MyPicUploadRef.value.fileList.push({
                'url': addModel.usr_avater
            })
        }


    }
}

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

//提交
const commit = async () => {
    console.log("useCommit")
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

    //修改
    res = await editUsrInfoApi(addModel)

    if (res.code == 200) {
        // console.log(res)
        ElMessage.success(res.msg)
        location.reload()


    }
}
onMounted(() => {
    getUsrInfo()
})


//表单验证
const rules = reactive({
    usr_email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
})
</script>

<style lang="scss" scoped></style>