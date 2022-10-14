<template>
  <div>
    <el-row style="margin-top:1%">
      <el-col :span="24">
        <el-card style="margin:auto;width:95%;padding:1% 4% 2% 4%;height:fit-content">
          <!-- 静态数据 -->
          <el-row>
            <el-col :span="24">
              <el-descriptions class="margin-top" :title="myTitle" :column="5" border>
                <el-descriptions-item v-for="item in items" :key="item.id">
                  <template slot="label">
                    {{ item.text }}
                  </template>
                  {{ item.value }}
                </el-descriptions-item></el-descriptions>
            </el-col>
          </el-row>
          <!-- 动态数据 -->
          <el-row :gutter="20" style="margin-top:5%">
            <!-- <div id="cpu使用率" style="margin-top:1%">
              <el-row>
                <el-row :span="24">
                  <el-col :span="5">
                    <el-button size="mini" type="primary" class="hisButtom" @click="hisCpu">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[0]"
                      v-model="value1"
                      type="datetimerange"
                      :picker-options="pickerOptions1"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      class="time"
                      @change="test1"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[0]" size="mini" type="primary" class="realButtom" @click="realCpuChange()">实时数据</el-button>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="6">
                    <el-card class="cardRight" shadow="never">
                      <div class="ul">
                        <ul>
                          <li v-for="li in lis" :key="li.message" class="li">
                            <el-button type="primary" plain @click="selectCpuId(li.cpuId)">{{ li.message }}号CPU</el-button>
                          </li>
                        </ul></div>
                    </el-card>
                  </el-col>
                  <el-col :span="17" style="margin-top:1%">
                    <RadarChart1 v-if="flag1" v-loading="loading[0]" element-loading-text="数据加载中" class="cate" :legend="legend1" :data-x="chartX1" :data-y="chartY1" />
                  </el-col>
                </el-row>
              </el-row>
            </div> -->
            <div id="内存使用率" style="margin-top:1%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button type="primary" size="mini" class="hisButtom" @click="hisMen">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[1]"
                      v-model="value2"
                      type="datetimerange"
                      :picker-options="pickerOptions2"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      class="time"
                      @change="test2"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[1]" size="mini" type="primary" class="realButtom" @click="realMen">实时数据</el-button>
                  </el-col>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="margin-top:1%">
                  <RadarChart2 v-if="flag2" v-loading="loading[1]" element-loading-text="数据加载中" class="cate" :legend="legend2" :data-x="chartX2" :data-y="chartY2" />
                </el-col>
              </el-row>
            </div>
            <div id="磁盘使用率" style="margin-top:5%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button type="primary" size="mini" class="hisButtom" @click="hisDisk">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[2]"
                      v-model="value3"
                      type="datetimerange"
                      :picker-options="pickerOptions3"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      class="time"
                      @change="test3"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[2]" size="mini" type="primary" class="realButtom" @click="realDisk">实时数据</el-button>
                  </el-col>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="margin-top:1%">
                  <RadarChart3 v-if="flag3" v-loading="loading[2]" element-loading-text="数据加载中" class="cate" :legend="legend3" :data-x="chartX3" :data-y="chartY3" />
                </el-col>
              </el-row>
            </div>
            <div id="磁盘使用率" style="margin-top:5%;margin-bottom:2%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button type="primary" class="hisButtom" @click="hisPro">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time[3]"
                      v-model="value4"
                      type="datetimerange"
                      :picker-options="pickerOptions4"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      class="time"
                      @change="test4"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[3]" type="primary" class="realButtom" @click="realPro">实时数据</el-button>
                  </el-col>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="margin-top:1%">
                  <RadarChart4 v-if="flag4" v-loading="loading[3]" element-loading-text="数据加载中" class="cate" :legend="legend4" :data-x="chartX4" :data-y="chartY4" />
                </el-col>
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
// import RadarChart1 from '@/components/Echarts/RadarChart'
import RadarChart2 from '@/components/Echarts/RadarChart'
import RadarChart3 from '@/components/Echarts/RadarChart'
import RadarChart4 from '@/components/Echarts/RadarChart'
import pmNode from '@/api/pm/pmNode'
import pmNodeState from '@/api/pm/pmNodeState'
import pmNodeCpu from '@/api/pm/pmNodeCpu'
import pmNodeCpuState from '@/api/pm/pmNodeCpuState'
import { mapGetters } from 'vuex'
export default {
  name: 'NodeChartDetail',
  components: {
    RadarChart2, RadarChart3, RadarChart4
  },
  data() {
    var _this = this
    return {
      // 面板第一列
      items: [{ id: 1, text: '操作系统', value: null },
        { id: 2, text: 'CPU数', value: null },
        { id: 3, text: '内存总量', value: null },
        { id: 4, text: '内存插槽数', value: null },
        { id: 5, text: '最大内存量', value: null },
        { id: 6, text: '总算力', value: null },
        { id: 7, text: 'ip地址', value: null },
        { id: 8, text: '创建时间', value: null },
        { id: 9, text: '主机用户名', value: null },
        { id: 10, text: '硬盘总大小', value: null }
      ],
      lis: {},
      // 添加标志位
      flag1: false,
      flag2: false,
      flag3: false,
      flag4: false,
      nodeId: this.$route.query.nodeid,
      // 存放真实的cpuId
      cpuId: null,
      nodeStaticData: [],
      nodeCpuBusy: [],
      nodeCpuData: [],
      nodeCpuList: [],
      // 图1数据
      legend1: '',
      chartX1: [],
      chartY1: [],
      // 图2数据
      legend2: '',
      chartX2: [],
      chartY2: [],
      // 图3数据
      legend3: '',
      chartX3: [],
      chartY3: [],
      // 图4数据
      legend4: '',
      chartX4: [],
      chartY4: [],
      myTitle: '',
      // 时间选择器绑定值
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
      // 定时器
      timer1: '',
      timer2: '',
      timer3: '',
      timer4: '',
      // 时间选择器
      value1: '',
      value2: '',
      value3: '',
      value4: '',
      endTime: new Date(),
      startTime: '',
      list: [],
      number: null,
      realButtom: [false, false, false, false],
      real: [true, true, true, true],
      time: [false, false, false, false],
      loading: [true, true, true, true]
    }
  },
  computed: {
    // 获取当前user
    ...mapGetters([
      'user'
    ])
  },
  watch: {
    real: {
      handler(val) {
        if (val[0] === false) { clearInterval(this.timer1) }
        if (val[1] === false) { clearInterval(this.timer2) }
        if (val[2] === false) { clearInterval(this.timer3) }
        if (val[3] === false) { clearInterval(this.timer4) }
      },
      deep: true
    }
  },
  beforeDestroy() {
    clearInterval(this.timer1)
    clearInterval(this.timer2)
    clearInterval(this.timer3)
    clearInterval(this.timer4)
  },
  created() {
    this.$store.state.isDataCenter = false
    // 静态数据初始化
    this.myTitle = this.nodeId + '号节点'
    var that = this
    pmNode.query(this.nodeId).then(function(response) {
      that.nodeStaticData = response.content
      that.initPannelData(that.nodeStaticData[0])
    })
    // 四图初始化
    that.flag1 = true
    that.flag2 = true
    that.flag3 = true
    that.flag4 = true
    pmNodeCpu.queryNodeId(this.nodeId).then(function(response) {
      that.listInit(response.content)
      that.selectCpuId(that.lis[0]['cpuId'])
    })
    this.realMen()
    this.realDisk()
    this.realPro()
  },
  mounted() {
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
  methods: {
    // cpuId列表生成函数
    listInit(data) {
      for (var i = 0; i < data.length; i++) {
        var tran = { message: i + 1, cpuId: data[i]['cpuId'] }
        var str = '' + i
        this.lis[str] = tran
      }
    },
    // 静态面板数据处理
    initPannelData(data) {
      // console.log(data)
      Object.keys(data).forEach((key) => {
        if (key === 'osName') {
          this.items[0].value = data[key]
        }
        if (key === 'cpuNums') {
          this.items[1].value = data[key]
        }
        if (key === 'memory') {
          this.items[2].value = data[key]
        }
        if (key === 'memorySlot') {
          this.items[3].value = data[key]
        }
        if (key === 'maxExtendMemory') {
          this.items[4].value = data[key]
        }
        if (key === 'performance') {
          this.items[5].value = data[key]
        }
        if (key === 'ip') {
          this.items[6].value = data[key]
        }
        if (key === 'createTime') {
          this.items[7].value = data[key]
        }
        if (key === 'hostName') {
          this.items[8].value = data[key]
        }
        if (key === 'diskTotal') {
          this.items[9].value = data[key]
        }
      })
    },
    // 隔离每个时间选择器处理函数
    initchart2(data) {
      for (var i = 0; i < data.length; i++) {
        this.chartX2[i] = data[i]['createTime']
        this.chartY2[i] = data[i]['memoryRate']
      }
    },
    initchart3(data) {
      for (var i = 0; i < data.length; i++) {
        this.chartX3[i] = data[i]['createTime']
        this.chartY3[i] = data[i]['diskRate']
      }
    },
    initchart4(data) {
      for (var i = 0; i < data.length; i++) {
        this.chartX4[i] = data[i]['createTime']
        this.chartY4[i] = data[i]['processNums']
      }
    },
    // 选取CPU编号触发
    realCpuChange() {
      this.$set(this.time, 0, false)
      this.$set(this.real, 0, true)
      this.selectCpuId(-1)
    },
    selectCpuId(cpuId) {
      // 判断是否是实时数据状态
      if (this.real[0] === true) {
        if (cpuId === -1) {
          this.realCpu(this.cpuId)
        } else {
          this.cpuId = cpuId
          this.realCpu(cpuId)
        }
      } else {
        this.test1()
      }
    },
    // 时间选择器监听函数
    test1() {
      // 用户选择时间段
      var that = this
      if (this.value1 !== null) {
        this.flag1 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[1])
        pmNodeCpuState.queryTime(this.cpuId, range).then(function(response) {
          // console.log(response.content)
          var data = response.content
          // 判断是否有数据
          if (response.content.length !== 0) {
            const gaptime = that.gaptime(range[0], response.content[response.content.length - 1].updateTime)
            // 判断时间是否过长
            if (gaptime > 0) {
              that.sendMessage2()
            }
            // 展示数据
            var tran1 = []
            var tran2 = []
            for (var i = 0; i < data.length; i++) {
              tran1.push(data[i]['createTime'])
              tran2.push((100 - data[i]['cpuIdleAvg']).toFixed(2))
            }
            that.chartX1 = tran1.reverse()
            that.chartY1 = tran2.reverse()
          } else {
            that.resetArray(that.chartX1, that.chartY1)
            that.sendMessage()
          }
          that.flag1 = true
        })
      } else {
      // 清空则恢复默认时间
        this.flag1 = false
        console.log(this.cpuId)
        // 请求默认时间的数据
        pmNodeCpuState.query(this.cpuId).then(function(response) {
          var data = response.content
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push((100 - data[i]['cpuIdleAvg']).toFixed(2))
          }
          that.chartX1 = tran1.reverse()
          that.chartY1 = tran2.reverse()
          that.flag1 = true
        })
      }
    },
    test2() {
      if (this.value2 !== null) {
        this.flag2 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value2[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value2[1])
        var that = this
        pmNodeState.queryTime(this.nodeId, range).then(function(response) {
          if (response.content.length !== 0) {
            const gaptime = that.gaptime(range[0], response.content[response.content.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart2(response.content)
          } else {
            that.resetArray(that.chartX2, that.chartY2)
            that.sendMessage()
          }
          that.flag2 = true
        })
      } else { // 清空则恢复默认时间
        var tthat = this
        this.flag2 = false
        pmNodeState.query(this.nodeId).then(function(response) {
          tthat.initchart2(response.content)
          tthat.flag2 = true
        })
      }
    },
    test3() {
      if (this.value3 !== null) {
        this.flag3 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value3[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value3[1])
        var that = this
        pmNodeState.queryTime(this.nodeId, range).then(function(response) {
          if (response.content.length !== 0) {
            const gaptime = that.gaptime(range[0], response.content[response.content.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart3(response.content)
          } else {
            that.resetArray(that.chartX3, that.chartY3)
            that.sendMessage()
          }
          that.flag3 = true
        })
      } else { // 清空则恢复默认时间
        var tthat = this
        this.flag3 = false
        pmNodeState.query(this.nodeId).then(function(response) {
          tthat.initchart3(response.content)
          tthat.flag3 = true
        })
      }
    },
    test4() {
      if (this.value4 !== null) {
        this.flag4 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value4[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value4[1])
        var that = this
        pmNodeState.queryTime(this.nodeId, range).then(function(response) {
          if (response.content.length !== 0) {
            const gaptime = that.gaptime(range[0], response.content[response.content.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart4(response.content)
          } else {
            that.resetArray(that.chartX4, that.chartY4)
            that.sendMessage()
          }
          that.flag4 = true
        })
      } else { // 清空则恢复默认时间
        var tthat = this
        this.flag4 = false
        pmNodeState.query(this.nodeId).then(function(response) {
          tthat.initchart4(response.content)
          tthat.flag4 = true
        })
      }
    },
    // 时间转换器
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
    // 重置图表数据
    resetArray(dataX, dataY) {
      for (var i = 0; i < dataX.length; i++) {
        dataX[i] = 0
      }
      for (var j = 0; j < dataY.length; j++) {
        dataY[j] = 0
      }
    },
    // 无历史数据时弹窗
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
    // 获取当前页面
    back() {
      var views = this.$store.state.tagsView.visitedViews
      for (var i = 0; i < views.length; i++) {
        if (views[i]['name'] === 'pmNodeChart') {
          this.$store.dispatch('tagsView/delView', views[i]).then(() => {
            this.$router.push({ path: '/Center/pmNodeInfo' })
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
    // CPU需要根据用户传参
    realCpu(val) {
      this.$set(this.realButtom, 0, false)
      this.legend1 = 'cpu使用率(%)'
      this.$set(this.time, 0, false)
      this.$set(this.real, 0, true)
      this.$set(this.loading, 0, true)
      var that = this
      pmNodeCpuState.query(val).then(function(response) {
        // console.log(response.content)
        var data = response.content
        if (that.loading[0] === true) { that.$set(that.loading, 0, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(parseFloat((100 - data[i]['cpuIdleAvg']).toFixed(2)))
        }
        that.chartX1 = tran1.reverse()
        that.chartY1 = tran2.reverse()
      })
      this.timer1 = setInterval(function() {
        // console.log(val)
        pmNodeCpuState.query(val).then(function(response) {
          var data = response.content
          if (that.loading[0] === true) { that.$set(that.loading, 0, false) }
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(parseFloat((100 - data[i]['cpuIdleAvg']).toFixed(2)))
          }
          that.chartX1 = tran1.reverse()
          that.chartY1 = tran2.reverse()
        })
      }, 6000)
    },
    hisCpu() {
      this.$set(this.realButtom, 0, true)
      this.$set(this.time, 0, true)
      this.$set(this.real, 0, false)
    },
    realMen() {
      this.$set(this.realButtom, 1, false)
      this.legend2 = '内存占有率(%)'
      // console.log('请求实时数据')
      var that = this
      this.$set(this.time, 1, false)
      this.$set(this.real, 1, true)
      this.$set(this.loading, 1, true)
      pmNodeState.query(that.nodeId).then(function(response) {
        var data = response.content
        if (that.loading[1] === true) { that.$set(that.loading, 1, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['memoryRate'])
        }
        that.chartX2 = tran1.reverse()
        that.chartY2 = tran2.reverse()
      })
      this.timer2 = setInterval(function() {
        pmNodeState.query(that.nodeId).then(function(response) {
          var data = response.content
          if (that.loading[1] === true) { that.$set(that.loading, 1, false) }
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(data[i]['memoryRate'])
          }
          that.chartX2 = tran1.reverse()
          that.chartY2 = tran2.reverse()
        })
      }, 6000)
    },
    hisMen() {
      this.$set(this.realButtom, 1, true)
      this.$set(this.time, 1, true)
      this.$set(this.real, 1, false)
    },
    realDisk() {
      this.$set(this.realButtom, 2, false)
      this.legend3 = '磁盘占有率(%)'
      // console.log('请求实时数据')
      var that = this
      this.$set(this.time, 2, false)
      this.$set(this.real, 2, true)
      this.$set(this.loading, 2, true)
      pmNodeState.query(that.nodeId).then(function(response) {
        var data = response.content
        if (that.loading[2] === true) { that.$set(that.loading, 2, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['diskRate'])
        }
        that.chartX3 = tran1.reverse()
        that.chartY3 = tran2.reverse()
      })
      this.timer3 = setInterval(function() {
        pmNodeState.query(that.nodeId).then(function(response) {
          var data = response.content
          if (that.loading[2] === true) { that.$set(that.loading, 2, false) }
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(data[i]['diskRate'])
          }
          that.chartX3 = tran1.reverse()
          that.chartY3 = tran2.reverse()
        })
      }, 6000)
    },
    hisDisk() {
      this.$set(this.realButtom, 2, true)
      this.$set(this.time, 2, true)
      this.$set(this.real, 2, false)
    },
    realPro() {
      this.$set(this.realButtom, 3, false)
      this.legend4 = '进程数'
      // console.log('请求实时数据')
      var that = this
      this.$set(this.time, 3, false)
      this.$set(this.real, 3, true)
      this.$set(this.loading, 3, true)
      pmNodeState.query(that.nodeId).then(function(response) {
        var data = response.content
        if (that.loading[3] === true) { that.$set(that.loading, 3, false) }
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['processNums'])
        }
        that.chartX4 = tran1.reverse()
        that.chartY4 = tran2.reverse()
      })
      this.timer4 = setInterval(function() {
        pmNodeState.query(that.nodeId).then(function(response) {
          var data = response.content
          if (that.loading[3] === true) { that.$set(that.loading, 3, false) }
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(data[i]['processNums'])
          }
          that.chartX4 = tran1.reverse()
          that.chartY4 = tran2.reverse()
        })
      }, 6000)
    },
    hisPro() {
      this.$set(this.realButtom, 3, true)
      this.$set(this.time, 3, true)
      this.$set(this.real, 3, false)
    }
  }
}
</script>
<style scoped>
.back{
  z-index: 1000;
  font-size:20px;
  position: fixed;
  color:brown;
  margin-left:1%;
}
  .card{
    margin-top: 1%;
    height: 250px;
  }
  .timeSelect{
    position: relative;
    /* margin-top: -1%; */
    /* height: 50px; */
  }
  .time{
    position: relative;
  margin-top: -1%;

  }
  .cardRight{
    /* position: relative; */
    /* height: 150px; */
    /* width: 100%; */
    /* margin-top: 10%;
    margin-left: 35%; */
  }
  .ul{
    overflow:scroll;
    /* 滚动条 */
    /* height: 180px; */
  }
  .li{
    /* height: 40px;
    width: 100%; */
  }
  .texxt{
    /* display: inline; */
    width: 100%;
    height: 100%;
    margin-left: 3%;
    /* background-color: aquamarine; */
  }
  .cate{
    /* margin-top: 1%; */
    height: 150px;
  width: 100%;
  }
  .hisButtom{
    position: relative;
  }
  .realButtom{
    position: relative;
    float:right;
  }
  .textcss{
  margin-top: 3%;
  color: rgba(0,0,0,0.5);
}
</style>
