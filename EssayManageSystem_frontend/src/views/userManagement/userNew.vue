<template>
  <div v-loading="loading" class="wrapper">
    <el-card class="transfer">
      <!--      <template #body>-->
      <span>
          <div class="card-header">
            <el-form label-position="left" class="info-table">
              <el-form-item label="学号">
                  <el-input v-model="user.uid" autocomplete="off"/>
              </el-form-item>
              <el-form-item label="姓名">
                  <el-input v-model="user.name" autocomplete="off"/>
              </el-form-item>
              <el-form-item label="密码">
                  <el-input v-model="user.password" autocomplete="off"/>
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


const emit = defineEmits(["success", "fail"])    //方法名
// // const handleClick=()=>{
// //   emit("on-change","子组件方法被调用")
// // }
// const props = defineProps({
//   msg: {
//     type: String,
//     default: () => '默认值'
//   },
//   currentUser: {
//     type: Object,
//     default: () => ({})
//   }
// })


const user = reactive({
  uid: '',
  name: '',
  pwd: '',
  type: 0,
  email: '',
})

const typeOption = reactive([
      {num:2, name:'管理员'},
      {num:1, name:'老师'},
      {num:0, name:'学生'}
    ]
)

//
// onMounted(() => {
//   fetchData();
// })
//
// const fetchData = () => {
//   const url = `/api/user/getAllTeacher/`
//   axios.get(url).then((resp) => {
//     console.log(resp.data)
//     teacher.value = resp.data;
//   })
// }

const saveData = () => {
  const url = `/api/user/newUser/j87y5ndh4sbg678h`;

  const formData = new FormData()

  formData.append("uid", user.uid.toString());
  formData.append("name", user.name.toString());
  formData.append("pwd", user.pwd.toString());
  formData.append("type", user.type.toString());
  // formData.append("instructorName", instructor.value.name);
  formData.append("email", user.email.toString());
  // formData.append("supervisorName", supervisor.value.name);
  axios.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((resp) => {
    if (resp.data.msg == 'success') {
      emit("success");
    } else {
      ElMessage({
        type: 'error',
        message: resp.data.msg,
      })
      emit("fail");
    }
    // console.log(resp.data)

  })
}
//
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
