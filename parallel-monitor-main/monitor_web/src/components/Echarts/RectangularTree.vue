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
    realTimeData: {
      type: Object,
      default: () => ({})
    },
    nodeId: {
      type: Number,
      default: null
    }
  },
  data() {
    // var that = this
    return {
      cpuChart: null,
      swapChart: null,
      indexs: [],
      // show: false,
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
          name: null,
          type: 'treemap',
          leafDepth: 2,
          squareRatio: 1 * 1,
          label: {
            show: true
            // color: '#fff'
          },
          // squareRatio: 0.5 * (1 + Math.sqrt(5)),
          // 控制图形平移与缩放
          roam: false,
          data: [],
          upperLabel: {
            show: true
          },
          animationDuration: 0,
          animationDelay: 0,
          animationEasing: 'quarticOut',
          breadcrumb: {
            show: true,
            height: 50,
            itemStyle: {

            }
          },
          levels: [
            // 一层样式控制
            {
              // 纯色选项
              color: ['#1A213A', '#637195', '#C35354', '#F4CCC5', '#BBD5DE'],
              // 渐变色选项
              // color: [
              //   {
              //     type: 'linear',
              //     x: 0,
              //     y: 0,
              //     x2: 0,
              //     y2: 1,
              //     colorStops: [{
              //       offset: 0, color: '#669FAB' // 0% 处的颜色
              //     }, {
              //       offset: 1, color: '#5D81C9' // 100% 处的颜色
              //     }],
              //     globalCoord: false // 缺省为 false
              //   }, {
              //     type: 'linear',
              //     x: 0,
              //     y: 0,
              //     x2: 1,
              //     y2: 0,
              //     colorStops: [{
              //       offset: 0, color: '#F99383' // 0% 处的颜色
              //     }, {
              //       offset: 1, color: '#656078' // 100% 处的颜色
              //     }],
              //     globalCoord: false // 缺省为 false
              //   }, {
              //     type: 'linear',
              //     x: 0,
              //     y: 0,
              //     x2: 0,
              //     y2: 1,
              //     colorStops: [{
              //       offset: 0, color: '#E36B78' // 0% 处的颜色
              //     }, {
              //       offset: 1, color: '#F88980' // 100% 处的颜色
              //     }],
              //     globalCoord: false // 缺省为 false
              //   }, {
              //     type: 'linear',
              //     x: 0,
              //     y: 0,
              //     x2: 0,
              //     y2: 1,
              //     colorStops: [{
              //       offset: 0, color: '#5773DE' // 0% 处的颜色
              //     }, {
              //       offset: 1, color: '#BE6E95' // 100% 处的颜色
              //     }],
              //     globalCoord: false // 缺省为 false
              //   }, {
              //     type: 'linear',
              //     x: 0,
              //     y: 0,
              //     x2: 0,
              //     y2: 1,
              //     colorStops: [{
              //       offset: 0, color: '#394D78' // 0% 处的颜色
              //     }, {
              //       offset: 1, color: '#506AC3' // 100% 处的颜色
              //     }],
              //     globalCoord: false // 缺省为 false
              //   }
              // ],
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
        if (val !== null) {
          this.myDefaultOption.series[0].data = [
            // CPU
            // 0
            {
              name: 'CPU平均负载',
              value: 5,
              children: [
                {
                  // 需固定区域
                  name: '温度' + '\n' + '\n' + ' ' + this.realTimeData['cpu_temperature'] + '摄氏度',
                  value: 15,
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

                },
                {
                  name: '频率' + '\n' + '\n' + ' ' + (this.realTimeData['frequency'] / 100000000).toFixed(2) + 'Ghz',
                  value: 5,
                  children: []
                }]
            },
            // 磁盘
            // 1
            {
              name: '磁盘',
              value: 4,
              children: [
                {
                  name: '磁盘使用率' + '\n' + '\n' + ' ' + this.realTimeData['disk_rate'] + '%',
                  value: 5,

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
                },
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
              name: '交换区使用率: ' + this.realTimeData['swap_rate'] + '%',
              value: 1,
              children: [
                {
                  name: '已使用' + '\n' + '\n' + ' ' + this.realTimeData['swap_used'] + 'GB',
                  value: this.realTimeData['swap_used']
                },
                {
                  name: '未使用' + '\n' + '\n' + ' ' + (this.realTimeData['swap_total'] - this.realTimeData['swap_used']).toFixed(2) + 'GB',
                  value: this.realTimeData['swap_total'] - this.realTimeData['swap_used']
                }
              ]
            },
            // 内存
            // 3
            {
              name: '内存使用率: ' + this.realTimeData['memory_rate'] + '%',
              value: 1,
              children: [
                {
                  name: '已使用' + '\n' + '\n' + ' ' + +this.realTimeData['memory_used'] + 'GB',
                  value: this.realTimeData['memory_used']
                },
                {
                  name: '未使用' + '\n' + '\n' + ' ' + (this.realTimeData['memory_total'] - this.realTimeData['memory_used']).toFixed(2) + 'GB',
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
            }
          ]
          this.myDefaultOption.series[0].name = this.nodeId + ' 号节点'
          this.handleDiff(val)
          this.cpuChart.setOption(this.myDefaultOption)
        }
      },
      deep: true
    }
  },
  created() {
  },
  mounted() {
    this.initChart()
    // 监听点击事件
    var that = this
    this.cpuChart.on('click', function(param) {
      console.log(param)
      // that.$alert('<strong>这是 <i>HTML</i> 片段</strong>', 'HTML 片段', {
      //   dangerouslyUseHTMLString: true
      // })
      that.show = true
    })
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
      this.cpuChart = echarts.init(this.$refs.cpuPannel, 'macarons')
      this.cpuChart.setOption(this.myDefaultOption)
    },
    handleDiff(data) {
      var that = this
      // 获取CPU信息
      var cpuData = data['cpu_state']
      if (cpuData !== {}) {
        Object.keys(cpuData).forEach((key) => {
          if (key !== 'temperature') {
            that.$set(that.myDefaultOption.series[0].data[0].children[0].children, key, { name: 'CPU ' + key + ' 温度： ' + cpuData[key]['temperature'] + '摄氏度', value: 1 })
            that.$set(that.myDefaultOption.series[0].data[0].children[1].children, key, { name: 'CPU ' + key + ' 用户使用率： ' + (cpuData[key]['cpu_user']).toFixed(2) + '%', value: 1, children: [] })
            that.$set(that.myDefaultOption.series[0].data[0].children[2].children, key, { name: 'CPU ' + key + ' 系统使用率： ' + (cpuData[key]['cpu_system']).toFixed(2) + '%', value: 1, children: [] })
            that.$set(that.myDefaultOption.series[0].data[0].children[3].children, key, { name: 'CPU ' + key + ' 频率： ' + (cpuData[key]['frequency'] / 1000000).toFixed(2) + 'GHz', value: 1, children: [] })
            // 核心
            var tthat = that
            var coreData = cpuData[key]['core_state']
            var cpuNode = key
            Object.keys(coreData).forEach((key) => {
              if (coreData[key]['cpu_user'] === 0) {
                tthat.$set(tthat.myDefaultOption.series[0].data[0].children[1].children[cpuNode].children, key, { name: '核心 ' + key + ' : ' + (coreData[key]['cpu_user']).toFixed(2) + '%', value: 0.1 })
              } else {
                tthat.$set(tthat.myDefaultOption.series[0].data[0].children[1].children[cpuNode].children, key, { name: '核心 ' + key + ' : ' + (coreData[key]['cpu_user']).toFixed(2) + '%', value: coreData[key]['cpu_user'] })
              }
              if (coreData[key]['cpu_system'] === 0) {
                tthat.$set(tthat.myDefaultOption.series[0].data[0].children[2].children[cpuNode].children, key, { name: '核心 ' + key + ' ： ' + (coreData[key]['cpu_system']).toFixed(2) + '%', value: 0.1 })
              } else {
                tthat.$set(tthat.myDefaultOption.series[0].data[0].children[2].children[cpuNode].children, key, { name: '核心 ' + key + ' ： ' + (coreData[key]['cpu_system']).toFixed(2) + '%', value: coreData[key]['cpu_system'] })
              }
              tthat.$set(tthat.myDefaultOption.series[0].data[0].children[3].children[cpuNode].children, key, { name: '核心 ' + key + ' ： ' + (coreData[key]['frequency'] / 1000000).toFixed(2) + 'GHz', value: coreData[key]['frequency'] })
            })
          }
        })
      }
      // 获取磁盘信息
      var diskData = data['disk_state']
      if (diskData !== {}) {
        Object.keys(diskData).forEach((key) => {
          that.$set(that.myDefaultOption.series[0].data[1].children[1].children, key, { name: '磁盘 ' + key + '读速度 : ' + diskData[key]['read_speed'].toFixed(2) + 'KiB/S', value: 1 })
          that.$set(that.myDefaultOption.series[0].data[1].children[2].children, key, { name: '磁盘 ' + key + '写速度 ： ' + diskData[key]['write_speed'].toFixed(2) + 'KiB/S', value: 1 })
        })
      }
      // 获取网卡信息
      var networkData = data['network_state']
      if (networkData !== {}) {
        Object.keys(networkData).forEach((key) => {
          that.$set(that.myDefaultOption.series[0].data[4].children[0].children, key, { name: '网卡 ' + key + '上传速度 : ' + networkData[key]['up_speed'].toFixed(2) + 'KiB/S', value: 1 })
          that.$set(that.myDefaultOption.series[0].data[4].children[1].children, key, { name: '网卡 ' + key + '下载速度 ： ' + networkData[key]['down_speed'].toFixed(2) + 'KiB/S', value: 1 })
        })
      }
    },
    test(data) {
      console.log(data)
    }
  }
}
</script>
<style scoped>
</style>>
