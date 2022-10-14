import request from '@/utils/request'

export function add(data) {
  console.log(data)
  return request({
    url: 'api/pmChartScheme',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmChartScheme/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  // console.log(data)
  return request({
    url: 'api/pmChartScheme',
    method: 'put',
    data
  })
}

export function queryUserId(data) {
  return request({
    url: 'api/pmChartScheme?userId=' + data + '&sort=position,asc',
    data
  })
}

export default { add, edit, del, queryUserId }
