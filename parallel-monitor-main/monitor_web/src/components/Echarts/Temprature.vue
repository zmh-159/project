<template>
  <div :class="charts" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  props: {
    charts: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '180px'
    },
    height: {
      type: String,
      default: '160px'
    },
    data: {
      type: Number,
      default: null
    },
    max: {
      type: Number,
      default: null
    },
    legend: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      chart: null,
      // data: this.cpudata,
      myOption: {
        tooltip: {
          show: false,
          formatter: '{a} <br/>{b} : {c}%'
        },
        toolbox: {
          show: false,
          feature: {
            restore: {},
            saveAsImage: {}
          }
        },
        // 配置
        series: [
          {
            // name: '温度',
            type: 'gauge',
            max: 0,
            startAngle: 215,
            endAngle: -35,
            splitNumber: 5,
            progress: {
              overlap: false,
              clip: true
            },
            axisLabel: { distance: 0, fontWeight: 200, shadowOffsetY: 100, height: 100, color: '#fff',
              fontFamily: 'tech-font-1'
              // fontSize: '1.3rem'
            },
            axisLine: {
              // 坐标轴线
              lineStyle: { // 属性lineStyle控制线条样式
                color: [
                  [0.2, '#EDCACA'],
                  [0.7, '#E46969'],
                  [1, '#DC1010']
                ],
                width: 10,
                shadowColor: '#DC1010', // 默认透明
                shadowBlur: 15
              }
            },
            // 刻度样式
            axisTick: {
              show: false
            },
            splitLine: {
              show: true
            },
            title: {
              offsetCenter: [0, '68%'],
              fontWeight: 'bolder',
              fontSize: 14,
              color: '#fff',
              shadowColor: '#fff',
              shadowBlur: 10
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontFamily: 'tech-font-1',
              formatter: '{value}',
              offsetCenter: [0, 0],
              backgroundColor: '#fff',
              // shadowColor:"red",
              color: '#DC1010',
              shadowBlur: 44,
              borderRadius: 58,
              fontSize: 16,
              borderColor: '#CD9696',
              borderWidth: 8,
              width: 25,
              height: 33,
              lineHeight: 35,
              rich: {
                score: {
                  color: '#fff',
                  fontSize: 16,
                  padding: 0
                }
              }
            },
            pointer: {
              width: 5
            },
            itemStyle: {
              // borderType: 'dashed'
            },
            data: [{ value: 0, name: this.legend }]
          }
        ]
      }
    }
  },
  watch: {
    data: {
      deep: true,
      handler(val) {
        this.myOption.series[0].data[0].value = val.toFixed(2)
        this.chart.setOption(this.myOption)
      }
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
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.myOption.series[0].max = this.max
      this.chart.setOption(this.myOption)
    }
  }
}
</script>
<style scoped>

</style>
