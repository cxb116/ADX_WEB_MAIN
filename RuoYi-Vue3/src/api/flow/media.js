import request from '@/utils/request'

// 获取媒体应用级联数据
export function getMediaAppCascader() {
  return request({
    url: '/flow/media/cascader',
    method: 'get'
  })
}

// 查询媒体管理列表
export function listMedia(query) {
  return request({
    url: '/flow/media/list',
    method: 'get',
    params: query
  })
}

// 查询媒体管理详细
export function getMedia(id) {
  return request({
    url: '/flow/media/' + id,
    method: 'get'
  })
}

// 新增媒体管理
export function addMedia(data) {
  return request({
    url: '/flow/media',
    method: 'post',
    data: data
  })
}

// 修改媒体管理
export function updateMedia(data) {
  return request({
    url: '/flow/media',
    method: 'put',
    data: data
  })
}

// 删除媒体管理
export function delMedia(id) {
  return request({
    url: '/flow/media/' + id,
    method: 'delete'
  })
}
