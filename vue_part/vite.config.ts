import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from "path"

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  server:{
    host:'0.0.0.0',    //IP地址
    port:8080,         //服务器端口
    open:true,         //是否自动打开浏览器
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
})
