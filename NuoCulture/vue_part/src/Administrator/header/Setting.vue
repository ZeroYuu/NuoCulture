<template>
    <el-dropdown>
        <span class="el-dropdown-link">

            <el-icon class="el-dropdown-link" :size="25">
                <Tools />
            </el-icon>

        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item @click="editInfo">修改信息</el-dropdown-item>
                <el-dropdown-item @click="editPsd">修改密码</el-dropdown-item>
                <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
    <el-drawer v-model="drawer" title="基本信息" :direction="direction" size="55%" :before-close="onClose">
        <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
            <div v-if="tags == '0'">
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="昵称" prop="usr_name">
                            <el-input v-model="addModel.usr_name" clearable style="width: 220px;" />
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
                            <el-input v-model="addModel.usr_email" clearable style="width: 220px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="电话" prop="usr_phone">
                            <el-input v-model="addModel.usr_phone" clearable style="width: 220px;" />
                        </el-form-item>
                    </el-col>

                </el-row>
                <el-row>
                    <el-col :span="10" :offset="0" style="margin-left: 20px;">
                        <el-form-item label="头像" prop="usr_avater">
                            <!-- <el-input  typt="text" autocapitalize="off" /> -->
                            <uploadAvater size="120px" :limit="1" ref="MyPicUploadRef" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <div v-if="tags == '1'">
                <el-row>
                    <el-col :span="24" :offset="0">
                        <el-form-item label="原密码" prop="usr_psd">
                            <el-input v-model="addModel.usr_psd" clearable style="width: 220px;" :type="typeOld" />
                            <el-icon>
                                <component :is="showOld ? 'Hide' : 'View'" @click="clickOld" />
                            </el-icon>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24" :offset="0">
                        <el-form-item label="新密码" prop="new_psd">
                            <el-input v-model="addModel.new_psd" clearable style="width: 220px;" :type="typeNew" />
                            <el-icon>
                                <component :is="showNew ? 'Hide' : 'View'" @click="clickNew" />
                            </el-icon>
                        </el-form-item>

                    </el-col>

                </el-row>
                <el-row>
                    <el-col :span="24" :offset="0">
                        <el-form-item label="确认密码" prop="confirm_psd">
                            <el-input v-model="addModel.confirm_psd" clearable style="width: 220px;"
                                :type="typeConfirm" />
                            <el-icon>
                                <component :is="showConfirm ? 'Hide' : 'View'" @click="clickConfirm" />
                            </el-icon>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
        </el-form>
        <!-- <template #footer> -->
        <div class="dialog-footer">
            <el-button color="#493131e6" @click="commit" plain>
                确定
            </el-button>
            <el-button color="#493131e6" @click="onClose" plain>
                取消
            </el-button>

        </div>
    </el-drawer>
</template>

<script lang="ts" setup>
import { ElMessage, type DrawerProps, type FormInstance } from 'element-plus';
import { nextTick, onMounted, reactive, ref } from 'vue';
const drawer = ref(false)
const direction = ref<DrawerProps['direction']>('ttb')
import uploadAvater from "@/components/frontAvater.vue"
import { getAccountApi, uploadApi, editUsrInfoApi, editPsdApi } from '@/api/frontend/usrinfo/index';
const user = userStore();
import { userStore } from '@/store/user/index'
const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()
const addRef = ref<FormInstance>()
const tags = ref("")

const exit = () => {
    sessionStorage.clear();
    window.location.href = '/login';
}
const editInfo = () => {
    tags.value = "0"
    drawer.value = true
    getUsrInfo()
}
const editPsd = () => {
    tags.value = "1"
    drawer.value = true

}
const addModel = reactive({
    usr_account: '',
    usr_name: '',
    usr_psd: '',
    usr_phone: '',
    usr_email: '',
    usr_bday: '',
    usr_avater: '',
    is_admin: '',

    new_psd: '',
    confirm_psd: '',
    usr_id: user.usr_id,

})



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
        addModel.usr_psd = ''
        //头像回显
        if (addModel.usr_avater) {
            MyPicUploadRef.value.fileList.push({
                'url': addModel.usr_avater
            })
        }


    }
}


const showOld = ref(true)
const showNew = ref(true)
const showConfirm = ref(true)
const typeOld = ref('password')
const typeNew = ref('password')
const typeConfirm = ref('password')


const clickOld = () => {
    showOld.value = !showOld.value
    typeOld.value === 'password' ? typeOld.value = 'text' : typeOld.value = 'password'

}
const clickNew = () => {
    showNew.value = !showNew.value
    typeNew.value === 'password' ? typeNew.value = 'text' : typeNew.value = 'password'
}
const clickConfirm = () => {
    showConfirm.value = !showConfirm.value
    typeConfirm.value === 'password' ? typeConfirm.value = 'text' : typeConfirm.value = 'password'
}


//弹窗 关闭
const onClose = () => {
    drawer.value = false
    addRef.value?.resetFields();
    MyPicUploadRef.value.fileList = []

}

//提交
const commit = async () => {
    console.log("useCommit")
    console.log("表单验证通过")
    let res = null;
    if (tags.value == "0") {
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
    } else {
        res = await editPsdApi(addModel)


    }

    if (res.code == 200) {
        // console.log(res)
        ElMessage.success(res.msg)
        location.reload()


    }
}
onMounted(() => {

})
//图片验证
const validList = (rule: any, value: any, callback: any) => {

    if (value !== addModel.new_psd) {
        callback(new Error('两次输入的密码不一致'));
    } else {
        callback();
    }
};

//表单验证
const rules = reactive({
    usr_email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
})
</script>

<style scoped>
.example-showcase .el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
}
</style>