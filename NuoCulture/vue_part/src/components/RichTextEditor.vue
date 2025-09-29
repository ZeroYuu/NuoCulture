<template>
  <div class="editor-wrapper">
    <Toolbar v-if="showToolbarFlag" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode"
      class="editor-toolbar-wrapper" />
    <Editor v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode" @onCreated="handleCreated"
      @onChange="handleChange" :style="{ height: editorHeight}" :readOnly="readOnlyFlag"
      class="editor-content-wrapper" />
  </div>
</template>

<script lang="ts" setup>
import { watch, onBeforeUnmount, nextTick, ref, shallowRef, onMounted, onBeforeMount } from 'vue'
//@ts-ignore
import { Editor, Toolbar, SlateElement } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css' // 引入 css


// Props：使用属性，子组件接收父组件传递的内容
const props = defineProps({
  // 内容
  content: { type: String, default: '' },
  // 工具栏是否显示，默认显示
  showToolbarFlag: { type: Boolean, default: true },
  // 编辑器高度，默认500px
  editorHeight: { type: String, default: '230px' },
  // 编辑器是否只读，默认可编辑
  readOnlyFlag: { type: Boolean, default: false }
})

// Emits：使用事件，将子组件内容传递给父组件。父组件使用 update(content: string) 
const emit = defineEmits<{ (e: 'update', content: string): void }>()

const mode = ref('default')

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('')

const toolbarConfig = {}

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {} as any,
}


// 上传图片配置
editorConfig.MENU_CONF['uploadImage'] = {
  // form-data fieldName ，默认值 'wangeditor-uploaded-image'。传给后端接口的参数名，重要!
  fieldName: 'file',
  server: 'http://localhost:8090/api/msg/uploadRichText',
}

// 上传视频配置
editorConfig.MENU_CONF['uploadVideo'] = {
  fieldName: 'file', // 假设后端接口期望的参数名为 'videoFile'
  maxFileSize: 100 * 1024 * 1024, // 最大单个文件大小，单位为字节
  server: 'http://localhost:8090/api/msg/uploadRichText', // 上传视频的后端接口地址

  // 自定义插入视频（如果需要处理特殊响应格式）
  customInsert(res: any, insertFn: (url: string) => void) {
    // 假设响应格式为 { data: { url: string } }
    console.log('上传视频的响应:', res.data);
    const url = res.data[0].url
    if (url) insertFn(url)
  },

  // 错误处理
  onError(error: Error, file: File) {
    if (error.message.includes('exceeds maxFileSize')) {
      alert(`文件大小不能超过 ${editorConfig.MENU_CONF!['uploadVideo']!.maxFileSize! / 1024 / 1024}MB`)
    } else {
      console.error('上传错误:', error)
    }
  }
};


const handleCreated = (editor: any) => {
  editorRef.value = editor // 记录 editor 实例，重要！

  // 根据父组件传递的readOnlyFlag，设置编辑器为只读
  if (props.readOnlyFlag) {
    editorRef.value.disable();
  } else {
    editorRef.value.enable();
  }

}

const handleChange = () => {
  valueHtml.value = editorRef.value.getHtml()
  emit('update', valueHtml.value)
}

// 监听 props 变化，监听父组件传来的content
watch(() => props.content, (newVal: string) => {
  nextTick(() => {
    if (props.content !== '' && editorRef.value != null) {
      // console.log("props.content",props.content);
      // console.log(" 当前编辑器的状态：", editorRef.value); 

      // 富文本编辑器按 html 格式回显
      editorRef.value.setHtml(newVal)
      valueHtml.value = newVal
    }
  })
})

onMounted(async () => {
  await nextTick(); // 延迟渲染，确保 DOM 更新完成
  if (props.content) {
    valueHtml.value = props.content
  }
})

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
  console.log('销毁编辑器');
})


</script>

<style lang="scss">
.editor-wrapper {
  border: 1px solid #dcdfe6;
  // max-width: 1050px;

  .editor-toolbar-wrapper {
    border-bottom: 1px solid #dcdfe6;
  }

  .editor-content-wrapper {
    overflow: auto;
  }
}
</style>
<style>
.editor-content-wrapper video,.editor-content-wrapper img{
    max-width: 100%;
  }
</style>