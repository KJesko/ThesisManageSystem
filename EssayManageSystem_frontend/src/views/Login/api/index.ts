import request from '@/utils/request'

const loginApi = {
  userLogin: '/api/user/login',
  userRegister: '/api/user/register',
  sendCaptcha: '/api/auth/email/sendCaptcha',
  forgetPassword: '/api/auth/email/forgot-password',
  resetPassword: `/api/auth/user/reset-password`,
  permissionRoutes: '/api/permission/routes',
  userInfo: '/api/user/userInfo'
}

class Service {
  /**
   * @description POST 用户登录接口
   */
  static postLogin(params: any) {
    console.log(params)
    return request({
      url: loginApi.userLogin,
      method: 'POST',
      json: true,
      params
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @descript POST  用户注册
   */
  static postRegister(params: any) {
    return request({
      url: loginApi.userRegister,
      method: 'POST',
      json: true,
      params
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @description   POST 发送验证码 /auth/email/sendCaptcha
   */
  static postCaptcha(data: any) {
    return request({
      url: loginApi.sendCaptcha,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @description POST 忘记密码接口
   */
  static postForgetPwd(data: any) {
    return request({
      url: loginApi.forgetPassword,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @description /auth/user/reset-password/{token}
   */
  static postResetPwd(data: any) {
    return request({
      url: loginApi.resetPassword,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @description POST 保存授权菜单权限
   */
  static postAuthPermission(params: any) {
    return request({
      url: loginApi.permissionRoutes,
      method: 'POST',
      json: true,
      params
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  /**
   * @description POST 查询用户信息
   */

  static postAuthUserInfo(params: any) {
    return request({
      url: loginApi.userInfo,
      method: 'POST',
      json: true,
      params
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
}
export default Service
