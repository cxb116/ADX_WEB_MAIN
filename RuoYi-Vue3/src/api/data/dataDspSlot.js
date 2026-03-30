import request from '@/utils/request'

// 查询预算报表列表
export function listDataDspSlot(query) {
  return request({
    url: '/system/dataDspSlot/list',
    method: 'get',
    params: query
  })
}

// 查询预算报表详细
export function getDataDspSlot(id) {
  return request({
    url: '/system/dataDspSlot/' + id,
    method: 'get'
  })
}

// 新增预算报表
export function addDataDspSlot(data) {
  return request({
    url: '/system/dataDspSlot',
    method: 'post',
    data: data
  })
}

// 修改预算报表
export function updateDataDspSlot(data) {
  return request({
    url: '/system/dataDspSlot',
    method: 'put',
    data: data
  })
}

// 删除预算报表
export function delDataDspSlot(id) {
  return request({
    url: '/system/dataDspSlot/' + id,
    method: 'delete'
  })
}

// 获取当月汇总统计数据
export function getMonthlySummary(tableName) {
  return request({
    url: '/system/dataDspSlot/summary',
    method: 'get',
    params: { tableName }
  })
}

// 获取趋势数据
export function getTrendData(startDate, endDate) {
  return request({
    url: '/system/dataDspSlot/trend',
    method: 'get',
    params: { startDate, endDate }
  })
}

// 获取收益数据
export function getRevenueData(days) {
  return request({
    url: '/system/dataDspSlot/revenue',
    method: 'get',
    params: { days }
  })
}

// 获取今日填充率数据
export function getTodayFillRate(tableName) {
  return request({
    url: '/system/dataDspSlot/todayFillRate',
    method: 'get',
    params: { tableName }
  })
}

// 获取近7天填充率数据
export function getFillRateData(days) {
  return request({
    url: '/system/dataDspSlot/fillRate',
    method: 'get',
    params: { days }
  })
}
