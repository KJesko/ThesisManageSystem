<template>
  <div>
    <div class="info">
      <el-divider content-position="left">学生信息</el-divider>
    </div>
    <el-card class="card-ctrl">
      <div>
        <span>指导的学生</span>
      </div>
      <el-table :data="studentForInstructor" stripe class="table" max-height="300px">
        <el-table-column prop="sid" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="projName" label="题目" align="center"></el-table-column>
        <el-table-column prop="type" label="状态" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.state == 4" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state == 3" size="mini" type="danger">待指导</el-tag>
            <el-tag v-else-if="scope.row.state == 2" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state == 1" size="mini" type="danger">待指导</el-tag>
            <el-tag v-else-if="scope.row.state == 0" size="mini" type="warning">立项</el-tag>
            <el-tag v-else size="mini" type="danger">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip  class="item" effect="dark" content="查看" placement="bottom">
              <el-button :disabled="scope.row.state <= 0" circle plain type="primary" size="small" @click="onReview0(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div>
        <br/>
        <span>分配的学生</span>
      </div>
      <el-table :data="studentForSupervisor" stripe class="table" max-height="300px">
        <el-table-column prop="sid" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="projName" label="题目" align="center"></el-table-column>
        <el-table-column prop="type" label="状态" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.state == 4" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state == 3" size="mini" type="success">完成</el-tag>
            <el-tag v-else-if="scope.row.state == 2" size="mini" type="danger">待评阅</el-tag>
            <el-tag v-else-if="scope.row.state == 1" size="mini" type="danger">待评阅</el-tag>
            <el-tag v-else-if="scope.row.state == 0" size="mini" type="warning">立项</el-tag>
            <el-tag v-else size="mini" type="danger">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip class="item" effect="dark" content="查看" placement="bottom">
              <el-button :disabled="scope.row.state <= 0" circle plain type="primary" size="small" @click="onReview1(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

  </div>
</template>


<script setup lang="ts">
import {ref, onMounted, reactive, computed} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {genFileId} from 'element-plus'
import type {UploadInstance, UploadProps, UploadRawFile} from 'element-plus'
import {Edit, Minus, Plus, Refresh} from '@element-plus/icons-vue'
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

const studentForInstructor = ref()
const studentForSupervisor = ref()

const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
const router = useRouter()

onMounted(() => {
  fetchData();
})

const fetchData = async () => {
  // const result = await getList()
  // state.tableData = result.data


  const url1 = `/api/review/studentForInstructor/${userinfo.uid}/`
  axios.get(url1).then((resp) => {
    // console.log(resp.data)
     studentForInstructor.value = resp.data
  })

  const url2 = `/api/review/studentForSupervisor/${userinfo.uid}/`
  axios.get(url2).then((resp) => {
    // console.log(resp.data)
    studentForSupervisor.value = resp.data
  })



}



const onReview0 = (index: any, row: any) => {
  console.log('row', row)
  router.push({
    path:'./reviewPaper',
    query:{
      num:0,
      sid: row.sid,
      name: row.name,
    }
  })
}
const onReview1 = (index: any, row: any) => {
  console.log('row', row)
  router.push({
    path:'./reviewPaper',
    query:{
      num:1,
      sid: row.sid,
      name: row.name,
    }
  })
}

</script>


