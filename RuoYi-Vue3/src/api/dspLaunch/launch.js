import request from '@/utils/request'

// 查询广告生成列表
export function listLaunch(query) {
  return request({
    url: '/dspLaunch/launch/list',
    method: 'get',
    params: query
  })
}

// 查询广告生成详细
export function getLaunch(id) {
  return request({
    url: '/dspLaunch/launch/' + id,
    method: 'get'
  })
}

// 新增广告生成
export function addLaunch(data) {
  return request({
    url: '/dspLaunch/launch',
    method: 'post',
    data: data
  })
}

// 修改广告生成
export function updateLaunch(data) {
  return request({
    url: '/dspLaunch/launch',
    method: 'put',
    data: data
  })
}

// 删除广告生成
export function delLaunch(id) {
  return request({
    url: '/dspLaunch/launch/' + id,
    method: 'delete'
  })
}
