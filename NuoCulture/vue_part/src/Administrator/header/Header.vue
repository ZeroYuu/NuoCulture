<template>
    <div class="header">
        <div class="left">
            <collapse></collapse>
            <bread-crumb></bread-crumb>
        </div>


        <Setting></Setting>
    </div>
</template>

<script setup lang="ts">
//引入Colloapse
import Collapse from '@/Administrator/header/Collapse.vue';

//引入BreadCrumb
import BreadCrumb from '@/Administrator/header/BreadCrumb.vue';
import Setting from '@/Administrator/header/Setting.vue';
</script>

<style lang="scss" scoped>
.header {
    .left{
        display: flex;
        flex-direction: row;
        align-items: center;

    }
}
</style>