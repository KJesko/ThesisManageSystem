<template>
  <div v-loading="loading" class="wrapper">
    <el-card class="transfer">
<!--      <template #body>-->
        <span>
          <div class="card-header">
            <el-form label-position="left" class="info-table">
              <el-form-item label="学号">
                  <el-input v-model="match.sid" autocomplete="off"/>
              </el-form-item>
              <el-form-item label="姓名">
                  <el-input v-model="match.name" autocomplete="off"/>
              </el-form-item>
              <el-form-item label="专业">
                  <el-input v-model="match.major" autocomplete="off"/>
              </el-form-item>
              <el-form-item label="指导老师">
                <el-select v-model="match.instructorId" placeholder="指导老师">
                  <el-option v-for="item in teacher" :label="`${item.name}(${item.uid})`" :value="item.uid"
                             :key="item.name"/>
                </el-select>
              </el-form-item>
              <el-form-item label="评阅老师">
                <el-select v-model="match.supervisorId" placeholder="评阅老师">
                  <el-option v-for="item in teacher" :label="`${item.name}(${item.uid})`" :value="item.uid"
                             :key="item.name"/>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </span>
<!--      </template>-->
    </el-card>
    <br/>
    <el-row class="btns">
      <el-button size="mini" type="primary" @click="saveData"><i class="fa fa-check"> </i> 新建配对关系</el-button>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import {computed, defineComponent, onMounted, watchEffect, reactive, toRef, toRefs, ref} from 'vue'
import {useStore} from '@/store'
import Service from './api/index'
import axios from "axios";
import {ElMessage} from "element-plus";


const props = defineProps({
  msg: {
    type: String,
    default: () => '默认值'
  },
})
const emit=defineEmits(["success","fail"])    //方法名
// // const handleClick=()=>{
// //   emit("on-change","子组件方法被调用")
// // }
const teacher = ref([])

const match = reactive({
  sid: '',
  name: "",
  major: '',
  instructorId: '',
  instructorName: '',
  supervisorId: '',
  supervisorName: '',
})


onMounted(() => {
  fetchData();
})

const fetchData = () => {
  const url = `/api/user/getAllTeacher/j87y5ndh4sbg678h`
  axios.get(url).then((resp) => {
    console.log(resp.data)
    teacher.value = resp.data;
  })
}

const saveData = () => {
  const url = `/api/user/newMatch/j87y5ndh4sbg678h`;

  const formData = new FormData()

  formData.append("sid", match.sid.toString());
  formData.append("name", match.name.toString());
  formData.append("major", match.major.toString());
  formData.append("instructorId", match.instructorId.toString());
  // formData.append("instructorName", instructor.value.name);
  formData.append("supervisorId", match.supervisorId.toString());
  // formData.append("supervisorName", supervisor.value.name);
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    if (resp.data.msg == 'success'){
      emit("success");
    }else {
      ElMessage({
        type: 'error',
        message: resp.data.msg,
      })
      emit("fail");
    }
    console.log(resp.data)

  })
}

watchEffect(() => {
  fetchData()
})


</script>
<style lang="stylus" scoped>
.btns {
  text-align: right;
}

.el-transfer {
  display: inline-block;
  text-align: left;
}

.transfer {
  text-align: center;
}

.card-header {
  text-align: left;
}
</style>
