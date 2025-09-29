<template>
    <div class="contain">
        <el-form ref="addRef" label-width="auto" :model="addModel" :inline="true" :rules="rules">
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
                        <el-input v-model="addModel.confirm_psd" clearable style="width: 220px;" :type="typeConfirm" />
                        <el-icon>
                            <component :is="showConfirm ? 'Hide' : 'View'" @click="clickConfirm" />
                        </el-icon>
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

import { editPsdApi } from '@/api/frontend/usrinfo/index';
import { userStore } from '@/store/user/index'
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, type FormInstance } from 'element-plus';
const user = userStore();
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


const addModel = reactive({
    usr_psd: '',
    new_psd: '',
    confirm_psd: '',
    usr_id: user.usr_id,

})
const addRef = ref<FormInstance>()
//提交
const commit = async () => {
    console.log("useCommit")
    console.log("表单验证通过")
    addRef.value?.validate(async (valid: any) => {
        if (valid) {
            let res = null;


            //修改
            res = await editPsdApi(addModel)

            if (res.code == 200) {
                // console.log(res)
                ElMessage.success(res.msg)
                location.reload()


            }
        }
    })
}
onMounted(() => {
    // getUsrInfo()
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
    new_psd: [
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
    usr_psd: [
        { message: '请输入原密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度应在6到20个字符之间', trigger: 'blur' }
    ]
})
</script>

<style lang="scss" scoped></style>