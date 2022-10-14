import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeNetwork',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeNetwork/',
    method: 'delete',
    data: ids
  })
}
export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmNodeNetwork?networkId=' + data,
    data
  })
}
export function edit(data) {
  return request({
    url: 'api/pmNodeNetwork',
    method: 'put',
    data
  })
}

export default { add, edit, del, query }
