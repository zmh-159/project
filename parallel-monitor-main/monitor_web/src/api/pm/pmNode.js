import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNode',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNode/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNode',
    method: 'put',
    data
  })
}
// 查询某个节点的信息
export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode?nodeId=' + data,
    data
  })
}

// 请求所有信息
export function queryAllData(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode',
    data
  })
}

// 判断是否离线
export function offline(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode/statusInfo',
    data
  })
}
export function queryData(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode/realTimeInfo',
    data
  })
}
export function queryNodeNums(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode/nodeSum',
    data
  })
}
export function querySingleNodeData(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNode/detail?nodeId=' + data,
    data
  })
}

export default { add, edit, del, query, queryData, offline, queryAllData, queryNodeNums, querySingleNodeData }
