<script setup>
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getllmHr } from '@/api/index.js'     // @microsoft這是包，這裡需要加 @/
import person_head from '@/assets/icon/person_head.png'
import cvicse_logo from '@/assets/icon/cvicse-logo.png'
</script>
<script>
import MarkdownIt from "markdown-it";
const mdi = new MarkdownIt({
  html: false,
  linkify: true,
});
export default {
  name: 'HR',
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
    getCurrentDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 月份是从0开始的，所以需要+1
      const day = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    addMessage(message) {
      this.messages.push(message)
    },
    sendMessage() {
      // console.log(this.getCurrentDateTime())
      this.addMessage({ type: 'user', text: this.userInput, time: this.getCurrentDateTime() })
      // 检查消息是否为空
      if (this.userInput.trim() === '' || this.userInput.length == 0) {
        this.addMessage({ type: 'ai', text: '问题不可为空', time: this.getCurrentDateTime() })
        this.scrollToBottom()
        return;
      }
      // this.submitQuestionNoStream()
      this.submitQuestionRAGStream()
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
    // RAG 流式输出
    submitQuestionRAGStream() {
      this.isLoading = true; // 开始加载
      // 向后端发请求。
      let params = {
        question: this.userInput
      }
      let _this = this
      let flag = true; // 仅执行一次
      let URL = '/chat/hr/questionStream?question=' + this.userInput
      let startTime = performance.now();// 获取开始时间
      let endTime;
      fetchEventSource(URL, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'sss',
          'Cache-Control': 'no-cache'
        },
        openWhenHidden: true,  // 禁止在页面隐藏时自动重连
        retryInterval: 0,       // 禁用自动重连
        retry: false,
        // 如果需要发送请求体，可以在这里设置
        // body: JSON.stringify(params),

        // 处理接收到的消息
        onmessage(ev) {
          // console.log('ssssssssss: ', ev)
          let content = ev.data
          // console.log('content: ', content)
          if (flag) {  // 仅添加一次，后面：只在数组的最后一个元素进行增量赋值即可
            if (content.trim() === '[END]') { // 防止出现异常
              _this.addMessage({ type: 'ai', text: '服务器出现异常请联系管理员：冯凡利！！！', time: _this.getCurrentDateTime() })
              return
            }
            _this.addMessage({ type: 'ai', text: '', time: _this.getCurrentDateTime() })
            _this.isLoading = false; // 关闭加载
            flag = false;
          }
          if (content.trim() === '[END]') {
            return
          }
          _this.generatingMessage.message += content
          _this.messages[_this.messages.length - 1].text = mdi.render(_this.generatingMessage.message) // _this.generatingMessage.message // mdi.render(_this.generatingMessage.message)

          _this.$nextTick(() => {
            _this.scrollToBottom();
          });

          if (_this.unReponse) {
            if (!_this.currentSessionId) {
              _this.loadSessions();
            }
            const now = new Date();
            const dateString = now.toLocaleString();
            _this.generatingMessage.createTime = dateString;
            _this.generatingMessage.generating = false;
            _this.unReponse = false
          }
        },
        // 处理错误
        onerror(error) {
          console.error('Error:', error);
          _this.isLoading = false; // 关闭加载
          _this.addMessage({ type: 'ai', text: '服务器出现异常请联系管理员：冯凡利！！！', time: _this.getCurrentDateTime() })
          // console.log('all message: ', _this.messages)
          return
        },
        // 连接打开时的回调
        onopen(response) {
          _this.isLoading = false; // 关闭加载
          endTime = performance.now();// 获取结束时间
          console.log(`fetchEventSource Execution time: ${(endTime - startTime) / 1000} seconds`); // 计算并输出执行时间（转换为秒）
          if (!response.ok) {
            _this.addMessage({ type: 'ai', text: '服务器出现异常请联系管理员：冯凡利！！！', time: _this.getCurrentDateTime() })
            // throw new Error(`HTTP error! status: ${response.status}`);
          }
          console.log('Connection opened!first launch this:');
          // console.log(response);
        },
        // 连接关闭时的回调
        onclose() {
          _this.isLoading = false; // 关闭加载
          _this.generatingMessage.message = '';
          _this.generatingMessage.completed = false;
          _this.generatingMessage.createTime = '';
          _this.generatingMessage.generating = false;
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
      this.isLoading = true; // 开始加载
      getllmHr(this.userInput).then(res => {
        console.log('response success!!!!!!!!!!!!!!!!!!!!!!')
        console.log('aaaa:', res)

        this.addMessage({ type: 'ai', text: res.answer, time: this.getCurrentDateTime() })
        this.isLoading = false;
        // this.answer = res.answer
      }).catch(error => {
        console.error('请求失败，发生错误:', JSON.stringify(error));
        // 你可以根据实际需求对错误进行进一步处理
        this.messages.push({ type: 'ai', text: 'AI 回答失败，请刷新后稍后重试。' })
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
    loadSessions() {

    }
  }
}
</script>

<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="app-logo">中创集团人力资源问答小助手</div>
      <div class="app-user">
        <el-avatar :src="person_head"></el-avatar>
      </div>
    </el-header>

    <!-- 左侧菜单和右侧内容 -->
    <div class="app-body">
      <!-- 左侧菜单栏 -->
      <el-aside width="240px" class="app-sidebar">
        <el-button type="primary" icon="el-icon-plus" @click="newSession" class="new-session-button">新建会话</el-button>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-main class="app-content">
        <!-- 问答历史展示区 -->
        <el-scrollbar class="chat-history" ref="scrollbarRef" :always="always" :min-size="scrollbar">
          <!-- 提示区域 -->
          <div style="padding: 0px 80px 0 68px; margin-bottom: 10px;">
            <div class="help-container">
              <div class="help-title">您好，请在对话过程中留意以下事项：</div>
              <div class="help-content">温馨提示：</div>
              <div class="help-content">目前知识库涉及: <span style="font-weight: bolder;">离职、用印、考勤及休假</span> 的知识。请尽量询问与之相关的内容, 后续开放其他知识内容</div>
              <div class="help-content">目前还在该问题系统还在开发/优化中, 响应时间较长, <span style="color: red;">请耐心等待, 请AI回答后再问下一个问题。</span></div>
              <div class="help-content">历史记录、新建会话等功能后续优化持续推出, 请各位同事多多期待！！！</div>
              <!-- <div class="help-content">在不同主题内容下新建独立对话, 以减少单轮对话中的交流次数, 目前新建对话功能暂未实现, 敬请期待 </div> -->
              <!-- <div class="help-content">所有的对话内容都将被记录且无法被删除，请合理规范地使用</div> -->
              <div class="help-content">提示: ENTER 回车键发送, ENTER + SHIFT 组合键 换行</div>
            </div>
          </div>
          <!-- 循环遍历 -->
          <div v-for="(message, index) in messages" :key="index" :class="['chat-message', message.type]">
            <template v-if="message.type === 'user'">
              <div>
                <div class="user-message">
                  <span>{{ message.text }}</span>
                  <el-avatar :src="person_head" class="user-icon"></el-avatar>
                </div>
                <!-- 显示时间 -->
                <div style="">
                  <div style="text-align: right; font-size: 10px; padding-right: 50px;">
                    <el-icon>
                      <Timer />
                    </el-icon>
                    {{ message.time }}
                  </div>
                </div>
              </div>
            </template>
            <template v-else>
              <div>
                <div class="ai-message">
                  <el-avatar :src="cvicse_logo" icon="el-icon-robot" class="ai-icon"></el-avatar>
                  <text v-html="message.text"></text>
                </div>
                <!-- 显示时间 -->
                <div style="">
                  <div style="text-align: left; font-size: 10px; padding-left: 50px;">
                    <el-icon>
                      <Timer />
                    </el-icon>
                    {{ message.time }}
                  </div>
                </div>
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
          <el-input v-model="userInput" class="input-box" :autosize="{ minRows: 4, maxRows: 8 }" type="textarea"
            placeholder="请输入您的问题..." @keydown="handleKeyDown" />
          <!-- @keydown.enter="sendMessage"：监听单独按下 Enter 键，进行发送 
                 @keydown="handleKeyDown": 监控所有事件
            -->
          <el-button type="primary" icon="el-icon-send" @click="sendMessage" class="send-button">
            发送
          </el-button>
        </div>
      </el-main>
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
  /* margin-right: 10px; */
}

.user-message {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  /* 垂直居中对齐 */
  /* margin-bottom: 15px; */
}

.ai-message {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  /* 垂直居中对齐 */
  /* margin-bottom: 15px; */
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
}

.input-box {
  flex-grow: 1;
  margin-right: 10px;
}

.send-button {
  min-width: 100px;
}

/* 帮助词 */
.help-container {
  width: 100%;
  height: auto;
  background-color: #e2eeff;
  padding: 10px;
  border-radius: 10px;
}

.help-title {
  color: #4257e9;
  font-size: 24px;
  font-weight: 700;
  padding: 10px 0 10px 20px;
}

.help-content {
  color: #606266;
  font-size: 14px;
  padding: 5px 0 5px 20px;
  ;
}
</style>