<!--
 * @Author: GeekQiaQia
 * @Date: 2022-02-18 16:13:43
 * @LastEditTime: 2022-04-28 11:14:35
 * @LastEditors: GeekQiaQia
 * @Description:
 * @FilePath: /github/vue3.0-template-admin/src/App.vue
-->
<template>
  <el-config-provider :locale="locale">
    <div id="app">
      <router-view :key="this.$route.path"></router-view>
    </div>
  </el-config-provider>
</template>

<script setup lang="ts">
import { ElConfigProvider } from 'element-plus'
import zhLocale from 'element-plus/lib/locale/lang/zh-cn'
import enLocale from 'element-plus/lib/locale/lang/en'
import { computed, onMounted } from 'vue'
import { useStore } from '@/store/index'

const store = useStore()
const resizeHeight = () => {
  const { clientHeight } = document.body // 获取文档可视区域的宽度
  const height = Math.max(600, clientHeight - 170) // 保证最小值大于600
  store.commit('settingsModule/setTableHeight', height) // 设置tableHeight
}
onMounted(() => {
  const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
  store.dispatch('permissionModule/getPermissions', userinfo.data)
})
resizeHeight()
const locale = computed(() => {
  const langState = store.getters['settingsModule/getLangState']
  const local = langState === '/zh-CN' ? zhLocale : enLocale
  return local
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.element-plus-logo {
  width: 50%;
}
.el-table .el-table__body td .cell:empty::after,
.el-table .el-table__body td .cell > span:empty::after,
.el-table .el-table__body td .cell .approval:empty::after,
.el-table .el-table__body td .cell div:empty::after,
.el-table .el-table__body td .cell .line-limit-length:empty::after{
  content: '--';
}
</style>
