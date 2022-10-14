<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>
<script>
import echarts from 'echarts'
import 'echarts-gl'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
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
    data: {
      type: Array,
      default: () => {
        return []
      }
    },
    max: {
      type: Number,
      default: 100
    }
  },
  data() {
    return {
      chart: null,
      myOption: {
        series: [
          {
            splitNumber: 5,
            max: 100,
            startAngle: 200,
            endAngle: -20,
            type: 'gauge',
            grid: {
              top: '0%',
              left: '0%',
              right: '0%',
              bottom: '0%'
            },
            progress: {
              show: true,
              width: 5
            },
            axisLine: {
              lineStyle: {
                color: [
                  [0.3, '#dcdfe6'],
                  [0.7, '#dcdfe6'],
                  [1, '#dcdfe6']
                ],
                // color: '#000',
                width: 5
              }
            },
            pointer: {
              itemStyle: {
                // color: 'auto'
              }
            },
            axisTick: {
              show: false
            },
            splitLine: {
              length: 10,
              lineStyle: {
                width: 2
                // color: 'auto'
              }
            },
            axisLabel: {
              show: false
            },
            anchor: {
              show: true,
              showAbove: true,
              size: 25,
              itemStyle: {
                borderWidth: 10
              }
            },
            title: {
              show: false
            },
            detail: {
              show: false
            },
            data: [
              {
                value: 0
              }
            ]
          }
        ]
      }
    }
  },
  watch: {
    data: {
      handler(val) {
        // console.log(val)

        this.myOption.series[0].data[0].value = val[2]
        this.chart.setOption(this.myOption)
      },
      deep: true
    }
  },
  mounted() {
    this.myOption.series[0].max = this.max
    this.test()
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
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(this.myOption)
    }
  }
}
</script>

