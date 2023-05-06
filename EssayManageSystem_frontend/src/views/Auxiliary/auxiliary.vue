<template>
  <div>
    <div class="info">
      <el-divider content-position="left">辅助功能</el-divider>
    </div>
    <el-card class="card-ctrl">
      <template #header>
        <div>
          <el-row>
            <el-button type="primary" size="large" @click="downloadReviewZip">
              <el-icon><download /></el-icon>导出评阅指导意见(.zip)
            </el-button>
            <el-button type="primary" size="large" @click="downloadPaperZip">
              <el-icon><download /></el-icon>导出论文文件(.zip)
            </el-button>
            <el-button size="large" @click="handleEmailTeacher" style="margin-left: auto">
              <el-icon><message/></el-icon>群发邮件提醒老师</el-button>
          </el-row>
        </div>
      </template>

      <div>
        <span>学生项目状态</span>
      </div>
      <el-table :data="projectInfo" stripe class="table" max-height="300px">
        <el-table-column prop="id" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="projName" label="题目" align="center"></el-table-column>
        <el-table-column prop="type" label="状态" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.state === 4" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state === 3" size="mini" type="danger">待指导</el-tag>
            <el-tag v-else-if="scope.row.state === 2" size="mini" type="danger">待评阅</el-tag>
            <el-tag v-else-if="scope.row.state === 1" size="mini" type="danger">待指导/评阅</el-tag>
            <el-tag v-else-if="scope.row.state === 0" size="mini" type="warning">立项</el-tag>
            <el-tag v-else size="mini" type="danger">未知</el-tag>
          </template>
        </el-table-column>
<!--        <el-table-column label="操作" align="center">-->
<!--          <template #default="scope">-->
<!--            <el-tooltip  class="item" effect="dark" content="查看" placement="bottom">-->
<!--              <el-button :disabled="scope.row.state <= 0" circle plain type="primary" size="small" @click="onReview0(scope.$index, scope.row)">-->
<!--                <el-icon><edit /></el-icon>-->
<!--              </el-button>-->
<!--            </el-tooltip>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>

      <div>
        <br/>
        <span>老师信息统计</span>
      </div>
      <el-table :data="teacherInfo" stripe class="table" max-height="300px">
        <el-table-column prop="id" label="工号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="instructorTodoNum" label="指导总数" align="center"></el-table-column>
        <el-table-column prop="instructorDoneNum" label="指导完成" align="center"></el-table-column>
        <el-table-column prop="supervisorTodoNum" label="评阅总数" align="center"></el-table-column>
        <el-table-column prop="supervisorDoneNum" label="评阅完成" align="center"></el-table-column>
        <el-table-column prop="type" label="状态" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.state === 1" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state === 0" size="mini" type="warning">未完成</el-tag>
            <el-tag v-else size="mini" type="danger">未知</el-tag>
          </template>
        </el-table-column>
<!--        <el-table-column label="操作" align="center">-->
<!--          <template #default="scope">-->
<!--            <el-tooltip class="item" effect="dark" content="查看" placement="bottom">-->
<!--              <el-button :disabled="scope.row.state <= 0" circle plain type="primary" size="small" @click="onReview1(scope.$index, scope.row)">-->
<!--                <el-icon><edit /></el-icon>-->
<!--              </el-button>-->
<!--            </el-tooltip>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>

    </el-card>

  </div>
</template>


<script setup lang="ts">
import {ref, onMounted, reactive, computed} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'

import {Edit, Minus, Plus, Refresh,Download,Message} from '@element-plus/icons-vue'
import axios from "axios";
// import RoleEdit from './MatchEdit.vue'

const state = reactive({
  param: {
    limit: 10,
    page: 1
  },
  posted: {
    role: {
      roleName: '',
      state: 1
    }
  },
  total:0,
})

const projectInfo = ref()
const teacherInfo = ref()

const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
const router = useRouter()

onMounted(() => {
  fetchData();
})

const fetchData = async () => {
  // const result = await getList()
  // state.tableData = result.data


  const url1 = `/api/auxiliary/projectInfo/j87y5ndh4sbg678h`
  axios.get(url1).then((resp) => {
    // console.log(resp.data)
    projectInfo.value = resp.data.projectInfo
  })

  const url2 = `/api/auxiliary/teacherInfo/j87y5ndh4sbg678h`
  axios.get(url2).then((resp) => {
    // console.log(resp.data)
    teacherInfo.value = resp.data.teacherInfo
  })



}

const downloadReviewZip =()=>{
  axios({
    method: 'get',
    url: `/api/auxiliary/allReviewZip/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', '所有评阅指导意见.zip')  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
    ElMessage.success("导出完成")
  })
}

const downloadPaperZip =()=>{
  axios({
    method: 'get',
    url: `/api/auxiliary/allPaperZip/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download',  '所有学生论文文件.zip')  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
    ElMessage.success("导出完成")
  })
}

const handleEmailTeacher=()=>{
  const url1 = `/api/auxiliary/mailTeacher/j87y5ndh4sbg678h`
  axios.get(url1).then((resp) => {
    // console.log(resp.data)
    if (resp.data.msg == "success"){
      ElMessage.success("邮件发送成功")
    }else {
      ElMessage.error(resp.data.msg)

    }

  })
}


</script>


