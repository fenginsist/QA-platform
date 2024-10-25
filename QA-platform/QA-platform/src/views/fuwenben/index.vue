<script setup>
import Editor from '@tinymce/tinymce-vue'

</script>
<script>
export default {
  name: 'FuWebBen',
  data() {
    return {
      showFloatMenu: false,           // 浮动框是否显示
      selectedText: '',               // 选中的文本
      position: { top: 0, left: 0 },  // 浮动框的位置
      init_config: {
        toolbar_mode: 'sliding',
        width: 1500,
        height: 1000,
        plugins: [
          // Core editing features
          'anchor', 'autolink', 'charmap', 'codesample', 'emoticons', 'image', 'link', 'lists', 'media', 'searchreplace', 'table', 'visualblocks', 'wordcount',
          // Your account includes a free trial of TinyMCE premium features
          // Try the most popular premium features until Oct 14, 2024:
          'checklist', 'mediaembed', 'casechange', 'export', 'formatpainter', 'pageembed', 'a11ychecker', 'tinymcespellchecker', 'permanentpen', 'powerpaste',
          'advtable', 'advcode', 'editimage', 'advtemplate', 'mentions', 'tinycomments', 'tableofcontents', 'footnotes', 'mergetags', 'autocorrect',
          // 'ai', // 这是高级用法，收费
          'typography', 'inlinecss', 'markdown',
        ],
        toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
        tinycomments_mode: 'embedded',
        tinycomments_author: 'Author name',
        mergetags_list: [
          { value: 'First.Name', title: 'First Name' },
          { value: 'Email', title: 'Email' },
        ],
        // ai_request: (request, respondWith) => respondWith.string(() => Promise.reject('See docs to implement AI Assistant')),  // 和plugins 中的ai插件一起使用的
        setup: (editor) => {
          // 在编辑器中监听鼠标松开事件
          editor.on('MouseUp', (e) => this.handleMouseUp(e, editor));
        },
      },
      init_value: `天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天天
      秋天，带着一丝凉意，悄然走进了我们的生活。它不像春天那样充满生机与活力，也不似夏天那样炙热难耐，更不同于冬天的冷冽刺骨。秋天是一幅静谧而温暖的画卷，色彩斑斓，恰到好处。\n
在这个季节里，万物开始褪去夏日的繁华，树叶渐渐变黄，像是披上了一层金色的外衣。走在林间小道上，踩着落叶，沙沙的声响仿佛在诉说着夏天的故事。那一片片枯黄的叶子，像是秋天的信使，告诉我们季节的更替与时间的流逝。
田野里，稻谷一片金黄，沉甸甸的穗头低垂着，仿佛在向人们敬礼。农民们忙碌的身影在田间穿梭，笑声与欢呼声交织在一起，构成了秋天丰收的乐章。每一粒稻谷都是他们辛勤劳动的结晶，每一片金黄都是对生活的热爱与期待。收获的季节，总是充满希望，令人感到无比的满足。
秋天的天空，湛蓝而高远，云朵轻轻飘荡，像是被风儿牵引着，随意而又自在。阳光透过云层洒下，温暖而不刺眼，给大地披上了一层柔和的光辉。这个时候，抬头仰望天空，心中总会涌起一股宁静的力量，让人不由自主地放慢脚步，享受这份独特的秋日阳光。
在这个季节里，果树也开始成熟，苹果、梨子、柿子挂满了枝头，仿佛在邀请人们品尝大自然的馈赠。果香四溢，沁人心脾，让人忍不住想要一尝为快。走进果园，满眼的丰收让人心旷神怡，似乎每一个果实都在述说着秋天的故事，传递着生命的喜悦。
秋天的夜晚，星空璀璨，月亮柔和。微风轻拂，带着一丝凉意，令人感到无比舒适。此时，坐在院子里，仰望星空，思绪会随着星星的闪烁而飘荡。这个时候，心灵变得格外宁静，似乎能够听到内心最真实的声音。秋天的夜，既神秘又温暖，让人感受到一种无法言喻的安详。
在这个季节里，许多节日也相继而至。中秋节是秋天最具代表性的节日之一。家人团聚，共赏明月，品尝月饼，传递着温情与祝福。那一轮明月，象征着团圆与美好，照亮了每一个人的心田。这个时候，人们在感慨岁月流逝的同时，也更加珍惜与家人团聚的时光。
秋天不仅仅是丰收的季节，它更是一段回忆的旅程。走过的每一条小路，见过的每一片风景，都在心中留下了深刻的印记。那些曾经的欢笑与泪水，都会在这个季节里重新浮现。秋天，让人感受到生活的美好与温暖，也让人思考生命的意义与价值。
随着秋天的脚步渐渐远去，冬天即将来临。尽管寒冷的季节即将到来，但秋天的温暖会永远留存在我们的心中。它教会我们珍惜当下，感悟生活的每一份美好。不论未来的路有多么坎坷，秋天的记忆将始终伴随着我们，成为心灵深处最温暖的力量。
秋天，一个充满诗意的季节，让人留恋，让人思索。在这段美好的时光里，让我们用心去感受、去享受，去记录下这份独特的温暖。`,
      messages: [],  // 存储接收到的LLM输出
      question: '',
      answer: '',
      generatingMessage: {
        message: '',       // 用于存储生成的消息内容
        completed: false,  // 标记消息是否生成完成
        createTime: '',    // 存储消息的创建时间
        generating: false, // 标记是否正在生成
      },
      currentSessionId: null, // 当前会话 ID（如果需要）
      unReponse: true,        // 控制状态的标志
    }
  },
  mounted() {
  },
  methods: {
    // 
    handleSelectionChange(e, editor) {
      // 监听文本选择使用
      // console.log('监听到了。。。。。。。。。。。。。。。')
      // console.log('e:', JSON.stringify(e))
      // console.log('editor:', editor)
      // const selection = editor.selection.getContent({ format: 'text' });  // 获取鼠标选中的内容
      // console.log('selection:', JSON.stringify(selection))
      // if (selection) {
      //   console.log('选中了。。。。。。。。。。。。。。。')
      //   this.selectedText = selection;
      //   const { x, y } = editor.selection.getBoundingClientRect();
      //   console.log('x,y:', x, y)
      //   console.log('window.scrollX,window.scrollY:', window.scrollX, window.scrollY)
      //   this.position = { top: y + window.scrollY + 20, left: x + window.scrollX };
      //   this.showFloatMenu = true;
      // } else {
      //   this.showFloatMenu = false;
      // }
    },
    handleMouseUp(e, editor) {
      console.log('鼠标监听到了。。。。。。。。。。。。。。。')
      console.log('e:', e)
      console.log('e中的: client, page, screen, XY:', e.clientX, e.clientY, e.pageX, e.pageY, e.screenX, e.screenY, e.x, e.y)
      console.log('editor:', editor)
      const selection = editor.selection.getContent({ format: 'text' });
      // 当有选中的文字时才显示浮动框
      if (selection) {
        this.selectedText = selection;
        const { x, y, width, height } = editor.selection.getBoundingClientRect();
        console.log('getBoundingClientRect(): x,y,width,height,scrollXY::', x, y, width, height, window.scrollX, window.scrollY)
        // 获取鼠标松开时的坐标
        const mouseX = e.screenX;
        const mouseY = e.screenY;
        // 设置浮动框的位置，基于鼠标的坐标
        this.position = {
          top: mouseY + window.scrollY - 100, // 确保浮动框在鼠标下方
          left: mouseX + window.scrollX,
        };
        this.showFloatMenu = true;
      } else {
        this.showFloatMenu = false;
      }
    },
    rewrite() {

    },
    expand() {

    },
    abbreviate() {

    },
    polish() {

    },
    customize() {

    },
  }
}
</script>

<template>
  <main id="sample">
    <Editor api-key="m531bc2dcc8b7xx2tabn49x2y0q0dsjc64s5em6vtuctpaip" :init=init_config
      v-on:selectionChange="handleSelectionChange" :initial-value=init_value />

    <div v-if="showFloatMenu" :style="{ top: position.top + 'px', left: position.left + 'px' }" class="floating-box">
      <el-button type="primary" plain @click="rewrite">
        <el-icon :size="20">
          <Edit />
        </el-icon>
        <span>改写</span>
      </el-button>
      <el-button type="primary" plain @click="expand">
        <el-icon :size="20">
          <Expand />
        </el-icon>
        <span>扩写</span>
      </el-button>
      <el-button type="primary" plain @click="abbreviate">
        <el-icon :size="20">
          <Fold />
        </el-icon>
        <span>缩写</span>
      </el-button>
      <el-button type="primary" plain @click="polish">
        <el-icon :size="20">
          <EditPen />
        </el-icon>
        <span>润色</span>
      </el-button>
      <el-button type="primary" plain @click="customize">
        <el-icon :size="20">
          <Search />
        </el-icon>
        <span>自定义</span>
      </el-button>
    </div>
  </main>
</template>

<style scoped>
/* 浮动框 */
.floating-box {
  position: absolute;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  /* 圆角 */
  padding: 8px 10px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  /* 阴影效果 */
  display: flex;
  gap: 10px;
  z-index: 1000;
  animation: fadeIn 0.3s ease-in-out;
  /* 动画效果 */
}

.floating-box el-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  /* 按钮圆角 */
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.floating-box el-button:hover {
  background-color: #45a049;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
