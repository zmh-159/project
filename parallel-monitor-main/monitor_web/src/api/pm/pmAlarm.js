import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmAlarm',
    method: 'post',
    data
  })
}
export function check(data) {
  return request({
    url: 'api/pmAlarm',
    method: 'get',
    data
  })
}
export function remarkPm(data) {
  console.log('成功了---------------------------')
  return request({
    url: '/api/pmAlarm/remark' + data,
    // method: 'get',
    data
  })
}
export function del(ids) {
  return request({
    url: 'api/pmAlarm/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmAlarm',
    method: 'put',
    data
  })
}
// 请求所有信息
export function query(data) {
  // data转换为字符串
  return request({
    url: 'api/pmAlarm',
    data
  })
}

export default { add, edit, del, query, check, remarkPm }
