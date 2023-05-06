<template>
  <div v-loading="loading" class="wrapper">
    <el-card class="transfer">
      <template #header>
        <span>
          <div class="card-header">
            <el-form label-position="left" inline class="info-table">
              <el-form-item label="学号">
                <span>{{ match.sid }}</span>
              </el-form-item>
              <el-form-item label="姓名">
                <span>{{ match.sname }}</span>
              </el-form-item>
              <el-form-item label="专业">
                <span>{{ match.major }}</span>
              </el-form-item>
              <el-form-item label="指导老师">
                <el-select v-model="instructor.id" placeholder="指导老师">
                  <el-option v-for="item in teacher" :label="`${item.name}(${item.uid})`" :value="item.uid" :key="item.name"/>
                </el-select>
              </el-form-item>
              <el-form-item label="评阅老师">
                <el-select v-model="supervisor.id" placeholder="指导老师">
                  <el-option v-for="item in teacher" :label="`${item.name}(${item.uid})`" :value="item.uid" :key="item.name"/>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </span>
      </template>
    </el-card>
    <br/>
    <el-row class="btns">
      <el-button size="mini" type="primary" @click="saveData"><i class="fa fa-check"> </i> 确认修改</el-button>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import {computed, defineComponent, onMounted, watchEffect, reactive, toRef, toRefs,ref} from 'vue'
import {useStore} from '@/store'
import Service from './api/index'
import axios from "axios";


const props=defineProps({
  msg:{
    type:String,
    default:()=>'默认值'
  },
  currentMatch: {
    type: Object,
    default: () => ({})
  }
})
const emit=defineEmits(["success"])    //方法名
// const handleClick=()=>{
//   emit("on-change","子组件方法被调用")
// }
const match = computed(()=>props.currentMatch.match)
const teacher = ref([])
const instructor = ref({name:'',id:''})
const supervisor = ref({name:'',id:''})



onMounted(() => {
  fetchData();
})

const fetchData=()=>{
  const url = `/api/user/getAllTeacher/j87y5ndh4sbg678h`
  axios.get(url).then((resp) => {
    console.log(resp.data)
    teacher.value = resp.data;
  })
  instructor.value.name = match.value.instructorName
  instructor.value.id = match.value.instructorId
  supervisor.value.name = match.value.supervisorName
  supervisor.value.id = match.value.supervisorId

}

const saveData=()=>{
  const url = `/api/user/updateMatch/j87y5ndh4sbg678h`;

  const formData = new FormData()

  formData.append("sid", match.value.sid);
  formData.append("instructorId", instructor.value.id);
  // formData.append("instructorName", instructor.value.name);
  formData.append("supervisorId", supervisor.value.id);
  // formData.append("supervisorName", supervisor.value.name);
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    console.log(resp.data)
    emit("success");
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
