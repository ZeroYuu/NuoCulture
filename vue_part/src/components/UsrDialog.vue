<template>
    <!-- 弹窗 -->
    <el-dialog :model-value="visible" :title="title" :width="width + 'px'" :before-close="onClose" append-to-body
        :close-on-click-modal="false" destroy-on-close >
        <div class="container" :style="{ height: height + 'px' }">
            <slot name="content"></slot>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button color="#493131e6" @click="onConfirm" plain>
                    确定
                </el-button>
                <el-button @click="onClose">取消</el-button>
            </div>
        </template>
    </el-dialog>

</template>

<script setup lang="ts">
interface DialogProps {
    title: string,
    visible: boolean,
    width: number,
    height: number,
}
//接受父组件传值
const props = withDefaults(defineProps<DialogProps>(), {
    title: "标题",
    visible: false,
    width: 1200,
    height: 300,
})

const emit = defineEmits(["onClose", "onConfirm"])

//定义关闭的方式
const onClose = () => {
    emit("onClose")
}

//定义确定的方式
const onConfirm = () => {
    emit("onConfirm")
}

</script>

<style>
/* .status_change {

    display: flex;
    flex-direction: column;
    margin: 0 !important;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    max-height: calc(100% - 30px);
    max-width: calc(100% - 30px);
    min-height: auto;

    .el-dialog__body {
        flex: 1;
        overflow: auto;
    }


    .el-dialog__title {
        color: brown;
    }

} */
</style>
