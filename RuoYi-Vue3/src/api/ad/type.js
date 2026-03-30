import request from '@/utils/request'

// 查询广告类型列表
export function listType(query) {
  return request({
    url: '/ad/type/list',
    method: 'get',
    params: query
  })
}

// 查询广告类型详细
export function getType(id) {
  return request({
    url: '/ad/type/' + id,
    method: 'get'
  })
}

// 新增广告类型
export function addType(data) {
  return request({
    url: '/ad/type',
    method: 'post',
    data: data
  })
}

// 修改广告类型
export function updateType(data) {
  return request({
    url: '/ad/type',
    method: 'put',
    data: data
  })
}

// 删除广告类型
export function delType(id) {
  return request({
    url: '/ad/type/' + id,
    method: 'delete'
  })
}

// 获取广告类型分布统计
export function getAdTypeDistribution() {
  return request({
    url: '/ad/type/distribution',
    method: 'get'
  })
}
