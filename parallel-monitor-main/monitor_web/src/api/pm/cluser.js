import request from '@/utils/request'

export function query(data) {
  return request({
    url: 'api/cluser' + '?' + 'cmd=' + data,
    method: '',
    data
  })
}

export default { query }
