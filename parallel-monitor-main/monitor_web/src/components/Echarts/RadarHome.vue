<template><div>
  <div :class="className" :style="{height:height,width:width}" /></div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

// const animationDuration = 3000

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
    },
    // 包含x轴数据
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      chart: null,
      // 定义一个数组变量用于存放配置
      series: []
    }
  },
  watch: {
    dataY(val) {
      // console.log(val)
    }
  },
  created() {
    // this.dataX = []
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
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      // series的循环必须写在setOption()前
      // console.log(this.dataY[0].length)
      this.chart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        radar: [
          {
            indicator: [
              { text: 'CPU使用率', max: 100 },
              { text: '内存使用率', max: 100 },
              { text: '磁盘使用率', max: 100 },
              { text: '交换区使用率', max: 100 }
            ],
            center: ['25%', '40%'],
            radius: 80
          }
        ],
        series: [
          {
            type: 'radar',
            tooltip: {
              trigger: 'item'
            },
            areaStyle: {},
            data: [
              {
                value: [60, 73, 85, 100],
                name: 'A Software'
              }
            ]
          }
        ]
      })
    }
  }
}
</script>
