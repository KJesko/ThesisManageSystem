<template xmlns="http://www.w3.org/1999/html">
  <div class="info">
    <el-divider content-position="left">论文提交</el-divider>
  </div>

  <el-card>
    <template #header>
      <div>
        <el-form label-position="left" inline>
          <el-form-item label="学号:">
            <span>{{ userinfo.uid }}</span>
          </el-form-item>
          <el-form-item label="姓名:">
            <span>{{ userinfo.name }}</span>
          </el-form-item>
          <el-form-item label="指导老师:">
            <span>{{ instructorName }}</span>
          </el-form-item>
<!--          <el-form-item label="评阅老师:">-->
<!--            <span>{{ supervisorName }}</span>-->
<!--          </el-form-item>-->
<!--          <el-row>-->
<!--            <el-form-item label="论文题目" style="width: auto;display: block">-->
<!--              <span>{{ title }}</span>-->
<!--              <el-button type="primary" @click="editTitle" style="display:flex;justify-content: flex-end">修改题目</el-button>-->
<!--            </el-form-item>-->
<!--          </el-row>-->
          <el-row>
            <el-col :span="18">
              <el-form-item label="论文题目" style="display: flex; align-items: center;">
                <span style="margin-right: 20px;">{{ title }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="editTitle" style="float: right;">点击编辑论文题目</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>


    </template>

    <el-container>
      <el-header>论文文件</el-header>
      <el-container>
        <el-main>
            <el-row style="text-align: left">
                <el-upload
                    ref="upload1"
                    class="upload-demo"
                    :action="`/api/project/submit/essay/${userinfo.uid}/j87y5ndh4sbg678h`"
                    :limit="1"
                    :on-exceed="handleExceed1"
                    :on-success="handleSuccess1"
                    :auto-upload="false"
                    accept=".pdf"
                >
                  <template #trigger>
                    <el-button type="primary">选择文件</el-button>
                  </template>
                  <el-button class="ml-3" type="success" @click="submitUpload1">
                    上传
                  </el-button>
                  <template #tip>
                    <div class="el-upload__tip text-red">
                      请上传pdf格式文件，方便老师查看
                      <br/>
                      只能选择一个文件，新文件会覆盖旧文件
                    </div>
                  </template>
                </el-upload>
            </el-row>
        </el-main>
        <el-aside style="width: 50%">
          <el-table :data="essayFile" size="small"  style="width: 100%" table-layout="fixed" >
            <el-table-column prop="name" label="文件名" style="width: 80%" align="center"></el-table-column>
            <el-table-column  align="right">
              <el-button size="small" type="danger" @click="deleteEssay">删除</el-button>
              <el-button size="small" type="success" @click="downloadEssay">下载</el-button>
            </el-table-column>
          </el-table>
        </el-aside>
      </el-container>
    </el-container>

    <el-divider/>
    <el-container>
      <el-header>查重文件</el-header>
      <el-container>
        <el-main>
          <el-row style="text-align: left">
            <el-upload
                ref="upload2"
                class="upload-demo"
                :action="`/api/project/submit/duplicate/${userinfo.uid}/j87y5ndh4sbg678h`"
                :limit="1"
                :on-exceed="handleExceed2"
                :on-success="handleSuccess2"
                :auto-upload="false"
                accept=".pdf"
            >
              <template #trigger>
                <el-button type="primary">选择文件</el-button>
              </template>
              <el-button class="ml-3" type="success" @click="submitUpload2">
                上传
              </el-button>
              <template #tip>
                <div class="el-upload__tip text-red">
                  请上传pdf格式文件，方便老师查看
                  <br/>
                  只能选择一个文件，新文件会覆盖旧文件
                </div>
              </template>
            </el-upload>
          </el-row>
        </el-main>
        <el-aside style="width: 50%">
          <el-table :data="duplicateFile" size="small" style="width: 100%" table-layout="fixed" >
            <el-table-column prop="name" label="文件名" style="width: 80%" align="center"></el-table-column>
            <el-table-column  align="right">
              <el-button size="small" type="danger" @click="deleteDuplicate">删除</el-button>
              <el-button size="small" type="success" @click="downloadDuplicate">下载</el-button>
            </el-table-column>
          </el-table>
        </el-aside>
      </el-container>
    </el-container>

    <el-divider />
    <el-container>
      <el-header>其他文件</el-header>
      <el-container>
        <el-main>
          <el-row style="text-align: left">
            <el-upload
                ref="upload3"
                class="upload-demo"
                :action="`/api/project/submit/other/${userinfo.uid}/j87y5ndh4sbg678h`"
                :on-success="handleSuccess3"
                :auto-upload="false"
            >
              <template #trigger>
                <el-button type="primary">选择文件</el-button>
              </template>
              <el-button class="ml-3" type="success" @click="submitUpload3">
                上传
              </el-button>
              <template #tip>
                <div class="el-upload__tip text-red">
                 上传其他需要的文件
                </div>
              </template>
            </el-upload>
          </el-row>
        </el-main>
        <el-aside style="width: 50%">
          <el-table :data="otherFile" size="small" style="width: 100%" table-layout="fixed" >
            <el-table-column prop="name" label="文件名" style="width: 80%" align="center"></el-table-column>
            <el-table-column  align="right">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteOther(scope.$index, scope.row)">删除</el-button>
                <el-button size="small" type="success" @click="downloadOther(scope.$index, scope.row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-aside>
      </el-container>
    </el-container>

  </el-card>

  <el-dialog v-model="state.edit_visible">
    <el-form :model="form">
      <el-form-item label="新题目" >
        <el-input v-model="newTitle" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="state.edit_visible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">确认修改</el-button>
      </span>
    </template>
  </el-dialog>


</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ElMessage, ElMessageBox, genFileId, UploadProps, UploadRawFile} from "element-plus";
import {onMounted, reactive, ref} from "vue";
import {UploadInstance} from "element-plus/es";
import axios from "axios";


const route = useRoute()
const router = useRouter()
const upload1 = ref<UploadInstance>()
const upload2 = ref<UploadInstance>()
const upload3 = ref<UploadInstance>()
const title = ref('')
const newTitle = ref('')
const state = reactive({
  edit_visible:false
})

interface myFile {
  id: String
  name: String
}

const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
const uid = userinfo.uid
const instructorName = ref('')
const supervisorName = ref('')

const essayFile = reactive<myFile[]>([]);
const duplicateFile = reactive<myFile[]>([]);
const otherFile = reactive<myFile[]>([]);


onMounted(()=>{
  console.log(userinfo.uid);
  fetchData();
})

const fetchData=()=>{
console.log("fetch now!")
  const url = '/api/project/info/'+userinfo.uid+'/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {
    console.log(resp.data)
    title.value = resp.data.title;
    instructorName.value = resp.data.instructorName;
    supervisorName.value = resp.data.supervisorName;
    essayFile.splice(0,essayFile.length);
    duplicateFile.splice(0,duplicateFile.length);
    otherFile.splice(0,otherFile.length);
    if (resp.data.essayFileId != null){
      essayFile.push({id:resp.data.essayFileId,name:resp.data.essayFileName});
    }

    if (resp.data.duplicateFileId != null){
      duplicateFile.push({id:resp.data.duplicateFileId,name:resp.data.duplicateFileName});
    }
    if(resp.data.otherFileJson != null){
      for (let i = 0;i<resp.data.otherFileJson.length;i++) {
        otherFile.push(resp.data.otherFileJson[i])
      }
    }
    console.log(essayFile)
    console.log(otherFile)


  })
}

const editTitle=()=>{
  state.edit_visible = true;
  newTitle.value = title.value;
}

const confirmEdit=()=>{
  const url = '/api/project/editTitle/j87y5ndh4sbg678h'

  const formData = new FormData()

  formData.append("uid", userinfo.uid);
  formData.append("title",newTitle.value);
  console.log(newTitle.value)
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    console.log(resp.data)
    if (resp.data.msg == 'success'){
      ElMessage({
        type: 'success',
        message: '修改成功',
      })
      fetchData();
    }else {
      throw new Error();
    }
  }).catch(()=>{
    ElMessage({
      type: 'info',
      message: '修改失败',
    })
  }).finally(()=>{
    state.edit_visible = false;
  })
}

const downloadEssay=()=>{
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

const deleteEssay=()=>{
  const url = '/api/project/removeEssay/'+userinfo.uid+'/'+essayFile[0].id+'/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {
    if (resp.data.msg == "success"){
      ElMessage({
        type:"success",
        message: '删除成功'
      })
    }else {
      ElMessage({
        type:"info",
        message: '删除失败,'+resp.data.msg
      })
    }
    fetchData();
  })
}

const downloadDuplicate=()=>{
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

const deleteDuplicate=()=>{
  const url = '/api/project/removeDuplicate/'+userinfo.uid+'/'+duplicateFile[0].id+'/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {
    if (resp.data.msg == "success"){
      ElMessage({
        type:"success",
        message: '删除成功'
      })
    }else {
      ElMessage({
        type:"info",
        message: '删除失败,'+resp.data.msg
      })
    }
    fetchData();
  })
}

const deleteOther = (index:any, row:any) =>{
  const url = '/api/project/removeOther/'+userinfo.uid+'/' +row.id+'/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {
    if (resp.data.msg == "success"){
      ElMessage({
        type:"success",
        message: '删除成功'
      })
    }else {
      ElMessage({
        type:"info",
        message: '删除失败,'+resp.data.msg
      })
    }
    fetchData();
  })
}

const downloadOther=(index:any, row:any)=>{
  axios({
    method: 'get',
    url: `/api/project/downloadOther/${userinfo.uid}/${row.id}/j87y5ndh4sbg678h`,
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

const handleExceed1: UploadProps['onExceed'] = (files) => {
  upload1.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload1.value!.handleStart(file)
}

const handleSuccess1: UploadProps['onSuccess'] = (response) => {

  ElMessage.success("文件上传成功")
  upload1.value!.clearFiles()
  fetchData();
  // console.log(response)
  // allMatchInfo.value = response;
}

const submitUpload1 = () => {
  ElMessageBox.confirm(
      '如果论文文件已经存在，那么将被新的文件覆盖，确定要执行吗？',
      '提示',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        upload1.value!.submit()
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

const handleExceed2: UploadProps['onExceed'] = (files) => {
  upload2.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload2.value!.handleStart(file)
}

const handleSuccess2: UploadProps['onSuccess'] = (response) => {

  ElMessage.success("文件上传成功")
  upload2.value!.clearFiles()
  fetchData();
  // console.log(response)
  // allMatchInfo.value = response;
}

const submitUpload2 = () => {

  ElMessageBox.confirm(
      '如果查重文件已经存在，那么将被新的文件覆盖，确定要执行吗？',
      '提示',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        upload2.value!.submit()
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

const handleSuccess3: UploadProps['onSuccess'] = (response) => {

  ElMessage.success("文件上传成功")
  upload3.value!.clearFiles()
  fetchData();
  // console.log(response)
  // allMatchInfo.value = response;
}

const submitUpload3 = () => {

  ElMessageBox.confirm(
      '本次操作并不会影响已经上传的文件',
      '提示',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'info',
      }
  )
      .then(() => {
        upload3.value!.submit()
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



</script>
