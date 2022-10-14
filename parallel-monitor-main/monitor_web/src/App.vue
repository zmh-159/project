<template>
  <div
    id="app"
  >
    <!-- 刷新不闪烁 -->
    <div id="app" class="main-app">
      <router-view v-if="isRouterAlive" />
    </div>
    <!-- 全局报警页面展示 -->
    <!-- <div v-show="showel" v-if="headerShow" class="alrm">
      <div class="top-title">
        <div class="num">
          <el-badge :value="alrmnum" class="item" :max="99">
            <span size="small" class="baojing" @click="$router.push('/Alarm/alarmHistory');change()">报警</span>
          </el-badge>
        </div>
        <div class="close" @click="close">
        <i class="close" />
        </div>
      </div>
      <div class="alarmtext">
        <p class="nodeid">{{ message }}</p>
      </div>
    </div> -->
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

// import Ball from '@/components/Echarts/Ball'
// import echarts from 'echarts'
// require('echarts/theme/macarons') // echarts theme
// import { debounce } from '@/utils'
export default {
  num: '',
  name: 'App',
  // components: {
  //   Ball
  // },
  data() {
    return {
      alrmnum: '',
      route1: false,
      message: '注意集群节点有报警',
      headerShow: true,
      showel: false,
      moveDataelse: {
        x: null,
        y: null
      },
      // 缩放比
      screenRatio: Math.round((window.outerWidth / window.innerWidth) * 100),
      isRouterAlive: true,
      docEle: document.documentElement
    }
  },
  provide() { // 父组件中通过provide来提供变量，在子组件中通过inject来注入变量。
    return {
      reload: this.reload
    }
  },
  computed: {
    data() {
      return this.$store.state.alarmSum
    },
    realTimeData() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    },
    alarmunreadSum() {
      return this.$store.state.alarmunreadSum
    },
    visitedViews() {
      return this.$store.state.tagsView.visitedViews
    },
    alarm_message() {
      return JSON.parse(JSON.stringify(this.$store.state.alarmMessage))
    }
  },
  watch: {
    $route(to, from) {
      if (this.$route.path === '/login' || this.$route.path === '/webssh' || this.$route.path === '/mydash') {
        this.headerShow = false
      } else {
        this.headerShow = true
        this.route1 = true
      }
    },
    screenWidth(newold, oldval) {
      this.widthclassinfo2()
    },
    alarm_message: {
      handler(newVal, oldVal) {
        if (this.$store.state.alarmMessage !== '') {
          this.message = newVal
          console.log(this.message)
        }
      }
    },
    alarmunreadSum: {
      handler(newVal, oldVal) {
        this.alrmnum = newVal
        if (newVal === 0) {
          this.showel = false
        } else {
          this.showel = true
        }
      },
      immediate: true
    },
    screenRatio: {
      immediate: true, // 开启一直监听
      handler: function(val) { // val是获取到的缩放比
        if (val < 125) { // 不同缩放比下进行不同的操作
          document.querySelector('#app').classList.add('small')
        } else {
          document.querySelector('#app').classList.remove('small')
        }
      }
    },
    realTimeData: {
      immediate: true,
      handler(val) {
        if (Object.keys(val).length === 0 && getToken !== undefined) {
          this.$store.commit('realData')
        }
      }
    }
  },
  created() {
    this.setHtmlFontSize()
  },
  mounted() {
    window.addEventListener('mousewheel', function(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    }, { passive: false })

    // firefox
    window.addEventListener('DOMMouseScroll', function(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    }, { passive: false })
    window.onresize = () => { // 不使用window.onresize只能监听一次，使用可以一直监听
      return (() => {
        window.screenRatio = Math.round((window.outerWidth / window.innerWidth) * 100)
        this.screenRatio = window.screenRatio
      })()
    }
    // 页面刷新前缓存和赋值
    this.beforeUnload()
  },
  methods: {
    change() {
      this.showel = false
    },
    // 点击关闭
    close() {
      this.showel = false
    },
    showTable() {
      this.showel1 = !this.showel1
    },
    reload() {
      this.isRouterAlive = false // 先关闭，
      this.$nextTick(function() {
        this.isRouterAlive = true // 再打开
      })
    },
    beforeUnload() {
      // 监听页面刷新
      window.addEventListener('beforeunload', () => {
        // visitedViews数据结构太复杂无法直接JSON.stringify处理，先转换需要的数据
        const tabViews = this.visitedViews.map(item => {
          return {
            fullPath: item.fullPath,
            hash: item.hash,
            meta: { ...item.meta },
            name: item.name,
            params: { ...item.params },
            path: item.path,
            query: { ...item.query },
            title: item.title
          }
        })
        sessionStorage.setItem('tabViews', JSON.stringify(tabViews))
      })
      // 页面初始化加载判断缓存中是否有数据
      const oldViews = JSON.parse(sessionStorage.getItem('tabViews')) || []
      if (oldViews.length > 0) {
        this.$store.state.tagsView.visitedViews = oldViews
      }
    },
    setHtmlFontSize() {
      console.log(window.screen.width)
      console.log(window.screen.height)
      this.docEle.style.fontSize = (window.screen.width / 10) + 'px'
      console.log(this.docEle.style.fontSize)
    }
  }
}
</script>
<style lang="scss" scoped>
.main-app{
  font-size: 16px;
  position: relative;
}
</style>
