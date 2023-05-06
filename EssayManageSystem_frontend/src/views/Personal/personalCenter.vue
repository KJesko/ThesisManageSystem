<template>
  <div class="page-container">
    <div class="info">
      <el-divider content-position="left">个人中心</el-divider>
    </div>
    <el-row :gutter="20">
      <el-col :span="7" :offset="1">
        <el-card class="box-card">
          <div class="account-avatar">
            <img src="../../assets/avatar-default.jpg"/>
            <div class="account-name">{{userinfo.uid}}</div>
            <div class="account-sign">{{userinfo.name}}</div>
          </div>
          <div class="account-detail">
            <el-descriptions class="detail" :column="1" :size="size">
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  姓名:
                </template>
                {{userinfo.name}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  权限角色:
                </template>
                {{ roles }}
              </el-descriptions-item>
              <el-descriptions-item>

                <template #label>
                  <i class="el-icon-user"></i>
                  修改密码:
                </template>
                <el-button size="small" plain type="info"  @click="dialogFormVisible = true">
                  <el-icon><Key /></el-icon>
                </el-button>
              </el-descriptions-item>


            </el-descriptions>

          </div>
        </el-card>
      </el-col>

      <el-col :span="15" :push="0">
        <el-card class="box-card">
          <fullcalendar></fullcalendar>
        </el-card>
      </el-col>

      <el-dialog v-model="dialogFormVisible" title="修改密码">
        <el-form >
          <el-form-item label="原密码" :label-width="formLabelWidth-30">
            <el-input v-model="password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="新密码" :label-width="formLabelWidth-30">
            <el-input v-model="resetPassword" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="editPassword">
              提交修改
            </el-button>
          </span>
        </template>
      </el-dialog>

    </el-row>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, ref, onMounted, computed} from 'vue'
import {useStore} from '@/store'
import axios from "axios";
import Service from './api/index'
import fullcalendar from './components/fullCalendar/index.vue'
import {Key} from '@element-plus/icons-vue'

// import { Console } from 'console';
import {ElMessage} from "element-plus";

interface stateType {
  dynamicTags: string[]
  inputVisible: boolean
  inputValue: string
  contentTip: string
}

export default defineComponent({
  name: 'PersonalCenter',
  components: {
    Key,
    fullcalendar
  },
  setup() {
    const state = reactive<stateType>({
      dynamicTags: ['default'],
      inputVisible: false,
      inputValue: '',
      contentTip: '您可：1、拖放事件任务 2、调整事件任务的大小 3、点击事件任务删除 4、点击日期添加新的事件任务'
    })
    const formLabelWidth = ref(100)
    const size = ref('medium')
    const showDesc = ref(true)
    const store = useStore()
    const password = ref()
    const resetPassword = ref()
    const dialogFormVisible = ref(false)

    const userinfo = JSON.parse(localStorage.getItem('userInfo') as string)
    console.log(userinfo)

    const roles = computed(() =>{
      if (store.state.permissionModule.roles == 'teacher'){
        return  '老师'
      }else if (store.state.permissionModule.roles == 'student'){
        return  '学生'
      }else if (store.state.permissionModule.roles == 'Admin'){
        return  '管理员'
      }
      return "未知"
    } )
    // mothods





    // const handleInputConfirm = () => {
    //   const {inputValue} = state
    //   if (inputValue) {
    //     state.dynamicTags.push(inputValue)
    //   }
    //   const form = new FormData()
    //   form.append("tag",state.dynamicTags.toString())
    //   form.append("sid",userinfo.userName)
    //   axios.post('/api/user/updatetag',form).then(resp=>{
    //
    //   })
    //   userinfo.data.tag=state.dynamicTags.toString()
    //   localStorage.setItem('userInfo', JSON.stringify(userinfo))
    //   state.inputVisible = false
    //   state.inputValue = ''
    // }
    const showInput = () => {
      state.inputVisible = true
    }

    const editPassword =()=>{
      const param = {
        "id": userinfo.uid,
        "old":password.value,
        "new":resetPassword.value
      }
      Service.editPassword(param).then((resp:any)=>{
        ElMessage.success("密码修改完成")
        password.value = ''
        resetPassword.value = ''
        dialogFormVisible.value = false
      }).catch((err:any)=>{
        ElMessage.warning(err.msg)
      })


    }


    onMounted(() => {


    })
    return {

      formLabelWidth,
      showInput,
      // handleInputConfirm,
      ...toRefs(state),
      size,
      roles,
      password,
      resetPassword,
      dialogFormVisible,
      userinfo,
      showDesc,
      editPassword

    }
  },
  data() {
    return {

      amount: '',
      trade_num: '',
      sid: '12011204',
    }
  },

  methods: {


  },

})
</script>
<style lang="stylus" scoped>
.page-container {
  .info {
    text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
  }
}

.box-card {
  p {
    text-align: right;
  }

  .is-selected {
    color: #1989FA;
  }

  ul {
    margin-top: 4%;
    margin-left: 15%;
    width: 80%;
    text-align: left;
    position: relative;
    display: inline-block;
    background-color: white;

    li {
      text-align: left;

      .list-tag {
        background-color: #ecf5ff;
        border-color: #d9ecff;
        height: 32px;
        padding: 0 10px;
        line-height: 30px;
        font-size: 12px;
        color: #409EFF;
        border-width: 1px;
        border-style: solid;
        border-radius: 4px;
        box-sizing: border-box;
        white-space: nowrap;

        .el-icon-close {
          border-radius: 50%;
          text-align: center;
          position: relative;
          cursor: pointer;
          font-size: 12px;
          height: 16px;
          width: 16px;
          line-height: 16px;
          vertical-align: middle;
          top: -1px;
          right: -5px;

          :before {
            display: block;
          }
        }

        .el-tag__close {
          color: #409eff;
        }
      }

    }
  }
  margin-top: 14px;

  .account-avatar {
    text-align: center;
    margin-bottom: 24px;

    img {
      width: 105px;
      height: 105px;
      margin-bottom: 20px;
      border-radius: 50%;
    }

    .account-name {
      margin-bottom: 4px;
      color: rgba(0, 0, 0, .85);
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
    }

  }

  .account-detail {
    .detail {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
    }
  }

  .divider {
    display: flex;
    clear: both;
    width: 100%;
    min-width: 100%;
    margin: 24px 0;
  }

  .divider-dashed {
    background: 0 0;
    border: dashed rgba(0, 0, 0, .06);
    border-width: 1px 0 0;
  }

  .tag-title {
    text-align: left;
  }

  .tag-dynamic {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-wrap: wrap;
    width: 100%;
    margin: 10px 0px;

  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
}
</style>
