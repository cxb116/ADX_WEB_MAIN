import request from '@/utils/request'

// 查询预算收益导入列表
export function listIncome(query) {
  return request({
    url: '/dsp/income/list',
    method: 'get',
    params: query
  })
}

// 查询预算收益导入详细
export function getIncome(id) {
  return request({
    url: '/dsp/income/' + id,
    method: 'get'
  })
}

// 新增预算收益导入
export function addIncome(data) {
  return request({
    url: '/dsp/income',
    method: 'post',
    data: data
  })
}

// 修改预算收益导入
export function updateIncome(data) {
  return request({
    url: '/dsp/income',
    method: 'put',
    data: data
  })
}

// 删除预算收益导入
export function delIncome(id) {
  return request({
    url: '/dsp/income/' + id,
    method: 'delete'
  })
}
