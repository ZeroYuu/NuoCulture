<template>
    <div class="cover">
        <!-- 添加遮罩元素 -->
        <div class="scroll-mask" :style="maskStyle" :height="carouselHeight + 'px'"></div>
        <el-carousel trigger="click" :height="carouselHeight + 'px'" :interval="6000">
            <el-carousel-item v-for="item in srcList" :key="item">
                <el-image :src="item" fit="cover" class="img" :style="imageStyle" />
            </el-carousel-item>
        </el-carousel>
    </div>

    <div class="List">
        <div class="part">
            <el-image :src="selectedCulture" fit="cover" class="partImg" />
            <el-collapse v-model="cultureNames" accordion @change="culturechange(cultureNames)">
                <el-text>
                    傩文化资讯
                </el-text>
                <br><br>
                <el-collapse-item v-for="(item, index) in cultureList" :title="item.cul_title" :name="index"
                    :key="index">
                    <div class="text-clamp" v-ignore-images="item.cul_content" @click="navigateToDetail(item.cul_id, 'cul')">
                    </div>
                </el-collapse-item>
            </el-collapse>
        </div>

        <div class="imgOne" :style="imageOneStyle" />

        <div class="part">
            <el-collapse v-model="activeNames" accordion @change="activechange(activeNames)">
                <el-text>
                    研学活动
                </el-text>
                <br><br>
                <el-collapse-item v-for="(item, index) in activeList" :title="item.actv_name" :name="index"
                    :key="index">
                    <div class="text-clamp" v-ignore-images="item.actv_content"  @click="navigateToDetail(item.actv_id, 'actv')">
                    </div>
                </el-collapse-item>
            </el-collapse>
            <el-image :src="selectedActive" fit="cover" class="partImg" />
        </div>

        <div class="imgTwo" :style="imageTwoStyle" />


        <div class="part">
            <el-image :src="selectedTopic" fit="cover" class="partImg" />
            <el-collapse v-model="topicNames" accordion @change="topicchange(topicNames)">
                <el-text>
                    话题讨论
                </el-text>
                <br><br>
                <el-collapse-item v-for="(item, index) in topicList" :title="item.topic_titile" :name="index"
                    :key="index">
                    <div class="text-clamp" v-ignore-images="item.topic_content" @click="navigateToDetail(item.topic_id, 'topic')">
                    </div>
                </el-collapse-item>
            </el-collapse>
        </div>

        <div class="imgThree" :style="imageThreeStyle" />

        <div class="partGoods">
            <el-text>
                傩文化周边
            </el-text>

            <div class="card-container" @mousedown="startDrag" @mousemove="handleDrag" @mouseup="endDrag"
                @mouseleave="endDrag">
                <div class="card-list" :style="{ transform: `translateX(${offset}px)` }">
                    <el-card v-for="(item, index) in goodsList" :key="index" class="card-item" @click="navigateToDetail(item.goods_id, 'goods')">
                        <template #header v-if="item != null">{{ item.goods_name }}</template>
                        <el-image :src="item.img_path_url" fit="cover" class="goodsImg" v-if="item != null" />
                    </el-card>
                </div>
            </div>

        </div>

    </div>


</template>

<script setup lang="ts">
import { getImagesApi, getCultureApi, getActiveApi, getTopicApi, getGoodsApi } from '@/api/frontend/home/index';
import type { CollapseModelValue } from 'element-plus';
import { computed, nextTick, onMounted, onUnmounted, ref, toRaw, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const navigateToDetail = (id: string, tag: string) => {
    console.log(id);
    if (tag == 'cul') {
        router.push({ name: 'frontculturedetial', params: { id } });
    } else if (tag == 'actv') {
        router.push({ name: 'frontactivedetial', params: { id } });
    }
    else if (tag == 'topic') {
        router.push({ name: 'fronttopicdetial', params: { id } });
    }
    else if (tag == 'goods') {
        router.push({ name: 'frontgoodsdetial', params: { id } });
    }

};

//上传图片墙
const carouselHeight = ref(0);
const srcList: any = ref(['']);
// 表格 照片墙
const getImages = async () => {
    const res = await getImagesApi();
    srcList.value = res.data;
    console.log(srcList.value);
    return srcList.value;
}


const cultureList: any = ref([{}]);
// 文化
const cultureNames = ref([0]); // 默认展开第一项
const selectedCulture = ref({});
const getculture = async () => {
    const res = await getCultureApi();
    cultureList.value = res.data;
    console.log("cultureList", cultureList.value);
    return cultureList.value;
}
const culturechange = (val: CollapseModelValue) => {
    console.log(val);
    selectedCulture.value = cultureList.value[val].cul_cover;
}


const activeList: any = ref([{}]);
// 活动
const activeNames = ref([0]); // 默认展开第一项
const selectedActive = ref({});
const getactive = async () => {
    const res = await getActiveApi();
    activeList.value = res.data;
    console.log("activeList", activeList.value);
    return activeList.value;
}
const activechange = (val: CollapseModelValue) => {
    console.log(val);
    selectedActive.value = activeList.value[val].actv_img_url;
    // return activeCulture.value;
}

const topicList: any = ref([]);
// 话题
const topicNames = ref([0]); // 默认展开第一项
const selectedTopic = ref({});
const gettopic = async () => {
    const res = await getTopicApi();
    topicList.value = res.data;
    console.log("topicList", topicList.value);
    return topicList.value;
}
const topicchange = (val: CollapseModelValue) => {
    console.log(val);
    selectedTopic.value = topicList.value[val].img_path_url;
    // return activeCulture.value;
}



// interface CarouselItem {
//     id: number
//     [key: string]: any
// }

const rawGoodsList: any = ref([]);
const goodsList = computed(() => {
    if (rawGoodsList.value.length > 0) {
        // 克隆足够多的元素确保任何时候都能显示3个商品
        return [
            ...rawGoodsList.value.slice(-2), // 最后两个放前面
            ...rawGoodsList.value,
            ...rawGoodsList.value.slice(0, 2) // 前两个放后面
        ]
    }
    return []
})
const props = defineProps<{
    autoPlay?: boolean
    interval?: number
}>()
let length = 0;
const itemWidth = ref(0)
const trackRef = ref<HTMLElement>()
const offset = ref(0)
const startX = ref(0)
const isDragging = ref(false)
const autoPlayTimer = ref<NodeJS.Timeout>()

// 计算卡片宽度
const calculateItemWidth = () => {
    if (goodsList.value.length > 0) {
        const card = document.querySelector('.card-item')
        if (card) {
            itemWidth.value = card.clientWidth + 40 // 60是margin和padding
        console.log(card.clientWidth)

        }
    }
}

// 商品
const getgoods = async () => {
    const res = await getGoodsApi();
    rawGoodsList.value = res.data;
    console.log("goodsList", rawGoodsList.value);
    length = rawGoodsList.value.length;

    nextTick(() => {
        calculateItemWidth()
        // 初始定位到第一个真实元素
        offset.value = -itemWidth.value*2
        startAutoPlay()
    })
    return rawGoodsList.value;
}

// 自动播放
const startAutoPlay = () => {
    if (autoPlayTimer.value) clearInterval(autoPlayTimer.value)
    if (props.autoPlay !== false) {
        autoPlayTimer.value = setInterval(() => {
            if (!isDragging.value) {
                offset.value -= itemWidth.value
                // 当滑动到倒数第二个真实商品时
                if (offset.value <= -itemWidth.value * (length + 1)) {
                    // 在动画开始前就禁用过渡效果
                    const oldTransition = document.querySelector('.card-list').style.transition
                    document.querySelector('.card-list').style.transition = 'none'
                    // 直接跳转到克隆元素位置
                    offset.value = -itemWidth.value
                    // 下一帧恢复过渡效果
                    requestAnimationFrame(() => {
                        document.querySelector('.card-list').style.transition = oldTransition
                    })
                }
            }
        }, props.interval || 4000)
    }
}

// 拖动处理
const startDrag = (e: MouseEvent) => {
    isDragging.value = true
    startX.value = e.clientX - offset.value
    if (autoPlayTimer.value) clearInterval(autoPlayTimer.value)
}

const handleDrag = (e: MouseEvent) => {
    if (!isDragging.value) return
    offset.value = e.clientX - startX.value
}

const endDrag = () => {
    if (!isDragging.value) return
    isDragging.value = false

    // 计算当前显示的真实商品索引
    let currentIndex = Math.round(-offset.value / itemWidth.value) - 1
    currentIndex = (currentIndex + length) % length
    console.log(currentIndex);

    // 处理边界情况
    const oldTransition = document.querySelector('.card-list').style.transition
    document.querySelector('.card-list').style.transition = 'none'

    // if (currentIndex >= length && offset.value < -itemWidth.value * length) {
    //     // 拖到倒数第二个商品之后，跳转到第个商品
    //     offset.value = -itemWidth.value 
    // } else if (currentIndex <= 0 && offset.value > -itemWidth.value) {
    //     // 拖到第一个商品之前，跳转到最后一个商品
    //     offset.value = -itemWidth.value * length
    // } else {
        // 正常情况对齐到最近的商品
        offset.value = -(currentIndex + 1) * itemWidth.value
        console.log(offset.value);
    // }

    // 下一帧恢复过渡效果
    // requestAnimationFrame(() => {
    //     document.querySelector('.card-list').style.transition = oldTransition
    // })

    startAutoPlay()
}









const scale = ref(1);
const scrollMaskOpacity = ref(0); // 遮罩透明度
const zIndex = ref(0); // 遮罩层级

const imageStyle = computed(() => ({
    transform: `scale(${scale.value})`,
    transformOrigin: 'center center',
    transition: 'transform  ease',
}));
const maskStyle = computed(() => ({
    backgroundColor: `rgba(0, 0, 0, ${scrollMaskOpacity.value})`,
    zIndex: `${zIndex.value}`,
    transition: 'transform  ease',
}));
const positionY1 = ref(0);
const Y1 = ref(0);
const positionY2 = ref(0);
const Y2 = ref(0);
const positionY3 = ref(0);
const Y3 = ref(0);
const ratio = ref(0.05);

const imageOneStyle = computed(() => ({
    backgroundPositionY: `${positionY1.value}px`,

}));
const imageTwoStyle = computed(() => ({
    backgroundPositionY: `${positionY2.value}px`,

}));
const imageThreeStyle = computed(() => ({
    backgroundPositionY: `${positionY3.value}px`,

}));





let lastScrollTop = 0;
// 处理滚动事件

const handleScroll = () => {
    const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
    const scrollTop = window.pageYOffset || scrollableContainer.scrollTop || document.body.scrollTop || 0;
    // console.log(scrollTop);
    if (scrollTop == 0) {
        zIndex.value = 0;
    } else {
        zIndex.value = 1;
    }
    if (scrollTop > lastScrollTop) {
        // 向下滚动
        if (scale.value < 2) { // 防止图片无限放大
            scale.value += (scrollTop - lastScrollTop) / 1000;
        } // 增加遮罩透明度
        // console.log(scrollMaskOpacity.value);
        if (scrollMaskOpacity.value < 0.5) {
            scrollMaskOpacity.value += (scrollTop - lastScrollTop) / 800; // 假设滚动500px时遮罩完全显示
        }



    } else {
        // 向上滚动
        if (scale.value > 1 && scrollTop < 1000) {
            scale.value -= (lastScrollTop - scrollTop) / 1000;
        }
        if (scrollMaskOpacity.value > 0) {
            scrollMaskOpacity.value -= (lastScrollTop - scrollTop) / 800; // 假设滚动500px时遮罩完全显示
        }
    }
    lastScrollTop = scrollTop;



    positionY1.value = Y1.value - scrollTop * ratio.value;
    positionY2.value = Y2.value - scrollTop * ratio.value;
    positionY3.value = Y3.value - scrollTop * ratio.value;
    // console.log(positionY1.value);

};

// 组件卸载时移除事件监听器

onMounted(() => {
    nextTick(() => {
        carouselHeight.value = window.outerHeight - 170;
    })
    // getList()
    getImages();
    getculture();
    getactive();
    gettopic();
    getgoods();

    const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
    console.log(scrollableContainer);
    if (scrollableContainer) {

        scrollableContainer.addEventListener('scroll', handleScroll);
    }


    positionY1.value = Y1.value = document.querySelector('.imgOne').offsetTop * ratio.value;
    positionY2.value = Y2.value = document.querySelector('.imgTwo').offsetTop * ratio.value;
    positionY3.value = Y3.value = document.querySelector('.imgThree').offsetTop * ratio.value;
    // console.log(positionY1.value);

    // 监听折叠面板的变化
    watch(topicList, () => {
        topicchange(0);

    })
    watch(activeList, () => {
        activechange(0);

    })
    watch(cultureList, () => {
        culturechange(0);
    })

});
onUnmounted(() => {
    const scrollableContainer: any = document.querySelector('.el-scrollbar__wrap');
    if (scrollableContainer) {
        scrollableContainer.removeEventListener('scroll', handleScroll);
    }

});
</script>

<style scoped>
.cover {
    transform-style: preserve-3d;
    perspective-origin: 100% 50%;
    height: 100%;
    /* 或动态高度 */
    width: 100%;
    perspective: 1000px;
    /* 透视 */
    position: relative;

    .el-carousel {
        width: 100%;
        height: 100%;

        .img {
            width: 100%;
            height: 100%;
        }
    }

}

.scroll-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* 初始透明度为0 */
    transition: background-color 0.3s ease;
    /* 过渡效果 */

}

.List {

    width: auto;
    margin: 100px 100px;

    .part {
        display: flex;
        justify-content: center;


        .partImg {
            width: 50%;
            height: 500px;
        }

        .el-collapse {
            width: 50%;
            padding: 30px;
            --el-collapse-header-bg-color: none;
            --el-collapse-content-bg-color: none;
            --el-collapse-header-font-size: 20px;
            --el-collapse-content-font-size: 20px;
            --el-collapse-border-color: none;

            .el-text {
                font-size: 32px;
                color: black;
                font-family: HANYIGUFENG;
                line-height: 70px;
                padding-bottom: 20px;
            }

            :deep(.el-collapse-item__content) {
                border-bottom: 2px dashed gainsboro !important;
            }

            .text-clamp {
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
                overflow: hidden;
                text-overflow: ellipsis;
                cursor: pointer;
            }

        }
    }
}

.partGoods {
    margin: 50px auto;

    .el-text {
        font-size: 32px;
        color: black;
        font-family: HANYIGUFENG;
        line-height: 70px;
        padding-bottom: 20px;
    }

    .card-container {
        overflow: hidden;
        white-space: nowrap;
        width: 100%;
        cursor: grab;
        height: 300px;
        position: relative;
        max-width: 900px;
        /* 限制最大宽度显示3个商品 */
        margin: 0 auto;
    }

    .card-list {
        display: flex;
        user-select: none;
        transition: transform 0.5s ease-out;
        will-change: transform;
        height: 100%;
    }

    .card-item {
        min-width: 260px;
        width: 260px;
        margin: 0 20px;
        flex: 0 0 auto;
        box-sizing: border-box;
        height: 100%;
        cursor: pointer;
        transition: transform 0.3s ease;

        &:hover {
            transform: scale(1.05);
        }
    }

    .goodsImg {
        width: 150px;
        height: 150px;
        object-fit: cover;
    }


}


.imgOne {
    width: 100%;
    height: 500px;
    background-size: cover;
    background-attachment: fixed;
    background-position-x: center;
    background-image: url('@/assets/images/homeImg1.png');
}

.imgTwo {
    width: 100%;
    height: 500px;
    background-size: cover;
    background-attachment: fixed;
    background-position-x: center;
    background-image: url('@/assets/images/homeImg2.jpg');
}

.imgThree {
    width: 100%;
    height: 500px;
    background-size: cover;
    background-attachment: fixed;
    background-position-x: center;
    background-image: url('@/assets/images/homeImg3.png');
}
</style>