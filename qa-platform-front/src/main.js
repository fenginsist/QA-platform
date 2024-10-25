import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'

import router from './router'

// import envConfig from './config/index'; // 引入环境配置


const app = createApp(App)  // .mount('#app')


// 将配置挂载到全局
// app.config.globalProperties.$env = envConfig;


app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(router)
app.mount('#app')

