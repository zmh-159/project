<template>
  <ul :style="stjson1" class="slider-list">
    <li v-for="i in number" :key="i" :style="stjson2">
      <el-card :shadow="select[i]===true?always:hover" @click.native="selectChart(i)">
        <div :id="name+i" class="chart" />
      </el-card>
    </li>
  </ul>
</template>
<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme
import tools from '@/utils/common.js'

export default {
  props: {
    stjson1: {
      type: String,
      default: 'width:30%;height:100%;overflow-y:auto'
    },
    stjson2: {
      type: String,
      default: 'width:100%;height:33%'
    },
    name: {
      type: String,
      default: 'chart'
    },
    type: {
      type: String,
      default: ''
    },
    number: {
      type: Number,
      default: 1
    },
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Number,
      default: 0
    },
    chartData: {
      type: Array,
      default: () => { return [] }
    },
    zeroFlag: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      chart: [],
      chartOption: {
        title: {
          text: '',
          x: 'center',
          textStyle: {
            fontSize: tools.fontSize(12),
            color: '#000'
          }
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '100',
          bottom: '1%'
        },
        xAxis: {
          type: 'category',
          show: false,
          nameGap: 5,
          splitLine: {
            show: false
          }
        },
        yAxis: {
          type: 'value',
          show: false,
          // min: this.min,
          // max: this.max,
          splitLine: {
            show: false
          },
          splitArea: {
            show: false
          }
        },
        series: [
          {
            name: 'Fake Data',
            type: 'line',
            showSymbol: false,
            lineStyle: {
              color: '#6A7C88'
            },
            data: []
          }
        ]
      },
      chartOptions: [],
      count: 0,
      select: [],
      selectId: null,
      always: 'always',
      hover: 'hover'
    }
  },
  watch: {
    chartData: {
      handler(val) {
        if (this.zeroFlag !== true) {
          this.setData(val, this.count)
          this.count++
        }
      },
      deep: true
    }
  },
  created() {
    this.initArray()
    var that = this
    setTimeout(function() {
      that.initChart()
    }, 500)
    this.selectChart(1)
  },
  mounted() {
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        for (var i = 0; i < this.chart.length; i++) {
          this.chart[i].resize()
        }
        // this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    // this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      if (this.zeroFlag === false) {
        for (var i = 0; i < this.number; i++) {
          var number = i + 1
          var id = this.name + number
          this.$set(this.chart, i, echarts.init(document.getElementById(id), 'macarons'))
          // 深拷贝
          var objString = JSON.stringify(this.chartOption)
          var obj = JSON.parse(objString)
          this.$set(this.chartOptions, i, obj)
          this.chartOptions[i].title.text = number + '号' + this.type
          this.$set(this.chartOptions[i].yAxis, 'min', this.min)
          if (this.max !== 0) {
            this.$set(this.chartOptions[i].yAxis, 'max', this.max)
          }
          this.chart[i].setOption(this.chartOptions[i])
        }
      } else {
        var idz = this.name + 1
        this.$set(this.chart, 0, echarts.init(document.getElementById(idz), 'macarons'))
        // 深拷贝
        var objString1 = JSON.stringify(this.chartOption)
        var obj1 = JSON.parse(objString1)
        this.$set(this.chartOptions, 0, obj1)
        this.chartOptions[0].title.text = '无' + this.type
        this.$set(this.chartOptions[0].yAxis, 'min', this.min)
        this.chart[0].setOption(this.chartOptions[0])
      }
    },
    setData(val, count) {
      // console.log(val[0])
      for (var i = 0; i < this.number; i++) {
        var number = i + 1
        this.chartOptions[i].title.text = number + '号' + this.type
        this.chartOptions[i].series[0].data.splice(0, 1)
        this.chartOptions[i].series[0].data.push([(47 + this.count).toString(), val[i]])
        this.chart[i].setOption(this.chartOptions[i])
      }
    },
    initArray() {
      for (var k = 0; k < 48; k++) {
        this.$set(this.chartOption.series[0].data, k, [k.toString(), 0])
      }
    },
    selectChart(index) {
      this.$set(this.select, index, true)
      this.selectId = index
      for (var i = 0; i < this.number; i++) {
        if (i + 1 !== index) {
          this.$set(this.select, i + 1, false)
        }
      }
      this.$emit('child', index)
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.chart{
  height:100%;
  width:100%
}
.slider-list{
  .el-card{
    margin: auto;
    height: 100%;
    width:95%;
    ::v-deep el-card_body{
      padding: 0;
      height: 100%;
      width:100%;
    }
    // box-shadow: ;
  }
  .el-card.is-always-shadow{
    -webkit-box-shadow: 0 2px 12px 0 rgba(0,0,0,.2);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.2);
  }
  li:not(:first-of-type){
    margin-top: 10%;
  }
}
/*滚动条样式*/
.slider-list::-webkit-scrollbar {/*滚动条整体样式*/
   width: 6px;     /*高宽分别对应横竖滚动条的尺寸*/
   height: 6px;
}
.slider-list::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
   border-radius: 5px;
   -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
   background: rgba(0,0,0,0.2);
}
.slider-list::-webkit-scrollbar-track {/*滚动条里面轨道*/
   -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
   border-radius: 0;
   background: rgba(0,0,0,0.1);
}
</style>
