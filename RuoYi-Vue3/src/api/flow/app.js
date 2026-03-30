import request from '@/utils/request'

// 查询应用管理列表
export function listApp(query) {
  return request({
    url: '/flow/app/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getApp(id) {
  return request({
    url: '/flow/app/' + id,
    method: 'get'
  })
}

// 新增应用管理
export function addApp(data) {
  return request({
    url: '/flow/app',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateApp(data) {
  return request({
    url: '/flow/app',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delApp(id) {
  return request({
    url: '/flow/app/' + id,
    method: 'delete'
  })
}
