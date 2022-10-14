import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeCpuState',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeCpuState/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeCpuState',
    method: 'put',
    data
  })
}

export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeCpuState?cpuId=' + data + '&page=0&size=40&sort=createTime,desc',
    data
  })
}
export function queryTime(data, data2) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeCpuState?cpuId=' + data + '&createTime=' + data2[0] + '&createTime=' + data2[1] + '&page=0&size=40&sort=createTime,desc',
    data,
    data2
  })
}
export default { add, edit, del, query, queryTime }
