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
      type: Array,
      default: () => {
        return []
      }
    },
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    var that = this
    return {
      chart: null,
      // 定义一个数组变量用于存放配置
      series: [],
      defaultOption: {
        title: {
          text: this.title,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          data: this.legend,
          left: 10
        },
        // toolbox: {
        //   feature: {
        //     dataZoom: {
        //       yAxisIndex: 'none'
        //     },
        //     restore: {},
        //     saveAsImage: {}
        //   }
        // },
        axisPointer: {
          link: [
            {
              xAxisIndex: 'all'
            }
          ]
        },
        // dataZoom: [
        //   {
        //     show: true,
        //     realtime: true,
        //     start: 30,
        //     end: 70,
        //     xAxisIndex: [0, 1]
        //   },
        //   {
        //     type: 'inside',
        //     realtime: true,
        //     start: 30,
        //     end: 70,
        //     xAxisIndex: [0, 1]
        //   }
        // ],
        grid: [
          {
            left: 60,
            right: 50,
            height: '35%'
          },
          {
            left: 60,
            right: 50,
            top: '58%',
            height: '35%'
          }
        ],
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: []
          },
          {
            gridIndex: 1,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: [],
            position: 'top'
          }
        ],
        yAxis: [
          {
            // name: 'Evaporation(m^3/s)',
            type: 'value',
            // max: 500,
            splitLine: {
              show: false
            },
            splitArea: {
              show: false
            }
          },
          {
            gridIndex: 1,
            // name: 'Rainfall(mm)',
            type: 'value',
            inverse: true,
            splitLine: {
              show: false
            },
            splitArea: {
              show: false
            }
          }
        ],
        series: [
          {
            name: this.legend[0],
            type: 'line',
            symbolSize: 8,
            data: [],
            itemStyle: {
              color: '#000',
              normal: {
                lineStyle: {
                  color: '#397FFF'
                }
              }
            },
            areaStyle: {
            /* normal: { origin: 'start', color: 'rgba(57,127,255,.1)', opacity: 1 },*/
              normal: {
                color: {
                  type: 'linear', // 设置线性渐变
                  x: 0,
                  y: 1,
                  x2: 0,
                  y2: 0,
                  colorStops: [{
                    offset: 1, color: 'rgba(57,127,255,.1)'
                  }, {
                    offset: 0, color: '#fff'
                  }],
                  globalCoord: false // 缺省为 false
                }

              }
            },
            markPoint: {
              label: {
                formatter: function(params) {
                  if (that.legend === '温度(摄氏度)') {
                    return params.value + '摄氏度'
                  } else if (that.legend === '频率(GHz)') {
                    return params.value + 'GHz'
                  } else if (that.legend === '系统使用率(%)' || that.legend === '用户使用率(%)' || that.legend === 'cpu使用率(%)' || that.legend === '内存占有率(%)' || that.legend === '磁盘占有率(%)') {
                    return params.value + '%'
                  } else { return params.value }
                }
              },
              symbol: 'pin',
              data: [{
                name: '最大值',
                type: 'max',
                itemStyle: {
                  color: '#397FFF',
                  shadowBlur: {
                    shadowColor: 'rgba(0, 0, 0, 1)',
                    shadowBlur: 10
                  }
                },
                symbolSize: 35

              },
              {
                name: '最小值',
                type: 'min',
                itemStyle: {
                  color: '#397FFF'
                },
                symbolSize: 35
              }
              ]
            }
          },
          {
            name: this.legend[1],
            type: 'line',
            xAxisIndex: 1,
            yAxisIndex: 1,
            symbolSize: 8,
            data: [],
            itemStyle: {
              color: '#000',
              normal: {
                lineStyle: {
                  color: '#397FFF'
                }
              }
            },
            areaStyle: {
            /* normal: { origin: 'start', color: 'rgba(57,127,255,.1)', opacity: 1 },*/
              normal: {
                color: {
                  type: 'linear', // 设置线性渐变
                  x: 0,
                  y: 1,
                  x2: 0,
                  y2: 0,
                  colorStops: [{
                    offset: 1, color: 'rgba(57,127,255,.1)'
                  }, {
                    offset: 0, color: '#fff'
                  }],
                  globalCoord: false // 缺省为 false
                }

              }
            },
            markPoint: {
              label: {
                formatter: function(params) {
                  if (that.legend === '温度(摄氏度)') {
                    return params.value + '摄氏度'
                  } else if (that.legend === '频率(GHz)') {
                    return params.value + 'GHz'
                  } else if (that.legend === '系统使用率(%)' || that.legend === '用户使用率(%)' || that.legend === 'cpu使用率(%)' || that.legend === '内存占有率(%)' || that.legend === '磁盘占有率(%)') {
                    return params.value + '%'
                  } else { return params.value }
                }
              },
              symbol: 'pin',
              data: [{
                name: '最大值',
                type: 'max',
                itemStyle: {
                  color: '#397FFF',
                  shadowBlur: {
                    shadowColor: 'rgba(0, 0, 0, 1)',
                    shadowBlur: 10
                  }
                },
                symbolSize: 35

              },
              {
                name: '最小值',
                type: 'min',
                itemStyle: {
                  color: '#397FFF'
                },
                symbolSize: 35
              }
              ]
            }
          }
        ]
      }
    }
  },
  watch: {
    dataX: {
      handler(val) {
        console.log('接收数据')
        this.defaultOption.xAxis[0].data = val
        this.defaultOption.xAxis[1].data = val
        this.defaultOption.series[0].data = this.dataY[0]
        this.defaultOption.series[1].data = this.dataY[1]
        this.chart.setOption(this.defaultOption)
      },
      deep: true
    },
    dataY: {
      handler(val) {
        console.log('接收数据')
      },
      deep: true
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
      this.chart.setOption(this.defaultOption)
    }
  }
}
</script>
