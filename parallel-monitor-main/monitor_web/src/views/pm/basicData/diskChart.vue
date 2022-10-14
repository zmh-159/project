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
          <el-row :gutter="20" style="margin-top:2%">
            <div id="读写速度" style="margin-top:2%;margin-bottom:2%">
              <el-row>
                <el-col :span="24">
                  <el-col :span="5">
                    <el-button type="primary" size="mini" class="hisButtom" @click="hisReadWrite">查看历史</el-button>
                  </el-col>
                  <el-col :span="9" :offset="4">
                    <el-date-picker
                      v-if="time"
                      v-model="value"
                      type="datetimerange"
                      :picker-options="pickerOptions"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      align="right"
                      @change="timeChange"
                    />
                  </el-col>
                  <el-col :span="5">
                    <el-button v-show="realButtom" size="mini" type="primary" class="realButtom" @click="realReadWrite">实时数据</el-button>
                  </el-col>
                </el-col></el-row>
              <el-row>
                <el-col :span="24">
                  <RadarChartVs v-if="show" v-loading="loading" class="chart" :legend="myLegend" :data-x="chartX" :data-y="chartY" :title="myRadarTitle" />
                </el-col>
                <el-backtop :bottom="500" />
              </el-row>
            </div>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// import pmNodeCpu from '@/api/pm/pmNodeCpu'
// import pmNodeCpuState from '@/api/pm/pmNodeCpuState'
import pmNodeDisk from '@/api/pm/pmNodeDisk'
import pmNodeDiskState from '@/api/pm/pmNodeDiskState'
import RadarChartVs from '@/components/Echarts/RadarChart_vs'

import { mapGetters } from 'vuex'

export default {
  name: 'DiskChart',
  components: {
    RadarChartVs
  },
  data() {
    var _this = this
    return {
      myTitle: '',
      myRadarTitle: this.$route.query.diskID + '号磁盘读/写速度（KiB/s）',
      chartX: [],
      chartY: [],
      // 总面板数据
      items: [{ id: 1, text: '磁盘ID', value: null },
        { id: 2, text: '所属节点', value: null },
        { id: 3, text: '可用磁盘大小', value: null },
        { id: 1, text: '磁盘名', value: null },
        { id: 2, text: '创建时间', value: null }
      ],
      // 时间选择器
      pickerOptions: {
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
      real: true,
      time: false,
      loading: true,
      value: [],
      disk_uuid: this.$route.query.diskID,
      timer: '',
      show: true,
      // 图表数据
      myLegend: ['磁盘读速度', '磁盘写速度'],
      time1: '',
      time2: '',
      realButtom: false
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
        if (val === false) { clearInterval(this.timer) }
      },
      deep: true
    }
  },
  created() {
    this.$store.state.isDataCenter = false
    this.myTitle = this.disk_uuid + '号磁盘'
    // 请求cpu静态数据
    var that = this
    pmNodeDisk.query(this.disk_uuid).then(function(response) {
      that.setStaticData(response.content)
    })
  },
  mounted() {
    // 数据初始化
    this.show1 = true
    this.realReadWrite()
    // this.realWrite()
    if (window.history && window.history.pushState) {
      // 往历史记录里面添加一条新的当前页面的url
      history.pushState(null, null, document.URL)
      // 给 popstate 绑定一个方法 监听页面刷新
      window.addEventListener('popstate', this.back, false)
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  destroyed() {
    window.removeEventListener('popstate', this.back, false)
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
      this.items[0].value = data[0]['diskId']
      this.items[1].value = data[0]['nodeId']
      this.items[2].value = data[0]['capacity']
      this.items[3].value = data[0]['name']
      this.items[4].value = data[0]['createTime']
    },
    timeChange1() {
      if (this.value1 !== null) {
        this.resetArray(this.chartX1, this.chartY1)
        this.show1 = false
        var range = []
        console.log(this.value)
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[1])
        var that = this
        pmNodeDiskState.queryTime(this.disk_uuid, range).then(function(response) {
          var data = response.content
          if (data.length !== 0) {
            const gaptime = that.gaptime(range[0], data[data.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            for (var i = 0; i < data.length; i++) {
              that.chartX1[i] = data[i]['createTime']
              that.chartY1[i] = data[i]['readSpeed']
            }
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
        pmNodeDiskState.query(tthat.disk_uuid).then(function(response) {
          var data = response.content
          // console.log('----')
          // console.log(data)
          for (var i = 0; i < data.length; i++) {
            tthat.chartX1[i] = data[i]['createTime']
            tthat.chartY1[i] = data[i]['readSpeed']
          }
          tthat.show1 = true
        })
      }
    },
    timeChange() {
      // this.resetArray(this.chartX, this.chartY)
      if (this.value !== null) {
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value[1])
        var that = this
        pmNodeDiskState.queryTime(this.disk_uuid, range).then(function(response) {
          var data = response.content
          if (data.length !== 0) {
            const gaptime = that.gaptime(range[0], data[data.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            var tran1 = []
            var tran2 = []
            var tran3 = []
            for (var i = 0; i < data.length; i++) {
              tran1.push(data[i]['createTime'])
              tran2.push(data[i]['readSpeed'])
              tran3.push(data[i]['writeSpeed'])
            }
            tran1.reverse()
            tran2.reverse()
            tran3.reverse()
            that.chartX = tran1
            that.$set(that.chartY, 0, tran2)
            that.$set(that.chartY, 1, tran3)
            // 数据为空
          } else {
            that.resetArray(that.chartX, that.chartY)
            that.sendMessage()
          }
        })
      } else { // 清空则恢复默认时间
        var tthat = this
        console.log('清空')
        pmNodeDiskState.query(tthat.disk_uuid).then(function(response) {
          var data = response.content
          var tran1 = []
          var tran2 = []
          var tran3 = []
          for (var i = 0; i < data.length; i++) {
            tthat.$set(tran1, i, data[i]['createTime'])
            tthat.$set(tran2, i, data[i]['readSpeed'])
            tthat.$set(tran3, i, data[i]['writeSpeed'])
          }
          tran1.reverse()
          tran2.reverse()
          tran3.reverse()
          tthat.chartX = tran1
          tthat.$set(tthat.chartY, 0, tran2)
          tthat.$set(tthat.chartY, 1, tran3)
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
        // dataX[i] = 0
        this.$set(dataX, i, 0)
      }
      for (var j = 0; j < dataY.length; j++) {
        for (var k = 0; k < dataY[j].length; k++) {
          this.$set(dataY[j], k, 0)
        }
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
        if (views[i]['name'] === 'diskChart') {
          this.$store.dispatch('tagsView/delView', views[i]).then(() => {
            this.$router.push({ path: '/Center/diskInfo' })
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
    realReadWrite() {
      this.realButtom = false
      // console.log('请求实时数据')
      this.time = false
      this.real = true
      this.loading = true
      var that = this
      pmNodeDiskState.query(that.disk_uuid).then(function(response) {
        var data = response.content
        // that.show = true
        if (that.loading === true) { that.loading = false }
        // console.log(response.content)
        var tran1 = []
        var tran2 = []
        var tran3 = []
        for (var i = 0; i < data.length; i++) {
          tran1.push(data[i]['createTime'])
          tran2.push(data[i]['readSpeed'])
          tran3.push(data[i]['writeSpeed'])
        }
        that.chartX = tran1.reverse()
        that.$set(that.chartY, 0, tran2.reverse())
        that.$set(that.chartY, 1, tran3.reverse())
      })
      this.timer = setInterval(function() {
        pmNodeDiskState.query(that.disk_uuid).then(function(response) {
          var data = response.content
          if (that.loading === true) { that.loading = false }
          // console.log(response.content)
          var tran1 = []
          var tran2 = []
          var tran3 = []
          for (var i = 0; i < data.length; i++) {
            tran1.push(data[i]['createTime'])
            tran2.push(data[i]['readSpeed'])
            tran3.push(data[i]['writeSpeed'])
          }
          that.chartX = tran1.reverse()
          that.$set(that.chartY, 0, tran2.reverse())
          that.$set(that.chartY, 1, tran3.reverse())
          // console.log('实时数据')
          // console.log(that.chartX)
          // console.log(that.chartY)
        })
      }, 6000)
    },
    hisReadWrite() {
      this.realButtom = true
      this.time = true
      this.real = false
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
  // position: relative;
  // margin-top: 2%;
  // right: -50%;
  // height: 150px;
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
  height: 200px;
  width: 100%;
  // margin-top: 3%;
  // margin-bottom: 2%;
}
.chart{
  position: relative;
  height: 500px;
  width: 100%;
  // margin-top: 5%;
  // margin-bottom: 2%;
}
.hisButtom{
  position: relative;
}
.realButtom{
  float:right;
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
