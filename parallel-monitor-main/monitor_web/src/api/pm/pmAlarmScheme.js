import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmAlarmScheme',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmAlarmScheme/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmAlarmScheme',
    method: 'put',
    data
  })
}

// export function addMyScheme(data) {
//   return request({
//     url: 'api/pmAlarmScheme?' + data,
//     method: 'post'
//   })
// }
export default { add, edit, del }
