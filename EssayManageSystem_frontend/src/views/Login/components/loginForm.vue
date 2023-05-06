<template>
  <div class="form-container">
    <el-form v-if="showLogin" ref="loginFormRef" :model="loginForm" status-icon :hide-required-asterisk="true"
             :rules="rules" label-width="100px" class="login-form">
      <el-form-item label="学号/工号" prop="number">
        <el-input v-model="loginForm.uid" autocomplete="off" placeholder="输入你的学号/工号"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="loginForm.password" type="password" autocomplete="off"
                  placeholder="输入你的密码"></el-input>
      </el-form-item>

      <el-form-item>
        <div class="btn-container">
          <el-button type="primary" style="width: 100%" @click="submitForm()">Login</el-button>
        </div>
<!--        <div class="go-login">-->
<!--          <span class="to-login" @click="showLogin = !showLogin">Do not have an account yet /<em> Register</em></span>-->
<!--          &lt;!&ndash; <span class="forget-password" @click="handleForget"></span> &ndash;&gt;-->
<!--        </div>-->
      </el-form-item>
    </el-form>
<!--    <el-form v-if="!showLogin" ref="registerRef" :model="registerForm" status-icon :hide-required-asterisk="true"-->
<!--             :rules="rules" label-width="100px" class="login-form">-->
<!--      <el-form-item label="UID" prop="number">-->
<!--        <el-input v-model.number="registerForm.uid" maxlength="8" autocomplete="off"-->
<!--                  placeholder="Please input your UID"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Name">-->
<!--        <el-input v-model.number="registerForm.name" autocomplete="off" placeholder="Please input your name"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Email" prop="number">-->
<!--        <el-input v-model="registerForm.email" autocomplete="off" placeholder="Please input your email">-->
<!--          &lt;!&ndash; <template #append>-->
<!--            <el-button :disabled="sendingCode" @click="handleGetCaptcha">{{ codeText }}</el-button>-->
<!--          </template> &ndash;&gt;-->
<!--        </el-input>-->
<!--      </el-form-item>-->
<!--      &lt;!&ndash; <el-form-item label="验证码" prop="capcha">-->
<!--        <el-input v-model.number="registerForm.capcha" maxlength="10" autocomplete="off" placeholder="请输入验证码"></el-input>-->
<!--      </el-form-item> &ndash;&gt;-->
<!--      <el-form-item label="Password" prop="password">-->
<!--        <el-input v-model="registerForm.password" type="password" autocomplete="off"-->
<!--                  placeholder="Please input your password"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Confirm" prop="checkPass">-->
<!--        <el-input v-model="registerForm.checkPass" type="password" autocomplete="off"-->
<!--                  placeholder="Confirm input your password"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Type" prop="resource">-->
<!--        <el-radio-group v-model="registerForm.type">-->
<!--          <el-radio label="0">Student</el-radio>-->
<!--          <el-radio label="1">Teacher</el-radio>-->
<!--          <el-radio label="2">Admin</el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->

<!--      <el-form-item>-->
<!--        <div class="btn-container">-->
<!--          <el-button type="primary" style="width: 100%" @click="handleRegister()">Finish Register</el-button>-->
<!--        </div>-->
<!--        <div class="go-login">-->
<!--          <span class="to-login" @click="showLogin = !showLogin">Already have an account /<em> Login</em></span>-->
<!--        </div>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
  </div>
</template>
<script lang="ts">
import {defineComponent, ref, toRefs, reactive} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage} from 'element-plus'
// import { encrypt } from '@/utils/aes' // aes 密码加密
import {useStore} from '@/store'
import Service from '../api/index'
import axios from "axios";

interface stateType {
  loginForm: {
    uid: string
    password: string
  }
  registerForm: {
    uid: string
    name: string
    email: string
    capcha: number | null
    password: string
    checkPass: string
    type: string
  }
  showLogin: boolean
}

// eslint-disable-next-line no-unused-vars
type VoidNoop = (arg0?: Error) => void
export default defineComponent({
  name: 'LoginForm',
  emits: ['toResetPwd'],
  setup(_props, {emit}) {
    const router = useRouter()
    const route = useRoute()
    const loginFormRef = ref()
    const registerRef = ref()
    const store = useStore()
    const sendingCode = ref(false)
    const codeText = ref('获取验证码')
    const state = reactive<stateType>({
      loginForm: {
        uid: '',
        password: ''
      },
      registerForm: {
        uid: '',
        name: '',
        email: '',
        capcha: null,
        password: '',
        checkPass: '',
        type: ''
      },
      showLogin: true
    })

    // eslint-disable-next-line no-unused-vars
    const validatePass2 = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== state.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    const validatePass = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (state.registerForm.checkPass !== '') {
          registerRef.value.validateField('checkPass')
        }
        callback()
      }
    }
    const rules = {
      password: [
        {validator: validatePass, trigger: 'blur'},
        {min: 6, message: '密码长度至少为6个字符 ', trigger: 'blur'}
      ],
      checkPass: [{validator: validatePass2, trigger: 'blur'}],
      email: [
        {required: true, message: '请输入注册邮箱', trigger: 'change'},
        {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
      ],
      capcha: [{required: true, message: '请输入验证码', trigger: 'blur'}]
    }

    // methods

    /**
     * @description  用户登录接口
     *
     */
    const submitForm = () => {
      loginFormRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            const {uid, password} = state.loginForm
            console.log(password)
            const data = {
              uid,
              // password
              password
            }
            const res = await Service.postLogin(data)
            // alert(res.message)
            const userInfo = await Service.postAuthUserInfo({uid})
            console.log(res.message)
            // console.log('userInfo is ')
            // console.log(`userInfo${ userInfo}`)
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
            // console.log(store)
            const accessToken = res?.data?.accessToken ?? null
            if (accessToken) {
              // 将角色存储到全局vuex roles
              if (userInfo.status === 0) {
                await store.dispatch('permissionModule/getPermissonRoles', userInfo.data)
              }
              // 先进行异步路由处理
              await store.dispatch('permissionModule/getPermissonRoutes', userInfo.data)
              await store.dispatch('permissionModule/getPermissions', userInfo.data)
              sessionStorage.setItem('auth', 'true')
              sessionStorage.setItem('accessToken', accessToken)
              console.log(route.query.redirect)
              if (route.query.redirect) {
                const path = route.query.redirect
                router.push({path: path as string})
              } else {
                router.push('/')
              }
            } else {
              ElMessage({
                type: 'warning',
                message: res.message
              })
            }
          }
          catch (err) {

            ElMessage({
              type: 'warning',
              message: err.message
            })
          }
          // finally {
          //   console.log('err')
          // }
        }
        return false
      })
    }
    /**
     * @description 处理注册接口
     */
    const handleRegister = () => {
      registerRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            const {uid, name, email, password, type} = state.registerForm
            const data = {
              uid,
              name,
              email,
              password,
              type
            }
            console.log(data)
            // await axios.post('http://localhost:8181/user/register',data)

            Service.postRegister(data)
                .then((res: any) => {

                  if (res.status === 0) {
                    ElMessage({
                      type: 'success',
                      message: '注册成功'
                    })
                    state.showLogin = true
                  } else {
                    ElMessage({
                      type: 'warning',
                      message: res.message
                    })
                  }
                })
                .catch((err) => {
                  ElMessage({
                    type: 'warning',
                    message: err.message
                  })
                })
          } catch (err) {
            ElMessage({
              type: 'error',
              message: err.message
            })
          }
        }
      })
    }
    /**
     * @description 获取验证码状态
     */
    const getCodeSucces = () => {
      let countDown = 60
      sendingCode.value = true
      const interval = setInterval(() => {
        if (countDown > 0) {
          codeText.value = `已发送(${countDown}s)`
          countDown -= 1
        } else {
          clearInterval(interval)
          sendingCode.value = false
          codeText.value = '获取验证码'
        }
      }, 1000)
    }
    /**
     * @description 获取验证码
     */
    const handleGetCaptcha = async (): Promise<boolean> => {
      try {
        const {email} = state.registerForm
        if (!email) {
          ElMessage({
            type: 'warning',
            message: '请输入注册邮箱'
          })
          return false
        }
        const data = {
          email
        }
        const res = await Service.postCaptcha(data)
        if (res.status === 0) {
          ElMessage({
            type: 'success',
            message: res.message
          })
          getCodeSucces()
          return true
        }
        ElMessage({
          type: 'warning',
          message: res.message
        })
        return false
      } catch (err) {
        console.error(err)
        return false
      }
    }
    /**
     * @description 忘记密码
     */
    const handleForget = () => {
      emit('toResetPwd')
    }
    return {
      ...toRefs(state),
      loginFormRef,
      registerRef,
      sendingCode,
      codeText,
      rules,
      submitForm,
      handleRegister,
      handleGetCaptcha,
      handleForget
    }
  }
})
</script>
<style lang="stylus" scoped>
.form-container {
  width: 100%;

  :deep(.el-input-group__append) {
    padding: 0px 7px;
  }

  :deep(.el-input-group__prepend) {
    padding: 0px 7px;
  }

  .login-form {
    width: 100%;
    margin: 0 auto;
  }

  .go-login {
    font-size: 12px;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    .to-login {
      color: #9fa2a8;

      em {
        color: #2878ff;
      }
    }
  }

  .operation {
    font-size: 12px;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;

    .free-register {
      color: #2878ff;
    }

    .forget-password {
      color: #9fa2a8;
    }
  }

  .btn-container {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
  }
}
</style>
