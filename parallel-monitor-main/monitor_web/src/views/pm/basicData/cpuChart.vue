<template>
  <div>
    <el-row style="margin-top:1%">
      <el-col :span="24">
        <el-card style="margin:auto;width:95%;padding:1% 4% 2% 4%;height:fit-content">
          <!-- 静态数据 -->
          <el-row>
            <el-col :span="11">
              <el-descriptions class="margin-top" :title="myTitle" :column="2" border>
                <el-descriptions-item v-for="item in items1" :key="item.text">
                  <template slot="label">
                    {{ item.text }}
                  </template>
                  {{ item.value }}
                </el-descriptions-item></el-descriptions>
            </el-col>
            <el-col :span="4" :offset="1">
              <Radar1 :data="userData" :title="title1" class="Radar" />
            </el-col>
            <el-col :span="4">
              <Radar2 :data="systemData" :title="title2" class="Radar" />
            </el-col>
            <el-col :span="4">
              <Radar3 :data="usedData" :title="title3" class="Radar" />
            </el-col>
          </el-row>
          <!-- 动态数据 -->
          <el-row :gutter="20" style="margin-top:5%">
            <div id="温度" style="margin-top:1%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button size="mini" type="primary" class="hisButtom" @click="hisTem">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[0]"
                      v-model="value1"
                      class="time-select"
                      type="datetimerange"
                      :picker-options="pickerOptions1"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      @change="timeChange1"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[0]" size="mini" type="primary" class="realButtom" @click="realTem">实时数据</el-button></el-col>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <RadarChart1 v-if="show1" v-loading="loading[0]" element-loading-text="数据加载中" class="chart" :legend="legend1" :data-x="chartX1" :data-y="chartY1" />
                </el-col>
              </el-row>
            </div>
            <div id="频率" style="margin-top:5%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button size="mini" type="primary" class="hisButtom" @click="hisFre">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[1]"
                      v-model="value2"
                      class="time-select"
                      type="datetimerange"
                      :picker-options="pickerOptions2"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      @change="timeChange2"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[1]" size="mini" type="primary" class="realButtom" @click="realFre">实时数据</el-button>
                  </el-col>
                </el-col></el-row>
              <el-row>
                <el-col :span="24">
                  <RadarChart2 v-if="show2" v-loading="loading[1]" element-loading-text="数据加载中" class="chart" :legend="legend2" :data-x="chartX2" :data-y="chartY2" /></el-col>
              </el-row>
            </div>
            <div id="系统使用率" style="margin-top:5%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button size="mini" type="primary" class="hisButtom" @click="hisSys">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[2]"
                      v-model="value3"
                      class="time-select"
                      type="datetimerange"
                      :picker-options="pickerOptions3"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      @change="timeChange3"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[2]" size="mini" type="primary" class="realButtom" @click="realSys">实时数据</el-button>
                  </el-col>
                </el-col></el-row>
              <el-row>
                <el-col :span="24"> <RadarChart3 v-if="show3" v-loading="loading[2]" element-loading-text="数据加载中" class="chart" :legend="legend3" :data-x="chartX3" :data-y="chartY3" /></el-col>
              </el-row>
            </div>
            <div id="用户使用率" style="margin-top:5%;margin-bottom:2%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button size="mini" type="primary" class="hisButtom" @click="hisUsr">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[3]"
                      v-model="value4"
                      class="time-select"
                      type="datetimerange"
                      :picker-options="pickerOptions4"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      @change="timeChange4"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[3]" size="mini" type="primary" class="realButtom" @click="realUsr">实时数据</el-button>
                  </el-col>
                </el-col></el-row>
              <el-row>
                <el-col :span="24">
                  <RadarChart4 v-if="show4" v-loading="loading[3]" element-loading-text="数据加载中" class="chart" :legend="legend4" :data-x="chartX4" :data-y="chartY4" /></el-col>
              </el-row>
            </div>
            <el-backtop :bottom="500" />
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import pmNodeCpu from '@/api/pm/pmNodeCpu'
import pmNodeCpuState from '@/api/pm/pmNodeCpuState'
import Radar1 from '@/components/Echarts/Radar'
import Radar2 from '@/components/Echarts/Radar'
import Radar3 from '@/components/Echarts/Radar'
import RadarChart1 from '@/components/Echarts/RadarChart'
import RadarChart2 from '@/components/Echarts/RadarChart'
import RadarChart3 from '@/components/Echarts/RadarChart'
import RadarChart4 from '@/components/Echarts/RadarChart'
import { mapGetters } from 'vuex'

export default {
  name: 'CpuChart',
  components: {
    RadarChart1, RadarChart2, RadarChart3, RadarChart4, Radar1, Radar2, Radar3
  },
  data() {
    var _this = this
    return {
      showCpu: true,
      // 总面板数据
      items1: [{ id: 1, text: '名称', value: null },
        { id: 2, text: '架构', value: null },
        { id: 3, text: '核心数', value: null },
        { id: 4, text: '线程数', value: null },
        { id: 5, text: '所属主机 ID', value: null },
        { id: 1, text: '一级指令缓存', value: null },
        { id: 2, text: '一级数据缓存', value: null },
        { id: 3, text: '二级缓存', value: null },
        { id: 4, text: '三级缓存', value: null },
        { id: 5, text: '基频', value: null }
      ],
      // 时间选择器
      pickerOptions1: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }],
        disabledDate(time) {
          const curDate = (new Date()).getTime()
          const three = new Date() - new Date(_this.user.createTime)
          const threeMonths = curDate - three
          return time.getTime() > Date.now() || time.getTime() < threeMonths
        }
      },
      pickerOptions2: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }],
        disabledDate(time) {
          const curDate = (new Date()).getTime()
          const three = new Date() - new Date(_this.user.createTime)
          const threeMonths = curDate - three
          return time.getTime() > Date.now() || time.getTime() < threeMonths
        }
      },
      pickerOptions3: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }],
        disabledDate(time) {
          const curDate = (new Date()).getTime()
          const three = new Date() - new Date(_this.user.createTime)
          const threeMonths = curDate - three
          return time.getTime() > Date.now() || time.getTime() < threeMonths
        }
      },
      pickerOptions4: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }],
        disabledDate(time) {
          const curDate = (new Date()).getTime()
          const three = new Date() - new Date(_this.user.createTime)
          const threeMonths = curDate - three
          return time.getTime() > Date.now() || time.getTime() < threeMonths
        }
      },
      value1: '',
      value2: '',
      value3: '',
      value4: '',
      datas: [],
      cpudata: {},
      cpuStatic: {},
      cpu_uuid: this.$route.query.cpuid,
      node_id: this.$route.query.nodeId,
      // 定时器
      timer1: '',
      timer2: '',
      timer3: '',
      timer4: '',
      show1: false,
      show2: false,
      show3: false,
      show4: false,
      // 图表数据
      legend1: '',
      chartX1: [],
      chartY1: [],
      legend2: '',
      chartX2: [],
      chartY2: [],
      legend3: '',
      chartX3: [],
      chartY3: [],
      legend4: '',
      chartX4: [],
      chartY4: [],
      // 实时数据按钮显示
      realButtom: [false, false, false, false],
      // 实时状态标志
      real: [true, true, true, true],
      time: [false, false, false, false],
      loading: [false, true, true, true],
      myTitle: '',
      userData: [],
      systemData: [],
      usedData: [],
      title1: '核心用户使用率',
      title2: '核心系统使用率',
      title3: '核心总使用率'
    }
  },
  computed: {
    // 获取当前user
    ...mapGetters([
      'user'
    ]),
    realTimeData() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
    real: {
      handler(val) {
        // 实时数据与历史数据交替时需要清除定时器
        if (val[0] === false) { clearInterval(this.timer1) }
        if (val[1] === false) { clearInterval(this.timer2) }
        if (val[2] === false) { clearInterval(this.timer3) }
        if (val[3] === false) { clearInterval(this.timer4) }
      },
      deep: true
    },
    realTimeData: {
      handler(newVal) {
        if (newVal !== {}) {
          this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)))
          if (this.real[0] === true) {
            this.chart1Quert(newVal[this.node_id])
          }
        }
      }
    }
  },
  created() {
    this.$store.state.isDataCenter = false
    this.myTitle = this.cpu_uuid + '号CPU'
    // 请求cpu静态数据
    var that = this
    pmNodeCpu.query(this.cpu_uuid).then(function(response) {
      that.cpuStatic = response.content
      that.setStaticData(that.cpuStatic)
    })
  },
  mounted() {
    // 数据初始化
    var that = this
    that.show1 = true
    that.show2 = true
    that.show3 = true
    that.show4 = true
    if (this.real[0] === true) { this.realTem() }
    if (this.real[1] === true) { this.realFre() }
    if (this.real[2] === true) { this.realSys() }
    if (this.real[1] === true) { this.realUsr() }
    if (window.history && window.history.pushState) {
      // 往历史记录里面添加一条新的当前页面的url
      history.pushState(null, null, document.URL)
      // 给 popstate 绑定一个方法 监听页面刷新
      window.addEventListener('popstate', this.back, false)
    }
  },
  destroyed() {
    window.removeEventListener('popstate', this.back, false)
  },
  beforeDestroy() {
    clearInterval(this.timer1)
    clearInterval(this.timer2)
    clearInterval(this.timer3)
    clearInterval(this.timer4)
    this.timer1 = null
    this.timer2 = null
    this.timer3 = null
    this.timer4 = null
  },
  methods: {
    // 事间戳转换
    timeTranslate(time) {
      var date1 = new Date(time[0])
      var date2 = new Date(time[1])
      var time1 = (date1.getTime() / 1000).toFixed(0) // 转换为秒级
      var time2 = (date2.getTime() / 1000).toFixed(0)
      // console.log(time)
      // console.log(time2)
      return [time1, time2]
    },
    // 静态数据处理函数
    setStaticData(data) {
      this.items1[0].value = data[0]['cpuName']
      this.items1[1].value = data[0]['arch']
      this.items1[2].value = data[0]['cores']
      this.items1[3].value = data[0]['threads']
      this.items1[4].value = data[0]['nodeId']
      this.node_id = data[0]['nodeId']
      this.items1[5].value = data[0]['l1ICache']
      this.items1[6].value = data[0]['l1DCache']
      this.items1[7].value = data[0]['l2Cache']
      this.items1[8].value = data[0]['l3Cache']
      this.items1[9].value = data[0]['mainFrequency']
    },
    // 时间选择器触发函数
    timeChange1() {
      this.$set(this.real, 0, false)
      if (this.value1 !== null) {
        // this.resetArray(this.chartX1, this.chartY1)
        this.show1 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[1])
        var that = this
        pmNodeCpuState.queryTime(this.cpu_uuid, range).then(function(response) {
          that.cpudata = response.content
          if (that.cpudata.length !== 0) {
            const gaptime = that.gaptime(range[0], that.cpudata[that.cpudata.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart1(that.cpudata)
          } else {
            // that.resetArray(that.chartX1, that.chartY1)
            that.sendMessage()
          }
          that.show1 = true
        })
      } else { // 清空则恢复默认时间
        this.show1 = false
        var tthat = this
        pmNodeCpuState.query(tthat.cpu_uuid).then(function(response) {
          tthat.cpudata = response.content
          tthat.initchart1(tthat.cpudata)
          tthat.show1 = true
        })
      }
    },
    timeChange2() {
      if (this.value2 !== null) {
        this.show2 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value2[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value2[1])
        var that = this
        pmNodeCpuState.queryTime(this.cpu_uuid, range).then(function(response) {
          that.cpudata = response.content
          if (that.cpudata.length !== 0) {
            const gaptime = that.gaptime(range[0], that.cpudata[that.cpudata.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            // console.log(that.cpudata)
            that.initchart2(that.cpudata)
          } else {
            that.resetArray(that.chartX2, that.chartY2)
            that.sendMessage()
          }
          that.show2 = true
        })
      } else { // 清空则恢复默认时间
        this.show2 = false
        var tthat = this
        pmNodeCpuState.query(tthat.cpu_uuid).then(function(response) {
          tthat.cpudata = response.content
          tthat.initchart2(tthat.cpudata)
          tthat.show2 = true
        })
      }
    },
    timeChange3() {
      if (this.value3 !== null) {
        this.show3 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value3[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value3[1])
        var that = this
        pmNodeCpuState.queryTime(this.cpu_uuid, range).then(function(response) {
          that.cpudata = response.content
          if (that.cpudata.length !== 0) {
            const gaptime = that.gaptime(range[0], that.cpudata[that.cpudata.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart3(that.cpudata)
          } else {
            that.resetArray(that.chartX3, that.chartY3)
            that.sendMessage()
          }
          that.show3 = true
        })
      } else { // 清空则恢复默认时间
        this.show3 = false
        var tthat = this
        pmNodeCpuState.query(tthat.cpu_uuid).then(function(response) {
          tthat.cpudata = response.content
          tthat.initchart3(tthat.cpudata)
          tthat.show3 = true
        })
      }
    },
    timeChange4() {
      if (this.value4 !== null) {
        this.show4 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value4[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value4[1])
        var that = this
        pmNodeCpuState.queryTime(this.cpu_uuid, range).then(function(response) {
          that.cpudata = response.content
          if (that.cpudata.length !== 0) {
            const gaptime = that.gaptime(range[0], that.cpudata[that.cpudata.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart4(that.cpudata)
          } else {
            that.resetArray(that.chartX4, that.chartY4)
            that.sendMessage()
          }
          that.show4 = true
        })
      } else { // 清空则恢复默认时间
        this.show4 = false
        var tthat = this
        pmNodeCpuState.query(tthat.cpu_uuid).then(function(response) {
          tthat.cpudata = response.content
          tthat.initchart4(tthat.cpudata)
          tthat.show4 = true
        })
      }
    },
    // 时间转换 ==> 2021-07-05 13:43:06
    dateFormat(fmt, date) {
      let ret
      const opt = {
        'Y+': date.getFullYear().toString(), // 年
        'm+': (date.getMonth() + 1).toString(), // 月
        'd+': date.getDate().toString(), // 日
        'H+': date.getHours().toString(), // 时
        'M+': date.getMinutes().toString(), // 分
        'S+': date.getSeconds().toString() // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      }
      for (const k in opt) {
        ret = new RegExp('(' + k + ')').exec(fmt)
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length === 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
        }
      }
      return fmt
    },
    // 图表数据处理函数
    initchart1(data) {
      this.legend1 = '温度(摄氏度)'
      for (var i = 0; i < data.length; i++) {
        this.chartX1[i] = data[i]['createTime']
        this.chartY1[i] = data[i]['temperature']
      }
      this.chartX1.reverse()
      this.chartY1.reverse()
    },
    initchart2(data) {
      this.legend2 = '频率(MHz)'
      var tran = []
      for (var i = 0; i < data.length; i++) {
        this.chartX2[i] = data[i]['createTime']
        tran.push(data[i]['coreState'])
      }
      for (var j = 0; j < tran.length; j++) {
        // 核心
        this.chartY2[j] = ((tran[j][0]['frequency']) / 1000000)
      }
      this.chartX2.reverse()
      this.chartY2.reverse()
    },
    initchart3(data) {
      this.legend3 = '系统使用率(%)'
      for (var i = 0; i < data.length; i++) {
        this.chartX3[i] = data[i]['createTime']
        this.chartY3[i] = data[i]['cpuSystemAvg'].toFixed(2)
      }
      this.chartX3.reverse()
      this.chartY3.reverse()
    },
    initchart4(data) {
      this.legend4 = '用户使用率(%)'
      for (var i = 0; i < data.length; i++) {
        this.chartX4[i] = data[i]['createTime']
        this.chartY4[i] = data[i]['cpuUserAvg'].toFixed(2)
      }
      this.chartX4.reverse()
      this.chartY4.reverse()
    },
    // 数组清空函数 ---> 清空数据并且重置数组长度为0
    resetArray(dataX, dataY) {
      for (var i = 0; i < dataX.length; i++) {
        this.$set(dataX, i, 0)
      }
      for (var j = 0; j < dataY.length; j++) {
        this.$set(dataY, i, 0)
      }
    },
    sendMessage() {
      this.$notify.error({
        title: '提示',
        message: '当前时间无数据'
      })
    },
    sendMessage2() {
      this.$notify.error({
        title: '提示',
        message: '数据量过大，只显示该段最新40条'
      })
    },
    // 返回上一级
    back() {
      var views = this.$store.state.tagsView.visitedViews
      for (var i = 0; i < views.length; i++) {
        if (views[i]['name'] === 'cpuChart') {
          this.$store.dispatch('tagsView/delView', views[i]).then(() => {
            this.$router.push({ path: '/Center/cpuInfo' })
          })
        }
      }
    },
    gaptime(beginTime, endTime) {
      var dateBegin = new Date(beginTime)
      var dateEnd = new Date(endTime)
      var dateDiff = dateEnd.getTime() - dateBegin.getTime()// 时间差的毫秒数
      // var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000))// 计算出相差天数
      var leave1 = dateDiff % (24 * 3600 * 1000)// 计算天数后剩余的毫秒数
      // var hours = Math.floor(leave1 / (3600 * 1000))// 计算出小时数
      // 计算相差分钟数
      var leave2 = leave1 % (3600 * 1000) // 计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000)) // 计算相差分钟数
      // 计算相差秒数
      // var leave3 = leave2 % (60 * 1000)// 计算分钟数后剩余的毫秒数
      // var seconds = Math.round(leave3 / 1000)
      return minutes
    },
    // 实时数据处理函数
    realTem() {
      for (var k = 0; k < 100; k++) {
        this.$set(this.chartX1, k, 0)
        this.$set(this.chartY1, k, 0)
      }
      this.$set(this.realButtom, 0, false)
      this.$set(this.time, 0, false)
      this.$set(this.real, 0, true)
      this.legend1 = '温度(摄氏度)'
    },
    chart1Quert(data) {
      console.log(this.chartX1)
      this.chartX1.splice(0, 1)
      this.chartY1.splice(0, 1)
      this.chartX1.push(data['create_time'])
      this.chartY1.push(data['cpu_temperature'])
    },
    realFre() {
      this.$set(this.realButtom, 1, false)
      // console.log('请求实时数据')
      this.$set(this.time, 1, false)
      this.$set(this.real, 1, true)
      this.$set(this.loading, 1, true)
      this.legend2 = '频率(GHz)'
      var that = this
      this.chart2Quert()
      this.timer2 = setInterval(function() { that.chart2Quert() }, 6000)
    },
    chart2Quert() {
      var that = this
      pmNodeCpuState.query(that.cpu_uuid).then(function(response) {
        var data = response.content
        if (that.loading[1] === true) { that.$set(that.loading, 1, false) }
        var tran1 = []
        var tran2 = []
        var tran = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran.push(data[i]['coreState'])
        }
        // console.log(tran)
        for (var j = 0; j < tran.length; j++) {
          tran2.push(((tran[j][0]['frequency']) / 1000000).toFixed(2))
        }
        that.chartX2 = tran1.reverse()
        that.chartY2 = tran2.reverse()
      })
    },
    realSys() {
      this.$set(this.realButtom, 2, false)
      // console.log('请求实时数据')
      this.$set(this.time, 2, false)
      this.$set(this.real, 2, true)
      this.$set(this.loading, 2, true)
      this.legend3 = '系统使用率(%)'
      var that = this
      this.chart3Quert()
      this.timer3 = setInterval(function() { that.chart3Quert() }, 6000)
    },
    chart3Quert() {
      var that = this
      pmNodeCpuState.query(that.cpu_uuid).then(function(response) {
        var data = response.content
        if (that.loading[2] === true) { that.$set(that.loading, 2, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['cpuSystemAvg'].toFixed(2))
        }
        that.chartX3 = tran1.reverse()
        that.chartY3 = tran2.reverse()
      })
    },
    realUsr() {
      this.$set(this.realButtom, 3, false)
      // console.log('请求实时数据')
      this.$set(this.time, 3, false)
      this.$set(this.real, 3, true)
      this.$set(this.loading, 3, true)
      this.legend4 = '用户使用率(%)'
      var that = this
      this.chart4Quert()
      this.timer4 = setInterval(function() {
        that.chart4Quert()
      }, 6000)
    },
    chart4Quert() {
      var that = this
      pmNodeCpuState.query(that.cpu_uuid).then(function(response) {
        var data = response.content
        if (that.loading[3] === true) { that.$set(that.loading, 3, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['cpuUserAvg'].toFixed(2))
        }
        that.chartX4 = tran1.reverse()
        that.chartY4 = tran2.reverse()
      })
    },
    // 点击查看历史处理函数
    hisTem() {
      this.$set(this.realButtom, 0, true)
      this.$set(this.time, 0, true)
      this.$set(this.real, 0, false)
      // for (var i = 0; i < this.chartX1.length; i++) {
      //   this.$set(this.chartX1, i, 0)
      //   this.$set(this.chartY1, i, 0)
      // }
      // this.chartX1 = []
      // this.chartY1 = []
      // console.log(this.chartX1)
    },
    hisFre() {
      this.$set(this.realButtom, 1, true)
      this.$set(this.time, 1, true)
      this.$set(this.real, 1, false)
    },
    hisSys() {
      this.$set(this.realButtom, 2, true)
      this.$set(this.time, 2, true)
      this.$set(this.real, 2, false)
    },
    hisUsr() {
      this.$set(this.realButtom, 3, true)
      this.$set(this.time, 3, true)
      this.$set(this.real, 3, false)
    },
    handleRealTimeData(data) {
      if (data[this.node_id]['cpu_state'] !== undefined) {
        var nodeCpuData = data[this.node_id]['cpu_state']
        var that = this
        Object.keys(nodeCpuData).forEach((key) => {
          var tthat = that
          if (key !== 'temperature') {
            if (nodeCpuData[key]['cpu_id'] === parseInt(tthat.cpu_uuid)) {
              var coresData = nodeCpuData[key]['core_state']
              var tran1 = []
              var tran2 = []
              var tran3 = []
              Object.keys(coresData).forEach((key) => {
                tran1.push(coresData[key]['cpu_user'])
                tran2.push(coresData[key]['cpu_system'])
                tran3.push(coresData[key]['cpu_busy'])
              })
              tthat.userData = tran1
              tthat.systemData = tran2
              tthat.usedData = tran3
              // console.log(that.userData)
            }
          }
        })
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
body {
    margin: 0;
  }
.back{
  z-index: 1000;
  font-size:20px;
  position: fixed;
  color:brown;
  margin-left:1%;
}
.radio{
  position: relative;
  left: 7%;
  margin-top: 10%;
}
.menu{
  position: relative;
}
.el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
// 导航栏样式覆盖
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 120%;
    min-height: 400px;
  }
// 时间选择器样式
.time-select{
  position: relative;
  margin-top: -1%;
  // margin-left: 30%;
}
// 参数选择器样式
.para-select{
  position: relative;
  margin-top: 10%;
  left: 10%;
}
// cpu展示模块
.cpu-pan{
  position: relative;
  margin-top: 2%;
  height: 250px;
}
.lineChart{
  background-color: blueviolet;
  // height: 500px;
}
.text-pan{
  margin-top: 7%;
}
.chart{
  height: 150px;
  width: 100%;
}
.hisButtom{
  position: relative;
  // margin-left: 7%;

}
.realButtom{
  float:right;
  position: relative;
  // margin-left: 78%;
}
.textcss{
  margin-top: 2%;
  color: rgba(0,0,0,0.5);
}
.Radar{
  height: 200px;
  width: 100%;
  // background-color: aqua;
}

</style>
