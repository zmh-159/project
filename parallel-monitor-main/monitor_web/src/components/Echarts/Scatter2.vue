<template>
  <div :class="className" :style="{height:height,width:width}" @click="test()" />
</template>

<script>
import echarts from 'echarts'

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
    datas: {
      type: Object,
      default: () => ({})
    },
    data2: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    datas: {
      deep: true,
      handler(val) {
        this.setOption(val)
      }
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
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      const data = []
      this.chart.setOption({
        tooltip: {// 提示框组件
          formatter: this.data2 + ': ({c0})',
          textStyle: {
            fontSize: 22,
            fontStyle: 'bold'
          }
        },
        legend: {
          right: '10%',
          top: '5%',
          data: ['节点'],
          textStyle: {
            color: '#fff',
            fontSize: 20
          }
        },
        xAxis: {
          max: 100,
          min: 0,
          name: this.data2[0],
          nameLocation: 'middle',
          // 坐标轴上的数据格式化
          axisLabel: {
            formatter: function(value) {
              return value + '%'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          nameTextStyle: {
            color: '#fff',
            fontSize: 20
            // fontWeight: 'bold'
          }
        },
        yAxis: {
          max: 100,
          min: 0,
          name: this.data2[1],
          // 坐标轴上的数据格式化
          axisLabel: {
            formatter: function(value) {
              return value + '%'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          scale: true,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          nameTextStyle: {
            color: '#fff',
            fontSize: 20
            // fontWeight: 'bold'
          }
        },
        series: [
          {
            name: '节点',
            data: data,
            type: 'scatter',
            symbolSize: 15,
            itemStyle: {
              normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(120, 36, 50, 1)',
                shadowOffsetY: 5,
                color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                  {
                    offset: 0,
                    color: 'rgb(251, 118, 123)'
                  },
                  {
                    offset: 1,
                    color: 'rgb(204, 46, 72)'
                  }
                ])
              }
            }
          }
        ]
      })
    },
    setOption(nodeData) {
      // var nodeTotalData = []
      // var nodeTypeData = []
      var nodeSingleData = []
      Object.keys(nodeData).forEach((key) => {
        nodeSingleData.push([nodeData[key][this.data2[0]].toFixed(0), nodeData[key][this.data2[1]].toFixed(0)])
        // nodeTypeData.push(nodeSingleData)
      })
      // nodeTotalData.push(nodeTypeData)
      this.chart.setOption({
        tooltip: {// 提示框组件
          formatter: this.data2 + ': ({c0})',
          textStyle: {
            fontSize: 22,
            fontStyle: 'bold'
          }
        },
        legend: {
          right: '10%',
          top: '5%',
          data: ['节点'],
          textStyle: {
            color: '#fff',
            fontSize: 20
          }
        },
        xAxis: {
          max: 100,
          min: 0,
          name: this.data2[0],
          nameLocation: 'middle',
          // 坐标轴上的数据格式化
          axisLabel: {
            formatter: function(value) {
              return value + '%'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          nameTextStyle: {
            color: '#fff',
            fontSize: 20
            // fontWeight: 'bold'
          }
        },
        yAxis: {
          max: 100,
          min: 0,
          name: this.data2[1],
          // 坐标轴上的数据格式化
          axisLabel: {
            formatter: function(value) {
              return value + '%'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          scale: true,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          nameTextStyle: {
            color: '#fff',
            fontSize: 20
            // fontWeight: 'bold'
          }
        },
        series: [
          {
            name: '节点',
            data: nodeSingleData,
            type: 'scatter',
            symbolSize: 15,
            itemStyle: {
              normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(120, 36, 50, 1)',
                shadowOffsetY: 5,
                color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                  {
                    offset: 0,
                    color: 'rgb(251, 118, 123)'
                  },
                  {
                    offset: 1,
                    color: 'rgb(204, 46, 72)'
                  }
                ])
              }
            }
          }
        ]
      })
      // 点击某个点的跳转函数
      this.chart.on('click', () => {
        this.$router.push({ path: '/pmNode/pmNodeInfo' })
      })
    }
  }
}
</script>
