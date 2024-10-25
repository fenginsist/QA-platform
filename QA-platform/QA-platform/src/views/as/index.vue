<script setup>
import { getllmAs } from '@/api/index.js'     // @microsoft這是包，這裡需要加 @/
import person_head from '@/assets/icon/person_head.png'
import cvicse_logo from '@/assets/icon/cvicse-logo.png'

</script>
<script>
export default {
  name: 'AS',
  data() {
    return {
      messages: [],  // 存储接收到的LLM输出
      userInput: '',
      answer: '',
      isLoading: false, // ai 回答加载中的loading效果
      generatingMessage: {
        message: '',       // 用于存储生成的消息内容
        completed: false,  // 标记消息是否生成完成
        createTime: '',    // 存储消息的创建时间
        generating: false, // 标记是否正在生成
      },
      currentSessionId: null, // 当前会话 ID（如果需要）
      unReponse: true,        // 控制状态的标志
      scrollbar: 50,
      always: true
    }
  },
  mounted() {
  },
  methods: {
    addMessage(message) {
      this.messages.push(message)
    },
    sendMessage() {
      this.addMessage({ type: 'user', text: this.userInput })
      // 检查消息是否为空
      if (this.userInput.trim() === '' || this.userInput.length == 0) {
        this.addMessage({ type: 'ai', text: '问题不可为空' })
        this.scrollToBottom()
        return;
      }
      this.submitQuestionNoStream()
      this.scrollToBottom()
      // this.submitQuestionStream()
      this.userInput = '';
    },
    // 监控键盘按键：Shift + Enter 组合键 换行，enter键发送消息。
    handleKeyDown(event) {
      // 检查是否按下了 Enter 键
      if (event.key === 'Enter') {
        if (event.shiftKey) {
          // Shift + Enter 换行
          event.preventDefault(); // 阻止默认行为
          this.userInput += '\n'; // 手动换行
        } else {
          // 处理 Enter 键发送消息
          event.preventDefault(); // 阻止默认的换行行为
          this.sendMessage();
        }
      }
    },
    // 流式输出
    submitQuestionStream() {
      // 向后端发请求。
      let params = {
        'message': this.question
      }
      let _this = this
      let flag = true; // 仅执行一次
      fetchEventSource('/api/chat/chat-stream', {
        // 你可以在这里设置请求方法、请求头和请求体
        method: 'POST', // 或者 'POST'，取决于你的后端端点
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'sss',
          'Cache-Control': 'no-cache'
        },
        openWhenHidden: true,
        // 如果需要发送请求体，可以在这里设置
        body: JSON.stringify(message),

        // 处理接收到的消息
        onmessage(ev) {
          // this.messages.push(event.data);// 动态接收和处理每条消息
          let content = ev.data
          if (data != '[DONE]') {
            if (flag) {
              _this.messages
            }
          }
        },
        // 处理错误
        onerror(error) {
          console.error('Error:', error);
        },
        // 连接打开时的回调
        onopen(response) {
          console.log('Connection opened!', response);
        },
        // 连接关闭时的回调
        onclose() {
          console.log('Connection closed!');
        },
      });
    },
    // 非流式输出
    submitQuestionNoStream() {
      let params = {
        'question': this.userInput
      }
      console.log('params: ', params)
      // axios.get('/api/chat/chatContent', {params}).then(response => {
      //   // 请求成功，处理响应数据
      //   console.log('Data received from server:', response.data);
      // }).catch(error => {
      //   // 请求失败，处理错误
      //   console.error('Error fetching data:', error);
      // });
      this.isLoading = true; // 开始加载
      getllmAs(this.userInput).then(res => {
        console.log('response success!!!!!!!!!!!!!!!!!!!!!!')
        console.log('aaaa:', res)

        this.addMessage({ type: 'ai', text: res.answer })
        this.isLoading = false;
        // this.answer = res.answer
      }).catch(error => {
        console.error('请求失败，发生错误:', JSON.stringify(error));
        // 你可以根据实际需求对错误进行进一步处理
        this.addMessage({ type: 'ai', text: 'AI 回答失败，请刷新后稍后重试。' })
        this.isLoading = false;
      })
    },
    // 滚动条显示最新的消息
    scrollToBottom() {
      let el = document.querySelector("#app > div > div > main > div.el-scrollbar.chat-history > div.el-scrollbar__wrap.el-scrollbar__wrap--hidden-default")
      let el1 = el.querySelector(".el-scrollbar__view")
      el.scrollTo({
        top: el1.clientHeight + 20,
        left: 0,
        behavior: "smooth",
      })
      // this.$nextTick(() => {
      //   const scrollbarRef = this.$refs.scrollbarRef;
      //   console.log('scrollbarRef: ', scrollbarRef)
      //   console.log('scrollbarRef: ', JSON.stringify(scrollbarRef))
      //   // scrollbarRef.scrollTo({ top: scrollbarRef.scrollHeight, behavior: 'smooth' });  // 滚动到最底部
      //   if (scrollbarRef) {
      //     const wrapRef = scrollbarRef.wrapRef;
      //     console.log('wrapRef: ', wrapRef)
      //     console.log('wrapRef: ', JSON.stringify(wrapRef))
      //     // wrapElement.scrollTop = wrapElement.max-height;
      //   }
      // });
    },
    // 开启新会话
    newSession() {

    },
  }
}
</script>

<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="app-logo">中间件AS小助手：知识库目前涉及AS相关内容</div>
      <div class="app-user">
        <el-avatar :src="person_head"></el-avatar>
      </div>
    </el-header>

    <!-- 左侧菜单和右侧内容 -->
    <div class="app-body">
      <!-- <div class="body-main"> -->
      <!-- 左侧菜单栏 -->
      <el-aside width="240px" class="app-sidebar">
        <el-button type="primary" icon="el-icon-plus" @click="newSession" class="new-session-button">新建会话</el-button>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-main class="app-content">
        <!-- 问答历史展示区 -->
        <el-scrollbar class="chat-history" ref="scrollbarRef" :always="always" :min-size="scrollbar">
          <!-- 循环遍历 -->
          <div v-for="(message, index) in messages" :key="index" :class="['chat-message', message.type]">
            <template v-if="message.type === 'user'">
              <div class="user-message">
                <span>{{ message.text }}</span>
                <el-avatar :src="person_head" class="user-icon"></el-avatar>
              </div>
            </template>
            <template v-else>
              <div class="ai-message">
                <el-avatar :src="cvicse_logo" icon="el-icon-robot" class="ai-icon"></el-avatar>
                <text>{{ message.text }}</text>
              </div>
            </template>
          </div>
          <!-- 加载状态 -->
          <div v-loading="isLoading" element-loading-text="AI正在思考...">
            <!-- 这里可以放置一些占位内容，或者保持为空 -->
          </div>
        </el-scrollbar>

        <!-- 问题输入区 -->
        <div class="chat-input">
          <el-input v-model="userInput" class="input-box" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea"
            placeholder="请输入您的问题..." @keydown="handleKeyDown" />
          <!-- @keydown.enter="sendMessage"：监听单独按下 Enter 键，进行发送 
                 @keydown="handleKeyDown": 监控所有事件
            -->
          <el-button type="primary" icon="el-icon-send" @click="sendMessage" class="send-button">
            发送
          </el-button>
        </div>
      </el-main>
      <!-- </div> -->
    </div>
  </div>

</template>

<style scoped>
/* 整体布局，确保占满全屏 */
.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #409eff;
  color: white;
  padding: 10px 20px;
  height: 60px;
}

.app-logo {
  font-size: 24px;
  font-weight: bold;
}

.app-user {
  display: flex;
  align-items: center;
}

.app-body {
  display: flex;
  flex-grow: 1;
  overflow: hidden;
}

.body-main {}

.app-sidebar {
  background-color: #f5f5f5;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.new-session-button {
  width: 100%;
  margin-bottom: 20px;
}

.app-content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px;
  background-color: #fafafa;
  overflow: hidden;
}

.chat-history {
  flex-grow: 1;
  overflow-y: auto;
  padding-bottom: 20px;
  padding-left: 30px;
  padding-right: 30px;
}

.chat-message {
  /* display: flex; */
  margin-bottom: 5px;
  /* padding-right: 300px;
  padding-left: 300px; */
}

.user-message {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  /* 垂直居中对齐 */
  margin-bottom: 15px;
}

.ai-message {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  /* 垂直居中对齐 */
  margin-bottom: 15px;
}

.user-icon {
  margin-left: 10px;
  width: 40px;
  height: 40px;
}

.ai-icon {
  margin-right: 10px;
}

.user-message span,
.ai-message span {
  background-color: #eef;
  padding: 10px 15px;
  border-radius: 10px;
  max-width: 60%;
  word-wrap: break-word;
  font-size: 15px;
}

.user-message span {
  background-color: #409eff;
  color: white;
}

.ai-message span {
  background-color: #e0e0e0;
}

.chat-input {
  display: flex;
  padding-top: 10px;
  background-color: white;

  /* padding-left: 300px;
  padding-right: 300px; */
}

.input-box {
  flex-grow: 1;
  margin-right: 10px;
}

.send-button {
  min-width: 100px;
}
</style>