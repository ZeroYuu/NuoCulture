<template>
    <div class="register">
        <div class="registerForm">


            <el-steps :active="activeStep" finish-status="success" align-center>
                <el-step title="验证码校验" />
                <el-step title="基本信息填写" />
                <el-button link class="login" @click="loginBtn"
                    style="position: absolute;left: 40px;top: 20px;">登录</el-button>
            </el-steps>

            <div v-if="activeStep === 0">
                <!-- 验证码校验步骤 -->
                <el-form :model="addModel" :rules="verifyRules" ref="verifyFormRef" :inline="false" class="first"
                    size="large" label-width="auto">
                    <el-form-item prop="usr_account">
                        <el-input v-model="addModel.usr_account" placeholder="请输入账号" clearable />
                    </el-form-item>

                    <el-form-item prop="usr_psd">
                        <el-input v-model="addModel.usr_psd" :type="typePsd" placeholder="请输入密码" />
                        <el-icon>
                            <component :is="showPsd ? 'Hide' : 'View'" @click="clickPsd" />
                        </el-icon>
                    </el-form-item>

                    <el-form-item prop="confirm_psd">
                        <el-input v-model="addModel.confirm_psd" :type="typeConfirm" placeholder="请确认密码" />
                        <el-icon>
                            <component :is="showConfirm ? 'Hide' : 'View'" @click="clickConfirm" />
                        </el-icon>
                    </el-form-item>

                    <el-form-item prop="code" style="margin-bottom:8px;">
                        <el-row :gutter="10">
                            <el-col :span="16" :offset="0">
                                <el-input v-model="addModel.code" placeholder="请输入验证码" />
                            </el-col>
                            <el-col :span="8" :offset="0">
                                <img class="images" :src="imageSrc" @click="getImage" />
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form>
            </div>

            <div v-else class="step-content">
                <!-- 基本信息填写步骤 -->
                <el-form :model="addModel" :rules="registerRules" ref="registerFormRef" size="large">
                    <el-form-item prop="usr_name">
                        <el-input v-model="addModel.usr_name" placeholder="请输入昵称" clearable />
                    </el-form-item>
                    <el-form-item prop="usr_phone">
                        <el-input v-model="addModel.usr_phone" placeholder="请输入联系方式" clearable />
                    </el-form-item>
                    <el-form-item prop="usr_email">
                        <el-input v-model="addModel.usr_email" placeholder="请输入邮箱" clearable />
                    </el-form-item>
                    <el-form-item prop="avatar">
                        <uploadAvater size="80px" :limit="1" ref="MyPicUploadRef" />
                    </el-form-item>
                </el-form>
            </div>

            <div class="formBtn">
                <el-button v-if="activeStep < 1" type="primary" @click="nextStep">
                    下一步
                </el-button>

                <div class="registerBtn" v-if="activeStep > 0">
                    <el-button @click="prevStep">上一步</el-button>
                    <el-button type="primary" @click="submitRegister">
                        提交注册
                    </el-button>
                </div>

            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useLoginStore } from '@/store/login'
import { ElMessage } from 'element-plus'
import uploadAvater from "@/components/frontAvater.vue"
import { addApi, uploadApi } from '@/api/backend/User/Account'
import { getImageApi } from '@/api/login/index';
import { useRouter } from 'vue-router';
import { code } from 'three/tsl';
const router = useRouter();

const activeStep = ref(0)
const verifyFormRef = ref()
const registerFormRef = ref()
const typePsd = ref('password')
const typeConfirm = ref('password')
const showPsd = ref(true)
const showConfirm = ref(true)

const clickPsd = () => {
    showPsd.value = !showPsd.value
    typePsd.value === 'password' ? typePsd.value = 'text' : typePsd.value = 'password'
}
const clickConfirm = () => {
    showConfirm.value = !showConfirm.value
    typeConfirm.value === 'password' ? typeConfirm.value = 'text' : typeConfirm.value = 'password'
}

const loginBtn = () => {
    router.push({ name: 'login' });
}

const imageSrc = ref('')
//获取验证码
const getImage = async () => {
    let res = await getImageApi()
    if (res.code == 200) {
        imageSrc.value = res.data
        console.log(imageSrc.value)
    }
}


//图片验证
const validList = (rule: any, value: any, callback: any) => {

    if (value !== addModel.usr_psd) {
        callback(new Error('两次输入的密码不一致'));
    } else {
        callback();
    }
};



// 验证码表单验证规则
const verifyRules = reactive({
    usr_account: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 4, max: 16, message: '长度在4到16个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
    ],
    usr_psd: [
        { message: '请输入新密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度应在6到20个字符之间', trigger: 'blur' },
        {
            validator: (rule: any, value: any, callback: any) => {
                if (!/[A-Za-z]/.test(value) || !/[0-9]/.test(value)) {
                    callback(new Error('密码应为数字和字母组合'))
                } else if (/(\w)\1\1/.test(value)) {
                    callback(new Error('密码不能包含连续三个相同字符'))
                }
                else {
                    callback()
                }
            }, trigger: 'blur'
        }

    ],
    confirm_psd: [
        { validator: validList, message: '两次密码输入不一致', trigger: 'change' },
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { len: 4, message: '验证码长度为4位', trigger: 'blur' }
    ]
})

// 注册表单
const addModel = reactive({
    usr_id: '',
    usr_account: '',
    usr_name: '',
    usr_psd: '',
    confirm_psd: '',

    usr_phone: '',
    usr_email: '',
    usr_avater: '',
    code: '',

    usr_bday: '',
    is_admin: '0',

})
// 注册表单验证规则
const registerRules = reactive({
    usr_name: [
        { required: true, message: '请输入昵称', trigger: 'blur' },
        { min: 2, max: 16, message: '长度在2到16个字符', trigger: 'blur' }
    ],
    usr_email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
    usr_phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
    ]
})



// 步骤切换
const nextStep = async () => {
    try {
        await verifyFormRef.value.validate()
        activeStep.value++
    } catch (error) {
        ElMessage.error('请填写正确的验证信息')
    }
}

const prevStep = () => {
    activeStep.value--
}

const MyPicUploadRef: any = ref<InstanceType<typeof uploadAvater>>()
// 提交注册
const submitRegister = async () => {
    registerFormRef.value?.validate(async (valid: any) => {
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


            res = await addApi(addModel)


            if (res.code == 200) {
                // console.log(res)
                ElMessage.success(res.msg)
                const loginStore = useLoginStore()
                loginStore.setCredentials(addModel.usr_account, addModel.usr_psd)
                router.push('/login')
            }
        } else {
            console.log('表单验证不通过')
        }
    })

}
onMounted(() => {
    getImage()
})
</script>

<style lang="scss" scoped>
.register {
    height: 100%;
    // background-color: #fff;
    background-image: url("@/assets/images/login.png");
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;



    .registerForm {
        height: 318px;
        width: 450px;
        background-color: rgba(255, 255, 255, 0.429);
        border-radius: 10px;
        box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.4);
        padding: 10px 35px;
        --el-color-danger: #726e73;
        margin-bottom: 3.5%;
        position: relative;


        :deep(.el-step__title) {
            font-size: 13px;
            line-height: 20px;
            padding-bottom: 10px;
        }

        :deep(.el-step__icon) {
            font-size: 13px;
            height: 20px;
            width: 20px;

        }

        .el-form-item {
            margin-bottom: 20px;
        }

        .first {
            .el-text {
                position: relative;
            }

            .el-icon {
                position: absolute;
                right: 10px;
            }
        }

        .images {
            height: 100%;
            width: 100%;
            cursor: pointer;
        }

        .formBtn {
            width: 100%;
            position: relative;

            .registerBtn {
                position: absolute;
                bottom: 40px;
                right: 30px;
            }

            .myBtn {
                width: 222px;

            }

            .myBtn:hover {
                background: #493131;
                border-color: #493131;
                color: #fff;
            }
        }
    }

}
</style>
