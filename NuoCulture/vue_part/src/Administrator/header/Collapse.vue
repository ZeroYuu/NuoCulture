<template>
    <div>
        <el-icon size="30px" @click="setCollapse" class="collapse">
            <component :is="status ? Expand : Fold"></component>
        </el-icon>
    </div>
</template>

<script setup lang="ts">
//引入menustore
import { useMenuStore } from '@/store/menu/index';
import { computed } from 'vue';
//引入图标
import { Fold,Expand } from '@element-plus/icons-vue';

//获取store
const store = useMenuStore();

//获取状态
const status = computed(()=>{
    return store.getCollapse;
})

//点击切换
const setCollapse= ()=>{
    store.setCollapse(!store.collapse)
}
</script>

<style lang="scss" scoped>
.collapse{
    cursor: pointer;
    margin-right: 10px;
    display: flex;
    align-items: center;
}
</style>