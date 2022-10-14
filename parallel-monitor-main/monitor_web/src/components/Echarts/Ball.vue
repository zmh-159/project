<template>
  <div :class="className" :style="{height:height,width:width,left:left}" />
</template>
<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
// const animationDuration = 6000 动态加载时间
export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    left: {
      type: String,
      default: '20px'
    },
    height: {
      type: String,
      default: '200px'
    },
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      chart: null,
      myOption: {
        tooltip: {
          trigger: ''
        },
        legend: {
          show: false,
          top: '10%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['50%', '95%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 4
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '14',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048 },
              { value: 735 },
              { value: 580 },
              { value: 484 },
              { value: 300 }
            ]
          }
        ]
      }
    }
  },
  watch: {
    data: {
      handler(val) {
        this.myOption.series[0].data[0].value = val[1]
        this.myOption.series[0].data[1].value = val[0] - val[1]
        this.chart.setOption(this.myOption)
      },
      deep: true
    }
  },
  mounted() {
    this.initChart()
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
      this.chart.setOption(this.myOption)
    }
  }
}
</script>
