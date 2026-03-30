import request from '@/utils/request'

// 查询广告场景列表
export function listScene(query) {
  return request({
    url: '/ad/scene/list',
    method: 'get',
    params: query
  })
}

// 查询广告场景详细
export function getScene(id) {
  return request({
    url: '/ad/scene/' + id,
    method: 'get'
  })
}

// 新增广告场景
export function addScene(data) {
  return request({
    url: '/ad/scene',
    method: 'post',
    data: data
  })
}

// 修改广告场景
export function updateScene(data) {
  return request({
    url: '/ad/scene',
    method: 'put',
    data: data
  })
}

// 删除广告场景
export function delScene(id) {
  return request({
    url: '/ad/scene/' + id,
    method: 'delete'
  })
}
