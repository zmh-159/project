import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeCpu',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeCpu/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeCpu',
    method: 'put',
    data
  })
}
export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeCpu?cpuId=' + data,
    data
  })
}
export function queryNodeId(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeCpu?nodeId=' + data,
    data
  })
}
export default { add, edit, del, query, queryNodeId }
