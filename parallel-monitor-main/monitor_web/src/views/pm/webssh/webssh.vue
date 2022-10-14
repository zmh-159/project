<template>
  <section>
    <div id="terminal" class="console" style="overflow: hidden" />
  </section>
</template>
<script>
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { WSSHClient } from '@/api/pm/webssh/webssh'
export default {
  props: {
    socketURI: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loginData: {},
      term: null,
      socket: null,
      SetOut: false,
      isKey: false
    }
  },
  mounted() {
    if (localStorage.getItem('loginData')) {
      this.loginData = JSON.parse(localStorage.getItem('loginData'))
      console.log(this.loginData)
    }
    this.initTerm()
  },
  beforeDestroy() {
    localStorage.clear()
  },
  methods: {
    // Xterm主题
    initTerm() {
      var that = this
      var options = {
        operate: 'connect',
        nodeId: that.loginData.nodeId,
        port: that.loginData.port,
        username: that.loginData.usrname,
        password: that.loginData.password
      }
      const client = new WSSHClient('/webSocket/webssh')
      var term = new Terminal({
        macOptionIsMeta: true,
        allowProposedApi: true,
        convertEol: true,
        enableBold: true,
        drawBoldTextInBrightColors: true,
        experimentalCharAtlas: 'dynamic',
        cursorBlink: true, // 光标闪烁
        cursorStyle: 'block', // 光标样式  null | 'block' | 'underline' | 'bar'
        scrollback: 40, // 回滚
        tabStopWidth: 4, // 制表宽度
        screenKeys: true,
        fontFamily: 'Ubuntu mono',
        fontWeight: 'bold',
        cols: parseInt(document.body.clientWidth / 10),
        rows: parseInt(document.body.clientHeight / 22),
        fontSize: 21,
        theme: {
          foreground: '#dddddd', // 字体
          background: '#222222', // 背景色
          cursor: '#dddddd'
        }
      })
      term.open(document.getElementById('terminal'))
      term.onData(function(data) {
        client.sendClientData(data, parseInt(document.body.clientWidth / 10), parseInt(document.body.clientHeight / 22))
      })
      term.write('\x1b\[33m' + 'Connecting...' + '\x1b[0m')
      client.connect({
        onError: function(error) {
          // 连接失败回调
          term.write('\x1b\[31m' + 'Error: ' + error + '\r\n')
        },
        onConnect: function() {
          // 连接成功回调
          client.sendInitData(options)
          term.onResize(function(data) {
            // console.log(data)
            client.sendClientData('', data.cols, data.rows)
          })
        },
        onClose: function() {
          // 连接关闭回调
          term.write('\x1b\[31m' + '\rconnection closed')
        },
        onData: function(data) {
          // 收到数据时回调
          console.log(data)
          term.write(data)
        }
      })
      const myObserver = new ResizeObserver(entries => {
        entries.forEach(entry => {
          term.resize(parseInt(document.body.clientWidth / 10), parseInt(document.body.clientHeight / 22))
        })
      })
      myObserver.observe(document.body)
    }
  }
}
</script>

<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
