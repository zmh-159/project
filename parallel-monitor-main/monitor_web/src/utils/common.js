var tools = {
  // 时间换算工具
  getNowTime: function() {
    var time = new Date()
    var month = null
    var date = null
    var minite = null
    var second = null
    if (Number(time.getMonth()) < 10) {
      month = '0' + (time.getMonth() + 1)
    } else {
      month = time.getMonth() + 1
    }
    if (Number(time.getDate()) < 10) {
      date = '0' + time.getDate()
    } else {
      date = time.getDate()
    }
    if (Number(time.getMinutes()) < 10) {
      minite = '0' + time.getMinutes()
    } else {
      minite = time.getMinutes()
    }
    if (Number(time.getSeconds()) < 10) {
      second = '0' + time.getSeconds()
    } else {
      second = time.getSeconds()
    }
    time = time.getFullYear() + '-' + month + '-' + date + ' ' + time.getHours() + ':' + minite + ':' + second
    return time
  },
  // 单位换算工具
  getUnit: function(data) {
    if (data === '用户使用率' || data === '系统使用率' || data === '总使用率' || data === '显卡使用率' || data === '显存使用率' || data === '内存使用率' || data === '交换区使用率') {
      return '%'
    }
    if (data === '温度' || data === '显卡温度' || data === '显存温度') {
      return '°C'
    }
    if (data === '磁盘读速度' || data === '磁盘写速度' || data === '网卡上传速度' || data === '网卡下载速度') {
      return 'KiB/s'
    }
    if (data === '频率' || data === '显卡频率') {
      return 'GHz'
    }
    if (data === '显卡功率') {
      return 'W'
    }
  },
  fontSize: function(val) {
    const nowClientWidth = document.documentElement.clientWidth
    return val * (nowClientWidth / 1920)
  }
}
export default tools
