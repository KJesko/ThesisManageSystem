<template>
  <div v-loading="loading" class="wrapper">
    <el-card class="transfer">
     <span>
        <div class="card-header">
          <el-form label-position="left" class="info-table">
            <el-form-item label="学号">
               <span>{{ user.uid }}</span>
            </el-form-item>
            <el-form-item label="姓名">
                <el-input v-model="user.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="user.pwd" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="权限">
              <el-select v-model="user.type" placeholder="权限">
                <el-option v-for="item in typeOption" :label="`${item.name}`" :value="item.num"
                           :key="item.num"/>
              </el-select>
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="user.email" autocomplete="off"/>
            </el-form-item>
          </el-form>
        </div>
     </span>
    </el-card>
    <br/>
    <el-row class="btns">
      <el-button size="mini" type="primary" @click="saveData"><i class="fa fa-check"> </i> 确认修改</el-button>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import {computed, defineComponent, onMounted, watchEffect, reactive, toRef, toRefs, ref} from 'vue'
import {useStore} from '@/store'
import Service from './api/index'
import axios from "axios";


const props = defineProps({
  msg: {
    type: String,
    default: () => '默认值'
  },
  currentUser: {
    type: Object,
    default: () => ({})
  }
})
const emit = defineEmits(["success"])    //方法名
// const handleClick=()=>{
//   emit("on-change","子组件方法被调用")
// }
const user = computed(()=>props.currentUser.user)

const typeOption = reactive([
      {num: 2, name: '管理员'},
      {num: 1, name: '老师'},
      {num: 0, name: '学生'}
    ]
)


onMounted(() => {
  console.log(props.currentUser.user)
  // fetchData();
})

// const fetchData=()=>{
//   const url = `/api/user/getAllTeacher/`
//   axios.get(url).then((resp) => {
//     console.log(resp.data)
//     teacher.value = resp.data;
//   })
//   instructor.value.name = match.value.instructorName
//   instructor.value.id = match.value.instructorId
//   supervisor.value.name = match.value.supervisorName
//   supervisor.value.id = match.value.supervisorId
//
// }

const saveData = () => {
  const url = `/api/user/updateUser/j87y5ndh4sbg678h`;

  const formData = new FormData()
  formData.append("uid" , user.value.uid.toString());

  formData.append("name", user.value.name.toString());
  formData.append("pwd", user.value.pwd.toString());
  formData.append("type", user.value.type.toString());
  // formData.append("instructorName", instructor.value.name);
  formData.append("email", user.value.email.toString());
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    console.log(resp.data)
    emit("success");
  })


}

// watchEffect(() => {
//   fetchData()
// })


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
