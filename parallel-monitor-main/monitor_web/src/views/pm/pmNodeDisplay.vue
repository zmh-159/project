<template>
  <div :class="widthclassinfo">
    <el-row>
      <el-col :span="20" :offset="2">
        <div class="dashboard">
          <!-- <el-button class="setOption" type="text" @click="open">点击配置指标</el-button> -->
          <div class="flex-container column">
            <div v-loading="loading[0]" class="item one" element-loading-text="数据加载中" element-loading-background="rgba(0, 0, 0, 0.2)" style="transform: translate(-32%,-33.5%) scale(0.33)" @click="clickChart('1')">
              <el-button v-show="flag[0]" class="setOption" type="text" @click="open(1)">配置</el-button>
              <div v-if="formFlag[0]" class="form">
                <el-checkbox-group v-model="checkOption[0]" @change="handleOption(checkOption[0],0)">
                  <el-checkbox v-for="(option,i) in optionDatas[0]" :key="i" :label="option.content" :disabled="option.checked" border>{{ option.content }}</el-checkbox>
                </el-checkbox-group>
                <div class="queryButtom">
                  <el-button @click="onCancle(1)">取消</el-button>
                  <el-button type="primary" @click="onSubmit(1)">确认</el-button><br>
                  <p v-if="mes[0]" class="mes">请至少选择两项</p>
                </div>
              </div>
              <Scatter3DP1 v-if="isShow[0][0]" :data="p1data" class="pan" :legend-data="AxisData[0]" />
              <Scatter2DP1 v-if="isShow[0][1]" :data="p1data" class="pan" :legend-data="AxisData[0]" />
            </div>
            <div v-loading="loading[1]" class="item two" element-loading-text="数据加载中" element-loading-background="rgba(0, 0, 0, 0.2)" style="transform: translate(-32%,0.5%) scale(0.33)" @click="clickChart('2')">
              <el-button v-show="flag[1]" class="setOption" type="text" @click="open(2)">配置</el-button>
              <div v-if="formFlag[1]" class="form">
                <el-checkbox-group v-model="checkOption[1]" @change="handleOption(checkOption[1],1)">
                  <el-checkbox v-for="(option,i) in optionDatas[1]" :key="i" :label="option.content" :disabled="option.checked" border>{{ option.content }}</el-checkbox>
                </el-checkbox-group>
                <div class="queryButtom">
                  <el-button @click="onCancle(2)">取消</el-button>
                  <el-button type="primary" @click="onSubmit(2)">确认</el-button><br>
                  <p v-if="mes[1]" class="mes">请至少选择两项</p>
                </div>
              </div>
              <Scatter3DP2 v-if="isShow[1][0]" :data="p2data" class="pan" :legend-data="AxisData[1]" />
              <Scatter2DP2 v-if="isShow[1][1]" :data="p2data" class="pan" :legend-data="AxisData[1]" />
            </div>
            <div v-loading="loading[2]" class="item three" element-loading-text="数据加载中" element-loading-background="rgba(0, 0, 0, 0.2)" style="transform: translate(-32%,34.5%) scale(0.33)" @click="clickChart('3')">
              <el-button v-show="flag[2]" class="setOption" type="text" @click="open(3)">配置</el-button>
              <div v-if="formFlag[2]" class="form">
                <el-checkbox-group v-model="checkOption[2]" @change="handleOption(checkOption[2],2)">
                  <el-checkbox v-for="(option,i) in optionDatas[2]" :key="i" :label="option.content" :disabled="option.checked" border>{{ option.content }}</el-checkbox>
                </el-checkbox-group>
                <div class="queryButtom">
                  <el-button @click="onCancle(3)">取消</el-button>
                  <el-button type="primary" @click="onSubmit(3)">确认</el-button><br>
                  <p v-if="mes[2]" class="mes">请至少选择两项</p>
                </div>
              </div>
              <Scatter3DP3 v-if="isShow[2][0]" :data="p3data" class="pan" :legend-data="AxisData[2]" />
              <Scatter2DP3 v-if="isShow[2][1]" :data="p3data" class="pan" :legend-data="AxisData[2]" />
            </div>
            <div v-loading="loading[3]" class="item four active" element-loading-text="数据加载中" element-loading-background="rgba(0, 0, 0, 0.2)" style="transform: translate(35%, 0) scale(1)" @click="clickChart('4')">
              <el-button v-show="flag[3]" class="setOption" type="text" @click="open(4)">配置</el-button>
              <div v-if="formFlag[3]" class="form">
                <el-checkbox-group v-model="checkOption[3]" @change="handleOption(checkOption[3],3)">
                  <el-checkbox v-for="(option,i) in optionDatas[3]" :key="i" class="el-checkbox" :label="option.content" :disabled="option.checked" border>{{ option.content }}</el-checkbox>
                </el-checkbox-group>
                <div class="queryButtom">
                  <el-button @click="onCancle(4)">取消</el-button>
                  <el-button type="primary" @click="onSubmit(4)">确认</el-button><br>
                  <p v-if="mes[3]" class="mes">请至少选择两项</p>
                </div>
              </div>
              <Scatter3DP4 v-if="isShow[3][0]" class="pan" :data="p4data" :legend-data="AxisData[3]" />
              <Scatter2DP4 v-if="isShow[3][1]" class="pan" :data="p4data" :legend-data="AxisData[3]" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import pmChartScheme from '@/api/pm/pmChartScheme'
import pmNode from '@/api/pm/pmNode'
import Scatter2DP1 from '@/components/Echarts/scatter2D'
import Scatter3DP1 from '@/components/Echarts/sca3D'
import Scatter2DP2 from '@/components/Echarts/scatter2D'
import Scatter3DP2 from '@/components/Echarts/sca3D'
import Scatter2DP3 from '@/components/Echarts/scatter2D'
import Scatter3DP3 from '@/components/Echarts/sca3D'
import Scatter2DP4 from '@/components/Echarts/scatter2D'
import Scatter3DP4 from '@/components/Echarts/sca3D'
// import Radar from '@/components/Echarts/Radar'

// import Star from '@/components/star/star.vue'
export default {
  name: 'PmNodedisplay',
  components: {
    Scatter2DP1,
    Scatter3DP1,
    Scatter2DP2,
    Scatter3DP2,
    Scatter2DP3,
    Scatter3DP3,
    Scatter2DP4,
    Scatter3DP4
    // Radar
    // Star
  },
  data() {
    return {
      mes: [false, false, false, false],
      checkOption: [[], [], [], []],
      optionDatas: [[{ content: 'CPU系统使用率', checked: false },
        { content: 'CPU用户使用率', checked: false },
        { content: 'CPU空闲率', checked: false },
        { content: 'CPU使用率', checked: false },
        { content: 'CPU温度', checked: false },
        { content: '核心频率', checked: false },
        { content: '内存使用率', checked: false },
        { content: '交换区使用率', checked: false },
        { content: '网卡上传速度', checked: false },
        { content: '网卡下载速度', checked: false },
        { content: '磁盘使用率', checked: false },
        { content: '磁盘写出速度', checked: false },
        { content: '磁盘写入速度', checked: false }],
      [{ content: 'CPU系统使用率', checked: false },
        { content: 'CPU用户使用率', checked: false },
        { content: 'CPU空闲率', checked: false },
        { content: 'CPU使用率', checked: false },
        { content: 'CPU温度', checked: false },
        { content: '核心频率', checked: false },
        { content: '内存使用率', checked: false },
        { content: '交换区使用率', checked: false },
        { content: '网卡上传速度', checked: false },
        { content: '网卡下载速度', checked: false },
        { content: '磁盘使用率', checked: false },
        { content: '磁盘写出速度', checked: false },
        { content: '磁盘写入速度', checked: false }],
      [{ content: 'CPU系统使用率', checked: false },
        { content: 'CPU用户使用率', checked: false },
        { content: 'CPU空闲率', checked: false },
        { content: 'CPU使用率', checked: false },
        { content: 'CPU温度', checked: false },
        { content: '核心频率', checked: false },
        { content: '内存使用率', checked: false },
        { content: '交换区使用率', checked: false },
        { content: '网卡上传速度', checked: false },
        { content: '网卡下载速度', checked: false },
        { content: '磁盘使用率', checked: false },
        { content: '磁盘写出速度', checked: false },
        { content: '磁盘写入速度', checked: false }],
      [{ content: 'CPU系统使用率', checked: false },
        { content: 'CPU用户使用率', checked: false },
        { content: 'CPU空闲率', checked: false },
        { content: 'CPU使用率', checked: false },
        { content: 'CPU温度', checked: false },
        { content: '核心频率', checked: false },
        { content: '内存使用率', checked: false },
        { content: '交换区使用率', checked: false },
        { content: '网卡上传速度', checked: false },
        { content: '网卡下载速度', checked: false },
        { content: '磁盘使用率', checked: false },
        { content: '磁盘写出速度', checked: false },
        { content: '磁盘写入速度', checked: false }]],
      options: [[{ name: 'x', value: '' }, { name: 'y', value: '' }, { name: 'z', value: '' }], [{ name: 'x', value: '' }, { name: 'y', value: '' }, { name: 'z', value: '' }], [{ name: 'x', value: '' }, { name: 'y', value: '' }, { name: 'z', value: '' }], [{ name: 'x', value: '' }, { name: 'y', value: '' }, { name: 'z', value: '' }]],
      detail: [],
      drawer: false,
      direction: 'rtl',
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      // 图表指标
      chartdata: [],
      chartType: [],
      // 图表数据
      p1data: [],
      p2data: [],
      p3data: [],
      p4data: [],
      // 图表坐标轴数据
      AxisData: [],
      // 图表位置数组
      isShow: [[false, false, false, false], [false, false, false, false], [false, false, false, false], [false, false, false, false]],
      loading: [true, true, true, true],
      // 控制点击配置出现
      flag: [false, false, false, true],
      // 控制表单出现
      formFlag: [false, false, false, false]
    }
  },
  computed: {
    // 获取当前user
    ...mapGetters([
      'user'
    ])
  },
  watch: {
    // that =this
    screenWidth(newold, oldval) {
      console.log(newold, oldval)
      this.widthclassinfo2()
    }
  },
  beforeCreate() {
  },
  created() {
    // console.log(this.form[0])
    // 初始化位置数组
    this.chartType = this.twoDarrMaker(4, 3)
    this.AxisData = this.twoDarrMaker(4, 3)
    var that = this
    pmChartScheme.queryUserId(this.user.id).then(function(response) {
      // 如果获取到用户配置信息则执行
      // console.log(response)
      if (response.content.length !== 0) {
        for (let i = 0; i < response.content.length; i++) {
          that.positionSelect(response.content[i]['description'], response.content[i]['position'])
          // 转换detail数据
          var data = (response.content[i]['detail'])
          var dataJSON = JSON.parse(data)
          var tran = []
          // console.log(response.content[i]['detail'])
          Object.keys(dataJSON).forEach((key) => {
            tran.push(dataJSON[key]['value'])
          })
          // console.log(tran)
          that.$set(that.chartdata, i, tran)
        }
        that.translateData(that.chartdata, that.isShow)
        that.setAxisData(that.chartdata, that.isShow)
        that.getData(that.chartType)
      } else {
        // 无用户配置则执行
        // 弹框提示
        that.$notify.info({
          title: '消息',
          message: '当前显示为默认配置，如需修改请用户前往配置页面进行配置',
          duration: 7000
        })
        // 默认配置为 CPU使用率，内存使用率，磁盘使用率的三维图以及其二维展开图
        that.positionSelect(1, 0)
        that.positionSelect(1, 1)
        that.positionSelect(1, 2)
        that.positionSelect(0, 3)
        that.$set(that.AxisData[0], 0, 'CPU使用率(%)')
        that.$set(that.AxisData[0], 1, '内存使用率(%)')
        that.$set(that.AxisData[1], 0, '内存使用率(%)')
        that.$set(that.AxisData[1], 1, '磁盘使用率(%)')
        that.$set(that.AxisData[2], 0, 'CPU使用率(%)')
        that.$set(that.AxisData[2], 1, '磁盘使用率(%)')
        that.$set(that.AxisData[3], 0, 'CPU使用率(%)')
        that.$set(that.AxisData[3], 1, '内存使用率(%)')
        that.$set(that.AxisData[3], 2, '磁盘使用率(%)')
        pmNode.queryData().then(function(response) {
          // console.log(response)
          if (JSON.stringify(response) !== '{}') {
            // console.log('获取成功')
            that.$set(that.loading, 0, false)
            that.$set(that.loading, 1, false)
            that.$set(that.loading, 2, false)
            that.$set(that.loading, 3, false)
            that.p1data = []
            that.p2data = []
            that.p3data = []
            that.p4data = []
            Object.keys(response).forEach((key) => {
              that.p1data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate']])
              that.p2data.push([response[key]['memory_rate'], response[key]['disk_rate']])
              that.p3data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['disk_rate']])
              that.p4data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate'], response[key]['disk_rate']])
            })
          } else {
            // console.log('获取实时数据为空')
          }
        })
        that.timer = setInterval(function() {
          pmNode.queryData().then(function(response) {
            if (JSON.stringify(response) !== '{}') {
              // console.log('获取成功')
              that.$set(that.loading, 0, false)
              that.$set(that.loading, 1, false)
              that.$set(that.loading, 2, false)
              that.$set(that.loading, 3, false)
              that.p1data = []
              that.p2data = []
              that.p3data = []
              that.p4data = []
              Object.keys(response).forEach((key) => {
                // console.log(response[key])
                that.p1data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate']])
                that.p2data.push([response[key]['memory_rate'], response[key]['disk_rate']])
                that.p3data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['disk_rate']])
                that.p4data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate'], response[key]['disk_rate']])
              })
            } else {
              // console.log('获取实时数据为空')
            }
          })
        }, 10000)
      }
    })
  },
  beforeMount() {
  },
  mounted() {
    this.screenWidth = document.body.clientWidth
    this.screenHeight = document.body.clientHeight
    window.onresize = () => {
      return (() => {
        this.screenWidth = document.body.clientWidth
        this.screenHeight = document.body.clientHeight
      })()
    }
    this.init()
  },
  methods: {
    widthclassinfo2() {
      if (this.screenWidth > 1192) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    init() {
      this.items = document.querySelectorAll('.flex-container .item')
      for (let i = 0; i < this.items.length; i++) {
        this.items[i].dataset.order = i + 1
      }
    },
    clickChart(clickIndex) {
      const activeItem = document.querySelector('.flex-container .active')
      const activeIndex = activeItem.dataset.order
      const clickItem = this.items[clickIndex - 1]
      if (activeIndex === clickIndex) {
        return
      }
      activeItem.classList.remove('active')
      clickItem.classList.add('active')
      this._setStyle(clickItem, activeItem)
      for (var i = 0; i < 4; i++) {
        if (i === clickIndex - 1) {
          this.$set(this.flag, i, true)
        } else {
          this.$set(this.flag, i, false)
          this.$set(this.formFlag, i, false)
        }
      }
    },
    _setStyle(el1, el2) {
      const transform1 = el1.style.transform
      const transform2 = el2.style.transform
      el1.style.transform = transform2
      el2.style.transform = transform1
    },
    // 创建row行col列的初始值为0的数组
    twoDarrMaker(row, col) {
      const twoDarr = []
      for (let i = 0; i < row; i++) {
        const subarray = []
        for (let j = 0; j < col; j++) {
          subarray.push(0)
        }
        twoDarr.push(subarray)
      }
      return twoDarr
    },
    // 位置数组赋值
    positionSelect(x, y) {
      // 使用$set才能监听数组变化
      this.$set(this.isShow[y], x, true)
    },
    strToJson(str) {
      var json = JSON.parse(str)
      return json
    },
    // 将中文数据指标转换为接口数据格式
    translateData(data, isShow) {
      // console.log(data)
      var k = 0
      while (k <= 3) {
        if (isShow[k].indexOf(true) !== -1) {
          for (var j = 0; j < data[k].length; j++) {
            if (data[k][j] === 'CPU系统使用率') {
              this.chartType[k][j] = 'cpu_system'
            }
            if (data[k][j] === 'CPU用户使用率') {
              this.chartType[k][j] = 'cpu_user'
            }
            if (data[k][j] === 'CPU空闲率') {
              this.chartType[k][j] = 'cpu_idle'
            }
            if (data[k][j] === '核心频率') {
              this.chartType[k][j] = 'frequency'
            }
            if (data[k][j] === 'CPU温度') {
              this.chartType[k][j] = 'cpu_temperature'
            }
            if (data[k][j] === 'CPU使用率') {
              this.chartType[k][j] = 'cpu_busy'
            }
            if (data[k][j] === '内存使用率') {
              this.chartType[k][j] = 'memory_rate'
            }
            if (data[k][j] === '交换区使用率') {
              this.chartType[k][j] = 'swap_rate'
            }
            if (data[k][j] === '网卡上传速度') {
              this.chartType[k][j] = 'up_speed'
            }
            if (data[k][j] === '网卡下载速度') {
              this.chartType[k][j] = 'down_speed'
            }
            if (data[k][j] === '磁盘写出速度') {
              this.chartType[k][j] = 'read_speed'
            }
            if (data[k][j] === '磁盘写入速度') {
              this.chartType[k][j] = 'write_speed'
            }
            if (data[k][j] === '磁盘使用率') {
              this.chartType[k][j] = 'disk_rate'
            }
          }
        }
        k++
      }
      // console.log(this.chartType)
    },
    // 设置坐标轴数据
    setAxisData(data, isShow) {
      for (var i = 0; i < data.length; i++) {
        if (isShow[i][0] === true || isShow[i][1] === true || isShow[i][2] === true || isShow[i][3] === true) {
          this.$set(this.AxisData[i], 0, data[i][0])
          this.$set(this.AxisData[i], 1, data[i][1])
          if (data[i][2] !== 'null') {
            this.$set(this.AxisData[i], 2, data[i][2])
          }
        }
      }
    },
    // 获取图表数据
    getData(data) {
      var that = this
      // 请求数据
      pmNode.queryData().then(function(response) {
        // 获取实时数据成功时执行
        // 清空数组
        that.p1data = []
        that.p2data = []
        that.p3data = []
        that.p4data = []
        if (JSON.stringify(response) !== '{}') {
          // console.log('获取成功')
          that.$set(that.loading, 0, false)
          that.$set(that.loading, 1, false)
          that.$set(that.loading, 2, false)
          that.$set(that.loading, 3, false)
          Object.keys(response).forEach((key) => {
            // 分开写是为了条件可能不能同时满足，注意
            if (data[0][2] === 0) {
              that.p1data.push([response[key][data[0][0]].toFixed(2), response[key][data[0][1]].toFixed(2)])
            }
            if (data[0][2] !== 0) {
              that.p1data.push([response[key][data[0][0]].toFixed(2), response[key][data[0][1]].toFixed(2), response[key][data[0][2]].toFixed(2)])
            }
            if (data[1][2] === 0) {
              that.p2data.push([response[key][data[1][0]].toFixed(2), response[key][data[1][1]].toFixed(2)])
            }
            if (data[1][2] !== 0) {
              that.p2data.push([response[key][data[1][0]].toFixed(2), response[key][data[1][1]].toFixed(2), response[key][data[1][2]].toFixed(2)])
            }
            if (data[2][2] === 0) {
              that.p3data.push([response[key][data[2][0]].toFixed(2), response[key][data[2][1]].toFixed(2)])
            }
            if (data[2][2] !== 0) {
              that.p3data.push([response[key][data[2][0]].toFixed(2), response[key][data[2][1]].toFixed(2), response[key][data[2][2]].toFixed(2)])
            }
            if (data[3][2] === 0) {
              that.p4data.push([response[key][data[3][0]].toFixed(2), response[key][data[3][1]].toFixed(2)])
            }
            if (data[3][2] !== 0) {
              that.p4data.push([response[key][data[3][0]].toFixed(2), response[key][data[3][1]].toFixed(2), response[key][data[3][2]].toFixed(2)])
            }
            // console.log(that.p1data)
          })
        } else {
          console.log('获取实时数据为空')
        }
      })
      this.timer = setInterval(function() {
        pmNode.queryData().then(function(response) {
          // 获取实时数据成功时执行
          // 清空数组
          that.p1data = []
          that.p2data = []
          that.p3data = []
          that.p4data = []
          if (JSON.stringify(response) !== '{}') {
            // console.log('获取成功')
            that.$set(that.loading, 0, false)
            that.$set(that.loading, 1, false)
            that.$set(that.loading, 2, false)
            that.$set(that.loading, 3, false)
            Object.keys(response).forEach((key) => {
              // 分开写是为了条件可能不能同时满足，注意
              if (data[0][2] === 0) {
                that.p1data.push([response[key][data[0][0]].toFixed(2), response[key][data[0][1]].toFixed(2)])
              }
              if (data[0][2] !== 0) {
                that.p1data.push([response[key][data[0][0]].toFixed(2), response[key][data[0][1]].toFixed(2), response[key][data[0][2]].toFixed(2)])
              }
              if (data[1][2] === 0) {
                that.p2data.push([response[key][data[1][0]].toFixed(2), response[key][data[1][1]].toFixed(2)])
              }
              if (data[1][2] !== 0) {
                that.p2data.push([response[key][data[1][0]].toFixed(2), response[key][data[1][1]].toFixed(2), response[key][data[1][2]].toFixed(2)])
              }
              if (data[2][2] === 0) {
                that.p3data.push([response[key][data[2][0]].toFixed(2), response[key][data[2][1]].toFixed(2)])
              }
              if (data[2][2] !== 0) {
                that.p3data.push([response[key][data[2][0]].toFixed(2), response[key][data[2][1]].toFixed(2), response[key][data[2][2]].toFixed(2)])
              }
              if (data[3][2] === 0) {
                that.p4data.push([response[key][data[3][0]].toFixed(2), response[key][data[3][1]].toFixed(2)])
              }
              if (data[3][2] !== 0) {
                that.p4data.push([response[key][data[3][0]].toFixed(2), response[key][data[3][1]].toFixed(2), response[key][data[3][2]].toFixed(2)])
              }
            })
          } else {
            console.log('获取实时数据为空')
          }
        })
      }, 5000)
    },
    // 对象深拷贝
    deepCopy(obj) {
      const item = {}
      for (const key in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, key)) {
          if (typeof obj[key] === 'object') {
            item[key] = this.deepCopy(obj[key])
          } else item[key] = obj[key]
        }
      }
      return item
    },
    // 打开配置弹窗
    open(val) {
      this.$set(this.flag, val - 1, false)
      this.$set(this.formFlag, val - 1, true)
    },
    // 取消配置
    onCancle(val) {
      this.$set(this.flag, val - 1, true)
      this.$set(this.formFlag, val - 1, false)
    },
    // 提交表单
    onSubmit(val) {
      // 获取配置的detail
      var n = val - 1
      // this.options[n] = []
      var flag
      // 判断图表类型
      if (this.checkOption[n].length >= 2) {
        if (this.checkOption[n].length === 2) { flag = 2 } else { flag = 3 }
        var that = this
        for (var j = 0; j < this.checkOption[n].length; j++) {
          this.options[n][j].value = this.checkOption[n][j]
          this.$set(this.AxisData[n], j, this.checkOption[n][j])
        }
        pmChartScheme.queryUserId(this.user.id).then(function(response) {
        // console.log(response.content)
          for (var i = 0; i < response.content.length; i++) {
            if (i === n) {
              var params = response.content[i]
              params.detail = that.options[n]
              // 修改图表类型
              if (flag === 2) {
                params.description = 1
                that.$set(that.isShow[n], 0, false)
              } else {
                params.description = 0
                that.$set(that.isShow[n], 1, false)
              }
              // 修改完成提交修改
              pmChartScheme.edit(params).then(function(response) {
                var thatt = that
                pmChartScheme.queryUserId(that.user.id).then(function(response) {
                  // 如果获取到用户配置信息则执行
                // console.log(response)
                  if (response.content.length !== 0) {
                    for (let i = 0; i < response.content.length; i++) {
                      thatt.positionSelect(response.content[i]['description'], response.content[i]['position'])
                      var data = (response.content[i]['detail'])
                      var dataJSON = JSON.parse(data)
                      var tran = []
                      Object.keys(dataJSON).forEach((key) => {
                        tran.push(dataJSON[key]['value'])
                      })
                      thatt.$set(thatt.chartdata, i, tran)
                    }
                    thatt.translateData(thatt.chartdata, thatt.isShow)
                    thatt.setAxisData(thatt.chartdata, thatt.isShow)
                    thatt.getData(thatt.chartType)
                  }
                })
              })
            }
          }
        })
        this.$set(this.flag, val - 1, true)
        this.$set(this.formFlag, val - 1, false)
      } else if (this.checkOption[n].length < 2) {
        this.$set(this.mes, n, true)
      }
    },
    handleOption(item, n) {
      // 控制最大只能选择三项
      this.$set(this.mes, n, false)
      if (item.length === 3) {
        var item1 = item[0]
        var item2 = item[1]
        var item3 = item[2]
        for (var i = 0; i < this.optionDatas[n].length; i++) {
          if (this.optionDatas[n][i]['content'] !== item1 && this.optionDatas[n][i]['content'] !== item2 && this.optionDatas[n][i]['content'] !== item3) {
            this.optionDatas[n][i]['checked'] = true
          }
        }
      } else if (item.length === 2) {
        for (var j = 0; j < item.length; j++) {
          for (var k = 0; k < this.optionDatas[n].length; k++) {
            if (this.optionDatas[n][k]['content'] !== item[j]) {
              this.optionDatas[n][k]['checked'] = false
            }
          }
        }
      }
    }
  }
}
</script>
<style scoped>
.init-enter-active{
  /* animation: diamond-in-hesitate 5s; */
}
.mes{
  margin-top: 3%;
  color:#fff;
  -webkit-animation: vibrate-1 0.3s ;
	animation: vibrate-1 0.3s ;
}
.dashboard {
  /* background-color: blue; */
  position: relative;
  width: 100%;
  height: 100%;
  min-height: calc(100vh - 104px);
  margin: 0px;
  padding: 0px;
  animation: polygon-in-opposing-corners 5s;
  /* background-color: rgba(0, 0, 0, 0.5); */
}
.flex-container.column {
  /* background-color:blueviolet; */
  position: relative;
  height: 100%;
  width: 100%;
  min-height: calc(100vh - 104px);
  overflow: hidden;
  margin: 0 auto 100px auto;
  box-sizing: content-box;
}
.item {
  position: relative;
  margin-top: 10px;
  /* background: rgb(171, 195, 209); */
  border-radius:15px;
  width: 70%;
  height: 93%;
  position: absolute;
  transform: scale(0.33);
  /*text-align: center;*/
  transition: all 0.8s;
  background:rgba(220, 220, 220, 0.2);
  /* border-radius: 5px; */
  /* opacity: 0.2; */
}
.active {
  height:91%;
  width: 70%;
  margin-left: 10px;
  /* line-height: 300px; */
}
.pan {
  position: relative;
  /* background-color:rgba(64,158,255,0.5) ; */
  /* border-radius: 20px; */
  height: 100%;
  width: 100%;
  padding: 15px;
}
.star{
  position: absolute;
  z-index: 1;
  /* 穿透 */
  pointer-events: none;
}
.dataPannel{
  position: absolute;
  margin-top: -12%;
  margin-left: 60%;
  color: aliceblue;
}
.setOption{
  position: absolute;
  z-index: 100;
  color:#fff;
  font-size: 1.5em;
  margin-left: 88%;
  margin-top:2% ;
}
.form{
  background-color: rgba(0, 0, 0, 0.5);
  display: block;
  /* padding: 0; */
  margin-left: 62%;
  margin-top: 2%;
  padding: 30px 20px 30px 30px;
  width: 30%;
  height: 85%;
  z-index: 1200;
  position: absolute;
  flex: 1;
  animation: wipe-in-left 0.3s;
}
.queryButtom{
  position: relative;
  margin-top:5%;
  margin-left: 3%;
}
.el-checkbox{
  color: #fff !important;
  /* float: left; */
  margin: 0%;
  margin-top: 20px;
  margin-left: 10px;
}
@keyframes wipe-in-left {
  from {
    clip-path: inset(0 0 0 100%);
  }
  to {
    clip-path: inset(0 0 0 0);
  }
}

[transition-style="in:wipe:left"] {
  animation: 2.5s cubic-bezier(.25, 1, .30, 1) wipe-in-left both;
}
@-webkit-keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
            transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
            transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
            transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
            transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
            transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
            transform: translate(0);
  }
}
@keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
            transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
            transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
            transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
            transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
            transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
            transform: translate(0);
  }
}
.Rader{
  position: relative;
  margin-bottom: 10%;
  height: 700px;
  width: 700px;
}
   .bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
</style>
