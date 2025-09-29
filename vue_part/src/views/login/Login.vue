<template>
    <div class="login">
        <el-form :inline="false" :model="loginModel" class="loginForm" ref="form" :rules="rules" size="large">
            <el-form-item>
                <div class="loginTile">
                    Login
                    <el-button link class="myBtn" @click="resetBtn"
                        style="position: absolute;right: 0;top: 30px;">重置</el-button>
                        <el-button link class="myBtn" @click="homeBtn"
                        style="position: absolute;left: 0;top: 30px;">免登录</el-button>
                </div>
            </el-form-item>
            <el-form-item prop="usr_account">
                <el-input v-model="loginModel.usr_account" placeholder="请输入账号" clearable />
            </el-form-item>
            <el-form-item prop="usr_psd">
                <el-input v-model="loginModel.usr_psd" placeholder="请输入密码" :type="typePsd" />
                <el-icon style="position: absolute;right: 10px;">
                    <component :is="showPsd ? 'Hide' : 'View'" @click="clickPsd" />
                </el-icon>
            </el-form-item>
            <el-form-item prop="code">
                <el-row :gutter="10">
                    <el-col :span="16" :offset="0">
                        <el-input v-model="loginModel.code" placeholder="请输入验证码" />
                    </el-col>
                    <el-col :span="8" :offset="0">
                        <img class="images" :src="imageSrc" @click="getImage" />
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item>
                <el-row :gutter="20" class="formBtn">
                    <el-col :span="12" :offset="0">
                        <el-button type="primary" class="myBtn" @click="commit">登 录</el-button>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-button type="danger" class="myBtn" @click="registerBtn">注册</el-button>
                    </el-col>
                </el-row>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup lang="ts">
import { getImageApi, loginApi } from '@/api/login/index';
import { useLoginStore } from '@/store/login'
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, type FormInstance } from 'element-plus';
import { useRouter } from 'vue-router';
import { userStore } from '@/store/user/index'
//路由对象
const router = useRouter()
//解引用方式
const store = userStore()

//表单对象
const form = ref<FormInstance>()
//表单绑定的对象
const loginModel = reactive({
    usr_account: '',
    usr_psd: '',
    code: ''
})

// 监听路由参数变化
import { useRoute } from 'vue-router'
const route = useRoute()

onMounted(() => {
    getImage()
    // 从Pinia获取账号密码并填充表单
    const loginStore = useLoginStore()
    if (loginStore.account && loginStore.password) {
        loginModel.usr_account = loginStore.account
        loginModel.usr_psd = loginStore.password
        loginStore.clearCredentials() // 使用后立即清除
    }
})

const showPsd = ref(true)
const typePsd = ref('password')
const clickPsd = () => {
    showPsd.value = !showPsd.value
    typePsd.value === 'password' ? typePsd.value = 'text' : typePsd.value = 'password'
    console.log(typePsd.value)

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

const registerBtn = () => {
    router.push({ name: 'register' });
}
const homeBtn = () => {
    router.push({ name: 'home' });
}



//重置
const resetBtn = () => {
    form.value?.resetFields()
}
//登录
const commit = () => {
    form.value?.validate(async (valid) => {
        if (valid) {
            //console.log("验证通过")
            let res = await loginApi(loginModel)
            //console.log("----login----",res)
            if (res.code == 200) {
                // console.log(res)
                store.setUserId(res.data.usr_id)
                store.setUsrName(res.data.usr_name)
                store.setToken(res.data.token)
                store.setAdmin(res.data.is_admin)
                console.log('store', res.data)
                //跳转首页
                if (res.data.is_admin == 1) {
                    ElMessage.success("登录成功!管理员身份登录！")
                    router.push({ path: '/administrator' })
                } else if (res.data.is_admin == 0) {
                    ElMessage.success(res.msg)
                    router.push({ path: '/client' })

                }
            }
        } else {
            console.log("不通过")
        }
    })
}

//表单验证
const rules = reactive({
    usr_account: [
        { required: true, message: '请输入账号', trigger: ['blur', 'change'] }
    ],
    usr_psd: [
        { required: true, message: '请输入密码', trigger: ['blur', 'change'] }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { len: 4, message: '验证码长度为4位', trigger: 'blur' }
    ]
})

onMounted(() => {
    getImage()
})

</script>

<style lang="scss" scoped>
.login {
    height: 100%;
    // background-color: #fff;
    background-image: url("@/assets/images/login.png");
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;


    .loginForm {
        height: 298px;
        width: 450px;
        background-color: rgba(255, 255, 255, 0.429);
        border-radius: 10px;
        box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.4);
        padding: 20px 35px;
        --el-color-danger: #726e73;
        margin-bottom: 3.5%;

        .el-form-item {

            margin-bottom: 20px;
        }


        .loginTile {
            display: flex;
            position: relative;
            justify-content: center;
            align-items: center;
            color: #ffffff;
            width: 100%;
            font-size: 32px;
            font-weight: 600;
            letter-spacing: 6px;
        }

        .images {
            height: 100%;
            width: 100%;
            cursor: pointer;
        }

        .formBtn {
            width: 100%;


            .myBtn {
                width: 222px;
                // --el-button-hover-bg-color: #464046b4;
                // --el-button-hover-border-color: #464046b4;
                // --el-button-active-bg-color: #464046b4;
                // --el-button-active-border-color: #464046b4;
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