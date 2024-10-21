<template>
    <el-container class="h-screen bg-gray-100">
        <!-- 导航栏 -->
        <el-header class="bg-white shadow-md z-10">
            <div class="flex justify-between items-center h-full">
                <h1 class="text-xl font-bold text-blue-600">AI问答平台</h1>
                <el-avatar :size="40" src="https://placeholder.svg?height=40&width=40" />
            </div>
        </el-header>

        <el-container class="flex-1 overflow-hidden">
            <!-- 左侧菜单栏 -->
            <el-aside width="250px" class="bg-white shadow-lg overflow-y-auto">
                <el-button type="primary" class="w-full my-4" @click="newConversation">
                    <el-icon>
                        <Plus />
                    </el-icon>新建会话
                </el-button>
                <el-menu default-active="1" class="border-0">
                    <el-menu-item v-for="(item, index) in conversations" :key="index" :index="String(index + 1)">
                        <el-icon>
                            <ChatLineRound />
                        </el-icon>
                        <span>会话 {{ index + 1 }}</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <!-- 主要内容区 -->
            <el-main class="flex flex-col p-0 overflow-hidden bg-gray-50">
                <!-- 聊天记录 -->
                <div class="flex-1 overflow-y-auto p-4 space-y-4" ref="chatContainer">
                    <div v-for="(message, index) in currentConversation" :key="index"
                        :class="['flex', message.type === 'user' ? 'justify-end' : 'justify-start']">
                        <div :class="['max-w-3/4 p-3 rounded-lg',
                            message.type === 'user' ? 'bg-blue-100 text-blue-800' : 'bg-white text-gray-800',
                            'shadow-md']">
                            <div class="flex items-start" :class="message.type === 'user' ? 'flex-row-reverse' : ''">
                                <el-avatar :size="30" :icon="message.type === 'user' ? 'User' : 'ChatDotRound'"
                                    :class="message.type === 'user' ? 'ml-2' : 'mr-2'" />
                                <p class="text-sm">{{ message.text }}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 输入区 -->
                <div class="p-4 bg-white shadow-lg">
                    <div class="flex">
                        <el-input v-model="userInput" placeholder="请输入您的问题..." class="flex-1 mr-2" />
                        <el-input v-model="userInput" style="width: 240px"  :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea" placeholder="请输入您的问题..." />
                        <el-button type="primary" @click="sendMessage" :disabled="!userInput.trim()">
                            <el-icon>
                                <Send />
                            </el-icon>
                        </el-button>
                    </div>
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { Plus, ChatLineRound, User, ChatDotRound, Send } from '@element-plus/icons-vue'



const conversations = reactive([[]])
const currentConversation = ref([])
const userInput = ref('')
const chatContainer = ref(null)

const newConversation = () => {
    conversations.push([])
    currentConversation.value = conversations[conversations.length - 1]
}

const sendMessage = () => {
    if (userInput.value.trim()) {
        currentConversation.value.push({ type: 'user', text: userInput.value.trim() })
        userInput.value = ''
        // 模拟AI回答
        setTimeout(() => {
            currentConversation.value.push({ type: 'ai', text: '这是AI的回答。' })
            scrollToBottom()
        }, 1000)
    }
}

const scrollToBottom = () => {
    nextTick(() => {
        if (chatContainer.value) {
            chatContainer.value.scrollTop = chatContainer.value.scrollHeight
        }
    })
}

onMounted(() => {
    currentConversation.value = conversations[0]
})
</script>

<style scoped>
/* 添加一些过渡效果 */
.el-menu-item {
    transition: all 0.3s ease;
}

.el-menu-item:hover {
    background-color: #e6f7ff;
}

/* 自定义滚动条样式 */
.overflow-y-auto {
    scrollbar-width: thin;
    scrollbar-color: #cbd5e0 #edf2f7;
}

.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: #edf2f7;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background-color: #cbd5e0;
    border-radius: 4px;
}
</style>