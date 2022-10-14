import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeInfo',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeInfo/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeInfo',
    method: 'put',
    data
  })
}

export function query(data) {
  console.log(data)
  return request({
    url: 'api/pmNodeInfo?collectTime=' + data[0] + '&collectTime=' + data[1] + '&page=0&size=100',
    data
  })
}

export function realData(data) {
  return request({
    url: 'api/pmNodeInfo/realData',
    data
  })
}

export default { add, edit, del, query, realData }
