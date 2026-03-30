import request from '@/utils/request'

// 查询样式尺寸列表
export function listSize(query) {
  return request({
    url: '/ad/size/list',
    method: 'get',
    params: query
  })
}

// 查询样式尺寸详细
export function getSize(id) {
  return request({
    url: '/ad/size/' + id,
    method: 'get'
  })
}

// 新增样式尺寸
export function addSize(data) {
  return request({
    url: '/ad/size',
    method: 'post',
    data: data
  })
}

// 修改样式尺寸
export function updateSize(data) {
  return request({
    url: '/ad/size',
    method: 'put',
    data: data
  })
}

// 删除样式尺寸
export function delSize(id) {
  return request({
    url: '/ad/size/' + id,
    method: 'delete'
  })
}
