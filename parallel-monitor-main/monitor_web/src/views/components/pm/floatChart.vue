<template>
  <div class="flex-chart-pannel">
    <div v-for="(item,i) in number" :id="name+i" :key="i" :style="stjson" />
  </div>
</template>
<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme
import tools from '@/utils/common.js'

export default {
  props: {
    stjson: {
      type: String,
      default: 'height:100%;width:25%'
    },
    number: {
      type: Number,
      default: 2
    },
    title: {
      type: Array,
      default: () => { return [] }
    },
    name: {
      type: String,
      default: 'chart'
    },
    left: {
      type: String,
      default: '38%'
    },
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Array,
      default: () => { return [] }
    },
    grid: {
      type: Object,
      default: () => ({
        left: '5%',
        right: '5%',
        top: tools.fontSize(1) + 'px',
        bottom: tools.fontSize(20) + 'px'
      })
    },
    chartData: {
      type: Array,
      default: () => { return [] }
    },
    change: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      chart: [],
      chartOptions: [],
      chartOption: {
        title: {
          text: this.title,
          x: 'center',
          y: '3%',
          zlevel: 10,
          textStyle: {
            fontSize: tools.fontSize(14),
            color: '#000'
          }
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          position: '',
          confine: true,
          textStyle: {
            fontSize: tools.fontSize(14)
          }
          // extraCssText: 'box-shadow: 0 0 3px rgba(0, 0, 0, 0.1);'
        },
        grid: this.grid,
        xAxis: {
          type: 'category',
          show: true,
          nameGap: 5,
          boundaryGap: false,
          splitLine: {
            show: true,
            lineStyle: {
              color: ['#c4d4df']
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#6A7C88',
            showMinLabel: true,
            showMaxLabel: true,
            rotate: 0,
            interval: 10000
          },
          axisLine: {
            lineStyle: {
              color: ['#c4d4df']
            }
          }
        },
        yAxis: {
          type: 'value',
          show: true,
          interval: 1000000,
          splitLine: {
            show: true,
            lineStyle: {
              color: ['#c4d4df']
            }
          },
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: ['#c4d4df']
            }
          },
          axisLabel: {
            show: false
          }
        },
        series: [
          {
            name: '',
            type: 'line',
            showSymbol: false,
            lineStyle: {
              color: '#6A7C88'
            },
            areaStyle: {
              normal: {
                color: {
                  type: 'linear', // 设置线性渐变
                  x: 0,
                  y: 1,
                  x2: 0,
                  y2: 0,
                  colorStops: [{
                    offset: 1, color: '#6A7C88'
                  }, {
                    offset: 0, color: '#F3F5F9'
                  }],
                  globalCoord: false // 缺省为 false
                }

              }
            },
            data: []
          }
        ]
      },
      count: 0,
      unit: []
    }
  },
  watch: {
    chartData: {
      handler(val) {
        this.setData(val, this.count)
        this.count++
      },
      deep: true
    },
    change(val) {
      this.createArray()
      this.$nextTick(function() {
        this.initChart()
      })
    }
  },
  created() {
    this.createArray()
  },
  mounted() {
    var that = this
    this.$nextTick(function() {
      that.initChart()
    })
    this.__resizeHandler = debounce(() => {
      for (var i = 0; i < this.number; i++) {
        this.chart[i].resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    for (var i = 0; i < this.number; i++) {
      this.chart[i].dispose()
      this.chart[i] = null
    }
  },
  methods: {
    initChart() {
      var fun = []
      for (var i = 0; i < this.number; i++) {
        var id = this.name + i
        this.$set(this.chart, i, echarts.init(document.getElementById(id), 'macarons'))
        // 深拷贝
        var objString = JSON.stringify(this.chartOption)
        var obj = JSON.parse(objString)
        this.$set(this.chartOptions, i, obj)
        // 赋值
        this.$set(this.unit, i, tools.getUnit(this.title[i]))
        // 不可定义在data中
        var that = this
        // 循环中定义函数需要使用闭包
        fun[i] = (function(k) {
          return function(param) {
            return param[0].seriesName + ':' + '\n' + param[0].data[1] + that.unit[k]
          }
        })(i)
        var position = function(point, params, dom, rect, size) {
          // 固定在顶部
          return [point[0], '10%']
        }
        this.$set(this.chartOptions[i].tooltip, 'formatter', fun[i])
        this.$set(this.chartOptions[i].tooltip, 'position', position)
        this.chartOptions[i].title.text = this.title[i] + ':0' + this.unit[i]
        this.chartOptions[i].series[0].name = this.title[i]
        this.$set(this.chartOptions[i].yAxis, 'min', this.min)
        if (this.max !== null || this.max !== undefined || this.max !== []) {
          this.$set(this.chartOptions[i].yAxis, 'max', this.max[i])
        }
        this.chart[i].setOption(this.chartOptions[i])
      }
    },
    setData(data, count) {
      for (var i = 0; i < this.number; i++) {
        this.chartOptions[i].series[0].name = this.title[i]
        this.chartOptions[i].title.text = this.title[i] + ':' + data[i] + tools.getUnit(this.title[i])
        this.$set(this.chartOptions[i].series[0].data[47], 0, (count + 47).toString())
        this.chartOptions[i].series[0].data.splice(0, 1)
        this.$set(this.chartOptions[i].series[0].data[0], 0, '2min')
        this.chartOptions[i].series[0].data.push(['0', data[i]])
        this.chart[i].setOption(this.chartOptions[i])
      }
    },
    createArray() {
      for (var k = 0; k < 48; k++) {
        if (k === 0) this.$set(this.chartOption.series[0].data, k, ['2min', 0])
        if (k === 47) this.$set(this.chartOption.series[0].data, k, ['0', 0])
        if (k !== 0 && k !== 47) this.$set(this.chartOption.series[0].data, k, [k.toString(), 0])
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.flex-chart-pannel{
    display: flex;
    height:100%;
    width:100%;
    overflow: hidden
}
</style>
