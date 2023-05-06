import request from '@/utils/request'

const permissionApi = {
  queryAuthedPermission: '/api/permission/routes',
  queryPermissions: '/api/permission/permissions'
}

class Service {
  /**
   * @description POST 查询授权菜单权限
   */
  static postAuthPermission(params: any) {
    return request({
      url: permissionApi.queryAuthedPermission,
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
   * @description POST 查询授权菜单权限
   */
  static postPermissions(params: any) {
    return request({
      url: permissionApi.queryPermissions,
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
