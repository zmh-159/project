import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import { WSSHClient } from '@/api/pm/webssh/webssh'

Vue.use(Vuex)

// https://webpack.js.org/guides/dependency-management/#requirecontext
const modulesFiles = require.context('./modules', true, /\.js$/)

// you do not need `import app from './modules/app'`
// it will auto require all vuex module from modules file
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  // set './app.js' => 'app'
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules[moduleName] = value.default
  return modules
}, {})

const store = new Vuex.Store({
  modules,
  getters,
  state: {
    realTimeData: {},
    nodeSum: 0,
    entropy: 0,
    alarmreadSum: 0,
    alarmunreadSum: 0,
    alarmMessage: '',
    oldRealData: {},
    timer: '',
    isDataCenter: true,
    cpuBusy: 0,
    cpuNums: 0,
    diskTotal: 0,
    diskUse: 0,
    gpuUtilization: 0,
    memoryTotal: 0,
    memoryUsed: 0,
    totalInfo: [],
    a: ''
  },
  mutations: {
    realData() {
      const client = new WSSHClient('/webSocket/realTimeInfo')
      var that = this
      client.connect({
        onError: function(error) {
          // 连接失败回调
          console.log('连接失败' + error)
        },
        onConnect: function(data) {
          // 连接成功回调
          console.log('连接成功')
        },
        onClose: function() {
          // 连接关闭回调
          console.log('连接关闭')
        },
        onData: data => {
          // 收到数据时回调
          var response = JSON.parse(data)
          if (JSON.stringify(response['node_data']) !== '{}') {
            that.state.realTimeData = response['node_data']
          }
          that.state.nodeSum = response['node_sum']
          that.state.entropy = response['entropy']
          that.state.alarmreadSum = response['alarm_read_sum']
          that.state.alarmunreadSum = response['alarm_un_read_sum']
          that.state.alarmMessage = response['alarm_message']
          that.state.cpuBusy = response['cpu_busy']
          that.state.cpuNums = response['cpu_nums']
          that.state.diskTotal = response['disk_total']
          that.state.diskUse = response['disk_use']
          that.state.diskTotal = response['disk_total']
          that.state.gpuUtilization = response['gpu_utilization']
          that.state.memoryTotal = response['memory_total']
          that.state.memoryUsed = response['memory_used']
          that.state.totalInfo = [that.state.cpuBusy, that.state.cpuNums, that.state.diskTotal, that.state.diskUse, that.state.diskTotal, that.state.gpuUtilization, that.state.memoryTotal, that.state.memoryUsed]
        }
      })
      // var that = this
      // // 保证不延时
      // pmNode.queryData().then(function(response) {
      //   that.state.a = response
      //   console.log(response)
      //   if (JSON.stringify(response['node_data']) !== '{}') {
      //     that.state.realTimeData = response['node_data']
      //   }
      //   that.state.nodeSum = response['node_sum']
      //   that.state.entropy = response['entropy']
      //   that.state.alarmreadSum = response['alarm_read_sum']
      //   that.state.alarmunreadSum = response['alarm_un_read_sum']
      //   that.state.alarmMessage = response['alarm_message']
      //   that.state.cpuBusy = response['cpu_busy']
      //   that.state.cpuNums = response['cpu_nums']
      //   that.state.diskTotal = response['disk_total']
      //   that.state.diskUse = response['disk_use']
      //   that.state.diskTotal = response['disk_total']
      //   that.state.gpuUtilization = response['gpu_utilization']
      //   that.state.memoryTotal = response['memory_total']
      //   that.state.memoryUsed = response['memory_used']
      //   that.state.totalInfo = [that.state.cpuBusy, that.state.cpuNums, that.state.diskTotal, that.state.diskUse, that.state.diskTotal, that.state.gpuUtilization, that.state.memoryTotal, that.state.memoryUsed]
      // })
      // this.timer = setInterval(function() {
      //   pmNode.queryData().then(function(response) {
      //     if (JSON.stringify(response['node_data']) !== '{}') {
      //       that.state.realTimeData = response['node_data']
      //     }
      //     that.state.nodeSum = response['node_sum']
      //     that.state.entropy = response['entropy']
      //     that.state.alarmreadSum = response['alarm_read_sum']
      //     that.state.alarmunreadSum = response['alarm_un_read_sum']
      //     that.state.alarmMessage = response['alarm_message']
      //     that.state.cpuBusy = response['cpu_busy']
      //     that.state.cpuNums = response['cpu_nums']
      //     that.state.diskTotal = response['disk_total']
      //     that.state.diskUse = response['disk_use']
      //     that.state.diskTotal = response['disk_total']
      //     that.state.gpuUtilization = response['gpu_utilization']
      //     that.state.memoryTotal = response['memory_total']
      //     that.state.memoryUsed = response['memory_used']
      //     that.state.totalInfo = [that.state.cpuBusy, that.state.cpuNums, that.state.diskTotal, that.state.diskUse, that.state.diskTotal, that.state.gpuUtilization, that.state.memoryTotal, that.state.memoryUsed]
      //   })
      // }, 5000)
    }
  }
})

export default store
