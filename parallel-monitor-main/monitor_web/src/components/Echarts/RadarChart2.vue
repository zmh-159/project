<template><div>
  <div :class="className" style="width: 400px;height:200px;" />
</div>

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
      series: [],
      myDataX: [],
      myDataY: []
    }
  },

  watch: {
    dataY(val) {
      console.log(val)
      this.myDataX = [].concat(this.dataX)
      this.myDataY = [].concat(this.dataY)// 值复制，引用复制将陷入死循环
      this.initChart()
    }
  },
  created() {
    // this.dataX = []
  },
  mounted() {
    this.chart = echarts.init(this.$el, 'macarons')
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
      // series的循环必须写在setOption()前
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'none',
            label: {
              backgroundColor: '#6a7985'
            }
          },
          formatter: function(params) {
            return (
              '数据包大小: ' +
              params[0].axisValue + 'Byte' +
              '<br />时间: ' +
              params[0].data + 'ms'
            )
          }
        },
        // toolbox: {
        //   show: true,
        //   feature: {
        //     mark: { show: true },
        //     saveAsImage: {
        //       show: true,
        //       excludeComponents: ['toolbox'],
        //       pixelRatio: 2,
        //       title: '保存为图片',
        //       type: 'png',
        //       lang: ['点击保存']
        //     }
        //   }
        // },
        // grid: {
        //   top: '16%',
        //   left: '8%',
        //   right: '8%',
        //   bottom: '3%',
        //   containLabel: true
        // },
        xAxis: {
          name: '数据包大小(Byte)',
          nameLocation: 'center',
          nameTextStyle: {
            padding: [10, 0, 0, 0] // 四个数字分别为上右下左与原位置距离
          },
          type: 'category',
          data: this.myDataX.reverse(),
          inverse: true,
          axisLabel: {
            // show: false
            formatter: function() {
              return ''// x轴不显示数值
            }
          }
        },
        yAxis: [
          {
            type: 'value',
            show: true, // 显示纵轴false-不显示，true-显示
            name: '时间(ms)', // 纵轴标题
            axisLabel: {
              formatter: function() {
                return ''
              }
            }
          }
        ],
        series: [{
          name: this.legend,
          type: 'line',
          smooth: true,
          data: this.myDataY.reverse(),
          itemStyle: {
            normal: {
              color: '#FFAE00',
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

