import request from '@/utils/request'

// 查询广告管理列表
export function listScene(query) {
  return request({
    url: '/flow/scene/list',
    method: 'get',
    params: query
  })
}

// 查询广告管理详细
export function getScene(id) {
  return request({
    url: '/flow/scene/' + id,
    method: 'get'
  })
}

// 新增广告管理
export function addScene(data) {
  return request({
    url: '/flow/scene',
    method: 'post',
    data: data
  })
}

// 修改广告管理
export function updateScene(data) {
  return request({
    url: '/flow/scene',
    method: 'put',
    data: data
  })
}

// 删除广告管理
export function delScene(id) {
  return request({
    url: '/flow/scene/' + id,
    method: 'delete'
  })
}
