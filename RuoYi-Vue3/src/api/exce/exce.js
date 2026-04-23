import request from '@/utils/request'

// 查询收益导入exce列表
export function listExce(query) {
  return request({
    url: '/exce/exce/list',
    method: 'get',
    params: query
  })
}

// 查询收益导入exce详细
export function getExce(id) {
  return request({
    url: '/exce/exce/' + id,
    method: 'get'
  })
}

// 新增收益导入exce
export function addExce(data) {
  return request({
    url: '/exce/exce',
    method: 'post',
    data: data
  })
}

// 修改收益导入exce
export function updateExce(data) {
  return request({
    url: '/exce/exce',
    method: 'put',
    data: data
  })
}

// 删除收益导入exce
export function delExce(id) {
  return request({
    url: '/exce/exce/' + id,
    method: 'delete'
  })
}
