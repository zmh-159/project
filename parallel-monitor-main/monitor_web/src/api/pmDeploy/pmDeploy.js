import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/pmDeploy',
    method: 'post',
    data
  })
}
export function install(data) {
  console.log(1)
  console.log(data)
  return request({
    url: 'api/pmDeploy/install?' + data,
    method: 'post'
    // data
  })
}
export function checkshell(data) {
  return request({
    url: 'api/pmDeploy/query' + data,
    method: 'get',
    data
  })
}
export function check(data) {
  return request({
    url: 'api/pmDeploy',
    method: 'get',
    data
  })
}
export function del(ids) {
  return request({
    url: 'api/pmDeploy/',
    method: 'delete',
    data: ids
  })
}
export function concheng(data) {
  return request({
    url: '/api/pmCollectConfig',
    method: 'put',
    data
  })
}
export function concheck(data) {
  return request({
    url: '/api/pmCollectConfig',
    method: 'get',
    data
  })
}
export function edit(data) {
  return request({
    url: 'api/pmDeploy',
    method: 'put',
    data
  })
}
export function checkdisk(data) {
  return request({
    url: '/api/detectNode/detectDisk',
    method: 'get',
    data
  })
}
export function checkmemory(data) {
  return request({
    url: 'api/detectNode/detectMemory' + data,
    data
  })
}
export function nodeDisk(data) {
  return request({
    url: '/api/pmNodeDisk' + data,
    data
  })
}
export function nodeDiskList(data) {
  return request({
    url: '/api/detectNode/diskInfo',
    method: 'get',
    data
  })
}
export default { add, edit, del, check, install, checkshell, concheng, concheck, checkdisk, checkmemory, nodeDisk, nodeDiskList }
