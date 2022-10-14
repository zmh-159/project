<template>
  <div>
    <el-row style="margin-top:1%">
      <el-col :span="24">
        <el-card style="margin:auto;width:95%;padding:1% 4% 2% 4%;height:fit-content">
          <!-- 静态数据 -->
          <el-row>
            <el-col :span="24">
              <el-descriptions class="margin-top" :title="myTitle" :column="6" border>
                <el-descriptions-item v-for="item in items" :key="item.id">
                  <template slot="label">
                    {{ item.text }}
                  </template>
                  {{ item.value }}
                </el-descriptions-item></el-descriptions>
            </el-col>
          </el-row>
          <!-- 动态数据 -->
          <el-row :gutter="20" style="margin-top:1%">
            <div id="显卡频率" style="margin-top:5%;margin-bottom:2%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button type="primary" class="hisButtom" @click="hisGpuFre">查看历史</el-button>
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
                      @change="timeChange1"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom[0]" type="primary" class="realButtom" @click="realgpuFre">实时数据</el-button>
                  </el-col>
                </el-col></el-row>
              <el-row>
                <el-col :span="24">
                  <RadarChart1 v-if="show1" v-loading="loading[0]" element-loading-text="数据加载中" class="chart" :legend="legend1" :data-x="chartX1" :data-y="chartY1" />
                </el-col>
                <el-backtop :bottom="500" />
              </el-row>
            </div>
          </el-row>
          <el-backtop :bottom="500" />
        </el-card></el-col></el-row></div>
</template>

<script>

import pmNodeGpu from '@/api/pm/pmNodeGpu'
import pmNodeGpuState from '@/api/pm/pmNodeGpuState'
import RadarChart1 from '@/components/Echarts/RadarChart'
// import RadarChart2 from '@/components/Echarts/RadarChart'

import { mapGetters } from 'vuex'

export default {
  name: 'NetworkChart',
  components: {
    RadarChart1
    // RadarChart2
  },
  data() {
    var _this = this
    return {
      showCpu: true,
      // 总面板数据
      items: [{ id: 1, text: '显卡ID', value: null },
        { id: 2, text: '所属主机', value: null },
        { id: 3, text: '制造商', value: null },
        { id: 4, text: '显卡名', value: null },
        { id: 5, text: '显存', value: null },
        { id: 6, text: '创建时间', value: null }
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
      value1: '',
      value2: '',
      value3: '',
      value4: '',
      datas: [],
      diskdata: {},
      diskStatic: {},
      gpu_uuid: this.$route.query.gpuID,
      timer: '',
      show1: false,
      show2: false,
      show3: false,
      show4: false,
      // 图表数据
      legend1: '',
      chartX1: [],
      chartY1: [],
      legend2: '上传速度(KB/s)',
      chartX2: [],
      chartY2: [],
      legend3: '',
      chartX3: [],
      chartY3: [],
      legend4: '',
      chartX4: [],
      chartY4: [],
      time1: '',
      time2: '',
      real: [true, true],
      time: [false, false],
      loading: [true, true],
      realButtom: [false],
      myTitle: ''
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
        // console.log('real')
        // console.log(val)
        if (val[0] === false) { clearInterval(this.timer1) }
        if (val[1] === false) { clearInterval(this.timer2) }
      },
      deep: true
    }
  },
  created() {
    this.$store.state.isDataCenter = false
    this.myTitle = this.gpu_uuid + '号GPU'
    // 请求cpu静态数据
    var that = this
    pmNodeGpu.query(this.gpu_uuid).then(function(response) {
      // console.log(response.content)
      that.setStaticData(response.content)
    })
  },
  mounted() {
    // 数据初始化
    this.show1 = true
    this.show2 = true
    this.realgpuFre()
    // this.realUp()
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
      this.items[0].value = data[0]['gpuId']
      this.items[1].value = data[0]['nodeId']
      this.items[2].value = data[0]['manufacturer']
      this.items[3].value = data[0]['name']
      this.items[4].value = data[0]['memory']
      this.items[5].value = data[0]['createTime']
    },
    timeChange1() {
      if (this.value1 !== null) {
        this.resetArray(this.chartX1, this.chartY1)
        this.show1 = false
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[1])
        var that = this
        pmNodeGpuState.queryTime(that.gpu_uuid, range).then(function(response) {
          var data = response.content
          // console.log(response.content)
          if (data.length !== 0) {
            const gaptime = that.gaptime(range[0], data[data.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            for (var i = 0; i < data.length; i++) {
              that.chartX1[i] = data[i]['createTime']
              that.chartY1[i] = data[i]['currentGraphicsFrequency'].slice(0, data[i]['currentGraphicsFrequency'].length - 4)
            }
            that.chartX1.reverse()
            that.chartY1.reverse()
          } else {
            // 数据值为空
            that.resetArray(that.chartX1, that.chartY1)
            // 发送信息
            that.sendMessage()
          }
          that.show1 = true
        })
      } else { // 清空则恢复默认时间
        this.show1 = false
        var tthat = this
        pmNodeGpuState.query(tthat.gpu_uuid).then(function(response) {
          var data = response.content
          // console.log('----')
          // console.log(data)
          for (var i = 0; i < data.length; i++) {
            tthat.chartX1[i] = data[i]['createTime']
            tthat.chartY1[i] = data[i]['currentGraphicsFrequency'].slice(0, data[i]['currentGraphicsFrequency'].length - 4)
          }
          tthat.show1 = true
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
    // 数组清空函数 ---> 清空数据并且重置数组长度为0
    resetArray(dataX, dataY) {
      for (var i = 0; i < dataX.length; i++) {
        dataX[i] = 0
      }
      for (var j = 0; j < dataY.length; j++) {
        dataY[j] = 0
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
    back() {
      var views = this.$store.state.tagsView.visitedViews
      for (var i = 0; i < views.length; i++) {
        if (views[i]['name'] === 'gpuChart') {
          this.$store.dispatch('tagsView/delView', views[i]).then(() => {
            this.$router.push({ path: '/Center/dataCenter' })
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
    realgpuFre() {
      this.$set(this.realButtom, 0, false)
      // console.log('请求实时数据')
      this.$set(this.time, 0, false)
      this.$set(this.real, 0, true)
      this.$set(this.loading, 0, true)
      this.legend1 = '显卡频率（MHz）'
      var that = this
      pmNodeGpuState.query(that.gpu_uuid).then(function(response) {
        var data = response.content
        if (that.loading[0] === true) { that.$set(that.loading, 0, false) }
        // console.log(response.content)
        var tran1 = []
        var tran2 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['currentGraphicsFrequency'].slice(0, data[i]['currentGraphicsFrequency'].length - 4))
        }
        that.chartX1 = tran1.reverse()
        that.chartY1 = tran2.reverse()
      })
      this.timer1 = setInterval(function() {
        pmNodeGpuState.query(that.gpu_uuid).then(function(response) {
          var data = response.content
          if (that.loading[0] === true) { that.$set(that.loading, 0, false) }
          // console.log(response.content)
          var tran1 = []
          var tran2 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(data[i]['currentGraphicsFrequency'].slice(0, data[i]['currentGraphicsFrequency'].length - 4))
          }
          that.chartX1 = tran1.reverse()
          that.chartY1 = tran2.reverse()
        })
      }, 6000)
    },
    hisGpuFre() {
      this.$set(this.realButtom, 0, true)
      this.$set(this.time, 0, true)
      this.$set(this.real, 0, false)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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
  margin-top: -1%;
}
// 参数选择器样式
.para-select{
  position: relative;
  margin-top: 10%;
  left: 10%;
}
// 网卡展示模块
.cpu-pan{
  position: relative;
  margin-top: 1%;
  height: 250px;
}
.lineChart{
  background-color: blueviolet;
  // height: 500px;
}
.text-pan{
  margin-top: 10%;
}
.chart{
  position: relative;
  height: 150px;
  width: 100%;
}
.hisButtom{
  position: relative;
  // margin-left: 10%;
}
.realButtom{
  float: right;
}
.pan1{
  position: relative;
  margin-top: 3%;
}
.textcss{
  margin-top: 2%;
  color: rgba(0,0,0,0.5);
}
</style>
