<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
import 'echarts-gl'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
var temp = []
export default {
  props: {
    datas: {
      type: Object,
      default: () => ({})
    }, // 一定记得这里！！！
    data1: {
      type: Array,
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
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
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
    // this.initChart()
    // this.test()
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
      var temp_new = []
      var i = 0
      var j = 0
      var length = Object.values(realData).length
      var result = 0
      var sum = 0
      var summ = 0
      var sum1 = 0
      var sum2 = 0
      var sum3 = 0
      // console.log(Object.values(realData)[0][this.data1[0]]);//获取确定值
      // console.log(Object.values(realData).length);
      Object.keys(realData).forEach((key) => {
        var temp_swap = []
        if (temp.length !== Object.values(realData).length) {
          temp_swap = [realData[key][this.data1[0]], realData[key][this.data1[1]], realData[key][this.data1[2]]]
          temp[i++] = temp_swap
          // console.log("存储结束")
        } else {
          temp_swap = [realData[key][this.data1[0]], realData[key][this.data1[1]], realData[key][this.data1[2]]]
          temp_new[j++] = temp_swap
        }
      })
      // console.log("旧数组"+temp)
      // console.log("新数组"+temp_new)
      // push数据
      for (var k = 0; k < length; k++) {
        sum1 = temp[k][0] - temp_new[k][0]
        sum2 = temp[k][1] - temp_new[k][1]
        sum3 = temp[k][2] - temp_new[k][2]
        // console.log(sum1);
        // console.log(sum2);
        // console.log(sum3);
        summ = Math.pow(sum1, 2) + Math.pow(sum2, 2) + Math.pow(sum3, 2)
        sum += Math.sqrt(summ)
      }
      // console.log(j);
      // console.log(sum);
      // console.log(length);
      result = (sum / length).toFixed(2)
      // console.log(result);
      data.push(result)
      // 更新新数组
      for (var l = 0; l < length; l++) {
        temp[l] = temp_new[l]
      }
      // console.log("更新旧数组"+temp)
      // console.log(temp);
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
        },
        toolbox: {
          show: true,
          feature: {
            restore: { show: false },
            saveAsImage: { show: false }
          }
        },
        series: [
          {
            name: '温度值',
            type: 'gauge',
            // detail: { formatter: '{value}%' },
            z: 3,
            min: 0,
            max: 100,
            splitNumber: 5,
            radius: '100%', // 仪表盘半径
            data: data,
            // startAngle: -30, // 起始角度
            // endAngle: 210, // 结束角度
            center: ['50%', '50%'], // 仪表盘位置
            axisLine: {// 仪表盘轴线相关配置
              lineStyle: {
                width: 7, // 仪表盘刻度宽度
                color: [
                  [0.5, '#4dabf7'],
                  [0.65, '#69db7c'],
                  [0.8, '#ffa94d'],
                  [1, '#ff6b6b']
                ]
              }
            },
            axisTick: { // 坐标轴小标记
              length: 10, // 属性length控制线长
              lineStyle: { // 属性lineStyle控制线条样式
                color: 'auto'
              }
            },
            splitLine: { // 大分隔线
              length: 10, // 属性length控制线长
              lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
                // color: '#b0b3b8',
                width: 2
              }
            },
            axisLabel: {
              // backgroundColor: 'auto',
              borderRadius: 1,
              color: '#eee',
              padding: 3,
              textShadowBlur: 2,
              textShadowOffsetX: 1,
              textShadowOffsetY: 1,
              // textShadowColor: '#222',
              textStyle: {
                fontSize: 15
              }
            },
            title: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 10,
              fontStyle: 'italic'
            },
            pointer: {
              width: 4,
              lengen: '50%'
            }
          }
        ]
      })
    }
    // initChart() {
    //   var data = []
    //   this.chart.setOption({
    //     tooltip: {
    //     },
    //     toolbox: {
    //       show: true,
    //       feature: {
    //         restore: { show: false },
    //         saveAsImage: { show: false }
    //       }
    //     },
    //     axisLine: {// 仪表盘轴线相关配置
    //       lineStyle: {
    //         width: 10,
    //         color: [1, '#8c929d']
    //       }
    //     },
    //     axisTick: { // 坐标轴小标记
    //       length: 5, // 属性length控制线长
    //       lineStyle: { // 属性lineStyle控制线条样式
    //         color: 'auto'
    //       }
    //     },
    //     splitLine: { // 分隔线
    //       length: 10, // 属性length控制线长
    //       lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
    //         color: '#b0b3b8'
    //       }
    //     },
    //     axisLabel: {
    //       backgroundColor: 'auto',
    //       borderRadius: 1,
    //       color: '#eee',
    //       padding: 3,
    //       textShadowBlur: 2,
    //       textShadowOffsetX: 1,
    //       textShadowOffsetY: 1,
    //       textShadowColor: '#222'
    //     },
    //     title: {
    //       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
    //       fontWeight: 'bolder',
    //       fontSize: 10,
    //       fontStyle: 'italic'
    //     },
    //     series: [
    //       {
    //         name: '温度值',
    //         type: 'gauge',
    //         // detail: { formatter: '{value}%' },
    //         z: 3,
    //         min: 0,
    //         max: 100,
    //         splitNumber: 5,
    //         radius: '100%', // 仪表盘半径
    //         data: data,
    //         startAngle: -30, // 起始角度
    //         endAngle: 210, // 结束角度
    //         center: ['50%', '50%'] // 仪表盘位置
    //       }
    //     ]
    //   })
    // }
  }
}
</script>
