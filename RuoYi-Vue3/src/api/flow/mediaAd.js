import request from '@/utils/request'

// 查询媒体广告位列表
export function listMediaAd(query) {
  return request({
    url: '/flow/mediaAd/list',
    method: 'get',
    params: query
  })
}

// 查询媒体广告位详细
export function getMediaAd(id) {
  return request({
    url: '/flow/mediaAd/' + id,
    method: 'get'
  })
}

// 新增媒体广告位
export function addMediaAd(data) {
  return request({
    url: '/flow/mediaAd',
    method: 'post',
    data: data
  })
}

// 修改媒体广告位
export function updateMediaAd(data) {
  return request({
    url: '/flow/mediaAd',
    method: 'put',
    data: data
  })
}

// 删除媒体广告位
export function delMediaAd(id) {
  return request({
    url: '/flow/mediaAd/' + id,
    method: 'delete'
  })
}

// 根据ssp广告位id查询匹配的预算方广告位列表
export function getMatchedDspSlots(sspSlotId) {
  return request({
    url: '/flow/mediaAd/matchedDspSlots/' + sspSlotId,
    method: 'get'
  })
}

// 保存媒体广告位的投放配置
export function saveLaunchConfig(data) {
  return request({
    url: '/flow/mediaAd/saveLaunchConfig',
    method: 'post',
    data: data
  })
}

// 根据媒体广告位ID查询投放配置列表
export function getLaunchConfig(sspSlotId) {
  return request({
    url: '/flow/mediaAd/launchConfig/' + sspSlotId,
    method: 'get'
  })
}
