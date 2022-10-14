import request from '@/utils/request'
// 发送测试指令
export function cpuTest(data) {
  return request({
    url: '/api/server/cpuTest' + data
  })
}

// 获取节点cpu核心信息
export function cpuInfo() {
  return request({
    url: '/api/server/cpuInfo'
  })
}

// 获取测试结果 无需传入节点id
export function cpuTestRes(data) {
  return request({
    url: '/api/server/cpuTestRes' + data
  })
}
export default { cpuTest, cpuInfo, cpuTestRes }
