import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeMemory',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeMemory/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeMemory',
    method: 'put',
    data
  })
}

export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeMemory?memoryId=' + data,
    data
  })
}
export default { add, edit, del, query }
