<template xmlns="http://www.w3.org/1999/html">
  <div class="info">
    <el-divider content-position="left">论文评阅</el-divider>
  </div>

  <el-card>
    <template #header>
      <div>
        <el-form label-position="left" inline>
          <el-form-item label="学号:">
            <span>{{ sid }}</span>
          </el-form-item>
          <el-form-item label="姓名:">
            <span>{{ name }}</span>
          </el-form-item>
          <el-form-item label="指导老师:">
            <span>{{ instructorName }}</span>
          </el-form-item>
          <el-form-item label="评阅老师:">
            <span>{{ supervisorName }}</span>
          </el-form-item>

          <el-row>
            <el-col :span="18">
              <el-form-item label="论文题目" style="display: flex; align-items: center;">
                <span style="margin-right: 0px;">{{ title }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="downloadZip" style="float: right;">下载zip(所有文件)</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>


    </template>

    <el-container>
      <el-table :data="essayFile" size="small" style="width: 100%" table-layout="fixed">
        <el-table-column prop="name" label="论文文件" style="width: 80%" align="center"></el-table-column>
        <el-table-column align="right">
          <el-button size="small" type="success" @click="downloadEssay">下载</el-button>
        </el-table-column>
      </el-table>
    </el-container>

    <!--    <el-divider/>-->
    <br/>
    <el-container>
      <el-table :data="duplicateFile" size="small" style="width: 100%" table-layout="fixed">
        <el-table-column prop="name" label="查重文件" style="width: 80%" align="center"></el-table-column>
        <el-table-column align="right">
          <el-button size="small" type="success" @click="downloadDuplicate">下载</el-button>
        </el-table-column>
      </el-table>
    </el-container>

    <!--    <el-divider />-->
    <br/>
    <el-container>
      <el-table :data="otherFile" size="small" style="width: 100%" table-layout="fixed">
        <el-table-column prop="name" label="其他文件" style="width: 80%" align="center"></el-table-column>
        <el-table-column align="right">
          <template #default="scope">
            <el-button size="small" type="success" @click="downloadOther(scope.$index, scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-container>
    <el-divider/>
    <el-container>
      <el-header>评阅意见提交</el-header>
      <el-container>

        <el-main>
          <el-col >
            <el-row>
              <el-button type="primary" @click="downloadTemplate">
                下载模板
              </el-button>
            </el-row>
            <br/>
            <el-row style="text-align: left">
            <el-upload
                ref="upload"
                class="upload-demo"
                :action="`/api/review/submit/${num}/${sid}/j87y5ndh4sbg678h`"
                :limit="1"
                :on-exceed="handleExceed"
                :on-success="handleSuccess"
                :auto-upload="false"
                accept=".doc,.docx,.pdf"
            >
              <template #trigger>
                <el-button type="primary">选择文件</el-button>
              </template>
              <el-button class="ml-3" type="success" @click="submitUpload">
                上传
              </el-button>
              <template #tip>
                <div class="el-upload__tip text-red">
                  上传评阅/指导意见文件
                  <br/>
                  只能选择一个文件，新文件会覆盖旧文件
                </div>
              </template>
            </el-upload>
          </el-row>
          </el-col>

        </el-main>
        <el-aside style="width: 50%">
          <el-table :data="reviewFile" size="small" style="width: 100%" table-layout="fixed">
            <el-table-column prop="name" label="文件名" style="width: 80%" align="center"></el-table-column>
            <el-table-column align="right">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteReview(scope.$index, scope.row)">删除</el-button>
                <el-button size="small" type="success" @click="downloadReview(scope.$index, scope.row)">下载</el-button>
              </template>

            </el-table-column>
          </el-table>
        </el-aside>
      </el-container>
    </el-container>

  </el-card>

</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ElMessage, ElMessageBox, genFileId, UploadProps, UploadRawFile} from "element-plus";
import {onMounted, reactive, ref} from "vue";
import {UploadInstance} from "element-plus/es";
import axios from "axios";


const route = useRoute()
const router = useRouter()
const upload = ref<UploadInstance>()
const title = ref('')
const newTitle = ref('')
const state = reactive({
  edit_visible: false
})

interface myFile {
  id: String
  name: String
}

const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
const sid = ref()
const name = ref()
const instructorName = ref('')
const supervisorName = ref('')
const instructorId = ref()
const supervisorId = ref()

const essayFile = reactive<myFile[]>([]);
const duplicateFile = reactive<myFile[]>([]);
const otherFile = reactive<myFile[]>([]);
const reviewFile = ref()
const num = ref()

//周期函数，获取A页面传递的数据
onMounted(async () => {
  sid.value = route.query.sid
  name.value = route.query.name
  num.value = route.query.num
  // console.log(route.query, 'A页面传递的数据')
  fetchData();
})


// onMounted(()=>{
//   console.log(userinfo.uid);
//   fetchData();
// })

const fetchData = () => {
  console.log("fetch now!")
  const url = '/api/project/info/' + sid.value + '/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {
    console.log(resp.data)
    title.value = resp.data.title;

    essayFile.splice(0, essayFile.length);
    duplicateFile.splice(0, duplicateFile.length);
    otherFile.splice(0, otherFile.length);
    if (resp.data.essayFileId != null) {
      essayFile.push({id: resp.data.essayFileId, name: resp.data.essayFileName});
    }

    if (resp.data.duplicateFileId != null) {
      duplicateFile.push({id: resp.data.duplicateFileId, name: resp.data.duplicateFileName});
    }
    if (resp.data.otherFileJson != null) {
      for (let i = 0; i < resp.data.otherFileJson.length; i++) {
        otherFile.push(resp.data.otherFileJson[i])
      }
    }
    // console.log(essayFile)
    // console.log(otherFile)
  })

  const url2 = `/api/review/info/${num.value}/${sid.value}/j87y5ndh4sbg678h`
  axios.get(url2).then((resp) => {
    console.log(resp.data)
    if (resp.data.fileInfo.length == 0){
      reviewFile.value = null;
    }else {
      reviewFile.value = resp.data.fileInfo;
    }
    instructorName.value = resp.data.instructorName;
    supervisorName.value = resp.data.supervisorName;
    instructorId.value = resp.data.instructorId;
    supervisorId.value = resp.data.supervisorId;
  })


}


const confirmEdit = () => {
  const url = '/api/project/editTitle/j87y5ndh4sbg678h'

  const formData = new FormData()

  formData.append("uid", userinfo.uid);
  formData.append("title", newTitle.value);
  console.log(newTitle.value)
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    console.log(resp.data)
    if (resp.data.msg == 'success') {
      ElMessage({
        type: 'success',
        message: '修改成功',
      })
      fetchData();
    } else {
      throw new Error();
    }
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '修改失败',
    })
  }).finally(() => {
    state.edit_visible = false;
  })
}

const downloadEssay = () => {
  axios({
    method: 'get',
    url: `/api/project/downloadEssay/${userinfo.uid}/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', essayFile[0].name.toString())  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}

const downloadDuplicate = () => {
  axios({
    method: 'get',
    url: `/api/project/downloadDuplicate/${userinfo.uid}/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', duplicateFile[0].name.toString())  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}

const downloadOther = (index: any, row: any) => {
  axios({
    method: 'get',
    url: `/api/project/downloadOther/${sid}/${row.id}/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', otherFile[index].name.toString())  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}

const handleSuccess: UploadProps['onSuccess'] = (response) => {

  ElMessage.success("文件上传成功")
  upload.value!.clearFiles()
  fetchData();
  // console.log(response)
  // allMatchInfo.value = response;
}

const submitUpload = () => {

  ElMessageBox.confirm(
      '如果评阅意见已经存在，那么将被新的文件覆盖，确定要执行吗？',
      '提示',
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
          message: '上传中',
        })
      })

      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消上传',
        })
      })


}

const downloadReview = (index: any, row: any) => {
  axios({
    method: 'get',
    url: `/api/review/download/${num.value}/${sid.value}/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', row.name)  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}

const deleteReview = (index: any, row: any) => {
  const url = `/api/review/remove/${num.value}/${sid.value}/${row.id}/j87y5ndh4sbg678h`
  axios.get(url).then((resp) => {
    if (resp.data.msg == "success") {
      ElMessage({
        type: "success",
        message: '删除成功'
      })
    } else {
      ElMessage({
        type: "info",
        message: '删除失败,' + resp.data.msg
      })
    }
    fetchData();
  })
}

const downloadZip = () => {
  axios({
    method: 'get',
    url: `/api/project/essayZip/${sid.value}/j87y5ndh4sbg678h`,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', name.value + '.zip')  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}

const downloadTemplate = () => {
  let url0 = ''
  let fileName = ''
  if (instructorId.value == userinfo.uid) {
    url0 = `/api/review/template/0/${sid.value}/j87y5ndh4sbg678h`
    fileName = '南方科技大学本科生毕业设计（论文）指导教师审阅意见表.docx'

  } else if (supervisorId.value == userinfo.uid) {
    url0 = `/api/review/template/1/${sid.value}/j87y5ndh4sbg678h`
    fileName = '南方科技大学本科生毕业设计（论文）评阅人意见表.docx'
  }

  axios({
    method: 'get',
    url: url0,
    responseType: 'blob'  // 指定返回数据的类型为 Blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]))  // 创建 URL 对象
    const link = document.createElement('a')  // 创建 a 标签
    link.href = url
    link.setAttribute('download', fileName)  // 设置下载文件名
    document.body.appendChild(link)
    link.click()  // 模拟点击 a 标签
    document.body.removeChild(link)  // 移除 a 标签
  })
}


</script>
