<template>
  <div style="position:relative">
    <div ref="cpuPannel" class="Pannel1" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

// const animationDuration = 3000

export default {
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
    },
    left: {
      type: String,
      default: '100%'
    },
    top: {
      type: String,
      default: '100%'
    },
    realTimeData: {
      type: Object,
      default: () => ({})
    },
    nodeId: {
      type: Number,
      default: null
    },
    Rectflag: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      chart: null,
      coresChart: null,
      swapChart: null,
      type: '',
      myTitle: '',
      // 外层矩形图
      myDefaultOption: {
        // silent: true,
        tooltip: {
          show: true,
          formatter: function(params) {
            // console.log(params)
            if (params['data']['seriesName'] !== undefined) { return params['data']['seriesName'] + '<br>' + params['data']['name'] } else {
              return params['data']['name']
            }
          }
        },
        aria: {
          enabled: true
        },
        series: [{
          nodeClick: false,
          name: null,
          type: 'treemap',
          leafDepth: 2,
          squareRatio: 1 * 1,
          label: {
            show: true
          },
          // squareRatio: 0.5 * (1 + Math.sqrt(5)),
          // 控制图形平移与缩放
          roam: 'move',
          data: [],
          upperLabel: {
            show: true
          },
          animationDuration: 0,
          animationDelay: 0,
          animationEasing: 'quarticOut',
          breadcrumb: {
            show: false,
            height: 40
          },
          levels: [
            // 一层样式控制
            {
              // 纯色选项
              // color: ['#1A213A', '#637195', '#C35354', '#F4CCC5', '#BBD5DE',''],
              // 渐变色选项
              color: [
                {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#669FAB' // 0% 处的颜色
                  }, {
                    offset: 1, color: '#5D81C9' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }, {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 1,
                  y2: 0,
                  colorStops: [{
                    offset: 0, color: '#F99383' // 0% 处的颜色
                  }, {
                    offset: 1, color: '#656078' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }, {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#E36B78' // 0% 处的颜色
                  }, {
                    offset: 1, color: '#F88980' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }, {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#5773DE' // 0% 处的颜色
                  }, {
                    offset: 1, color: '#BE6E95' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }, {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#394D78' // 0% 处的颜色
                  }, {
                    offset: 1, color: '#506AC3' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }
              ],
              itemStyle: {
                borderColor: '#fff',
                borderWidth: 5,
                gapWidth: 5
              },
              upperLabel: {
                show: false
              }
              // borderWidth: 20
            },
            // 二层样式控制
            {
              color: 'inherit',
              itemStyle: {
                borderColor: '#333',
                borderWidth: 5,
                gapWidth: 1
              },
              emphasis: {
                itemStyle: {
                  borderColor: '#ddd'
                }
              },
              label: {
                color: '#fff',
                position: 'top',
                distance: 10
              }
            },
            // 三层样式控制
            {
              colorSaturation: [0.35, 0.5],
              itemStyle: {
                borderColor: '#333',
                borderWidth: 5,
                gapWidth: 1
              },
              emphasis: {
                itemStyle: {
                  borderColor: '#ddd'
                }
              },
              label: {
                show: true,
                color: '#fff',
                fontWeight: 'bolder',
                fontSize: 15
                // rotate: '45'
                // formatter: function(params) {
                //   // console.log(params['name'] + ' ' + params['dataIndex'])
                //   return that.indexs[params['dataIndex']] + '\n' + ' ' + params['name']
                // }
              }
            },
            // 四层样式控制
            {
              colorSaturation: [0.35, 0.5],
              itemStyle: {
                borderColor: '#444',
                borderWidth: 10,
                gapWidth: 1
              },
              emphasis: {
                itemStyle: {
                  borderColor: '#ddd'
                }
              },
              label: {
                show: true,
                color: '#fff',
                fontWeight: 'bolder',
                fontSize: 12
              }
            }
          ]
        }]
      }
    }
  },
  watch: {
    realTimeData: {
      handler(val) {
        // console.log(val)
        if (val !== null && this.Rectflag === true) {
          this.myDefaultOption.series[0].data = [
            // CPU
            // 0
            {
              name: 'CPU平均负载',
              value: 5,
              children: [
                {
                  name: '频率' + '\n' + '\n' + ' ' + (this.realTimeData['frequency'] / 100000000).toFixed(2) + 'Ghz',
                  value: 5,
                  children: []
                },
                {
                  // 需固定区域
                  name: '温度' + '\n' + '\n' + ' ' + this.realTimeData['cpu_temperature'] + '摄氏度',
                  value: 5,
                  children: []
                },
                {
                  name: '用户使用率' + '\n' + '\n' + ' ' + this.realTimeData['cpu_user'] + '%',
                  value: 5,
                  children: []

                },
                {
                  name: '系统使用率' + '\n' + '\n' + ' ' + this.realTimeData['cpu_system'] + '%',
                  value: 5,
                  children: []

                }]
            },
            // 磁盘
            // 1
            {
              name: '磁盘',
              value: 3,
              children: [
                {
                  name: '读速度' + '\n' + '\n' + ' ' + this.realTimeData['read_speed'] + 'KiB/S',
                  value: 5,
                  children: []
                },
                // 6
                {
                  name: '写速度' + '\n' + '\n' + ' ' + this.realTimeData['write_speed'] + 'KiB/S',
                  value: 5,
                  children: []
                }
              ]
            },
            // 交换区
            // 2
            {
              name: '交换区使用率: ' + this.realTimeData['swap_rate'].toFixed(0) + '%',
              value: 1,
              children: [
                {
                  name: '交换区已使用' + '\n' + '\n' + ' ' + this.realTimeData['swap_used'] + 'GB',
                  value: this.realTimeData['swap_used']
                },
                {
                  name: '交换区未使用' + '\n' + '\n' + ' ' + (this.realTimeData['swap_total'] - this.realTimeData['swap_used']).toFixed(2) + 'GB',
                  value: this.realTimeData['swap_total'] - this.realTimeData['swap_used']
                }
              ]
            },
            // 内存
            // 3
            {
              name: '内存使用率: ' + this.realTimeData['memory_rate'].toFixed(0) + '%',
              value: 1,
              children: [
                {
                  name: '内存已使用' + '\n' + '\n' + ' ' + +this.realTimeData['memory_used'] + 'GB',
                  value: this.realTimeData['memory_used']
                },
                {
                  name: '内存未使用' + '\n' + '\n' + ' ' + (this.realTimeData['memory_total'] - this.realTimeData['memory_used']).toFixed(2) + 'GB',
                  value: this.realTimeData['memory_total'] - this.realTimeData['memory_used']
                }
              ]
            },
            // 网卡
            // 4
            {
              name: '网卡',
              value: 3,
              children: [
                {
                  name: '上传速度' + '\n' + '\n' + ' ' + (this.realTimeData['up_speed']).toFixed(2) + 'KiB/S',
                  value: 5,
                  children: []
                },
                {
                  name: '下载速度' + '\n' + '\n' + ' ' + (this.realTimeData['down_speed']).toFixed(2) + 'KiB/S',
                  value: 5,
                  children: []
                }]
            },
            {
              name: '磁盘使用率: ' + this.realTimeData['disk_rate'].toFixed(0) + '%',
              value: 1,
              children: [
                {
                  name: '磁盘未使用大小' + '\n' + '\n' + ' ' + (this.realTimeData['disk_total'] - this.realTimeData['disk_used']).toFixed(2) + 'GB',
                  value: this.realTimeData['disk_total'] - this.realTimeData['disk_used']
                },
                {
                  name: '已使用磁盘大小' + '\n' + '\n' + ' ' + this.realTimeData['disk_used'] + 'GB',
                  value: this.realTimeData['disk_used']
                }
              ]
            }
          ]
          // this.myDefaultOption.series[0].name = this.nodeId + ' 号节点'
          // this.handleDiff(val)
          this.chart.setOption(this.myDefaultOption)
        }
      },
      deep: true
    },
    Rectflag: {
      handler(val) {
        this.clickListen()
      }
      // deep: true
    }
  },
  created() {
    this.myTitle = this.nodeId + '号节点'
  },
  mounted() {
    this.initChart()
    // 监听点击事件
    // this.clickListen()
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
      this.chart = echarts.init(this.$refs.cpuPannel, 'macarons')
      this.chart.setOption(this.myDefaultOption)
    }
    // clickListen() {
    //   var that = this
    //   this.chart.on('click', function(param) {
    //     console.log(param)
    //     // 停止第一层的动态数据触发器
    /*    that.Rectflag = false*/
    /*    // 截取字符串*/
    /*    var tran = ''*/
    /*    var slice5 = param.name.slice(0, 6)*/
    /*    var i = 0*/
    /*    while (i < 6) {*/
    /*      if (slice5[i] !== '\n') { tran += slice5[i] } else {*/
    /*        break*/
    /*      }*/
    /*      i++*/
    /*    }*/
    /*    that.type = tran*/
    /*    // 清空矩形图数据*/
    /*    that.myOtherOption.yAxis.data = []*/
    /*    that.myOtherOption.series[0].data = []*/
    /*    // 雷达图*/
    /*    if (tran === '频率' || tran === '系统使用率' || tran === '用户使用率') {*/
    /*      // that.type = tran*/
    /*      that.handleCoresData(that.realTimeData['cpu_state'], tran)*/
    /*      that.chart.setOption(that.myCoresOption)*/
    //     }
    //     if (tran === '读速度' || tran === '写速度') {
    //       that.handleOtherData(that.realTimeData['disk_state'])
    //       that.chart.setOption(that.myOtherOption)
    //     }
    //     if (tran === '上传速度' || tran === '下载速度') {
    //       that.handleOtherData(that.realTimeData['network_state'])
    //       that.chart.setOption(that.myOtherOption)
    //     }
    //   })
    // }
  }
}
</script>
<style scoped>
.Pannel1{
  position: relative;
  /* margin-top: -7%; */
  /* z-index: 50; */
}
</style>>
