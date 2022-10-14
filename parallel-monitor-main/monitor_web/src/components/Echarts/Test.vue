<template>
  <div style="position:relative">
    <div ref="cpuPannel" :style="{height:height,width:width}" />
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
    },
    flag: {
      type: Boolean,
      default: false
    }
  },
  data() {
    var that = this
    return {
      chart: null,
      coresChart: null,
      swapChart: null,
      indexs: [],
      Rectflag: true,
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
          // nodeClick: false,
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
      },
      // 核心级别雷达图
      myCoresOption: {
        // backgroundColor: '#161627',
        title: {
          text: '',
          // subtext: 'Fake Data',
          top: 10,
          left: 10
        },
        tooltip: {
          trigger: 'item'
          // show: true
        },
        toolbox: {
          show: true,
          feature: {
            myTool1: {
              show: true,
              title: '返回',
              icon: 'path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891',
              onclick: function() {
                that.Rectflag = true
                that.chart.dispose()
                that.chart = null
                that.chart = echarts.init(that.$refs.cpuPannel, 'macarons')
                that.chart.setOption(that.myDefaultOption)
              }
            }
          }
        },
        legend: {
          bottom: 5,
          // data需自己生成
          data: [],
          itemGap: 20,
          textStyle: {
            fontSize: 14
          }
        },
        radar: {
          // 需自己生成indicator属性
          indicator: [],
          symbol: 'circle',
          splitNumber: 5,
          name: {
            textStyle: {
              color: 'rgb(238, 197, 102)'
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: [
                'rgba(238, 197, 102, 0.1)', 'rgba(238, 197, 102, 0.2)',
                'rgba(238, 197, 102, 0.4)', 'rgba(238, 197, 102, 0.6)',
                'rgba(238, 197, 102, 0.8)', 'rgba(238, 197, 102, 1)'
              ].reverse()
            }
          },
          splitArea: {
            show: true
          }
        },
        series: []
      },
      // 动态柱状图
      myOtherOption: {
        // backgroundColor: '#000',
        grid: {
          left: '3%',
          right: '15%',
          bottom: '20%',
          containLabel: true
        },
        xAxis: {
          // type: 'value',
          max: 'dataMax',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitArea: {
            show: true
          },
          // 不显示X轴刻度线和数字
          splitLine: { show: false },
          axisLabel: { show: false }
        },
        toolbox: {
          show: true,
          feature: {
            myTool1: {
              show: true,
              title: '返回',
              icon: 'path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891',
              onclick: function() {
                that.Rectflag = true
                // 先销毁
                that.chart.dispose()
                that.chart = null
                // 重新回到矩形图
                that.chart = echarts.init(that.$refs.cpuPannel, 'macarons')
                that.chart.setOption(that.myDefaultOption)
              }
            }
          }
        },
        yAxis: {
          type: 'category',
          data: [],
          splitLine: { show: false },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          inverse: true,
          animationDuration: 300,
          animationDurationUpdate: 300,
          offset: 10,
          axisLabel: {
            show: true,
            valueAnimation: true,
            fontSize: 14,
            rich: {
              flag: {
                fontSize: 25,
                padding: 5
              }
            }
          }
        },
        series: [
          {
            realtimeSort: true,
            name: '',
            type: 'bar',
            data: [],
            barWidth: 50,
            barGap: 5,
            smooth: true,
            valueAnimation: true,
            label: {
              show: true,
              position: 'right',
              valueAnimation: true,
              fontFamily: 'monospace',
              formatter: function(params) {
                return params.data + 'KiB/s'
              }
            },
            itemStyle: {
              emphasis: {
                barBorderRadius: 7
              },
              // 颜色样式部分
              normal: {
                barBorderRadius: 7,
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: '#3977E6' },
                  { offset: 1, color: '#37BBF8' }
                ])
              }
            }
          }
        ],
        legend: {
          show: true
        },
        animationDuration: 500,
        animationDurationUpdate: 500,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear'
      }
    }
  },
  watch: {
    realTimeData: {
      handler(val) {
        // console.log(val)
        // this.chart.resize()
        if (val !== null && this.Rectflag === true) {
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
          this.myDefaultOption.series[0].name = this.nodeId + ' 号节点'
          // this.handleDiff(val)
          this.chart.setOption(this.myDefaultOption)
        } else {
          if (this.type === '频率' || this.type === '用户使用率' || this.type === '系统使用率') {
            this.handleCoresData(val['cpu_state'], this.type)
            this.chart.setOption(this.myCoresOption)
          } else if (this.type === '读速度' || this.type === '写速度') {
            this.handleOtherData(this.realTimeData['disk_state'])
            this.chart.setOption(this.myOtherOption)
          } else if (this.type === '上传速度' || this.type === '下载速度') {
            this.handleOtherData(this.realTimeData['network_state'])
            this.chart.setOption(this.myOtherOption)
          } else {
            // this.Rectflag
          }
        }
      },
      deep: true
    },
    Rectflag: {
      handler(val) {
        this.clickListen()
      }
    },
    flag: {
      handler(val) {
        console.log(this.height)
        setTimeout(() => {
          this.chart.resize()
        })
      }
    }
  },
  created() {
    this.myTitle = this.nodeId + '号节点'
  },
  mounted() {
    this.initChart()
    // 监听点击事件
    this.clickListen()
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
    },
    handleDiff(data) {
      var that = this
      // 获取CPU信息
      var cpuData = data['cpu_state']
      if (cpuData !== {}) {
        // that.coresData = cpuData
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
    // 雷达图赋值
    handleCoresData(data, type) {
      // console.log(data)
      var that = this
      Object.keys(data).forEach((key) => {
        if (key !== 'temperature') {
          that.$set(that.myCoresOption.legend.data, key, key + '号CPU')
          // that.myCoresOption.series[key].name = key + '号CPU'
          that.$set(that.myCoresOption.series, key, {
            // name与data对应
            name: '',
            type: 'radar',
            lineStyle: {
              normal: {
                width: 1,
                opacity: 0.5
              }
            },
            // data需自己赋值
            data: [{
              value: [],
              label: {
                show: true,
                formatter: function(params) {
                  return params.value
                }
              }
            }
            ],
            symbol: 'none',
            itemStyle: {
              color: '#99CCFF'
            },
            areaStyle: {
              opacity: 0.6
            }
          })
          that.myCoresOption.series[key].name = key + '号CPU'
          var inCpuCoresData = data[key]['core_state']
          var tthat = that
          var cpuKey = key
          Object.keys(inCpuCoresData).forEach((key) => {
            // indicator赋值
            // var tran = []
            if (type === '频率') {
              // tthat.type = '频率'
              tthat.myCoresOption.title.text = 'CPU频率总览'
              tthat.myCoresOption.radar.indicator[key] = { max: 4000000, text: key + '号核心' }
              tthat.myCoresOption.series[cpuKey].data[0].value.push(inCpuCoresData[key]['frequency'])
              // console.log(tthat.myCoresOption.series[cpuKey].data[0])
              // tthat.myCoresOption.series[cpuKey].data[0].push(inCpuCoresData[key]['frequency'])
            }
            if (type === '系统使用率') {
              // tthat.type = '系统使用率'
              tthat.myCoresOption.title.text = 'CPU系统使用率总览'
              tthat.myCoresOption.radar.indicator[key] = { max: 100, text: key + '号核心' }
              tthat.myCoresOption.series[cpuKey].data[0].value.push(inCpuCoresData[key]['cpu_system'])
            }
            if (type === '用户使用率') {
              // tthat.type = '用户使用率'
              tthat.myCoresOption.title.text = 'CPU用户使用率总览'
              tthat.myCoresOption.radar.indicator[key] = { max: 100, text: key + '号核心' }
              tthat.myCoresOption.series[cpuKey].data[0].value.push(inCpuCoresData[key]['cpu_user'])
            }
          })
        }
      })
    },
    handleOtherData(data) {
      var that = this
      Object.keys(data).forEach((key) => {
        if (that.type === '读速度') {
          that.myOtherOption.series[0].name = '磁盘读速度'
          that.$set(that.myOtherOption.yAxis.data, key, key + '号磁盘')
          that.$set(that.myOtherOption.series[0].data, key, data[key]['read_speed'])
        }
        if (that.type === '写速度') {
          that.myOtherOption.series[0].name = '磁盘写速度'
          that.$set(that.myOtherOption.yAxis.data, key, key + '号磁盘')
          that.$set(that.myOtherOption.series[0].data, key, data[key]['write_speed'])
        }
        if (that.type === '下载速度') {
          that.myOtherOption.series[0].name = '网卡下载速度'
          that.$set(that.myOtherOption.yAxis.data, key, key + '号网卡')
          that.$set(that.myOtherOption.series[0].data, key, data[key]['down_speed'])
        }
        if (that.type === '上传速度') {
          that.myOtherOption.series[0].name = '网卡上传速度'
          that.$set(that.myOtherOption.yAxis.data, key, key + '号网卡')
          that.$set(that.myOtherOption.series[0].data, key, data[key]['up_speed'])
        }
      })
    },
    // 点击事件监听
    clickListen() {
      var that = this
      this.chart.on('click', function(param) {
        // 停止第一层的动态数据触发器
        that.Rectflag = false
        // 截取字符串
        var tran = ''
        var slice5 = param.name.slice(0, 6)
        var i = 0
        while (i < 6) {
          if (slice5[i] !== '\n') { tran += slice5[i] } else {
            break
          }
          i++
        }
        that.type = tran
        // 清空雷达图数据
        that.myCoresOption.title.text = ''
        that.myCoresOption.radar.indicator = []
        that.myCoresOption.series = []
        // 清空矩形图数据
        that.myOtherOption.yAxis.data = []
        that.myOtherOption.series[0].data = []
        // 雷达图
        if (tran === '频率' || tran === '系统使用率' || tran === '用户使用率') {
          // that.type = tran
          that.handleCoresData(that.realTimeData['cpu_state'], tran)
          that.chart.setOption(that.myCoresOption)
        }
        if (tran === '读速度' || tran === '写速度') {
          that.handleOtherData(that.realTimeData['disk_state'])
          that.chart.setOption(that.myOtherOption)
        }
        if (tran === '上传速度' || tran === '下载速度') {
          that.handleOtherData(that.realTimeData['network_state'])
          that.chart.setOption(that.myOtherOption)
        }
      })
    }
  }
}
</script>
<style scoped>
</style>>
