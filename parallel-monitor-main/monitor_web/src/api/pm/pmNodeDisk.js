import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeDisk',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeDisk/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeDisk',
    method: 'put',
    data
  })
}

export function query(data) {
  console.log(data)
  // data转换为字符串
  return request({
    url: 'api/pmNodeDisk?diskId=' + data,
    data
  })
}

export default { add, edit, del, query }
