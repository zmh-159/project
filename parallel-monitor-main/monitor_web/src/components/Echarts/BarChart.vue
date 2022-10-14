<template>
  <div :class="className" :style="{height:height,width:width}" @click="test" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

// const animationDuration = 6000 动态加载时间
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
      default: '300px'
    }
  },
  data() {
    return {
      index: 0,
      time: [0, 0, 0, 0, 0, 0],
      cpu: [0, 0, 0, 0, 0, 0],
      memory: [0, 0, 0, 0, 0, 0],
      gpu: [1, 2, 3, 4, 5, 6],
      chart: [0, 0, 0, 0, 0, 0],
      timesToview: [1, 1, 1, 1, 1, 1],
      cpuToview: [1, 1, 1, 1, 1, 1],
      memoryToview: [1, 1, 1, 1, 1, 1],
      gpuToview: [1, 1, 1, 1, 1, 1]
    }
  },
  watch: {
    index(val, oldVal) {
      for (let i = 0; i < 6; ++i) {
        this.timesToview[i] = this.time[(val + i) % 6]
        this.cpuToview[i] = this.cpu[(val + i) % 6]
        this.memoryToview[i] = this.memory[(val + i) % 6]
        this.gpuToview[i] = this.gpu[(val + i) % 6]
      }
      this.setChart()
    }
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
  },
  methods: {
    test() {
      this.index = 1
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setChart()
    },
    setChart() {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: this.timesToview,
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: 'cpu',
          type: 'bar',
          stack: 'vistors',
          barWidth: '100%',
          data: this.cpuToview
          // animationDuration
        }, {
          name: '内存',
          type: 'bar',
          stack: 'vistors',
          barWidth: '100%',
          data: this.memoryToview
        }, {
          name: '显存',
          type: 'bar',
          stack: 'vistors',
          barWidth: '100%',
          data: this.gpuToview
        }]
      })
    }
  }
}
</script>
