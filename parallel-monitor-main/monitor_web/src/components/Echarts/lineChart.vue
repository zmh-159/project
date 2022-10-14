<template><div>
  <div :class="className" :style="{height:height,width:width}" /></div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme

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
    dataX: {
      type: Array,
      default: () => {
        return []
      }
    },
    // 包含y轴数据
    dataY: {
      type: Array,
      default: () => {
        return []
      }
    },
    legend: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      chart: null,
      // 定义一个数组变量用于存放配置
      series: []
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
      // series的循环必须写在setOption()前
      // console.log(this.dataY[0].length)
      this.chart.setOption({
        title: {
          text: this.legend,
          x: '100px'
        },
        tooltip: { trigger: 'axis' },
        // legend: {
        //   orient: 'horizontal',
        //   x: 'right',
        //   y: 'top',
        //   data: str
        // },
        grid: {
          top: '16%',
          left: '8%',
          right: '8%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          name: '时间',
          type: 'category',
          data: this.dataX.reverse(),
          // data:[1,2,3],
          axisLabel: {
            showMinLabel: true,
            showMaxLabel: true,
            rotate: 0,
            interval: 10000
          }
        },
        yAxis: {},
        series: [{
          name: this.legend,
          type: 'line',
          smooth: true,
          data: this.dataY,
          // data:[1,2,3],
          areaStyle: {
            normal: {
              color: {
                type: 'linear', // 设置线性渐变
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(57,127,255,.1)' // 0% 处的颜色
                }, {
                  offset: 1, color: '#fff' // 100% 处的颜色
                }],
                globalCoord: false // 缺省为 false
              }

            }
          }
        }
        ]

      }
      )
    }
  }
}
</script>
