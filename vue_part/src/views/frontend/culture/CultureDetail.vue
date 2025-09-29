<template>
    <div class="contain">
        <bread-crumb></bread-crumb>

        <el-empty description="暂时没有数据" v-if="detail.length === 0" />

        <el-skeleton :rows="5" animated :loading="loading">
            <div class="cards" :model="detail">
                <div class="card">
                    <!-- <el-image :src="item.cul_cover" alt="" fit="cover" /> -->

                    <div class="content">
                        <div class="top">
                            <el-text class="title">{{ detail.cul_title }}</el-text>
                            <el-text class="time">{{ detail.cul_time }}</el-text>

                        </div>
                        <div class="text-clamp" v-html="detail.cul_content" />

                        <div style="display: flex;justify-content: flex-end;align-items: center;">
                            <el-icon :size="35" style="margin-right: 10px;">
                                <component :is="showNew ? 'StarFilled' : 'Star'" @click="Star" />
                            </el-icon>
                            <el-button @click="open3DViewer" v-if="detail.cul_type_id == '5'">查看3D模型</el-button>

                        </div>

                        <div style="text-align: right;margin-top: 20px;">
                            <el-text>发布者：{{ detail.cul_pub }}</el-text>
                        </div>
                    </div>
                </div>
            </div>

        </el-skeleton>

    </div>

    <!-- 3D模型查看弹窗 -->
    <div class="model">
        <el-dialog v-model="show3DModel" fullscreen :before-close="() => show3DModel = false" class="dialog"
            width="1480px">
            <div id="3d-container" style="width: 100%; height: 100%;"></div>
        </el-dialog>
    </div>

</template>

<script setup lang="ts">
import { defineProps, onUnmounted } from 'vue';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

const props = defineProps<{ id: string }>();
// 使用 props.id 来获取传递过来的 id
console.log('Detail ID:', props.id);

import BreadCrumb from '@/Client/Header/BreadCrumb.vue';
import { JwtToken } from '@/api/frontend/usrinfo/index';
import { getDetailApi, StarCulApi, getStarApi } from '@/api/frontend/culture/index';
import { onMounted, reactive, ref, watch, nextTick } from 'vue';
import { ElMessage } from 'element-plus';


const showEmpty = ref(false)
const loading = ref(true)
//收藏

//引入路由
import { userStore } from '@/store/user/index'
const user = userStore()
import { useRouter } from 'vue-router';

const router = useRouter();
const showNew = ref(false)

const Star = async () => {

    try {
        await JwtToken();
        // 发送请求
        showNew.value = !showNew.value
        let res = await StarCulApi(props.id, user.usr_id, showNew.value)
        if (res.code == 200) {
            console.log(res.msg);
            ElMessage.success(res.msg)
            // getStar()
        }
    } catch (error) {
        // 处理请求过程中的任何错误（如网络问题）
        if (error.response && error.response.status === 401) {
            // 如果错误响应包含401状态码，重定向到登录页面
            router.push({ path: '/login' });
        }
    }
}


const getStar = async () => {
    if (user.usr_id != '') {
        let res = await getStarApi(props.id, user.usr_id)
        if (res.code == 200) {
            console.log(res.data);
            showNew.value = res.data;
        }
    }


}



// 3D模型查看相关状态
const show3DModel = ref(false);
const flag = ref(false);
let scene: THREE.Scene, camera: THREE.PerspectiveCamera, renderer: THREE.WebGLRenderer, controls: OrbitControls;
// 初始化3D场景
const init3DScene = () => {
    const container = document.getElementById('3d-container')!;

    // 创建场景
    scene = new THREE.Scene();
    // scene.background = new THREE.Color(0xf0f0f0);


    // 创建相机
    camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
    camera.position.z = 5;

    // 创建渲染器
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(container.clientWidth, container.clientHeight);
    container.appendChild(renderer.domElement);

    // scene.background = new THREE.Color(0x000000); // 黑色
    renderer.setClearColor(new THREE.Color(0x000000), 0); // 第二个参数是透明度，范围从 0 到 1

    // 添加控制器
    controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;

    // 添加光源
    const ambientLight = new THREE.AmbientLight(0xffffff, 2);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 2);
    directionalLight.position.set(0, 1, 1);
    scene.add(directionalLight);

    // 加载模型

    console.log('开始加载模型');
    const loader = new GLTFLoader();
    loader.load('/mask.glb', (gltf: { scene: any; }) => {

        const model = gltf.scene;
        model.scale.set(5, 5, 5); // 将模型放大两倍
        model.rotation.set(0, -90, 0); // 可以根据需要调整模型的旋转



        scene.add(model);
        flag.value = true;

    }, undefined, (error: any) => {
        console.error('Error loading model:', error);
    })
        ;

    // 动画循环
    const animate = () => {
        requestAnimationFrame(animate);
        controls.update();
        renderer.render(scene, camera);
    };
    animate();

    // 窗口大小调整
    window.addEventListener('resize', () => {
        camera.aspect = container.clientWidth / container.clientHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(container.clientWidth, container.clientHeight);
    });
};

// 打开3D模型查看
const open3DViewer = () => {
    show3DModel.value = true;
    nextTick(() => {
        if (flag.value == false) {
            init3DScene();
        }
    });
};



const detail: any = ref([{}]);
const getDetail = async () => {
    //发送请求
    let res = await getDetailApi(props.id)
    if (res.code == 200) {
        // console.log(res)
        //设置表格数据
        detail.value = res.data[0];
        console.log(detail.value);
        loading.value = false;
    }
}


onMounted(() => {
    getDetail();
    getStar();
});

onUnmounted(() => {
    if (renderer) {
        renderer.dispose();
    }


});

</script>
<style scoped>
/* 3D模型查看弹窗样式 */

.contain {
    width: auto;
    margin: 50px 50px;


    .el-breadcrumb {
        margin-bottom: 20px;
    }
}

.card {
    border-radius: 10px;
    box-shadow: 0px 8px 20px #00000022;
    letter-spacing: 2px;
    width: 100%;
    height: 100%;
    background-color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 0;

    .content {
        display: flex;
        width: 90%;
        height: 100%;
        height: 100%;
        flex-direction: column;
        align-content: flex-start !important;


        .top {
            display: flex;
            justify-content: space-between;
            height: 20%;

            .title {
                font-size: 28px;
                color: black;
                font-weight: 500;
            }

            .time {
                font-size: 20px;
                color: #999;
            }

        }

        .text-clamp {
            display: flex;
            flex-direction: column;
            /* align-items: center; */
            text-align: left;
            text-indent: 2em;
            /* width: 100%; */
        }

        .text-clamp:deep(img) {
            max-width: 80%;
            /* margin-left: 8%; */

            /* height: 200px; */
        }

        .text-clamp:deep(video) {
            max-width: 80%;

            /* height: 200px; */
        }


    }

}

.page {
    height: 50px;
    line-height: 50px;
    position: relative;
    bottom: 0;
}

.typetitle {
    display: flex;
    gap: 1rem;
}
</style>
<style>
.model {
    .dialog {
        background-color: transparent;
        padding: 0;
        margin: 0;



    }

    .el-overlay {
        background-color: rgba(0, 0, 0, 0.9) !important;
    }
}
</style>