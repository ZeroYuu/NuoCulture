<!-- <template>
    <div id="my-three"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'; //gltf

const { proxy } = getCurrentInstance();
onMounted(() => {
    document.getElementById('my-three')?.appendChild(renderer.domElement)
    init()
    renderModel()
    gltfModel1()
    render()
})


const width = window.innerWidth, height = window.innerHeight;
const scene = new THREE.Scene();
const renderer = new THREE.WebGLRenderer()
const loader = new GLTFLoader();
const camera = new THREE.PerspectiveCamera(45, width / height, 1, 1000);
const controls = new OrbitControls(camera, renderer.domElement)


function init() {
    //光源
    const ambient = new THREE.AmbientLight(0xffffff, 0.4);
    scene.add(ambient);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);//光源，color:灯光颜色，intensity:光照强度
    directionalLight.position.set(400, 200, 300);
    scene.add(directionalLight);

    //设置相机位置
    camera.position.set(300, 300, 300);
    //设置相机方向
    camera.lookAt(0, 0, 0);

    //辅助坐标轴
    const axesHelper = new THREE.AxesHelper(200);//参数200标示坐标系大小，可以根据场景大小去设置
    scene.add(axesHelper);
    scene.background = new THREE.Color(0xeaeaea);

    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.antialias = true;
}

function gltfModel1() {
    // glb
    loader.load("@/assets/e6bec4801aaa40b9a407f0e60e6d7b9d.glb",
        function (gltf) {
            scene.add(gltf.scene)
        },
        function (xhr) {
            const percent = Math.floor((xhr.loaded / xhr.total) * 100); // 计算加载进度百分比
            // console.log(`模型加载进度：${percent}%`);
        }
    )

}



function renderModel() {
    //渲染
    renderer.setSize(width, height)//设置渲染区尺寸
    renderer.render(scene, camera)//执行渲染操作、指定场景、相机作为参数
    // renderer.setClearColor(0x00ff00); // 设置背景颜色为绿色
    renderer.toneMapping = THREE.ACESFilmicToneMapping;
    // 设置曝光度
    renderer.toneMappingExposure = 1.5; // 适当调整曝光度

    controls.minPolarAngle = Math.PI / 4; // 最小极角为 45 度
    controls.maxPolarAngle = Math.PI / 2; // 最大极角为 90 度
}

function render() {
    renderer.render(scene, camera);
    controls.update()
    requestAnimationFrame(render);
}



// 画布跟随窗口变化
window.onresize = function () {
    renderer.setSize(window.innerWidth, window.innerHeight);
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
};


// 交互事件
// addEventListener('dblclick', onMouseDblclick, false)
function onMouseDblclick(event) {
    let intersects = getIntersects(event);

    if (intersects.length !== 0 && intersects[0].object instanceof THREE.Mesh) {

        const selectedObject = intersects[0].object;
        let selectedObjects = [];
        selectedObjects.push(selectedObject);
        console.log(selectedObjects)
        // outlinePass.selectedObjects = selectedObjects;

    }
}
//获取与射线相交的对象数组
function getIntersects(event) {
    let rayCaster = new THREE.Raycaster();
    let mouse = new THREE.Vector2();

    //通过鼠标点击位置，计算出raycaster所需点的位置，以屏幕为中心点，范围-1到1
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1; //这里为什么是-号，没有就无法点中

    //通过鼠标点击的位置(二维坐标)和当前相机的矩阵计算出射线位置
    rayCaster.setFromCamera(mouse, camera);
    return rayCaster.intersectObjects(scene.children);
}

</script>

<style scoped lang="scss"></style> -->
