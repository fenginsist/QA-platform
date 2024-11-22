import { createMemoryHistory, createRouter, createWebHistory, createWebHashHistory } from 'vue-router'

import AS from '@/views/as/index.vue'
import HR from '@/views/hr/index.vue'
import RAG from '@/views/llm/rag.vue'
import LLM from '@/views/llm/llm.vue'
import FuWebBen from '@/views/fuwenben/index.vue'

import Test from '@/views/fuwenben/index.vue'

const routes = [
    { path: '/', redirect: '/as' }, // 添加重定向到默认路由
    { path: '/as', component: AS },
    { path: '/hr', component: HR },
    { path: '/rags', component: RAG },
    { path: '/llms', component: LLM },
    { path: '/fuwenben', component: FuWebBen },
]


const router = createRouter({
    history: createWebHashHistory(),// import.meta.env.BASE_URL
    // history: createWebHistory(),
    // history: createMemoryHistory(),
    routes,
})

export default router;