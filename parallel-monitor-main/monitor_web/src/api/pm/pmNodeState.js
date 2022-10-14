import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeState',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeState/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeState',
    method: 'put',
    data
  })
}
// 初始化
export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeState?nodeId=' + data + '&page=0&size=40&sort=createTime,desc',
    data
  })
}
// 时间选择器
export function queryTime(data, data2) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeState?nodeId=' + data + '&createTime=' + data2[0] + '&createTime=' + data2[1] + '&page=0&size=40&sort=createTime,desc',
    data,
    data2
  })
}
export function everydayData(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeState/dailyTotalUsage',
    data
  })
}

export default { add, edit, del, query, queryTime, everydayData }
