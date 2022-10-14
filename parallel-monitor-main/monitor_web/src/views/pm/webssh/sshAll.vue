<template>
  <div class="container">
    <div id="terminal-container" />
  </div>
</template>

<script>
import 'xterm/css/xterm.css'
import cluser from '@/api/pm/cluser'
import { Terminal } from 'xterm'

export default {
  data() {
    return {
      database: ''
    }
  },
  created() {

  },
  mounted() {
    const terminalContainer = document.getElementById('terminal-container')
    const term = new Terminal({
      cols: 300,
      rows: 30,
      cursorBlink: true, // 光标闪烁
      cursorStyle: 'block', // 光标样式  null | 'block' | 'underline' | 'bar'
      scrollback: 800, // 回滚
      tabStopWidth: 8, // 制表宽度
      screenKeys: true,
      theme: {
        foreground: 'yellow', // 字体
        background: '#060101', // 背景色
        cursor: 'help' // 设置光标
      }
    })
    term.open(terminalContainer)
    var that = this
    function runFakeTerminal() {
      if (term._initialized) {
        return
      }

      term._initialized = true

      term.prompt = () => {
        term.write('\r\n~$ ')
      }

      term.writeln('Welcome to xterm.js')
      prompt(term)

      term.onKey(e => {
        const printable = !e.domEvent.altKey && !e.domEvent.altGraphKey && !e.domEvent.ctrlKey && !e.domEvent.metaKey
        // enter key
        if (e.domEvent.keyCode === 13) {
          cluser.query(that.database).then(function(response) {
            term.write('\r\n')
            var result = response.res
            term.write(result)
            console.log(response)
            prompt(term)
            that.database = ''
          })
        } else if (e.domEvent.keyCode === 8) { // BackSpace key
          if (term._core.buffer.x > 2) {
            term.write('\b \b')
          }
        } else if (printable) {
          that.database = that.database + e.key
          term.write(e.key)
        }
        console.log(e.key)

        // console.log(e.key)
      })
    }

    function prompt(term) {
      term.write('\r\n~$ ')
    }
    runFakeTerminal()
  }
}
</script>
