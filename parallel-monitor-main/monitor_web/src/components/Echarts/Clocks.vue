<template>
  <div ref="timeblock" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme
import { mapState } from 'vuex'
export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
    },
    // 父组件数据
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    // var that = this
    return {
      chart: null,
      // 定义一个数组变量用于存放配置
      series: [],
      chartTypeData: {
        // backgroundColor: '#1b1b1b',
        series: [
          {
            name: '小时',
            type: 'gauge',
            min: 0,
            max: 12,
            splitNumber: 12,
            radius: '50%',
            startAngle: 90,
            endAngle: -269.999,
            animation: 0,
            axisLine: {
              show: false,
              // 坐标轴线
              lineStyle: {
                // 属性lineStyle控制线条样式
                // color: [[0.1, '#0063B8'], [0.2, '#1C5DA8'], [0.3, '#32599C'], [0.4, '#42548F'], [0.5, '#5B4D7D'], [0.6, '#71466D'],
                //   [0.7, '#7B4365'], [0.8, '#853E5A'], [0.9, '#953A4F'], [1, '#A43543']],
                color: [[1, 'rgba(64,158,255,0.8)']],
                width: 0,
                shadowColor: 'rgba(0, 0, 0, 0.4)',
                shadowBlur: 10
              }
            },
            axisLabel: {
              // color: '#fff',
              show: 0,
              fontSize: 0
            },
            axisTick: {
              // 坐标轴小标记
              show: false
            },
            splitLine: {
              // 分隔线
              show: true,
              length: '5%',
              lineStyle: {
                color: '#fff',
                width: 20,
                cap: 'round'
              }
            },
            pointer: {
              // 分隔线
              shadowColor: '#fff', // 默认透明
              shadowBlur: 10,
              length: '50%',
              width: 3,
              color: '#ffffff',
              itemStyle: {
                color: '#FFFFFF'
              }
            },
            detail: {
              show: false
            },
            data: [{ value: 6, name: '' }]
          },
          {
            name: '分钟',
            type: 'gauge',
            min: 0,
            max: 12,
            splitNumber: 12,
            radius: '60%',
            startAngle: 90,
            endAngle: -269.999,
            animation: 0,
            axisLine: {
              // 坐标轴线
              show: false,
              lineStyle: {
                // 属性lineStyle控制线条样式
                color: [[1, '#f4b400']],
                width: 0
              }
            },
            axisLabel: {
              show: false
            },
            axisTick: {
              // 坐标轴小标记
              show: false
            },
            splitLine: {
              // 分隔线
              show: false,
              fontSize: 0
            },
            pointer: {
              // 分隔线
              shadowColor: '#fff', // 默认透明
              shadowBlur: 10,
              length: '60%',
              width: 3
            },
            detail: {
              show: false
            },
            data: [{ value: 3, name: '' }]
          },
          {
            name: '秒',
            type: 'gauge',
            min: 0,
            max: 12,
            splitNumber: 12,
            radius: '60%',
            startAngle: 90,
            endAngle: -269.999,
            animation: 0,
            axisLine: {
              // 坐标轴线
              show: false,
              lineStyle: {
                // 属性lineStyle控制线条样式
                color: [[1, '#fff']],
                width: 0,
                shadowColor: '#fff', // 默认透明
                shadowBlur: 1
              }
            },
            axisLabel: {
              // 坐标轴小标记
              show: 0,
              textStyle: {
                // 属性lineStyle控制线条样式
                fontWeight: 'bolder',
                color: '#fff',
                shadowColor: '#fff', // 默认透明
                shadowBlur: 10
              }
            },
            axisTick: {
              // show: false,
              // 坐标轴小标记
              length: 1, // 属性length控制线长
              lineStyle: {
                // 属性lineStyle控制线条样式
                color: 'auto',
                shadowColor: '#fff', // 默认透明
                shadowBlur: 5
              }
            },
            splitLine: {
              // 分隔线
              show: false,
              length: 4, // 属性length控制线长
              lineStyle: {
                // 属性lineStyle（详见lineStyle）控制线条样式
                width: 3,
                color: '#fff',
                shadowColor: '#fff', // 默认透明
                shadowBlur: 10
              }
            },
            pointer: {
              // 分隔线
              shadowColor: '#fff', // 默认透明
              shadowBlur: 10,
              length: '80%',
              width: 2
            },
            title: {
              textStyle: {
                // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                fontWeight: 'bolder',
                fontSize: 20,
                fontStyle: 'italic',
                color: '#fff',
                shadowColor: '#fff', // 默认透明
                shadowBlur: 10
              }
            },
            detail: {
              show: false
            },
            data: [{ value: 0, name: '' }]
          }
        ]
      },
      max: 0,
      timer: ''
    }
  },
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar
    }),
    isCollapsede() {
      return this.sidebar.opened
    }
  },
  watch: {
    data: {
      handler(val) {

      },
      deep: true
    },
    isCollapsede(val) {
      // console.log('gaibian')
      setTimeout(() => {
        this.chart = echarts.init(this.$el, 'macarons')
        this.chart.resize()
      }, 100)
    }
  },
  created() {
  },
  mounted() {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(this.chartTypeData)
      var that = this
      // const timeblock = this.$refs.timeblock
      this.timer = setInterval(function() {
        var time = new Date()
        var h = time.getHours() >= 12 ? time.getHours() - 12 : time.getHours()
        var m = time.getMinutes() / 5
        var s = time.getSeconds() / 5
        h = h + time.getMinutes() / 60
        that.chartTypeData.series[0].data[0].value = h
        that.chartTypeData.series[1].data[0].value = m.toFixed(1)
        that.chartTypeData.series[2].data[0].value = s.toFixed(1)
        that.chart.setOption(that.chartTypeData)
      }, 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.echart-test {
  background: #ffffff;
  z-index: 20;
  position: relative;
  .test-card {
    position: absolute;
    top: 65%;
    color: #ffffff;
    left: 48%;
  }
}
</style>
