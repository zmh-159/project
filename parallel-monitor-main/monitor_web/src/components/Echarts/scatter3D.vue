<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
import 'echarts-gl' // 制作3D图 数据可视化相关图表时需引入
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme
import { mapState } from 'vuex'
import tools from '@/utils/common.js'

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
          show: false,
          // padding: [5, 10],
          // extraCssText: 'box-shadow: 0 0 4px rgba(0, 0, 0, 0.3);',
          axisPointer: {
            type: 'none',
            label: {
              precision: 2
            }
            // label: { show: false }
          },
          textStyle: {
            fontSize: 20,
            // fontStyle: 'italic',
            // color: '#fff',
            fontWeight: 'bold',
            fontFamily: 'monospace'
          },
          position: function(pos, params, dom, rect, size) {
            return ['33%', '90%']
          },
          // position: 'bottom',
          backgroundColor: 'rgba(255,255,255,0)',
          // hideDelay: 2000,
          alwaysShowContent: true,
          formatter: function(params) {
            // console.log(params)
            if (params.value.length > 1) {
              return that.legendData[0] + ':' + params.value[0] + '% ' +
                that.legendData[1] + ':' + params.value[1] + '% ' +
                that.legendData[2] + ':' + params.value[2] + '%'
              // params.seriesName + ' :<br/>' +
            } else {
              return params.seriesName + ' :<br/>' +
                params.name + ' : ' + params.value
            }
          }
        },
        xAxis3D: {
          // show: false,
          name: this.legendData[0],
          type: 'value',
          max: 100,
          splitNumber: 5,
          nameGap: 20,
          // splitNumber: 0,
          nameTextStyle: {
            fontSize: tools.fontSize(17),
            align: 'center',
            lineHeight: 56
          }
        },
        yAxis3D: {
          name: this.legendData[1],
          nameGap: 20,
          max: 100,
          splitNumber: 5,

          // splitNumber: 1,
          type: 'value',
          nameTextStyle: {
            fontSize: tools.fontSize(17),
            align: 'center',
            lineHeight: 56
          }
        },
        zAxis3D: {
          name: this.legendData[2],
          nameGap: 20,
          max: 100,
          splitNumber: 5,
          // splitNumber: 1,
          type: 'value',
          nameTextStyle: {
            fontSize: tools.fontSize(17),
            align: 'center',
            lineHeight: 56
          }
        },
        grid3D: {
          top: '-10%',
          left: '5%',
          right: '5%',
          // 调整整个盒子的位置
          splitArea: {
            show: true,
            // interval: 100,
            areaStyle: {
              color: ['rgba(0,0,0,0.1)']
              // shadowColor: 'rgba(0, 0, 0, 0.5)'
              // shadowBlur: 10
            }
            // interval:
          },
          axisLabel: {
            show: false
          },
          // splitLine: {
          //   show: true,
          //   interval: 100
          // },
          axisLine: {
            show: true,
            lineStyle: { color: '#000', opacity: 1 }// 坐标轴颜色
          },
          axisTick: {
            show: false
          },
          axisPointer: {
            lineStyle: { color: '#ffbd67' }
          },
          // environment: '#555555',
          // boxHeight: 120,
          // boxWidth: 120,
          // boxDepth: 120,
          viewControl: {
            alpha: 10,
            projection: 'perspective',
            // autoRotate: true,
            // autoRotateSpeed: 5,
            distance: 220
            // animation: true
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
          }
          // top: '-10%'
          // a ,b,c,d为0，1
          // a:1 arr中的颜色右到左
          // c:1 arr中的颜色左到右
          // b:1 arr中的颜色下到上
          // d:1 arr中的颜色上到下

          // environment: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
          //   offset: 1, color: 'rgba(189,195,199,1)'
          // }, {
          //   offset: 0, color: 'rgba(44,62,80,1)' }
          // ], false)
          // {
          //   offset: 1, color: 'rgba(14,92,123,1)'
          // }
        },
        // visualMap: {
        //   show: false,
        //   min: 0,
        //   // max: 5000,
        //   dimension: 1,
        //   orient: 'vertical',
        //   right: 10,
        //   top: 'center',
        //   // text: ['HIGH', 'LOW'],
        //   calculable: true,
        //   inRange: {
        //     color: ['#f2c31a', '#24b7f2']
        //   }
        // },
        series: [{
          type: 'scatter3D',
          name: '节点坐标',
          shading: 'lambert',
          data: [[]],
          slient: true,
          lineStyle: {
            width: 100,
            color: '#fff'
          },
          itemStyle: {
            color: function(params) {
              if (params.data[0] > 70) { return '#E65D6E' } return '#409EFF'
            }
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
    // console.log(this.sidebar.opened)
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
<style scoped>
</style>
