<template>
  <h1>HelloWorld</h1>
  <div class="mb-4">
    <el-button>Default</el-button>
    <el-button type="primary">Primary</el-button>
    <el-button type="success">Success</el-button>
    <el-button type="info">Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button type="danger">Danger</el-button>
  </div>
  <p>
    with extra class <b>is-loading</b>, your icon is able to rotate 360 deg in 2
    seconds, you can also override this
  </p>
  <el-icon :size="20">
    <Edit />
  </el-icon>
  <el-icon color="#409efc" class="no-inherit">
    <Share />
  </el-icon>
  <el-icon>
    <Delete />
  </el-icon>
  <el-icon class="is-loading">
    <Loading />
  </el-icon>
  <el-button type="primary">
    <el-icon style="vertical-align: middle">
      <Search />
    </el-icon>
    <span style="vertical-align: middle"> Search </span>
  </el-button>
  <hr>

  <h1>{{ store.count }}</h1>

  <el-button type="primary" @click="addBtn">点击累加</el-button>
  <h1>{{ store.getCount }}</h1>
  <el-button type="primary" @click="addBtn1">点击累加</el-button>

</template>

<script setup lang="ts">

import { usetestStore } from '@/store/test'
// import { storeToRefs } from 'pinia';

const store = usetestStore();

// const { count } = storeToRefs(store);

const addBtn = () => {
  // store.count++;
  store.setCount(++store.count)

}
const addBtn1 = () => {
  store.count++;

}

</script>

<style scoped></style>
