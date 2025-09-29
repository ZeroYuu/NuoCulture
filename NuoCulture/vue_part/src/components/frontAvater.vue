<template>
    <el-upload class="avatar-uploader" :class="fileList.length < limit ? '' : 'restrict-uploader'" :size="size"
        v-model:file-list="fileList" action="#" :limit="limit" list-type="picture-card" :auto-upload="false"
        :on-preview="handlePictureCardPreview" >
        <el-icon>
            <Plus />
        </el-icon>
    </el-upload>

    <!-- <el-dialog  v-model="avatarVisible" class="dialog">
        <el-image :src="avatarImageUrl" alt="Preview Image" fit="contain" />
    </el-dialog> -->
    <el-image-viewer v-if="avatarVisible" :url-list="avatarImageUrl" :z-index="3000"
        :initial-index="initialImgPreviewIndex" @close="closeImgViewer" />

</template>

<script setup lang='ts'>
import { ref } from 'vue'
import type { UploadProps, UploadUserFile } from 'element-plus'
// import { delImgApi} from '@/api/backend/Active/Publish/index'

const fileList = ref<UploadUserFile[]>([])

defineProps<{
    size: string, // 上传窗口大小
    limit: number // 物理层限制文件上传数量
}>()


// const handleRemove: UploadProps['onRemove'] = async(uploadFile, uploadFiles) => {
//     if (!uploadFile.url) return;
//     let imgPath = uploadFile.url.split('/').pop();
//     // 对路径进行URL编码处理
//     imgPath = encodeURIComponent(imgPath as string);

//     await delImgApi(imgPath);
//     console.log(uploadFile, uploadFiles);
//     // 从前端列表中移除已删除的图片
//     fileList.value = fileList.value.filter(file => file.url !== uploadFile.url);
// }


const avatarVisible = ref(false) // 头像大图预览弹框
const avatarImageUrl = ref<string[]>([]) // 头像大图预览地址
let initialImgPreviewIndex = ref(0)

// 图片预览
const handlePictureCardPreview: UploadProps['onPreview'] = (file) => {
    console.log(file);
    if (file.url) { // 确保file.url不是undefined
        avatarImageUrl.value = fileList.value.map(file => file.url);
        let index = avatarImageUrl.value.indexOf(file.url);
        console.log(index);

        if (index >= 0) {
            avatarImageUrl.value[index] = file.url;
            initialImgPreviewIndex.value = index;
        }
        avatarVisible.value = true;
    }
}

// 关闭图片预览
const closeImgViewer = () => {
    avatarVisible.value = false;
    avatarImageUrl.value = [];
    return true; // 必须返回true才能关闭
}

// 文件列表暴露给父组件
defineExpose({
    fileList
})



</script>

<style lang="scss" scoped>
.avatar-uploader {
    :deep(.el-upload-list--picture-card) {
        --el-upload-list-picture-card-size: v-bind(size)
    }

    :deep(.el-upload--picture-card) {
        --el-upload-picture-card-size: v-bind(size)
    }
}

.avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
}

.restrict-uploader {
    :deep(.el-upload) {
        display: none;
    }
}

.avatar-uploader {
    :deep(.el-upload-list__item-actions) {
        span:not(.el-upload-list__item-preview) {
            display: none !important;
        }
    }
}
</style>

<style>
.dialog {
    padding: 0;
    margin: 0;
    background: transparent !important;

}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
</style>