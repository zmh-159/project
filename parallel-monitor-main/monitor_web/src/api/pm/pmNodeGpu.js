import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmNodeGpu',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/pmNodeGpu/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/pmNodeGpu',
    method: 'put',
    data
  })
}

export function query(data) {
  console.log(data)
  // data转换为字符串
  return request({
    url: 'api/pmNodeGpu?gpuId=' + data,
    data
  })
}
export default { add, edit, del, query }
