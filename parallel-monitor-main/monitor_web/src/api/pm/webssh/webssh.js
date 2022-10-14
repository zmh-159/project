
// '/webSocket/realTimeInfo?token='
import { getToken } from '@/utils/auth'
function WSSHClient(url) {
  WSSHClient.prototype.basicUrl = url
}

WSSHClient.prototype._generateEndpoint = function() {
  var endpoint = process.env.VUE_APP_WS_API + WSSHClient.prototype.basicUrl + '?token=' + getToken()
  return endpoint
}

WSSHClient.prototype.connect = function(options) {
  var endpoint = this._generateEndpoint()
  if (window.WebSocket) {
    // 如果支持websocket
    this._connection = new WebSocket(endpoint)
  } else {
    // 否则报错
    options.onError('WebSocket Not Supported')
    return
  }

  this._connection.onopen = function() {
    options.onConnect()
  }

  this._connection.onmessage = function(evt) {
    var data = evt.data.toString()
    // data = base64.decode(data);
    options.onData(data)
  }

  this._connection.onclose = function(evt) {
    options.onClose()
  }
}

WSSHClient.prototype.send = function(data) {
  this._connection.send(JSON.stringify(data))
}

WSSHClient.prototype.sendInitData = function(options) {
  // 连接参数
  this._connection.send(JSON.stringify(options))
}

WSSHClient.prototype.sendClientData = function(data, col, row) {
  // 发送指令
  this._connection.send(JSON.stringify({ 'operate': 'command', 'command': data, cols: col, rows: row }))
}

export {
  WSSHClient
}

