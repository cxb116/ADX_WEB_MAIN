import request from '@/utils/request'

// 查询支出修正列表
export function listCorrected(query) {
  return request({
    url: '/data/corrected/list',
    method: 'get',
    params: query
  })
}

// 查询支出修正详细
export function getCorrected(id) {
  return request({
    url: '/data/corrected/' + id,
    method: 'get'
  })
}

// 新增支出修正
export function addCorrected(data) {
  return request({
    url: '/data/corrected',
    method: 'post',
    data: data
  })
}

// 修改支出修正
export function updateCorrected(data) {
  return request({
    url: '/data/corrected',
    method: 'put',
    data: data
  })
}

// 删除支出修正
export function delCorrected(id) {
  return request({
    url: '/data/corrected/' + id,
    method: 'delete'
  })
}
