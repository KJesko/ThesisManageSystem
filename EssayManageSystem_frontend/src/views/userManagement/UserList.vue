<template>
  <div>
    <div class="info">
      <el-divider content-position="left">用户信息</el-divider>
    </div>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="8" style="text-align: left">
          <el-button type="primary" size="small" @click="onCreate">
            <el-icon><plus /></el-icon>新增
          </el-button>
<!--          <el-button type="success" size="small" @click="onRefresh">-->
<!--            <el-icon><refresh /></el-icon>-->
<!--            刷新-->
<!--          </el-button>-->
          <el-button type="danger" size="small" @click="deleteAllUser">
            <el-icon><delete /></el-icon>
            全部删除
          </el-button>
        </el-col>
      </el-row>
      <br />
      <el-table :data="allUserShow" stripe class="table">
        <el-table-column prop="uid" label="学号/工号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="type" label="身份" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.type == 2" size="mini">管理员</el-tag>
            <el-tag v-else-if="scope.row.type == 1" size="mini" type="success">老师</el-tag>
            <el-tag v-else-if="scope.row.type == 0" size="mini" type="warning">学生</el-tag>
            <el-tag v-else size="mini" type="danger">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pwd" label="密码" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip v-if="scope.row.uid != 10000001" class="item" effect="dark" content="编辑" placement="bottom">
              <el-button circle plain type="primary" size="small" @click="onEdit(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.uid != 10000001" class="item" effect="dark" content="删除" placement="bottom">
              <el-button circle plain type="danger" size="small" @click="onDelete(scope.$index, scope.row)">
                <el-icon><minus /></el-icon>
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
      </div>
      <el-divider/>
      <el-row>

        <el-col :span="8" style="text-align: left">
          <div>
            <span>批量生成用户</span>
          </div>
          <el-upload
              ref="upload"
              class="upload-demo"
              action="/api/user/createUserByCSV/j87y5ndh4sbg678h"
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
              生成用户
            </el-button>
            <template #tip>
              <div class="el-upload__tip text-red">
                限制一个.csv文件
              </div>
            </template>
          </el-upload>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog v-model="state.edit_visible" center title="修改用户信息">
      <user-edit :current-user="state.posted" @success="onEditSuccess"></user-edit>
    </el-dialog>

    <el-dialog v-model="state.new_visible" center title="新增用户">
      <user-new  @success="onCreateSuccess" @fail="onCreateFail"></user-new>
    </el-dialog>


  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, reactive, computed} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {genFileId} from 'element-plus'
import type {UploadInstance, UploadProps, UploadRawFile} from 'element-plus'
import {Edit, Minus, Plus, Refresh,Delete} from '@element-plus/icons-vue'
import axios from "axios";
import UserEdit from './userEdit.vue'
import UserNew from './userNew.vue'
import request from "@/utils/request";
import Service from './api/index'
import {Serializer} from "v8";

const state = reactive({
  edit_visible: false,
  new_visible: false,
  param: {
    limit: 10,
    page: 1
  },
  posted: {
    user: {},
  },
  total:0,
})


const allUserShow = ref([])
const upload = ref<UploadInstance>()

onMounted(() => {
  fetchData();
})

const fetchData = () => {
  console.log("userList fetch data");
  Service.postUserListPage(state.param).then((resp: any)=> {
    // 异步操作成功时的回调函数
    allUserShow.value = resp
    console.log(resp); // 输出异步操作的结果
  }).catch((error: any) =>{
    // 异步操作失败时的回调函数
    console.error(error); // 输出错误信息
  });


  const url = `/api/user/allUserCount/j87y5ndh4sbg678h`
  axios.get(url).then((resp) => {
    // console.log(resp.data)
    state.total = resp.data;
  })

}

const onCurrentChange = (val: number) => {
  state.param.page = val
  fetchData()
}

const onSizeChange = (val: number) => {
  state.param.limit = val
  fetchData()
}

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}

const handleSuccess: UploadProps['onSuccess'] = (response) => {
  if (response.message == "success"){
    ElMessage.success("用户批量创建成功")

  }else {
    ElMessage.success("用户批量创建失败")
  }
  upload.value!.clearFiles()
  fetchData();
  // console.log(response);
  // allUser.value = response;

}

const submitUpload = () => {
  ElMessageBox.confirm(
      '如果用户已经存在，那么将被新的文件中的关系覆盖，确定要执行吗？',
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

const onDelete =  async (index: any, row: any) => {
  console.log(index, row)
  await useConfirmDelete(row.uid)
  await fetchData();
}

const useConfirmDelete = (uid: any) => {
  console.log(uid);
  return new Promise<void>((resolve, reject) => {
    ElMessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
        .then(() => {
          // 此处执行接口异步删除角色
          const url = `/api/user/deleteUser/${uid}/j87y5ndh4sbg678h`;
          axios.get(url).then((resp) => {
            // console.log(resp.data);
            ElMessage({
              type: 'success',
              message: '删除成功'
            });
            resolve(); // resolve Promise
          });
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消删除'
          });
          reject(); // reject Promise
        });
  });
};

const onEdit = (index: any, row: any) => {
  console.log('row', row)
  // alert("right here")
  state.posted.user = row
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


const deleteAllUser=()=>{
  ElMessageBox.confirm('此操作将永久删除用户数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(() => {
        // 此处执行接口异步删除角色
        const url = `/api/user/deleteAllUser/j87y5ndh4sbg678h`
        axios.get(url).then((resp) => {
          // console.log(resp.data)
        })
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        fetchData();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除'
        })
      })

}



</script>

