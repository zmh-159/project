// 定义一些公共过滤器
// 问候语时区过滤器
export const dateTranGreetings = (value) => {
  var realTime = new Date(value)
  var time = new Date()
  time = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate()
  if (isDuringDate(time + ' 05:01:00', time + ' 06:59:59', realTime) === true) {
    return '清晨好,'
  } else if (isDuringDate(time + ' 07:00:00', time + ' 08:59:59', realTime) === true) {
    return '早上好,'
  } else if (isDuringDate(time + ' 09:00:00', time + ' 12:00:59', realTime) === true) {
    return '上午好,'
  } else if (isDuringDate(time + ' 12:01:00', time + ' 13:59:59', realTime) === true) {
    return '中午好,'
  } else if (isDuringDate(time + ' 14:00:00', time + ' 17:59:59', realTime) === true) {
    return '下午好,'
  } else if (isDuringDate(time + ' 18:00:00', time + ' 18:59:59', realTime) === true) {
    return '傍晚好,'
  } else if (isDuringDate(time + ' 19:00:00', time + ' 23:59:59', realTime) === true) {
    return '晚上好,'
  } else if (isDuringDate(time + ' 24:00:00', time + ' 05:00:59', realTime) === true) {
    return '凌晨好,'
  }
}
function isDuringDate(beginDateStr, endDateStr, value) {
  var beginDate = new Date(beginDateStr)
  var endDate = new Date(endDateStr)
  if (value >= beginDate && value <= endDate) {
    return true
  }
  return false
}
// 单位转换过滤器
export const unitFilter = (value, unit) => {
  switch (unit) {
    // GB超过1024转换为TB
    case 'TB':
      if (value > 1024) {
        return (value / 1024).toFixed(2) + 'TB'
      } else {
        return value + 'GB'
      }
    case 'GHz':
      if (value > 1024) {
        return (value / 1024).toFixed(2) + 'GHz'
      } else {
        return value + 'MHz'
      }
    case ' ':
      return value
  }
}
