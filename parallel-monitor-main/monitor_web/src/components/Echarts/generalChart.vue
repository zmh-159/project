<template>
  <div :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'// echarts theme
export default {
  props: {
    className: {
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
    // 父组件数据
    data: {
      type: Number,
      default: null
    },
    chartOption: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      chart: null,
      times: 0,
      initFlag: false
    }
  },
  computed: {
  },
  watch: {
    data: {
      handler(val) {
        if (this.initFlag !== true) {
          this.initChart()
          this.initFlag = true
        }
        if (this.chartOption.xAxis.data.length < 30) {
          this.chartOption.xAxis.data.push(this.times)
          this.chartOption.series[0].data.push(val)
          this.chart.setOption(this.chartOption)
          this.times++
        }
        // console.log(this.chartOption.series[0].data)
      },
      deep: true
    }
    // width: {
    //   handler(newVal, oldVal) {
    //     if (newVal !== oldVal) { this.initChart() }
    //   }
    // }
  },
  created() {
  },
  mounted() {
    // this.initChart()
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
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(this.chartOption)
    }
  }
}
</script>
