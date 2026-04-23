import request from '@/utils/request'

// 查询导入流水列表
export function listExce(query) {
  return request({
    url: '/system/exce/list',
    method: 'get',
    params: query
  })
}

// 查询导入流水详细
export function getExce(id) {
  return request({
    url: '/system/exce/' + id,
    method: 'get'
  })
}

// 新增导入流水
export function addExce(data) {
  return request({
    url: '/system/exce',
    method: 'post',
    data: data
  })
}

// 修改导入流水
export function updateExce(data) {
  return request({
    url: '/system/exce',
    method: 'put',
    data: data
  })
}

// 删除导入流水
export function delExce(id) {
  return request({
    url: '/system/exce/' + id,
    method: 'delete'
  })
}
