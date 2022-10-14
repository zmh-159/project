<template>
  <div :class="className" :style="{height:height,width:width}" />
  <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
</template>

<script>
import echarts from 'echarts'
import 'echarts-gl'
// import request from '@/utils/request'
// import Cookie from 'js-cookie'

require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
// import Bold from 'wangeditor/dist/menus/bold'
// import pmNodeInfo from '@/api/pmNode/pmNodeInfo'
// var data2 = ['xiaoming', 'xiao', 'xaiomin']
export default {
  props: {
    datas: {
      type: Object,
      default: () => ({})
    },
    data1: {
      type: null,
      default: () => {
        return []
      }
    },
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '95%'
    },
    height: {
      type: String,
      default: '80%'
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
        this.test(val)
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
    test(realData) {
      var data = []
      var total = 0 // 总结点数
      var helnum = 0 // 健康节点
      var unhelnum = 0 // 非健康节点
      var str = ''
      Object.keys(realData).forEach((key) => {
        total++
        if (realData[key][this.data1[0]].toFixed(2) > 50) {
          helnum++
        }
        data.push([realData[key][this.data1[0]].toFixed(2), realData[key][this.data1[1]].toFixed(2), realData[key][this.data1[2]].toFixed(2)])
      })
      // console.log(realData);
      // console.log(data);
      unhelnum = total - helnum
      if (helnum > unhelnum) {
        str = '良好'
      } else { str = '较差' }
      // 向父模块传递数据
      this.$emit('eventSend', total, helnum, unhelnum, str)
      this.chart.setOption({
        tooltip: {

          textStyle: {
            fontSize: 20,
            fontStyle: 'bold'
          }
        },
        xAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[0],
          nameLocation: 'middle',
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 25,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        yAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[1],
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 5,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        zAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[2],
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 25,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        grid3D: {
          viewControl: {
            projection: 'orthographic'
          }
        },
        series: [{
          type: 'scatter3D',
          name: '节点坐标',
          data: data,
          lineStyle: {
            width: 100,
            color: '#fff'
          }

        }]
      })
      // 点击某个点的跳转函数
      this.chart.on('click', () => {
        // console.log("点击完成");
        this.$router.push({ path: '/pmNode/pmNodeInfo' })
        // console.log("跳转完成");
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      const data = []
      this.chart.setOption({
        tooltip: {
          // formatter: '{c}'
        },
        xAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[0],
          nameLocation: 'middle',
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 25,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        yAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[1],
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 5,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        zAxis3D: {
          max: 100,
          min: 0,
          type: 'value',
          name: this.data1[2],
          nameTextStyle: {
            color: '#fff',
            fontSize: 22
            // fontWeight: 'bold'
          },
          nameGap: 25,
          axisLine: { // 一条坐标横轴（x轴）
            lineStyle: {
              color: '#fff'
            }
          },
          axisLable: { // 小坐标标签（多）
            textStyle: {
              color: '#fff'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        grid3D: {
          viewControl: {
            projection: 'orthographic'
          }
        },
        series: [{
          type: 'scatter3D',
          name: '节点坐标',
          data: data
        }]
      })
    }
  }

}
</script>
