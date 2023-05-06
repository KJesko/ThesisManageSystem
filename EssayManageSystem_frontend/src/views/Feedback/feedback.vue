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
          <el-row>
            <el-col :span="18">
              <el-form-item label="论文题目" style="display: flex; align-items: center;">
                <span style="margin-right: 20px;">{{ title }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="指导老师评语" style="display: flex; align-items: center;">
                <span style="margin-right: 20px;">{{ instructorFeedback }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="评阅老师评语" style="display: flex; align-items: center;">
                <span style="margin-right: 20px;">{{ supervisorFeedback }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </template>
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
const title = ref('')
const state = reactive({
  edit_visible:false
})


const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
const instructorFeedback = ref()
const supervisorFeedback = ref()


onMounted(()=>{
  console.log(userinfo.uid);
  fetchData();
})

const fetchData=()=>{
  console.log("fetch now!")
  const url = '/api/review/feedback/'+userinfo.uid+'/j87y5ndh4sbg678h'
  axios.get(url).then((resp) => {

    instructorFeedback.value = resp.data.instructorString
    supervisorFeedback.value = resp.data.supervisorString
    title.value = resp.data.title

  })
}




</script>
