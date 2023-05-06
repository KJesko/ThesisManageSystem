<template>
  <div class="info">
    <el-divider content-position="left">师生配对</el-divider>
  </div>
  <el-card>

    <el-row>
      <el-button type="primary" size="small" @click="onCreate">
        <el-icon><plus /></el-icon>新增
      </el-button>
      <el-button type="danger" size="small" @click="handleDeleteAll">
        <el-icon><delete /></el-icon>全部删除
      </el-button>
      <el-input v-model="search"  placeholder="模糊查找" style="width:200px; margin-left: auto"/>
      <el-button size="large" @click="handleSearch">搜索</el-button>
<!--      <el-button size="large" type="danger" style="margin-left: auto" @click="handleDeleteAll">删除所有</el-button>-->

    </el-row>

    <el-table :data="allMatchInfo" max-height="500" stripe table-layout="fixed" align="center">
      <el-table-column v-if="true" prop="sid" label="学号" align="center"/>
      <el-table-column v-if="true" prop="sname" label="姓名" align="center"/>
      <el-table-column v-if="true" prop="major" label="专业" align="center"/>
      <el-table-column v-if="false" prop="instructorId" label="指导老师工号" align="center"/>
      <el-table-column v-if="true" prop="instructorName" label="指导老师" align="center"/>
      <el-table-column v-if="false" prop="supervisorId" label="评阅老师工号" align="center"/>
      <el-table-column v-if="true" prop="supervisorName" label="评阅老师" align="center"/>

      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-tooltip v-if="$isPermission(['mode:Admin'])" class="item" effect="dark" content="编辑分配关系"
                      placement="bottom">
            <el-button circle plain type="primary" size="small" @click="onEdit(scope.$index, scope.row)">
              <el-icon>
                <edit/>
              </el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip v-if="$isPermission(['mode:Admin'])" class="item" effect="dark" content="删除" placement="bottom">
            <el-button circle plain type="danger" size="small" @click="onDelete(scope.$index, scope.row)">
              <el-icon>
                <minus/>
              </el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
          :current-page="state.param.page"
          :page-size="state.param.limit"
          layout="sizes,prev,pager,next,total"
          :page-sizes="[5, 10, 20]"
          :total="state.total"
          background
          @current-change="onCurrentChange"
          @size-change="onSizeChange"
      >
      </el-pagination>

      <el-divider/>
      <el-row>
        <el-col :span="8" style="text-align: left">
          <span>批量创建师生配对</span>
          <el-upload
              ref="upload"
              class="upload-demo"
              action="/api/user/createMatchByCSV/j87y5ndh4sbg678h"
              :limit="1"
              :on-exceed="handleExceed"
              :on-success="handleSuccess"
              :auto-upload="false"
              accept=".csv"
          >
            <template #trigger>
              <el-button type="primary">选择文件</el-button>
            </template>
            <el-button class="ml-3" type="success" @click="submitUpload">
              生成师生匹配
            </el-button>
            <template #tip>
              <div class="el-upload__tip text-red">
                限制一个.csv文件
              </div>
            </template>
          </el-upload>
        </el-col>
      </el-row>

    </div>

    <el-dialog v-model="state.edit_visible" center title="修改配对关系">
      <match-edit :current-match="state.posted" @success="onEditSuccess"></match-edit>
    </el-dialog>

    <el-dialog v-model="state.new_visible" center title="新增配对关系">
      <match-new @success="onCreateSuccess" @fail="onCreateFail"></match-new>
    </el-dialog>
    <!--    <el-dialog v-model="add_visible" title="新增角色">-->
    <!--      <role-new @success="onCreateSuccess"></role-new>-->
    <!--    </el-dialog>-->


  </el-card>

</template>

<script setup lang="ts">
import {ref, onMounted, reactive} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {genFileId} from 'element-plus'
import type {UploadInstance, UploadProps, UploadRawFile} from 'element-plus'
import {Edit, Minus, Plus, Refresh,Delete} from '@element-plus/icons-vue'
import axios from "axios";
import MatchEdit from './MatchEdit.vue'
import MatchNew from './MatchNew.vue'

const route = useRoute()
const router = useRouter()
const upload = ref<UploadInstance>()
let allMatchInfo = ref([]);
const search = ref('');
// let edit_visible =ref(false);
const state = reactive({
  edit_visible: false,
  new_visible: false,
  param: {
    limit: 10,
    page: 1
  },
  posted: {
    match: {}
  },
  total: 0,
})


onMounted(() => {
  fetchData()
})

const onCurrentChange = (val: number) => {
  state.param.page = val
  fetchData()
}

const onSizeChange = (val: number) => {
  state.param.limit = val
  fetchData()
}

const fetchData = () => {
  console.log("fetch data")
  const url1 = `/api/user/allMatchInfo/${state.param.page}/${state.param.limit}/j87y5ndh4sbg678h`
  axios.get(url1).then((resp) => {
    // console.log(resp.data)
    allMatchInfo.value = resp.data;
  })
  const url2 = `/api/user/allMatchInfo/count/j87y5ndh4sbg678h`
  axios.get(url2).then((resp) => {
    // console.log(resp.data)
    state.total = resp.data
  })
}

const onEdit = (index: any, row: any) => {
  console.log('row', row)
  // alert("right here")
  state.posted.match = row
  state.edit_visible = true
}

const onEditSuccess = () => {
  ElMessage({
    type: 'success',
    message: '修改完成',
  })
  // alert("修改成功")
  state.edit_visible = false
  fetchData()
}

const onCreate = (index: any, row: any) => {
  console.log('row', row)
  // alert("right here")
  state.posted.match = row
  state.new_visible = true
}

const onCreateSuccess = () => {
  ElMessage({
    type: 'success',
    message: '新建配对完成',
  })
  // alert("修改成功")
  state.new_visible = false
  fetchData()
}

const onCreateFail = () => {
  state.new_visible = false
  fetchData()
}

const useConfirmDelete = (row: any) => {
  console.log(row)
  ElMessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(() => {
        const url = `/api/user/deleteMatch/${row.sid}/j87y5ndh4sbg678h`
        axios.get(url).then((resp) => {
          // console.log(resp.data)
          if (resp.data.msg == "success") {
            ElMessage({
              type: 'success',
              message: '删除成功'
            })
          } else if (resp.data.msg == "fail") {
            ElMessage({
              type: 'info',
              message: '删除失败'
            })
          }
        })


        // 此处执行接口异步删除角色

      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除'
        })
      }).finally(() => {
    fetchData();
  })
}

const onDelete = (index: any, row: any) => {
  console.log(index, row)
  useConfirmDelete(row)
}


const handleSearch = () => {
  const url = `/api/user/MatchSearch/j87y5ndh4sbg678h`
  const formData = new FormData()
  if (search.value == null) {
    search.value = ""
  }
  formData.append("searchString", search.value);
  formData.append("number", state.param.page.toString())
  formData.append("size", state.param.limit.toString())
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    console.log(resp.data)
    allMatchInfo.value = resp.data;
    state.total = allMatchInfo.value.length
  })
}

const handleEdit = (index, row) => {
  console.log(row.sid);
}

const handleDeleteAll = () => {
  const url = `/api/user/deleteAllMatch/j87y5ndh4sbg678h`
  ElMessageBox.confirm(
      '确认要这么做吗？这会删除所有匹配数据',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        axios.get(url).then((resp: { data: any; }) => {
          // console.log(resp.data)
          allMatchInfo.value = []
          ElMessage({
            type: 'success',
            message: '删除完成',
          })
          fetchData()
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消删除',
        })
      })

}

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}

const handleSuccess: UploadProps['onSuccess'] = (response) => {

  ElMessage.success("关系创建成功")
  upload.value!.clearFiles()

  // console.log(response)
  fetchData();

}

const submitUpload = () => {
  ElMessageBox.confirm(
      '如果学生老师关系已经存在，那么将被新的文件中的关系覆盖，确定要执行吗？',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        upload.value!.submit()
        ElMessage({
          type: 'success',
          message: '上传成功',
        })
      })

      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消上传',
        })
      })


}
</script>
