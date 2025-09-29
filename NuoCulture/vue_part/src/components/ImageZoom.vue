
<template>
  <div class="image-zoom-container">
    <div class="image-container" 
         @mouseenter="showZoom = true" 
         @mouseleave="showZoom = false"
         @mousemove="trackMousePosition">
      <slot></slot>
      <div v-if="showZoom" class="zoom-lens"></div>
      <div v-if="showZoom" class="zoom-container" :style="zoomStyle">
        <div class="zoomed-image" :style="zoomedImageStyle"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const showZoom = ref(false)
const zoomLevel = ref(2)
const mouseX = ref(0)
const mouseY = ref(0)

const trackMousePosition = (e: MouseEvent) => {
  const rect = (e.currentTarget as HTMLElement).getBoundingClientRect()
  const lensWidth = 100 // 放大镜宽度
  const lensHeight = 100 // 放大镜高度
  
  // 计算鼠标位置
  let x = e.clientX - rect.left
  let y = e.clientY - rect.top
  
  // 限制x坐标在边界内
  if (x < lensWidth / 2) {
    x = lensWidth / 2
  } else if (x > rect.width - lensWidth / 2) {
    x = rect.width - lensWidth / 2
  }
  
  // 限制y坐标在边界内
  if (y < lensHeight / 2) {
    y = lensHeight / 2
  } else if (y > rect.height - lensHeight / 2) {
    y = rect.height - lensHeight / 2
  }
  
  mouseX.value = x
  mouseY.value = y
}

const zoomStyle = computed(() => ({
  display: showZoom.value ? 'block' : 'none'
}))

const zoomedImageStyle = computed(() => {
  const lensWidth = 100 // 放大镜宽度
  const lensHeight = 100 // 放大镜高度
  return {
    backgroundImage: `url(${props.src})`,
    backgroundSize: `${100 * zoomLevel.value}%`,
    backgroundRepeat: 'no-repeat',
    backgroundPosition: `-${(mouseX.value - lensWidth/2) * zoomLevel.value}px -${(mouseY.value - lensHeight/2) * zoomLevel.value}px`
  }
})

const props = defineProps({
  src: {
    type: String,
    required: true
  },
  width: {
    type: Number,
    default: 400
  },
  height: {
    type: Number,
    default: 400
  }
})
</script>

<style scoped>
.image-zoom-container {
  position: relative;
  display: inline-block;
}

.image-container {
  position: relative;
  cursor: crosshair;
}

.zoom-lens {
  position: absolute;
  border: 1px solid #d4d4d4;
  width: 100px;
  height: 100px;
  background-color: rgba(255, 255, 255, 0.3);
  pointer-events: none;
  transform: translate(-50%, -50%);
  left: v-bind('mouseX + "px"');
  top: v-bind('mouseY + "px"');
  z-index: 10;
}

.zoom-container {
  position: absolute;
  top: 0;
  left: calc(100% + 20px);
  width: v-bind('props.width + "px"');
  height: v-bind('props.height + "px"');
  border: 1px solid #ddd;
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  z-index: 100;
}

.zoomed-image {
  width: 100%;
  height: 100%;
}
</style>
