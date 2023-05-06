import request from '@/utils/request'

const roleApi = {
  queryAuthedPermission: '/api/permission/routes'
}

class Service {
  /**
   * @description POST 保存授权菜单权限
   */
  static postAuthPermission(params: any) {
    return request({
      url: roleApi.queryAuthedPermission,
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

  static postUserListPage(params: any) {
    return request({
      url: `/api/user/allUserPageInfo/j87y5ndh4sbg678h`,
      method: 'POST',
      json: true,
      params
    }).then((res) => {
      console.log(res)
      return Promise.resolve(res)
      // if (res.status === 0) {
      //   return Promise.resolve(res)
      // }
      // return Promise.reject(res)
    })
  }


}
export default Service
