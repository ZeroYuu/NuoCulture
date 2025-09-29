<template>
    <el-select v-model="selectedOptions" placeholder="请选择"  style="width: 200px">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>

</template>

<script setup lang="ts">
import { ref } from 'vue';

//数据类型
type SelectItem = {
    value: string | number;
    label: string;
    // check: boolean;
}

//下拉框数据
let selectedOptions = ref<Array<string | number>>()

//父组件的数据
let props = defineProps({
    options: {
        type: Array<SelectItem>,
        required: true
    }
})

//暴露事件
const emit=defineEmits(['selected'])

// //删除
// const removeTag = (value: any) => {
// props.options.forEach((elm)=>{
//     if(elm.value==value){
//         elm.check=false
//     }
// })
// emit('selected',selectedOptions.value)
// }


</script>

<style scoped></style>