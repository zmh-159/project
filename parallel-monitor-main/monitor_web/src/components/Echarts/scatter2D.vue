<template>
  <div :class="className" :style="{height:height,width:width}" />
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
    },
    legendData: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    var that = this
    return {
      chart: null,
      // 定义一个数组变量用于存放配置
      series: [],
      chartTypeData: {
        title: {
          show: true,
          left: '30%',
          top: '5%',
          backgroundColor: 'rgba(255,255,255,0.2)',
          text: this.legendData[0] + ' & ' + this.legendData[1] + '关系散点图',
          textStyle: {
            color: '#fff',
            fontWeight: 'bolder',
            fontFamily: 'Arial',
            // lineHeight: 50,
            fontSize: 24

          }
        },
        backgroundColor: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
          offset: 1, color: 'rgba(96,108,136,0.9)'
        }, {
          offset: 0, color: 'rgba(63,76,107,0.8)' }
        ], false),
        grid: {
          show: true,
          top: '20%',
          bottom: '15%'
          // containLabel: true,
          // backgroundColor: 'rgba(0, 0, 0, 0.5)'
        },
        // 鼠标悬停在圆点时的提示框
        tooltip: {
          // trigger: 'axis',
          showDelay: 0,
          formatter: function(params) {
            // console.log(params)
            if (params.value.length > 1) {
              return params.seriesName + ' :<br/>' +
                that.legendData[0] + ': ' + params.value[0] + ' <br/>' +
                that.legendData[1] + ': ' + params.value[1]
            } else {
              return params.seriesName + ' :<br/>' +
                params.name + ' : ' + params.value
            }
          },
          textStyle: {
            fontSize: 20,
            fontStyle: 'bold'
          }
        },
        visualMap: {
          min: 0,
          show: false,
          // max: 3000,
          // type: 'piecewise',
          // pieces: [
          //   { min: 0 }
          // ],
          dimension: 1,
          orient: 'vertical',
          right: 10,
          top: 'center',
          text: ['HIGH', 'LOW'],
          calculable: true,
          inRange: {
            color: ['#f2c31a', '#24b7f2']
          }
        },
        xAxis: {
          name: this.legendData[0],
          type: 'value',
          nameLocation: 'center',
          nameTextStyle: {
            fontSize: 20,
            align: 'center',
            lineHeight: 56,
            color: 'rgba(255, 255, 255, 1)'
          },
          axisLabel: {
            // show: true,
            color: '#fff'
          },
          axisLine: {
            show: false
          },
          splitLine: {
            show: false
          },
          splitArea: {
            show: false
          }
        },
        yAxis: {
          name: this.legendData[1],
          type: 'value',
          nameLocation: 'end',
          nameTextStyle: {
            fontSize: 20,
            align: 'center',
            lineHeight: 56,
            color: 'rgba(255, 255, 255, 1)'

          },
          axisLabel: {
            // show: true,
            color: '#fff'
          },
          axisLine: {
            show: false
          },
          splitLine: {
            show: false,
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.8)'
            }
          },
          splitArea: {
            show: false
          }
        },
        series: [{
          name: '节点坐标',
          zlevel: 1,
          type: 'scatter',
          smooth: true,
          symbolSize: 9,
          data: [],
          largeThreshold: 2000
        }]
      },
      max: 0
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
        var that = this
        this.chartTypeData.xAxis.name = this.legendData[0]
        this.chartTypeData.yAxis.name = this.legendData[1]
        this.chartTypeData.tooltip = {
          // trigger: 'axis',
          showDelay: 0,
          formatter: function(params) {
            // console.log(params)
            if (params.value.length > 1) {
              return params.seriesName + ' :<br/>' +
                that.legendData[0] + ': ' + params.value[0] + ' <br/>' +
                that.legendData[1] + ': ' + params.value[1]
            } else {
              return params.seriesName + ' :<br/>' +
                params.name + ' : ' + params.value
            }
          },
          textStyle: {
            fontSize: 20,
            fontStyle: 'bold'
          }
        }
        this.chartTypeData.title = {
          show: true,
          left: '30%',
          top: '5%',
          backgroundColor: 'rgba(255,255,255,0.2)',
          text: this.legendData[0] + ' & ' + this.legendData[1] + '关系散点图',
          textStyle: {
            color: '#fff',
            fontWeight: 'bolder',
            fontFamily: 'Arial',
            // lineHeight: 50,
            fontSize: 24

          }
        }
        if (val.length !== 0) {
          // console.log(val)
          this.chartTypeData.series[0].data = val
          this.chart.setOption(this.chartTypeData)
        }
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
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(this.chartTypeData)
    }
  }
}
</script>
