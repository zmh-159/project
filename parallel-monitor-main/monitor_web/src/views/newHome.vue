<template>
  <div :class="widthclassinfo">
    <el-container class="my-el-container">
      <!-- 主要部分 -->
      <el-row class="my-main-container" :gutter="15">
        <!-- 左侧部分 -->
        <el-col class="my-left-aside" :xs="24" :sm="24" :md="10" :lg="7" :xl="7" :span="7">
          <el-divider content-position="center">集群节点监控</el-divider>
          <div class="nodeinfo">
            <el-row :gutter="5" class="left-row-1">
              <el-col :span="8">
                <el-card shadow="hover" @click.native="goTo('nodeInfo')">
                  <span class="all-title-style">总结点</span>
                  <h2> {{ topData[0] }}</h2>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" @click.native="goTo('nodeInfo')">
                  <span class="all-title-style">在线节点</span>
                  <h2>{{ topData[1] }}</h2>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" @click.native="goTo('nodeInfo')">
                  <span class="all-title-style">离线节点 </span>
                  <h2>{{ topData[2] }}</h2>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="5" class="left-row-2">
              <el-col :span="6">
                <el-card shadow="hover" @click.native="goTo('nodeInfo')">
                  <span class="all-title-style">异常节点 </span>
                  <h2>{{ alarmNodeNum.length }}</h2>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="never">
                  <span class="all-title-style">CPU数量</span>
                  <h2>{{ topData[1] }}</h2>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <span class="all-title-style">磁盘已使用/总量 </span>
                  <h2>{{ (parseInt(diskUsed[1])/1000).toFixed(2) }}/{{ parseInt(diskUsed[0])/1000 }}TB</h2>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <el-divider content-position="center">集群节点展示</el-divider>
          <!-- <div v-if="Object.keys(items).length >= 10" class="my-node-pannel">
            <div v-for="(item,i) in items" :key="item.index" class="nodeDiv many" @click="goTo('nodeDetails',myAllTitle[i])">
              <el-popover
                placement="right"
                width="200"
                trigger="hover"
              >
                <div @click.native="goTo('nodeDetails',myAllTitle[i])">
                  <el-row>
                    <el-col :span="24"><div>{{ myAllTitle[i] }}号节点</div></el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="23">
                      <el-row>
                        <el-col :span="12">
                          <span>CPU使用率</span>
                          <div>{{ nodeData[i][0] }}%</div>
                        </el-col>
                        <el-col :span="12">
                          <span>内存使用率</span>
                          <h2>{{ nodeData[i][1] }}%</h2>
                        </el-col>
                      </el-row>
                      <el-row style="margin-top:6%">
                        <el-col :span="12">
                          <span>磁盘使用率</span>
                          <div>{{ nodeData[i][2] }}%</div>
                        </el-col>
                        <el-col :span="12">
                          <span>显卡使用率</span>
                          <div>{{ nodeData[i][3] }}%</div>
                        </el-col>
                      </el-row>
                    </el-col>
                  </el-row>
                </div>
                <el-card slot="reference" />
              </el-popover>
            </div>
          </div> -->
          <div class="my-node-pannel">
            <div v-for="(item,i) in items" :key="item.index" class="nodeDiv few">
              <el-card @click.native="goTo('nodeDetails',myAllTitle[i])">
                <el-row>
                  <el-col :span="24"><div><span class="single-div-few">{{ myAllTitle[i] }}</span>号节点</div></el-col>
                </el-row><br>
                <el-row>
                  <el-col :span="23">
                    <el-row>
                      <el-col :span="12">
                        <span>CPU使用率</span>
                        <div class="single-div-few">{{ nodeData[i][0] }}%</div>
                      </el-col>
                      <el-col :span="12">
                        <span>内存使用率</span>
                        <div class="single-div-few">{{ nodeData[i][1] }}%</div>
                      </el-col>
                    </el-row>
                    <el-row style="margin-top:6%">
                      <el-col :span="12">
                        <span>磁盘使用率</span>
                        <div class="single-div-few">{{ nodeData[i][2] }}%</div>
                      </el-col>
                      <el-col :span="12">
                        <span>显卡使用率</span>
                        <div class="single-div-few">{{ nodeData[i][3] }}%</div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-card>
            </div>
          </div>
          <!-- <cabinets :cheight="cheight" :bheight=bheight @bindnode="bindNode" /> -->
        </el-col>
        <!-- 中间部分 -->
        <el-col class="all-info-container" :xs="24" :sm="24" :md="14" :lg="10" :xl="10" :span="10">
          <div class="header-main">
            <el-divider content-position="center">集群节点监控</el-divider>
            <el-row :gutter="5" class="middle-row-1">
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="cpuUsed" />
                  </div>
                  <div>
                    <span class="all-title-style">CPU使用率</span>
                    <h2>{{ cpuUsed[2] }}%</h2>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="memoryUsed" />
                  </div>
                  <div>
                    <span class="all-title-style">内存使用率</span>
                    <h2>{{ memoryUsed[2] }}%</h2>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="temData" />
                  </div>
                  <div>
                    <span class="all-title-style">温度</span>
                    <h2>{{ temData[2] }}</h2>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="5" class="middle-row-2">
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="diskUsed" />
                  </div>
                  <div>
                    <span class="all-title-style">磁盘使用率</span>
                    <h2>{{ diskUsed[2] }}%</h2>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="gpuUsed" />
                  </div>
                  <div>
                    <span class="all-title-style">显卡使用率</span>
                    <h2>{{ gpuUsed[2] }}%</h2>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="icon-style">
                    <gauge-1 :data="entropyData" :max="1" />
                  </div>
                  <div>
                    <span class="all-title-style">熵</span>
                    <h2>{{ entropyData[2] }}</h2>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <!-- <Temprature1 class="pannel temprature" :data="temData" :legend="legendTem" :max="maxTem" /> -->

            <!-- <Temprature2 class="pannel entropy" :data="entropyData" :legend="legendEntropy" :max="maxEnt" /> -->
            <el-divider content-position="center" style="margin-top:-5px">相空间展示</el-divider>
            <div class="main-pannel">
              <Scatter3D class="scatter" :data="scatterData" :legend-data="AxisData" />
            </div>
          </div>
        </el-col>
        <!-- 右侧部分 -->
        <el-col class="my-right-aside" :xs="24" :sm="24" :md="24" :lg="7" :xl="7" :span="7">
          <el-divider content-position="center">集群报警总览</el-divider>
          <el-row :gutter="5" class="right-row">
            <el-col :span="8">
              <el-card shadow="hover" @click.native="goTo('AlarmList')">
                <!-- <svg-icon icon-class="monitor" /> -->
                <span class="all-title-style">季度报警数</span>
                <h2> {{ topData[3] }}</h2>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" @click.native="goTo('AlarmList')">
                <span class="all-title-style">已解决</span>
                <h2>{{ topData[4] }}</h2>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" @click.native="goTo('AlarmList')">
                <span class="all-title-style">未解决 </span>
                <h2>{{ topData[5] }}</h2>
              </el-card>
            </el-col>
          </el-row>
          <el-divider content-position="center" class="nodeserver">报警清单</el-divider>
          <el-table ref="table" :data="listData" class="my-right-table">
            <el-table-column type="index" label="序号" />
            <el-table-column prop="nodeId" label="节点号" />
            <el-table-column prop="alarmItem" label="报警项" width="180%" />
            <el-table-column prop="alarmNum" label="报警值" />
            <el-table-column prop="date" label="时间" />
          </el-table>
        </el-col>
      </el-row>
      <el-divider content-position="center">{{ month }}月集群情况</el-divider>
      <el-row class="my-row">
        <el-col :span="24">
          <div class="month-data-card">
            <div ref="monthChart" :class="className" :style="{height:height,width:width}" />
          </div>
        </el-col>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import Gauge1 from '@/components/Echarts/Gauge1'
import Scatter3D from '@/components/Echarts/scatter3D'
import pmNodeState from '@/api/pm/pmNodeState'
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
import Cabinets from '@/components/Cabinets/Cabinets.vue'
import tools from '@/utils/common.js'

export default {
  name: 'Dash',
  components: {
    Scatter3D,
    Gauge1,
    Cabinets
  },
  data() {
    return {
      statuscolor: {
        off: 'background:#333333',
        on: 'background:#008500',
        alarm: 'background:#ff4040'
      },
      MutiAlarmItem: {
        'disk_read_speed': '磁盘读速度',
        'disk_write_speed': '磁盘写速度',
        'cpu_temperature': 'CPU温度',
        'cpu_user': 'CPU用户使用率',
        'cpu_system': 'CPU系统使用率',
        'cpu_frequency': 'CPU频率',
        'cpu_busy': 'CPU总使用率',
        'memory_utilization': '显存使用率',
        'gpu_utilization': '显卡使用率',
        'gpu_temperature': '显卡温度',
        'gpu_power_draw': '显卡功耗',
        'network_up_speed': '网卡上传速度',
        'network_down_speed': '网卡下载速度'
      },
      SigleAlarmItem: {
        'gpu_temperature_avg': '显卡平均温度',
        'gpu_power_draw_avg': '显卡平均功耗',
        'gpu_power_draw_total': '显卡总功耗',
        'gpu_utilization_avg': '显卡平均使用率',
        'cpu_system_avg': 'CPU平均系统使用率',
        'cpu_user_avg': 'CPU平均用户使用率',
        'cpu_busy_avg': 'CPU平均总使用率',
        'cpu_frequency_avg': 'CPU平均频率',
        'cpu_temperature_avg': 'CPU平均温度',
        'network_down_speed_total': '网卡总下载速度',
        'network_up_speed_total': '网卡总上传速度',
        'memory_rate': '内存使用率',
        'memory_used': '内存使用',
        'memory_utilization_avg': '显存平均使用率',
        'disk_used': '磁盘使用',
        'disk_rate': '磁盘使用率',
        'disk_write_speed_total': '磁盘总写速度',
        'disk_read_speed_total': '磁盘总读速度',
        'swap_rate': '交换区使用率',
        'swap_used': '交换区使用'
      },
      bheight: 1,
      cheight: 32,
      nodeData: [],
      alarmNodeNum: [],
      alarm_Sum: 0,
      alarmindex: 0,
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      // 块状节点数据
      myAllTitle: [],
      items: [],
      myAllNodeStyle: [],
      // 任务版数据
      tableData: [],
      // 相空间数据
      AxisData: ['CPU使用率', '内存使用率', '磁盘使用率'],
      scatterData: [],
      // 月图表数据
      chart: null,
      className: '',
      height: '100%',
      width: '100%',
      myChartOption: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          top: '3%',
          data: ['磁盘使用率', '内存使用率', 'CPU使用率', 'GPU使用率'],
          textStyle: {
            fontSize: tools.fontSize(14)
            // color: '#fff'
          },
          itemHeight: tools.fontSize(14),
          itemWidth: tools.fontSize(25)
        },
        axisLabel: {
          // color: '#fff'
        },
        grid: {
          top: tools.fontSize(10) + 'px',
          left: tools.fontSize(15) + 'px',
          right: tools.fontSize(70) + 'px',
          bottom: tools.fontSize(30) + 'px',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [],
            axisLabel: {
              fontSize: tools.fontSize(14)
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            max: 100,
            axisLabel: {
              fontSize: tools.fontSize(14)
            },
            splitLine: {
              show: false
            },
            splitArea: {
              show: false
            }
          }
        ],
        series: [
          {
            name: '磁盘使用率',
            type: 'line',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            // markPoint: {
            //   symbolSize: [tools.fontSize(50), tools.fontSize(50)],
            //   data: [
            //     { type: 'max', name: 'Max' },
            //     { type: 'min', name: 'Min' }
            //   ]

            // },
            markLine: {
              data: [{ type: 'average', name: 'Avg' }],
              label: {
                fontSize: tools.fontSize(14)
              }

            },
            data: []
          },
          {
            name: '内存使用率',
            type: 'line',
            // stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            // markPoint: {
            //   symbolSize: [30, 30],

            //   data: [
            //     { type: 'max', name: 'Max' },
            //     { type: 'min', name: 'Min' }
            //   ]
            // },
            markLine: {
              data: [{ type: 'average', name: 'Avg' }],
              label: {
                fontSize: tools.fontSize(14)
              }
            },
            data: []
          },
          {
            name: 'CPU使用率',
            type: 'line',
            // stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            // markPoint: {
            //   symbolSize: [30, 30],
            //   data: [
            //     { type: 'max', name: 'Max' },
            //     { type: 'min', name: 'Min' }
            //   ]
            // },
            markLine: {
              data: [{ type: 'average', name: 'Avg' }],
              label: {
                fontSize: tools.fontSize(14)
              }
            },
            data: []
          },
          {
            name: 'GPU使用率',
            type: 'line',
            // stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            // markPoint: {
            //   symbolSize: [30, 30],

            //   data: [
            //     { type: 'max', name: 'Max' },
            //     { type: 'min', name: 'Min' }
            //   ]
            // },
            markLine: {
              data: [{ type: 'average', name: 'Avg' }],
              label: {
                fontSize: tools.fontSize(14)
              }
            },
            data: []
          }
        ]
      },
      monthData: [[], [], [], [], []],
      // 温度和熵
      temData: [],
      entropyData: [],
      maxTem: 100,
      maxEnt: 1,
      entropyArray: [],
      flipped: [false, false, false, false],
      // 集群基本情况数据
      diskUsed: [],
      memoryUsed: [],
      cpuUsed: [],
      gpuUsed: [],
      // 弹窗
      dialogVisible: false,
      // 选项卡绑定值
      activeName: 'first',
      // 保存时间值
      month: null,
      nowDate: '',
      topData: [0, 0, 0, 0, 0, 0],
      currentDate: '',
      timer: '',
      listData: [],
      warning: [],
      classOption: {
        step: 0.4
      },
      contextColor: [],
      popoverContext: [],
      activeIndex: '1'
      // visible: false
    }
  },
  computed: {
    realTimedata() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    },
    alarmreadSum() {
      return this.$store.state.alarmreadSum
    },
    alarmunreadSum() {
      return this.$store.state.alarmunreadSum
    },
    nodeSum() {
      return this.$store.state.nodeSum
    }
  },
  watch: {
    // 监听宽度
    screenWidth(newval, oldval) {
      // console.log(newold)
      this.widthclassinfo2()
    },
    realTimedata: {
      handler(newVal, oldVal) {
        console.log('-----------')
        this.$set(this.topData, 0, this.nodeSum)
        this.$set(this.topData, 1, Object.keys(newVal).length)
        this.$set(this.topData, 3, this.alarmreadSum + this.alarmunreadSum)
        this.$set(this.topData, 4, this.alarmreadSum)
        this.$set(this.topData, 5, this.alarmunreadSum)
        // 更新离线节点数
        if (this.topData[0] !== 0 && this.topData[1] !== 0) { this.$set(this.topData, 2, this.topData[0] - this.topData[1]) }
        this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)))
        this.initScatter3D(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
        // this.musicChart.setOption(this.musicChartOption)
      },
      deep: true
    }
  },
  created() {
    this.$cookies.set('tran', 'nodeInfo')
    // this.$store.commit('realData')
    this.getNowDate()
    // 锁定屏幕不可滚动
    // document.addEventListener('keydown', this.down, false)
    // window.addEventListener('mousewheel', this.wheel, { passive: false })
    // window.addEventListener('DOMMouseScroll', this.mouseSroll, { passive: false })
    // this.initAlarm()
  },
  mounted() {
    // this.scroll()
    this.initMyChart()
    // this.initMusicChart()
    this.initMonthData()
    this.screenWidth = document.body.clientWidth
    this.screenHeight = document.body.clientHeight
    window.onresize = () => {
      return (() => {
        this.screenWidth = document.body.clientWidth
        this.screenHeight = document.body.clientHeight
      })()
    }
    // this.initMyChart()

    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
      // if (this.musicChart) {
      //   this.musicChart.resize()
      // }
      // if (this.musicChart1) {
      //   this.musicChart1.resize()
      // }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
    var that = this
    this.timer = setInterval(function() {
      that.getNowDate()
    }, 1000)
    // console.log(this.$store.state.app.sidebar)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    // this.initMyChart()
    if (!this.chart) {
      return
    }
    // if (!this.musicChart) {
    //   return
    // }
    // if (!this.musicChart1) {
    //   return
    // }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
    // this.musicChart1.dispose()
    // this.musicChart1 = null
    window.removeEventListener('keydown', this.down, false)
    window.removeEventListener('mousewheel', this.wheel, false)
    window.removeEventListener('DOMMouseScroll', this.mouseSroll, false)
  },
  methods: {
    bindNode(data) {
      console.log(data)
    },
    widthclassinfo2() {
      if (this.screenWidth > 1710) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    handleRealTimeData(data) {
      // console.log(this.$refs)
      var that = this
      var k = 0
      Object.keys(data).forEach((key) => {
        if (key !== 'node_sum' && key !== undefined) {
          that.$set(that.items, k, { index: k })
          that.$set(that.myAllTitle, k, key)
          that.$set(that.myAllNodeStyle, k, 'background-color:#dcdfe6;color:#000')
          k++
        } else {
          console.log(key)
        }
      })
    },
    // 锁定屏幕不可滚动
    down(event) {
      if ((event.ctrlKey === true || event.metaKey === true) &&
      (event.which === 61 || event.which === 107 ||
        event.which === 173 || event.which === 109 ||
        event.which === 187 || event.which === 189)) {
        event.preventDefault()
      }
    },
    wheel(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    mouseSroll(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    // 月数据图表
    initMyChart() {
      // this.initMonthData()
      this.chart = echarts.init(this.$refs.monthChart, 'macarons')
      this.chart.setOption(this.myChartOption)
    },
    // 相空间数据处理
    initScatter3D(realData, oldRealData) {
      var newData = []
      var oldData = []
      var totalDisk = 0
      var usedDisk = 0
      var totalMemory = 0
      var usedMemory = 0
      var totalCpu = 0
      var usedCpu = 0
      var totalGpu = 0
      var usedGpu = 0
      var that = this
      var nn = 0
      Object.keys(realData).forEach((key) => {
        totalDisk = totalDisk + realData[key]['disk_total']
        usedDisk = usedDisk + realData[key]['disk_used']
        totalMemory = totalMemory + realData[key]['memory_total']
        usedMemory = usedMemory + realData[key]['memory_used']
        totalCpu = totalCpu + 100
        usedCpu = usedCpu + realData[key]['cpu_busy']
        totalGpu = totalGpu + 100
        usedGpu = usedGpu + realData[key]['gpu_utilization']
        that.$set(that.nodeData, nn, [realData[key]['cpu_busy'], realData[key]['memory_rate'], realData[key]['disk_rate'], realData[key]['gpu_utilization']])
        newData.push([parseFloat((100 - realData[key]['cpu_idle']).toFixed(2)), realData[key]['memory_rate'], realData[key]['disk_rate']])
        that.$set(that.scatterData, nn, [realData[key]['cpu_busy'], realData[key]['memory_rate'], realData[key]['disk_rate']])
        nn++
        if (realData[key]['alarm_state'] === 1) {
          if (Object.keys(realData[key]['alarm']).length !== 0) {
            Object.keys(realData[key]['alarm']).forEach((num) => {
              if (that.listData.length === 0) {
                that.$set(that.listData, that.alarmindex, { 'nodeId': key, 'alarmItem': num, 'alarmNum': realData[key]['alarm'][num]['outlier'], 'date': that.nowDate })
              } else if (that.listData.length < 20) {
                var a = that.listData.findIndex((item) => item.nodeId === key && item.alarmItem === that.SigleAlarmItem[num])
                // console.log(that.listData,a)
                if (a === -1) {
                  that.$set(that.listData, that.alarmindex, { 'nodeId': key, 'alarmItem': num, 'alarmNum': realData[key]['alarm'][num]['outlier'], 'date': that.nowDate })
                  that.alarmindex++
                } else {
                  that.$set(that.listData, a, { 'nodeId': key, 'alarmItem': num, 'alarmNum': realData[key]['alarm'][num]['outlier'], 'date': that.nowDate })
                }
              }
              if (that.alarmNodeNum.indexOf(key) === -1) {
                that.alarmNodeNum.push(key)
              }
              this.$nextTick(() => {
                const container = this.$el.querySelector('.el-table__body-wrapper')
                container.scrollTop = container.scrollHeight
              })
            })
          }
        }
      })
      Object.keys(oldRealData).forEach((key) => {
        oldData.push([parseFloat((100 - oldRealData[key]['cpu_idle']).toFixed(2)), oldRealData[key]['memory_rate'], oldRealData[key]['disk_rate']])
      })
      // 计算温度与熵
      this.$set(this.entropyData, 2, this.computedEntropy(newData).toFixed(2))
      this.$set(this.temData, 2, this.computedTem(oldData, newData).toFixed(2))
      // this.entropyData = this.computedEntropy(newData)
      // this.temData = this.computedTem(oldData, newData)
      // console.log(this.entropyData, this.temData)
      // 四个指标集群级别数据
      this.$set(this.diskUsed, 0, parseFloat(totalDisk.toFixed(2)))
      this.$set(this.diskUsed, 1, parseFloat(usedDisk.toFixed(2)))
      this.$set(this.diskUsed, 2, parseFloat((usedDisk / totalDisk * 100).toFixed(2)))
      this.$set(this.diskUsed, 3, parseFloat((totalDisk - usedDisk).toFixed(2)))
      this.$set(this.memoryUsed, 0, parseFloat(totalMemory.toFixed(2)))
      this.$set(this.memoryUsed, 1, parseFloat(usedMemory.toFixed(2)))
      this.$set(this.memoryUsed, 2, parseFloat((usedMemory / totalMemory * 100).toFixed(2)))
      this.$set(this.memoryUsed, 3, parseFloat((totalMemory - usedMemory).toFixed(2)))
      this.$set(this.cpuUsed, 0, parseFloat(totalCpu.toFixed(2)))
      this.$set(this.cpuUsed, 1, parseFloat(usedCpu.toFixed(2)))
      this.$set(this.cpuUsed, 2, parseFloat((usedCpu / totalGpu * 100).toFixed(2)))
      this.$set(this.gpuUsed, 0, parseFloat(totalGpu.toFixed(2)))
      this.$set(this.gpuUsed, 1, parseFloat(usedGpu.toFixed(2)))
      this.$set(this.gpuUsed, 2, parseFloat((usedGpu / totalGpu * 100).toFixed(2)))
    },
    // 温度计算
    computedTem(oldData, newData) {
      var nodeNum = newData.length
      var sum = 0
      var m = nodeNum
      if (oldData.length === 0) {
        return 0
      } else {
        // 外层为节点循环
        for (var i = 0; i < nodeNum; i++) {
          // 内层为三个指标循环
          var sum1 = 0
          for (var j = 0; j < 3; j++) {
            sum1 = sum1 + (newData[i][j] - oldData[i][j]) ** 2
          }
          // 累积所有节点的值
          sum1 = Math.sqrt(sum1)
          sum = sum + sum1
        }
        return sum / m
      }
    },
    // 熵计算
    computedEntropy(newData) {
      // 每次计算重新赋值
      this.entropyArray = []
      var count = 0
      if (newData !== undefined) {
        for (var i = 0; i < newData.length; i++) {
          var x = parseInt((newData[i][0] / 20).toFixed(0))
          var y = parseInt((newData[i][1] / 20).toFixed(0))
          var z = parseInt((newData[i][2] / 20).toFixed(0))
          // 先判断再赋值
          if (this.entropyArray[x * 20 + y * z] !== 1) {
            count++
          }
          this.$set(this.entropyArray, x * 20 + y * z, 1)
        }
        return count / 125
      } else {
        return 0
      }
    },
    onClick(i) {
      this.$set(this.flipped, i, !this.flipped[i])
    },
    goTo(k, b) {
      this.$router.push({ path: '/Center/dataCenter' })
      switch (k) {
        case 'nodeInfo':
          this.$cookies.set('tran', 'nodeInfo')
          this.activeName = 'nodeInfo'
          break
        case 'nodeDetails':
          this.$router.push({ path: '/Center/pmNodeChart', query: { nodeid: b }})
          // this.$cookies.set('tran', 'cpuInfo')
          // this.activeName = 'cpuInfo'
          break
        case 'AlarmList':
          this.$router.push({ path: '/Alarm/pmAlarmHistory' })

          // this.$cookies.set('tran', 'gpuInfo')
          // this.activeName = 'gpuInfo'
          break
        case 4:
          this.$cookies.set('tran', 'diskInfo')
          this.activeName = 'diskInfo'
          break
        case 5:
          this.$cookies.set('tran', 'networkInfo')
          this.activeName = 'networkInfo'
          break
        case 6:
          this.$cookies.set('tran', 'memoryInfo')
          this.activeName = 'memoryInfo'
          break
      }
      // switch (k) {
      //   case 7:
      //     this.$router.push({ path: '/apps/clusterManager' })
      //     break
      //   case 8:
      //     this.$router.push({ path: '/apps/testTool' })
      //     break
      //   case 9:
      //     this.$router.push({ path: '/Alarm/pmAlarmScheme' })
      //     break
      //   case 10:
      //     this.$router.push({ path: '/Alarm/alarmHistory' })
      //     break
      //   case 11:
      //     this.$router.push({ path: '/apps/scment' })
      //     break

      // }
    },
    initMonthData() {
      var that = this
      pmNodeState.everydayData().then(function(response) {
        var data = response
        Object.keys(data).forEach((key) => {
          // var date = key.slice(4, 6) + '-' + key.slice(6, 8)
          that.month = key.slice(4, 6)
          var day = parseInt(key.slice(6, 8))
          that.$set(that.monthData[1], day, parseFloat(data[key]['cpu_idle']))
          that.$set(that.monthData[2], day, parseFloat(data[key]['gpu_utilization']))
          that.$set(that.monthData[3], day, parseFloat(data[key]['disk_rate']))
          that.$set(that.monthData[4], day, parseFloat(data[key]['memory_rate']))
        })
        var k = 0
        while (k < 31) {
          that.$set(that.monthData[0], k, that.month + '-' + (k + 1))
          if (that.monthData[1][k] === undefined) {
            that.$set(that.monthData[1], k, 0)
            that.$set(that.monthData[2], k, 0)
            that.$set(that.monthData[3], k, 0)
            that.$set(that.monthData[4], k, 0)
          }
          k++
        }
        // console.log(that.monthData[0])
        that.myChartOption.xAxis[0].data = that.monthData[0]
        that.myChartOption.series[0].data = that.monthData[3]
        that.myChartOption.series[1].data = that.monthData[4]
        that.myChartOption.series[2].data = that.monthData[1]
        that.myChartOption.series[3].data = that.monthData[2]
        that.chart.setOption(that.myChartOption)
      })
    },
    getNowDate() {
      var date = new Date()
      var year = date.getFullYear() // 年 ,从 Date 对象以四位数字返回年份
      var month = date.getMonth() + 1 // 月 ,从 Date 对象返回月份 (0 ~ 11) ,date.getMonth()比实际月份少 1 个月
      var day = date.getDate() // 日 ,从 Date 对象返回一个月中的某一天 (1 ~ 31)
      var hours = date.getHours() // 小时 ,返回 Date 对象的小时 (0 ~ 23)
      var minutes = date.getMinutes() // 分钟 ,返回 Date 对象的分钟 (0 ~ 59)
      var seconds = date.getSeconds() // 秒 ,返回 Date 对象的秒数 (0 ~ 59)
      if (month < 10) {
        month = '0' + month
      }
      if (day < 10) {
        day = '0' + day
      }
      if (hours < 10) {
        hours = '0' + hours
      }
      if (minutes < 10) {
        minutes = '0' + minutes
      }
      if (seconds < 10) {
        seconds = '0' + seconds
      }
      // 获取当前系统时间
      this.currentDate = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
      // if(this)
      this.nowDate = hours + ':' + minutes + ':' + seconds
      // console.log(this.nowDate)
      // this.newDateWithout
    },
    handleClick(tab, event) {
      console.log(tab, event)
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.my-el-container{
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  span{
        white-space: nowrap;
      }
  h2{
    font-family: 'tech-font-1';
    margin-block-start: 10px;

    // font-size: 1.5em;
    // margin-block-start: 0.1em;
    // margin-block-end: 0em;
  }
  .my-row{
    .month-data-card{
      height: 200px;
      // margin-top: 4%;
      // margin-bottom: 4%;
    }
  }
  .el-card{
      margin-bottom: 4px;
      border-radius: 8px;
      min-height: 87.84px;
      max-height: 87.84px;
      height: 87.84px;
  }
}
.all-title-style{
  font-size:16px !important;
  width:80px
}
  .my-el-container .el-col {
    text-align: center;
  }
  .my-main-container{
    width: 100%;
    position: relative;
  }
  .my-left-aside{
    .my-node-pannel{
      border-bottom: solid #d8d9da 1px;
      // border: solid 1px;
      // max-height: 395px;
      // min-height: 395px;
      height: 540px;
      // width: 200px;
      display: flex;
      flex-wrap: wrap;
      // align-content:center;
      justify-content: center;
      position: relative;
      overflow-y: auto;
       &::-webkit-scrollbar {
    display: none;
  }
      // background-color: #dcdfe6;
      // .Cabinets{
      //   height: 360px;width: 30%;
      //   border-top: solid #666363 4px;
      //   // border-bottom: solid #3a3737 10px;
      //   .blocks{
      //     height: 80px;width: 100%;
      //     border-left: solid #c3c3c3 5px;
      //     border-right: solid #c3c3c3 5px;
      //     border-bottom: solid #3a3737 1px;
      //     .racks{
      //       // background-image: linear-gradient(to bottom right,#484743,#bbb7ae);
      //       // border: solid #494444 1px;
      //       border-bottom: solid 1px;
      //       height: 100%;
      //     }
      //     .column0{
      //       height: 50%
      //     }
      //   }
      //   .flex-blocks{
      //     display: flex;
      //     height: 80px;width: 100%;
      //     border-left: solid #c3c3c3 5px;
      //     border-right: solid #c3c3c3 5px;
      //     border-bottom: solid #3a3737 1px;
      //     .racks{
      //       // transform: rotate(90deg);
      //       border:solid 1px;
      //       height: 100%;
      //       width: 50%;
      //     }
      //   }
      //   .bars{
      //       background-image: radial-gradient(circle,#eeeeee,#c3c3c3);
      //       border-top: solid #000000 3px;
      //       border-left: solid #666363 8px;
      //       border-right: solid #666363 8px;
      //       height: 10px;
      //     }

      // }
      .nodeDiv{
          position: relative;
          display: block;
          flex-wrap:  wrap;  /*flex 可以自动换行，这个属性很重要*/
          //float: center;
          font-size: 7px;
          width: 200px;
          cursor:pointer
      }
      .few{
        .single-div-few{
          font-size: 18px;
          font-weight: bolder;
        font-family: 'tech-font-1';
        }
          .el-card{
              ::v-deep .el-card__body{
              // max-height: 86px;
              // display: flex;
              // justify-content: center;
              padding: 0;
              padding-top: 10px;
              min-width: 200px;
              font-size: 16px;
            }
            // padding: 10px;
            margin: 1px;
            min-height: 160px;
            max-height: 160px;
            height: 160px;
          }
        }
        .many{
          .el-card{
            border-radius: 50%;
            max-height: 30px;
            min-height:30px;
            height: 30px;
            max-width: 30px;
            min-width:30px;
            width: 30px;
            ::v-deep .el-card__body{
              padding: 0px;
              padding-top: 10px;

            }
          }
            min-height:10%;
            height: 10%;
            width: 10%;
            min-width: 4%;
            max-width:100%;
            margin-left: 2%;
            margin-top: 2%;
            flex-shrink: 0;
            padding: 2px;
            // background-color: #dcdfe6;
        }
    }
    .my-node-pannel:hover{
      overflow-y: auto;
    }
    .left-row-1{
      ::v-deep .el-card__body{
        font-size: 16px;
        padding: 0;
        padding-top: 15px;

      }
      .el-col:nth-of-type(1){
          .el-card{
            cursor: pointer;
          // color: #667db6;
          }
      }
      .el-col:nth-of-type(2){
          .el-card{
            cursor: pointer;
          // color: #1D976C;
          }
      }
      .el-col:nth-of-type(3){
          .el-card{
            cursor: pointer;
            // color: #6A7C88;
          }
      }
    }
    .left-row-2{
      ::v-deep .el-card__body{
        font-size: 16px;
        padding: 0;
        padding-top: 15px;
      }
       .el-col:nth-of-type(1){
          .el-card{
            cursor: pointer;
            // color: #D2691E;  /* fallback for old browsers */
          }
        }
        .el-col:nth-of-type(2){
          .el-card{
            // color: #667db6;  /* fallback for old browsers */
          }
        }
        .el-col:nth-of-type(3){
          .el-card{
            span{
              font-size: 0.5em;
            }
            // color: #667db6;  /* fallback for old browsers */
          }
        }
    }
  }
  .all-info-container{
    .header-main{
      font-size: 16px;
        .icon-style{
          margin-top: 10px;
          width: 100px;
          height: 87px;
        }
      .number-style{
        font-size: 24px;
        font-family: 'tech-font-1';
        font-weight: bolder;
        line-height: 60px;
      }
      .el-card:nth-of-type(1){
        // max-height: 87.84px;

        div:nth-of-type(1){
          height: 87px;
          overflow: hidden;
          display: flex;
          flex-direction: column;
          justify-content: center;
          margin: 5px 10px 0px 10px;
        }
        div:nth-of-type(2){
          height: 87px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          margin: 10px 10px 0px 10px;
        }
      }
      .el-card{
        ::v-deep .el-card__body{
          // max-height: 86px;
          display: flex;
          justify-content: center;
          padding: 0;
          // padding-top: 10px;
          h2{
            margin-block-start: 10px;
          }
        }
      }
      .main-pannel{
        // margin-top: 40px;
        height: 540px;
        width: 100%;
        position: relative;
        .scatter{
          // border-block-end: solid #dcdfe6 1px;
          // max-height: 395px;
          // min-height: 395px;
          height: 460px;
          width: 346px;
        }
      }
    }
  }
  .my-right-aside{
    position: relative;
    height: 100%;
    .my-right-table{
      height: 530px;
       &::-webkit-scrollbar {
    display: none;
  }
    }
    .my-right-table:hover{
overflow-y: auto;
    }
    .el-card{
      min-height: 180px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      ::v-deep .el-card__body{
        font-size: 16px;
        padding: 0;
        padding-top: 15px;
     }
    }
    .el-col:nth-of-type(1){
          .el-card{
            cursor: pointer;
          // color: #667db6;
          }
    }
    .el-col:nth-of-type(2){
          .el-card{
            cursor: pointer;
          // color: #1D976C;
          }
    }
    .el-col:nth-of-type(3){
      .el-card{
        cursor: pointer;
        // color: #D2691E;
      }
    }

  }
  .h-top{
    margin-bottom: 14px;
  }
  .bodywidthmin{
  // width:1860px;
  height: 100%;
  /* background: blueviolet; */
  }
  .bodywidthmax{
  width:100%;
  height: 100%;
  }
</style>
