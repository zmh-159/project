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
      default: '100%'
    },
    title: {
      type: String,
      default: ''
    },
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
      series: [],
      defaultOption: {
        legend: {
          bottom: 5,
          data: [this.title],
          itemGap: 20,
          textStyle: {
            fontSize: 14
          }
        },
        radar: {
          indicator: [],
          symbol: 'circle',
          splitNumber: 5,
          name: {
            textStyle: {
              color: 'rgb(238, 197, 102)'
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: [
                'rgba(238, 197, 102, 0.1)', 'rgba(238, 197, 102, 0.2)',
                'rgba(238, 197, 102, 0.4)', 'rgba(238, 197, 102, 0.6)',
                'rgba(238, 197, 102, 0.8)', 'rgba(238, 197, 102, 1)'
              ].reverse()
            }
          },
          splitArea: {
            // show: true
          },
          axisLine: {
            show: false,
            lineStyle: {
            }
          }
        },
        series: [
          {
            name: '',
            type: 'radar',
            lineStyle: {
              normal: {
                width: 1,
                opacity: 0.5
              }
            },
            data: [[]],
            symbol: 'none',
            itemStyle: {
              color: '#99CCFF'
            },
            areaStyle: {
              opacity: 0.6
            }
          }
        ]
      },
      timer: ''
    }
  },
  watch: {
    data: {
      handler(val) {
        this.handler(val)
        this.chart.setOption(this.defaultOption)
      },
      deep: true
    }
  },
  created() {
    this.initChart()
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
      this.chart.setOption(this.defaultOption)
    },
    handler(data) {
      this.defaultOption.legend.data[0] = this.title
      this.defaultOption.series[0].data[0] = data
      this.defaultOption.series[0].name = this.title
      for (var i = 0; i < data.length; i++) {
        this.$set(this.defaultOption.radar.indicator, i, { max: 100 })
      }
    }
  }
}
</script>
