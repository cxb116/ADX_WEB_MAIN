import request from '@/utils/request'

// 查询媒体数据报表列表
export function listData_ssp_slot(query) {
  return request({
    url: '/data/data_ssp_slot/list',
    method: 'get',
    params: query
  })
}


// 根据预算dspSlotId 查询媒体子表数据
export function listData_ssp_slot_list(query) {
  return request({
    url: '/data/data_ssp_slot/dataDspSlotList',
    method: 'get',
    params: query
  })
}

// 查询媒体数据报表详细
export function getData_ssp_slot(id) {
  return request({
    url: '/data/data_ssp_slot/' + id,
    method: 'get'
  })
}

// 新增媒体数据报表
export function addData_ssp_slot(data) {
  return request({
    url: '/data/data_ssp_slot',
    method: 'post',
    data: data
  })
}

// 修改媒体数据报表
export function updateData_ssp_slot(data) {
  return request({
    url: '/data/data_ssp_slot',
    method: 'put',
    data: data
  })
}

// 删除媒体数据报表
export function delData_ssp_slot(id) {
  return request({
    url: '/data/data_ssp_slot/' + id,
    method: 'delete'
  })
}
