<template>
    <template v-for="menu in menuList" :key="menu.path">
        <!-- 有子菜单 -->
        <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path"> <!-- index是唯一路径 -->
            <template #title>
                <el-icon>
                    <component :is="menu.meta.icon"></component>
                </el-icon>
                <span>{{ menu.meta.title }}</span>
            </template>
            <!-- 循环递归调用 -->
            <menu-item :menuList="menu.children"></menu-item>
        </el-sub-menu>

        <!-- 没有子菜单 -->
        <el-menu-item v-else :index="menu.path">
            <el-icon>
                <component :is="menu.meta.icon"></component>
            </el-icon>
            <template #title>{{ menu.meta.title }}</template>
        </el-menu-item>

    </template>

</template>

<script setup lang="ts">
//接受父组件的数据
defineProps(["menuList"])
</script>

<style lang="scss" scoped>

</style>