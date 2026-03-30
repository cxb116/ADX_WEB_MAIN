import request from '@/utils/request'

// 查询预算广告位列表
export function listInfo(query) {
  return request({
    url: '/budget/info/list',
    method: 'get',
    params: query
  })
}

// 查询预算广告位详细
export function getInfo(id) {
  return request({
    url: '/budget/info/' + id,
    method: 'get'
  })
}

// 新增预算广告位
export function addInfo(data) {
  return request({
    url: '/budget/info',
    method: 'post',
    data: data
  })
}

// 修改预算广告位
export function updateInfo(data) {
  return request({
    url: '/budget/info',
    method: 'put',
    data: data
  })
}

// 删除预算广告位
export function delInfo(id) {
  return request({
    url: '/budget/info/' + id,
    method: 'delete'
  })
}
