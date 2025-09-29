import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
//引入
import router from './router'

//引入icon图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn'

//引入pinia
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
//实例化pinia
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

const app = createApp(App);
app.use(router).use(pinia).use(ElementPlus, {
    locale: zhCn,
}).mount('#app')

app.directive('ignore-images', {
    beforeMount(el: { innerHTML: string }, binding: { value: string }) {
        const content = binding.value || ''; // 处理undefined情况
        el.innerHTML = content.replace(/<(img|video)[^>]*>/gi, '');
    },
    updated(el: { innerHTML: string }, binding: { value: string }) {
        const content = binding.value || ''; // 处理undefined情况
        el.innerHTML = content.replace(/<(img|video)[^>]*>/gi, '');
    }
});

//全局注明图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
import "@/assets/css/text.scss"

// 解决路由跳转后滚动条回到顶部的问题
router.afterEach((to,from,next) => {
    if(document.querySelector('.fontscrollbar .el-scrollbar__wrap')){

        document.querySelector('.fontscrollbar .el-scrollbar__wrap').scrollTo(0,0);
    }
})