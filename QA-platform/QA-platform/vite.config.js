import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  base: './',  // 配置静态资源为相对路径
  build: {
    outDir: 'dist',  // 打包输出目录，默认是这个目录，不配置也行
    assetsDir: 'assets',  // 静态资源默认放在 assets 文件夹中
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),  // 配置后，在src下的引用都可以通过 @/ 开头了
    }
  },
  /**
   * 前端不设置代理了，走nginx跨域或者是后端跨域。
   */
  server: {
    host: true,
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, ''),
        rewrite: (path) => path.replace(/^\/api/, '/api'), // 修正重写规则
      },
      //   '/questionAs': {
      //     target: 'http://192.168.16.21:8002', 
      //     changeOrigin: true,
      //     // rewrite: (path) => path.replace(/^\/api/, ''),
      //     rewrite: (path) => path.replace(/^\/api/, '/api'), // 修正重写规则
      //   },
      //   '/questionHr': {
      //     target: 'http://192.168.16.21:8001', 
      //     changeOrigin: true,
      //     // rewrite: (path) => path.replace(/^\/api/, ''),
      //     rewrite: (path) => path.replace(/^\/api/, '/api'), // 修正重写规则
      //   },
    },
  },
})
