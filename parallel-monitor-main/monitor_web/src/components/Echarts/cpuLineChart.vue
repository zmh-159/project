<template>
  <div :class="charts" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  props: {
    charts: {
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
    },
    // 父组件传递的数据
    cpudata: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      chart: null,
      data: this.cpudata,
      // legend数据，可扩展,
      // data: [],
      selectdata: ['温度', '系统使用率', '用户使用率', '频率'],
      // 数据颜色选择器
      lineColors: ['#ff7473', '#ffc952', '#47b8e0', '#a3a9c7'],
      // 存储每一个core的所有指标的数据
      trans: [],
      // 存储每一个core相应指标的数据
      coresdata: [],
      // 存储所有core相应指标的平均值
      corestotal: [0, 0, 0],
      // 图表数据
      temp: [],
      cpuBusy: [],
      cpuUser: [],
      freuency: [],
      timeX: [],
      // 记录一段时间内其他指标的值
      pointavg: [0, 0, 0],
      // 记录一段时间内温度的平均值
      tempavg: 0
    }
  },
  computed: {
    dataStatus: function() {
      return this.data
    }
  },
  watch: {
    cpudata(newV, oldV) {
      this.data = newV
      this.handle(this.data)
      this.initchart()
    }
  },
  created() {
  },
  mounted() {
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
    // 数据预处理 ==> 得到某一时刻一次的值
    initdata(data) {
      // datas是一个对象
      var datas = data
      var corestotal = [0, 0, 0]
      var coresdata = []
      // trans是一个数组
      var trans = datas['pmCoreStates']
      // 遍历每一个core的key值
      for (var k = 0; k < trans.length; k++) {
        coresdata[k] = ([trans[k]['cpuBusy'], trans[k]['cpuUser'], trans[k]['freuency']])
      }
      // 获得所有core每个指标的平均值
      var length = coresdata.length
      for (var j = 0; j < length; j++) {
        for (var i = 0; i < coresdata[j].length; i++) {
          corestotal[i] = corestotal[i] + coresdata[j][i]
        }
      }
      var total_length = corestotal.length
      for (var r = 0; r < total_length; r++) {
        corestotal[r] = Number((corestotal[r] / length))
      }
      return corestotal
    },
    // 数据预处理 ==> 批处理得到某一段时间的值 ==>某一段时间某个cpu的值
    initseries(data) {
      var sum = 0
      var single = []
      for (var i = 0; i < data.length; i++) {
        single[i] = this.initdata(data[i])
      }
      // 处理温度
      for (var j = 0; j < data.length; j++) {
        sum = sum + data[j]['temperature']
      }
      // 处理剩余指标
      for (var m = 0; m < single.length; m++) {
        for (var n = 0; n < single[m].length; n++) {
          this.pointavg[n] = this.pointavg[n] + single[m][n]
        }
      }
      var datan = data.length
      for (var x = 0; x < this.pointavg.length; x++) {
        this.pointavg[x] = this.pointavg[x] / datan
      }
      this.tempavg = sum / data.length
    },
    // 图表数据预处理
    handle(data) {
      // console.log(data)
      // 存放温度
      for (var i = 0; i < data.length; i++) {
        this.temp[i] = data[i]['temperature']
      }
      // 存放时间
      for (var k = 0; k < data.length; k++) {
        this.timeX[k] = data[k]['createTime']
      }
      // 存放用户使用率
      var sumPoint = []
      for (var j = 0; j < data.length; j++) {
        sumPoint[j] = data[j]['pmCoreStates']
      }
      for (var m = 0; m < sumPoint.length; m++) {
        for (var n = 0; n < sumPoint[m].length; n++) { // 12
          this.cpuBusy[m] = sumPoint[m][n]['cpuBusy']
          this.cpuUser[m] = sumPoint[m][n]['cpuUser']
          this.freuency[m] = sumPoint[m][n]['freuency']
        }
      }
      this.timeX.reverse()
      this.temp.reverse()
      this.cpuBusy.reverse()
      this.cpuUser.reverse()
      this.freuency.reverse()
    },
    // 图表初始化
    initchart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        color: this.lineColors,
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data: this.selectdata,
          textStyle: {
            fontSize: '12',
            color: '#fff'
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.timeX,
          axisLabel: {
            textStyle: {
              fontSize: '12',
              color: '#fff'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            textStyle: {
              fontSize: '12',
              color: '#fff'
            }
          }
        },
        series: [{
          name: '温度',
          smooth: true,
          data: this.temp,
          type: 'line',
          areaStyle: {}
        },
        {
          name: '用户使用率',
          smooth: true,
          data: this.cpuUser,
          type: 'line',
          areaStyle: {}
        },
        {
          name: '系统使用率',
          smooth: true,
          data: this.cpuBusy,
          type: 'line',
          areaStyle: {}
        },
        {
          name: '频率',
          smooth: true,
          data: this.freuency,
          type: 'line',
          areaStyle: {}
        }
        ]
      })
    }
  }
}
</script>
<style scoped>

</style>
