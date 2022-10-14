<template><div>
  <div :class="className" :style="{height:height,width:width}" /></div>
</template>

<script>
import echarts from 'echarts'
import 'echarts-gl' // 制作3D图 数据可视化相关图表时需引入
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
      color: 'fiber',
      chartTypeData: {
        tooltip: {
          textStyle: {
            fontSize: 20,
            fontStyle: 'bold'
          },
          axisLabel: {
            lebel: { show: false }
          },
          hideDelay: 2000,
          formatter: function(params) {
            // console.log(params)
            if (params.value.length > 1) {
              return params.seriesName + ' :<br/>' +
                that.legendData[0] + ': ' + params.value[0] + ' <br/>' +
                that.legendData[1] + ': ' + params.value[1] + ' <br/>' +
                that.legendData[2] + ': ' + params.value[2]
            } else {
              return params.seriesName + ' :<br/>' +
                params.name + ' : ' + params.value
            }
          }
        },
        backgroundColor: 'rgba(255,255,255,0)',
        alwaysShowContent: true,
        // textStyle: {
        //   fontSize: 20,
        //   // fontStyle: 'italic',
        //   fontWeight: 'bold',
        //   fontFamily: 'monospace'
        // },
        xAxis3D: {
          name: this.legendData[0],
          type: 'value',
          nameGap: 20,
          nameTextStyle: {
            fontSize: 20,
            align: 'center',
            lineHeight: 56
          }
        },
        yAxis3D: {
          name: this.legendData[1],
          nameGap: 20,
          type: 'value',
          nameTextStyle: {
            fontSize: 20,
            align: 'center',
            lineHeight: 56
          }
        },
        zAxis3D: {
          name: this.legendData[2],
          nameGap: 20,
          type: 'value',
          nameTextStyle: {
            fontSize: 20,
            align: 'center',
            lineHeight: 56
          }
        },
        grid3D: {
          // 调整整个盒子的位置
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(0,0,0,0.2)']
            }
          },

          splitLine: {
            show: true,
            interval: 100
          },
          axisLine: {
            lineStyle: { color: '#fff', opacity: 1 }
          },
          axisTick: {
            show: false
          },
          axisPointer: {
            lineStyle: { color: '#fff' }
          },
          viewControl: {
            alpha: 10,
            projection: 'perspective',
            autoRotate: true,
            autoRotateSpeed: 8,
            distance: 260,
            animation: true
          },
          postEffect: {
            enable: true,
            bloom: true
          },
          light: {
            main: {
              color: '#000',
              intensity: 1,
              shadow: true,
              beta: 0
            },
            ambient: {
              color: '#000',
              intensity: 1
            }
          },
          top: '0%',
          environment: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
            offset: 1, color: 'rgba(189,195,199,1)'
          }, {
            offset: 0, color: 'rgba(44,62,80,1)' }
          ], false)
        },
        visualMap: {
          show: false,
          min: 0,
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
        series: [{
          type: 'scatter3D',
          name: '节点坐标',
          data: [[]],
          lineStyle: {
            width: 100,
            color: '#fff'
          }
        }]
      }
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
        this.chartTypeData.xAxis3D.name = this.legendData[0]
        this.chartTypeData.yAxis3D.name = this.legendData[1]
        this.chartTypeData.zAxis3D.name = this.legendData[2]
        this.chartTypeData.tooltip = {
          textStyle: {
            fontSize: 20,
            fontStyle: 'bold'
          },
          axisLabel: {
            lebel: { show: false }
          },
          hideDelay: 2000,
          formatter: function(params) {
            // console.log(params)
            if (params.value.length > 1) {
              return params.seriesName + ' :<br/>' +
                that.legendData[0] + ': ' + params.value[0] + ' <br/>' +
                that.legendData[1] + ': ' + params.value[1] + ' <br/>' +
                that.legendData[2] + ': ' + params.value[2]
            } else {
              return params.seriesName + ' :<br/>' +
                params.name + ' : ' + params.value
            }
          }
        }
        // console.log(JSON.parse(JSON.stringify(val[0])))
        if (val.length <= 1) {
          this.chartTypeData.series[0].data = [[]]
          this.chartTypeData.series[0].data.push(val[0])
          this.chart.setOption(this.chartTypeData)
        } else {
          this.chartTypeData.series[0].data = val
          this.chart.setOption(this.chartTypeData)
        }
        this.chart.setOption(this.chartTypeData)
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
