<template>
  <div class="totalPannel" :style="{height:pannelHeight}">
    <div class="left-pannel">
      <el-card class="node-information-overview">
        <div style="font-weight: bold;font-family:'tech-font-1'" class="title">
          <svg-icon icon-class="节点" /> {{ nodeId }}号节点
        </div>
        <div class="overview-text">
          <span style="cursor:pointer;color:#6A7C88" @click="changeMode">切换单/多屏模式</span><br>
          <div style="margin-top:3%">主机名: {{ nodeArray[7] }}</div>
          <div>位数: {{ nodeArray[8] }}</div>
          <div>操作系统: {{ nodeArray[9] }}</div>
        </div>
        <!-- <span>主机名: {{ nodeArray[7] }}</span> -->
        <div class="node-info">
          <div><svg-icon icon-class="磁盘" /> 磁盘<br> <div style="margin-top:2%">总容量<span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[3] | unitFilter('TB') }}</span> <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[4] }}</span>块</div></div>
          <div style="margin-top:4%"><svg-icon icon-class="CPU" /> CPU <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[0] }}</span>核 <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[1] }}</span>块</div>
          <div style="margin-top:4%"><svg-icon icon-class="显卡" /> 显卡
            <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[2] }}</span>块</div>
          <div style="margin-top:4%"><svg-icon icon-class="网卡" /> 网卡 <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[5] }}</span>块</div>
          <div style="margin-top:4%"><svg-icon icon-class="内存条" /> 内存条
            <span style="font-family:'tech-font-1';font-weight:bold">{{ nodeArray[6] }}</span>条
            <div v-for="(item,index) in memoryList" :key="index" style="border:1px solid #000;display:inline-block;margin-left:1%" :style="memoryStyle[index]" class="memory-block" />
          </div>
        </div>
      </el-card>
      <div class="memory-list">
        <el-table
          v-fit-columns
          :data="tableData"
          style="width:100%;height:100%;overflow:auto"
          :cell-style="tableCellStyle"
          class="el-table"
          border
        >
          <el-table-column
            prop="id"
            label="id"
            width="30px"
          />
          <el-table-column
            prop="capacity"
            label="容量"
            show-overflow-tooltip
          />
          <el-table-column
            prop="specifications"
            label="内存规格"
            show-overflow-tooltip
          />
          <el-table-column
            prop="speed"
            label="速度"
            show-overflow-tooltip
          />
        </el-table>
      </div>
      <el-card class="memory-pannel">
        <div class="inner-right-chart-swap">
          <floatChart class="flex-chart-memory" :chart-data="nodeMemory[0]" :max="max[0]" :change="selectArray[4]" :name="chart[0]" :stjson="style6" :number="chartNumber" :grid="gridStyle3" :title="memoryName[0]" />
        </div></el-card>
      <el-card class="swap-pannel">
        <div class="inner-right-chart-swap">
          <floatChart class="flex-chart-swap" :chart-data="nodeSwap[0]" :max="max[0]" :name="chart[1]" :stjson="style6" :number="chartNumber" :grid="gridStyle3" :title="swapName[0]" />
        </div>
      </el-card>
    </div>
    <el-row class="all-right-pannel">
      <!-- 多屏模式 -->
      <span v-if="mutiMode===true">
        <el-col :span="12" class="left-col">
          <!-- 节点CPU信息 -->
          <div class="single-pannel-top">
            <!-- 选择哪块CPU -->
            <sliderPannelCpu class="sliderPannel" :name="cpu" :stjson1="style1" :stjson2="style2" :number="smallChartNumber[0]" :type="chartType[0]" :chart-data="nodeBusy" :max="max[0][0]" @child="selectCpu" />
            <div class="inner-right-chart">
              <div class="top-title"><svg-icon icon-class="CPU" />{{ chartTitle[0] }}</div>
              <floatChart class="flex-chart" :name="chart[2]" :stjson="style5" :title="cpuName[0]" :chart-data="nodeCpu[0]" :max="max[0]" :change="selectArray[0]" />
              <floatChart class="flex-chart" :name="chart[3]" :stjson="style5" :title="cpuName[1]" :chart-data="nodeCpu[1]" :max="max[1]" :change="selectArray[0]" />
              <floatChart class="flex-chart" :name="chart[4]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="cpuName[2]" :chart-data="nodeCpu[2]" :max="max[2]" :change="selectArray[0]" />
              <div class="bottom-text">
                <bigSubTitle :styjson="style7" style="width:50%" :top-title="topTitle[0]" :sub-title="bottomData[0]" />
                <div style="width:50%;margin-left:3%">
                  <div style="display:flex">
                    <div style="width:45%;font-size:14px">架构:{{ staticData[0][0] }}</div>
                    <div style="width:55%;font-size:14px">一级指令架构:{{ staticData[0][1] }}</div>
                  </div>
                  <div style="display:flex;margin-top:3%">
                    <div style="width:45%;font-size:14px">二级缓存:{{ staticData[0][2] }}</div>
                    <div style="width:55%;font-size:14px">一级数据缓存:{{ staticData[0][3] }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 节点磁盘信息 -->
          <div class="single-pannel-bottom">
            <sliderPannelDisk class="sliderPannel" :name="disk" :stjson1="style3" :stjson2="style4" :number="smallChartNumber[1]" :type="chartType[2]" :chart-data="nodeWrite" @child="selectDisk" />
            <div class="inner-right-chart">
              <div class="top-title"><svg-icon icon-class="磁盘" />{{ chartTitle[2] }}</div>
              <floatChart class="flex-chart-disk" :name="chart[5]" :stjson="style6" :change="selectArray[2]" :number="chartNumber" :title="diskName[0]" :grid="gridStyle4" :chart-data="nodeDisk[0]" />
              <floatChart class="flex-chart-disk" :name="chart[6]" :stjson="style6" :change="selectArray[2]" :number="chartNumber" :title="diskName[1]" :grid="gridStyle4" :chart-data="nodeDisk[1]" />
            </div>
          </div>
        </el-col>
        <el-col :span="12" class="right-col">
          <!-- 节点GPU信息 -->
          <div class="single-pannel-top">
            <sliderPannelGpu class="sliderPannel" :name="gpu" :stjson1="style1" :stjson2="style2" :number="smallChartNumber[2]" :type="chartType[1]" :zero-flag="zeroFlag[1]" :chart-data="gpuUserd" @child="selectGpu" />
            <div class="inner-right-chart">
              <div class="top-title"><svg-icon icon-class="显卡" />{{ chartTitle[1] }}</div>
              <floatChart class="flex-chart" :name="chart[7]" :stjson="style5" :title="gpuName[0]" :change="selectArray[2]" :chart-data="nodeGpu[0]" />
              <floatChart class="flex-chart" :name="chart[8]" :stjson="style5" :title="gpuName[1]" :change="selectArray[2]" :chart-data="nodeGpu[1]" />
              <floatChart class="flex-chart-gpu" :name="chart[9]" :stjson="style5" :title="gpuName[2]" :change="selectArray[2]" :chart-data="nodeGpu[2]" />
              <div class="bottom-text-gpu">
                <bigSubTitle :styjson="style7" style="width:50%" :top-title="topTitle[1]" :sub-title="bottomData[1]" />
                <div style="width:50%;margin-left:3%">
                  <div style="display:flex">
                    <div style="width:50%;font-size:14px">显卡频率:{{ staticData[1][0] }}</div>
                    <div style="width:50%;font-size:14px">关机温度:{{ staticData[1][1] }}</div>
                  </div>
                  <div style="display:flex;margin-top:3%">
                    <div style="width:50%;font-size:14px">显存频率:{{ staticData[1][2] }}</div>
                    <div style="width:50%;font-size:14px">降频温度:{{ staticData[1][3] }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 节点网卡信息 -->
          <div class="single-pannel-bottom">
            <sliderPannelNetwork class="sliderPannel" :name="network" :stjson1="style3" :stjson2="style4" :number="smallChartNumber[3]" :type="chartType[3]" :chart-data="nodeDown" @child="selectNetwork" />
            <div class="inner-right-chart">
              <div class="top-title"><svg-icon icon-class="网卡" />{{ chartTitle[3] }}</div>
              <floatChart class="flex-chart-net" :change="selectArray[3]" :name="chart[10]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="networkName[0]" :chart-data="nodeNetwork[0]" />
              <floatChart class="flex-chart-net" :change="selectArray[3]" :name="chart[11]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="networkName[1]" :chart-data="nodeNetwork[1]" />
              <div class="bottom-text">
                <bigSubTitle :styjson="style8" style="width:100%" :title-number="titleNumber" :top-title="topTitle[2]" :sub-title="bottomData[2]" />
              </div>
            </div>
          </div>
        </el-col>
      </span>
      <!-- 单屏模式 -->
      <el-card v-else style="width:100%;height:100%; display:flex;" class="right-el-card">
        <singleSliderPannelMuti class="single-left-pannel" :name="all" :type="typeList" :stjson1="style1" :stjson2="style2" :number="singleSliderNumber" :chart-data="nodeList" @child="selectHandware" />
        <div v-if="singleFlag[0]" ref="pannel1" class="inner-right-chart">
          <div class="top-title"><svg-icon icon-class="CPU" />{{ chartTitle[0] }}</div>
          <floatChartCpu class="flex-chart" :name="chart[12]" :stjson="style5" :title="cpuName[0]" :chart-data="nodeCpu[0]" :max="max[0]" :change="selectArray[0]" />
          <floatChartCpu class="flex-chart" :name="chart[13]" :stjson="style5" :title="cpuName[1]" :chart-data="nodeCpu[1]" :max="max[1]" :change="selectArray[0]" />
          <floatChartCpu class="flex-chart" :name="chart[14]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="cpuName[2]" :chart-data="nodeCpu[2]" :max="max[2]" :change="selectArray[0]" />
          <div class="bottom-text" style="display:flex;width:50%;margin-top:1%">
            <bigSubTitle :styjson="style7" style="width:50%" :top-title="topTitle[0]" :sub-title="bottomData[0]" />
            <div style="width:50%;margin-left:3%">
              <div style="display:flex">
                <div style="width:45%;font-size:14px">架构:{{ staticData[0][0] }}</div>
                <div style="width:55%;font-size:14px">一级指令架构:{{ staticData[0][1] }}</div>
              </div>
              <div style="display:flex;margin-top:3%">
                <div style="width:45%;font-size:14px">二级缓存:{{ staticData[0][2] }}</div>
                <div style="width:55%;font-size:14px">一级数据缓存:{{ staticData[0][3] }}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="singleFlag[1]" class="inner-right-chart">
          <div class="top-title"><svg-icon icon-class="显卡" />{{ chartTitle[1] }}</div>
          <floatChart class="flex-chart" :name="chart[7]" :stjson="style5" :title="gpuName[0]" :change="selectArray[2]" :chart-data="nodeGpu[0]" />
          <floatChart class="flex-chart" :name="chart[8]" :stjson="style5" :title="gpuName[1]" :change="selectArray[2]" :chart-data="nodeGpu[1]" />
          <floatChart class="flex-chart-gpu" :name="chart[9]" :stjson="style5" :title="gpuName[2]" :change="selectArray[2]" :chart-data="nodeGpu[2]" />
          <div class="bottom-text-gpu" style="display:flex;width:50%;margin-top:1%">
            <bigSubTitle :styjson="style7" style="width:50%" :top-title="topTitle[1]" :sub-title="bottomData[1]" />
            <div style="width:50%;margin-left:3%">
              <div style="display:flex">
                <div style="width:50%;font-size:14px">显卡频率:{{ staticData[1][0] }}</div>
                <div style="width:50%;font-size:14px">关机温度:{{ staticData[1][1] }}</div>
              </div>
              <div style="display:flex;margin-top:3%">
                <div style="width:50%;font-size:14px">显存频率:{{ staticData[1][2] }}</div>
                <div style="width:50%;font-size:14px">降频温度:{{ staticData[1][3] }}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="singleFlag[2]" ref="pannel2" class="inner-right-chart">
          <div class="top-title"><svg-icon icon-class="磁盘" />{{ chartTitle[2] }}</div>
          <floatChartDisk class="flex-chart-disk" :name="chart[5]" :stjson="style6" :change="selectArray[2]" :number="chartNumber" :title="diskName[0]" :grid="gridStyle4" :chart-data="nodeDisk[0]" />
          <floatChartDisk class="flex-chart-disk" :name="chart[6]" :stjson="style6" :change="selectArray[2]" :number="chartNumber" :title="diskName[1]" :grid="gridStyle4" :chart-data="nodeDisk[1]" />
        </div>
        <div v-if="singleFlag[3]" ref="pannel2" class="inner-right-chart">
          <div class="top-title"><svg-icon icon-class="网卡" />{{ chartTitle[3] }}</div>
          <floatChartNetwork class="flex-chart-net" :change="selectArray[3]" :name="chart[15]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="networkName[0]" :chart-data="nodeNetwork[0]" />
          <floatChartNetwork class="flex-chart-net" :change="selectArray[3]" :name="chart[16]" :stjson="style6" :number="chartNumber" :grid="gridStyle" :title="networkName[1]" :chart-data="nodeNetwork[1]" />
          <div class="bottom-text">
            <bigSubTitle :styjson="style8" style="width:100%" :title-number="titleNumber" :top-title="topTitle[2]" :sub-title="bottomData[2]" />
          </div>
        </div>
      </el-card>
    </el-row>
  </div>
</template>
<script>
import bigSubTitle from '@/views/components/pm/bigSubTitle'
import floatChart from '@/views/components/pm/floatChart'
import floatChartCpu from '@/views/components/pm/floatChart'
import floatChartNetwork from '@/views/components/pm/floatChart'
import floatChartDisk from '@/views/components/pm/floatChart'
import sliderPannelCpu from '@/views/components/pm/sliderPannel'
import sliderPannelGpu from '@/views/components/pm/sliderPannel'
import sliderPannelDisk from '@/views/components/pm/sliderPannel'
import sliderPannelNetwork from '@/views/components/pm/sliderPannel'
import singleSliderPannelMuti from '@/views/components/pm/singleSliderPannel'
import pmNode from '@/api/pm/pmNode'
import tools from '@/utils/common.js'

export default {
  name: 'NodesDetail',
  components: {
    sliderPannelCpu, sliderPannelGpu, sliderPannelDisk, sliderPannelNetwork, floatChart, bigSubTitle, floatChartCpu, floatChartNetwork, floatChartDisk, singleSliderPannelMuti
  },
  data() {
    return {
      // 初始高
      pannelHeight: (document.body.offsetHeight - 137) + 'px',
      // pannelWidth:
      // 单屏多屏切换
      mutiMode: false,
      // 记录当前节点的所有硬件信息
      nodeCpu: [[]],
      nodeBusy: [],
      nodeDisk: [[]],
      nodeWrite: [],
      nodeNetwork: [[]],
      nodeDown: [],
      nodeMemory: [[]],
      nodeMemoryUsed: [],
      nodeGpu: [[]],
      gpuUserd: [],
      nodeSwap: [[]],
      cpuName: [['用户使用率', '系统使用率'], ['总使用率', '温度'], ['频率']],
      gpuName: [['显卡使用率', '显卡温度'], ['显存使用率', '显存温度'], ['显卡功率', '显卡频率']],
      diskName: [['磁盘读速度'], ['磁盘写速度']],
      networkName: [['网卡上传速度'], ['网卡下载速度']],
      memoryName: [['内存使用率']],
      swapName: [['交换区使用率']],
      chartType: ['CPU', '显卡', '磁盘', '网卡', 'M'],
      singleFlag: [true, false, false, false],
      width: '', height: '100%',
      data: {},
      style1: 'width:15%;height:100%;overflow-y:auto;max-width:321px',
      style2: 'width:100%;height:12%;max-height:150px',
      style3: 'width:15%;height:100%;overflow-y:auto',
      style4: 'width:100%;height:19%',
      style5: 'height:100%;width:50%',
      style6: 'height:100%;width:100%',
      cpu: 'cpu',
      memory: 'memory',
      disk: 'disk',
      network: 'network',
      gpu: 'gpu',
      all: 'myall',
      chart: ['chart1', 'chart2', 'chart3', 'chart4', 'chart5', 'chart6', 'chart7', 'chart8', 'chart9', 'chart10', 'chart11', 'chart12',
        'chart13', 'chart14', 'chart15', 'chart16', 'chart17'],
      style7: 'width:50%',
      style8: 'width:40%',
      chartNumber: 1,
      titleNumber: 2,
      // CPU
      gridStyle: {
        left: '2.5%',
        right: '2%',
        top: '5%',
        bottom: '18%'
      },
      // 内存
      gridStyle2: {
        left: '7%',
        right: '2%',
        top: '5%',
        bottom: '30%'
      },
      // 交换区
      gridStyle3: {
        left: '5%',
        right: '2%',
        top: '5%',
        bottom: '25%'
      },
      // 磁盘
      gridStyle4: {
        left: '3%',
        right: '2%',
        top: '5%',
        bottom: '22%'
      },
      topTitle: [['核数', '主频', '三级缓存'], ['驱动版本', '制造商', '性能模式'], ['IP', 'MAC']],
      bottomData: [[6, '3.6Ghz', '3.2MiB'], ['无', '无', '无'], ['192.168.31.153', '2c:f0:5d:c1:f9:71']],
      // cpu,gpu
      staticData: [['无', '无', '无', '无'], ['无', '无', '无', '无']],
      smallChartNumber: [],
      singleSliderNumber: 0,
      // 范围数组
      max: [[100, 100], [100, 120], [5]],
      // cpu,GPU,DISK,NETWORK,memory
      selectArray: [1, 1, 1, 1],
      chartTitle: [],
      // 内存条列表
      tableData: [],
      //
      memoryList: [{
        id: null,
        style: 'border:1px solid #000'
      }],
      memoryStyle: [],
      zeroFlag: [false, false, false, false],
      nodeArray: [0, 0, 0, 0, 0, 0, 0],
      nodeId: this.$route.query.nodeid,
      // CPU、显卡、磁盘、网卡
      nodeList: [[]],
      typeList: [],
      allStaticData: {},
      tableCellStyle: { fontSize: tools.fontSize(14) + 'px' }
    }
  },
  computed: {
    realTimedata() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
    realTimedata: {
      handler(newVal, oldVal) {
        this.handleRealTimeData(newVal)
      },
      deep: true
    }
  },
  created() {
    this.memoryArrayInit()
    this.initNodeList()
    var that = this
    pmNode.querySingleNodeData(this.nodeId).then(function(response) {
      that.allStaticData = response[0]
      that.singleSliderNumber = that.allStaticData['cpuNums'] + that.allStaticData['gpuNums'] + Object.keys(that.allStaticData['disks']).length + Object.keys(that.allStaticData['networks']).length
      for (var i = 0; i < that.allStaticData['cpuNums']; i++) {
        that.typeList.push(i + 1 + '号CPU')
      }
      for (var j = 0; j < that.allStaticData['gpuNums']; j++) {
        that.typeList.push(j + 1 + '号显卡')
      }
      for (var k = 0; k < Object.keys(that.allStaticData['disks']).length; k++) {
        that.typeList.push(k + 1 + '号磁盘')
      }
      for (var m = 0; m < Object.keys(that.allStaticData['networks']).length; m++) {
        that.typeList.push(m + 1 + '号网卡')
      }
      // }
    })
  },
  mounted() {
    window.onresize = () => {
      return (() => {
        this.pannelHeight = (document.body.offsetHeight - 137) + 'px'
      })()
    }
  },
  methods: {
    handleRealTimeData(data) {
      this.data = data
      var nodeData = data[this.nodeId]
      // 初始化多屏数据
      this.initChartArray()
      this.initCpuData(nodeData)
      this.initDiskData(nodeData)
      this.initNetworkData(nodeData)
      this.initGpuData(nodeData)
      this.initMemory(nodeData)
      this.initSwap(nodeData)
      this.initSingleNodeData(nodeData)
      // 初始化单屏数据
    },
    // 数组初始化
    initChartArray() {
      this.nodeWrite = []
      this.nodeDown = []
      this.nodeBusy = []
      this.nodeMemoryUsed = []
      this.gpuUserd = []
      this.nodeList = [[]]
      this.$set(this.nodeCpu, 0, [])
      this.$set(this.nodeCpu, 1, [])
      this.$set(this.nodeCpu, 2, [])
      this.$set(this.nodeGpu, 0, [])
      this.$set(this.nodeGpu, 1, [])
      this.$set(this.nodeGpu, 2, [])
      this.$set(this.nodeDisk, 0, [])
      this.$set(this.nodeDisk, 1, [])
      this.$set(this.nodeNetwork, 1, [])
      this.$set(this.nodeNetwork, 1, [])
      this.$set(this.nodeMemory, 0, [])
      this.$set(this.nodeSwap, 0, [])
    },
    initCpuData(nodeData) {
      var nodeCpu = nodeData['cpu_state']
      if (Object.keys(nodeCpu).length > 0) {
        this.$set(this.nodeList, 0, [])
        for (const key in nodeCpu) {
          // CPU缩略图
          if (key !== 'temperature') {
            this.nodeBusy.push(nodeCpu[key]['cpu_busy'])
            this.nodeList[0].push([nodeCpu[key]['cpu_busy'], (Number(key) + 1) + '号CPU'])
          }
          if (key === ((this.selectArray[0] - 1).toString())) {
            // 右侧完整图,只取一个的
            this.nodeCpu[0].push(nodeCpu[key]['cpu_user'], nodeCpu[key]['cpu_system'])
            this.nodeCpu[1].push(nodeCpu[key]['cpu_busy'], parseFloat(nodeCpu[key]['temperature']))
            this.nodeCpu[2].push(parseFloat((nodeCpu[key]['frequency'] / 1000000).toFixed(2)))
          }
        }
      }
    },
    initGpuData(nodeData) {
      var nodeGpu = nodeData['gpu_state']
      this.$set(this.nodeList, 1, [])
      if (Object.keys(nodeGpu).length > 0) {
        // this.$set(this.smallChartNumber, 2, Object.keys(nodeGpu).length)
        for (const key in nodeGpu) {
          this.gpuUserd.push(nodeGpu[key]['gpu_utilization'])
          this.nodeList[1].push([nodeGpu[key]['gpu_utilization'], (Number(key) + 1) + '号显卡'])
          if (key === (this.selectArray[1] - 1).toString()) {
            var temp = parseInt(nodeGpu[key]['gpu_current_temp'].split('C')[0])
            var gpuFre = parseInt(nodeGpu[key]['current_sm_frequency'].split('MHz')[0])
            var memoryTemp = parseInt(nodeGpu[key]['memory_current_temp'].split('C')[0])
            // 加入动态数据
            this.nodeGpu[0].push(parseInt(nodeGpu[key]['gpu_utilization']), temp)
            if (nodeGpu[key]['memory_current_temp'] === 'N/A') {
              this.nodeGpu[1].push(parseInt(nodeGpu[key]['memory_utilization']), 0)
            } else {
              this.nodeGpu[1].push(parseInt(nodeGpu[key]['memory_utilization']), memoryTemp)
            }
            if (nodeGpu[key]['gpu_power_draw'] === 'N/A') {
              this.nodeGpu[2].push(0, (gpuFre / 1024).toFixed(2))
            } else {
              this.nodeGpu[2].push(nodeGpu[key]['gpu_power_draw'], (gpuFre / 1024).toFixed(2))
            }
            this.$set(this.bottomData[1], 2, nodeGpu[key]['performance_state'])
          }
        }
      } else {
        this.$set(this.zeroFlag, 1, true)
        this.$set(this.chartTitle, 1, '无显卡')
      }
    },
    initDiskData(nodeData) {
      var nodeDisk = nodeData['disk_state']
      if (Object.keys(nodeDisk).length > 0) {
        // 磁盘缩略图
        this.$set(this.nodeList, 2, [])
        for (const key in nodeDisk) {
          this.nodeWrite.push(nodeDisk[key]['write_speed'])
          this.nodeList[2].push([nodeDisk[key]['write_speed'], (Number(key) + 1) + '号磁盘'])
          if (key === (this.selectArray[2] - 1).toString()) {
            this.nodeDisk[0].push(nodeDisk[key]['read_speed'])
            this.nodeDisk[1].push(nodeDisk[key]['write_speed'])
          }
        }
        // this.nodeDisk = diskData
      }
    },
    initNetworkData(nodeData) {
      var nodeNetwork = nodeData['network_state']
      if (Object.keys(nodeNetwork).length > 0) {
        // 网卡缩略图
        this.$set(this.nodeList, 3, [])
        for (const key in nodeNetwork) {
          this.nodeDown.push(nodeNetwork[key]['down_speed'])
          this.nodeList[3].push([nodeNetwork[key]['down_speed'], (Number(key) + 1) + '号网卡'])
          if (key === ((this.selectArray[3] - 1).toString())) {
            this.nodeNetwork[0].push(nodeNetwork[key]['up_speed'])
            this.nodeNetwork[1].push(nodeNetwork[key]['down_speed'])
          }
        }
      }
    },
    initMemory(nodeData) {
      this.$set(this.smallChartNumber, 4, 4)
      for (var i = 0; i < 4; i++) {
        this.nodeMemoryUsed.push(nodeData['memory_rate'])
      }
      // for (const key in nodeData) {
      this.nodeMemory[0].push(nodeData['memory_rate'])
      // }
    },
    initSwap(nodeData) {
      this.nodeSwap[0].push(nodeData['swap_rate'])
    },
    selectCpu(data) {
      // 向右边图传递选择第几块硬件
      this.$set(this.selectArray, 0, data)
      // 初始化右端静态信息
      var that = this
      // pmNode.querySingleNodeData(this.nodeId).then(function(response) {

      setTimeout(function() {
        var cpudata = that.allStaticData['cpus'][data - 1]
        that.$set(that.smallChartNumber, 0, that.allStaticData['cpus'].length)
        that.$set(that.chartTitle, 0, cpudata['cpuName'])
        that.$set(that.bottomData[0], 0, cpudata['cores'])
        that.$set(that.bottomData[0], 1, cpudata['mainFrequency'])
        that.$set(that.bottomData[0], 2, cpudata['l3Cache'])
        that.$set(that.staticData[0], 0, cpudata['arch'])
        that.$set(that.staticData[0], 1, cpudata['l1ICache'])
        that.$set(that.staticData[0], 2, cpudata['l2Cache'])
        that.$set(that.staticData[0], 3, cpudata['l1DCache'])
      }, 500)
      // })
    },
    selectGpu(data) {
      this.$set(this.selectArray, 1, data)
      var that = this
      // pmNode.querySingleNodeData(this.nodeId).then(function(response) {
      // setTimeout(function() {
      var gpudata = that.allStaticData['gpus'][data - 1]
      that.$set(that.smallChartNumber, 2, that.allStaticData['disks'].length)
      that.$set(that.chartTitle, 1, gpudata['name'])
      that.$set(that.bottomData[1], 0, gpudata['driverVersion'])
      that.$set(that.bottomData[1], 1, gpudata['manufacturer'])
      that.$set(that.staticData[1], 0, gpudata['maxSmFrequency'])
      that.$set(that.staticData[1], 1, gpudata['gpuShutdownTemp'].split('C')[0] + '°C')
      that.$set(that.staticData[1], 2, gpudata['maxMemoryFrequency'])
      that.$set(that.staticData[1], 3, gpudata['gpuSlowdownTemp'].split('C')[0] + '°C')
      // }, 1000)
      // })
    },
    selectDisk(data) {
      this.$set(this.selectArray, 2, data)
      var that = this
      // pmNode.querySingleNodeData(this.nodeId).then(function(response) {
      var cpudata = this.allStaticData['disks'][data - 1]
      that.$set(that.smallChartNumber, 1, this.allStaticData['disks'].length)
      that.$set(that.chartTitle, 2, cpudata['name'])
      // })
    },
    selectNetwork(data) {
      this.$set(this.selectArray, 3, data)
      var that = this
      // pmNode.querySingleNodeData(this.nodeId).then(function(response) {
      var networkData = this.allStaticData['networks'][data - 1]
      that.$set(that.smallChartNumber, 3, this.allStaticData['networks'].length)
      that.$set(that.chartTitle, 3, networkData['name'])
      that.$set(that.bottomData[2], 0, networkData['ip'])
      that.$set(that.bottomData[2], 1, networkData['mac'])
      // })
    },
    memoryArrayInit() {
      var that = this
      pmNode.querySingleNodeData(this.nodeId).then(function(response) {
        var memoryData = response[0]['memoryMoudles']
        Object.keys(memoryData).forEach((key) => {
          that.$set(that.tableData, key, {})
          that.$set(that.tableData[key], 'id', key)
          that.$set(that.tableData[key], 'capacity', memoryData[key]['capacity'])
          that.$set(that.tableData[key], 'specifications', memoryData[key]['memoryType'])
          that.$set(that.tableData[key], 'speed', memoryData[key]['speed'])
        })
      })
    },
    // 初始化总览数据-静态数据
    initNodeList() {
      var that = this
      pmNode.querySingleNodeData(this.nodeId).then(function(response) {
        var cpudata = response[0]['cpus']
        var gpudata = response[0]['gpus']
        var diskdata = response[0]['disks']
        var networkData = response[0]['networks']
        var memoryData = response[0]['memoryMoudles']
        that.nodeArray[1] = Object.keys(cpudata).length
        that.nodeArray[2] = Object.keys(gpudata).length
        that.nodeArray[4] = Object.keys(diskdata).length
        that.nodeArray[5] = Object.keys(networkData).length
        that.nodeArray[6] = Object.keys(memoryData).length
        that.$set(that.nodeArray, 7, response[0]['hostName'])
        that.$set(that.nodeArray, 8, response[0]['osBit'])
        that.$set(that.nodeArray, 9, response[0]['osName'] + response[0]['osVersion'])
        for (const k in cpudata) {
          that.nodeArray[0] = that.nodeArray[0] + cpudata[k]['cores']
        }
        var memory = parseInt(response[0]['memorySlot'])
        for (var i = 0; i < memory; i++) {
          that.$set(that.memoryList, i, {})
          that.$set(that.memoryList[i], 'id', i + '号内存')
          that.$set(that.memoryStyle, i, { background: 'fff' })
        }
        for (const k in diskdata) {
          // console.log(k)
          var cap = Number(diskdata[k]['capacity'].replace(/G/g, ''))
          that.nodeArray[3] = that.nodeArray[3] + cap
        }
        for (var j = 0; j < memoryData.length; j++) {
          that.$set(that.memoryStyle, j, { background: '#30B08F' })
        }
      })
    },
    initSingleNodeData(nodeData) {
    },
    selectHandware(flag) {
      var type = flag.split('号')[1]
      var number = parseInt(flag.split('号')[0])
      switch (type) {
        case 'CPU':
          this.$set(this.singleFlag, 0, true)
          this.$set(this.singleFlag, 1, false)
          this.$set(this.singleFlag, 2, false)
          this.$set(this.singleFlag, 3, false)
          this.selectCpu(number)
          break
        case '磁盘':
          this.$set(this.singleFlag, 0, false)
          this.$set(this.singleFlag, 1, false)
          this.$set(this.singleFlag, 2, true)
          this.$set(this.singleFlag, 3, false)
          this.selectDisk(number)
          break
        case '网卡':
          this.$set(this.singleFlag, 3, true)
          this.$set(this.singleFlag, 1, false)
          this.$set(this.singleFlag, 2, false)
          this.$set(this.singleFlag, 0, false)
          this.selectNetwork(number)
          break
        case '显卡':
          this.$set(this.singleFlag, 3, false)
          this.$set(this.singleFlag, 1, true)
          this.$set(this.singleFlag, 2, false)
          this.$set(this.singleFlag, 0, false)
          this.selectGpu(number)
      }
    },
    changeMode() {
      if (this.mutiMode === false) {
        this.mutiMode = true
      } else {
        this.mutiMode = false
        this.singleSliderNumber = 0
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.node-info{
  margin-top: 10%;
  font-size: 22px;
  font-weight: 500;
  .memory-block{
    width:5px;
  height:15px
  }
}
  .totalPannel {
    display: flex;
    width: 100%;
    // height: `window.innerHeight - 137px`;
    padding: 1%;
    cursor: default;
    overflow: auto;
    // 列表总样式
    ul {
        display: block;
        // list-style-type: disc;
        padding: 1%;
        margin-block-start: 0px;
        margin-block-end: 0px;
        padding-inline-start: 0px;
    }
    .left-pannel{
        width: 18%;
        height: 100%;
      .node-information-overview  {
        width: 100%;
        height: 50%;
        .overview-text{
          font-size: 16px;
        }
        .title{
          font-size: 32px;
        }
      }
      .el-card{
        overflow: auto;
      }
      // ::v-deep .el-card__body{
      //   overflow: auto;
      // }
      .memory-pannel{
        width: 100%;
        margin-top: 1%;
        height:12%;
        ::v-deep .el-card__body{
          padding: 1%;
          height: 100%;
          width: 100%;
          display: flex;
        }
        .top-title{
          height: 5%;
          font-size:18px;
          margin-bottom: 10%;
          font-weight:600;
          // font-weight: bold;
          text-align: center;
        }
        .bottom-text{
          height:40% ;
          width:100%;
          display: flex;
          align-items: center; /*定义body的元素垂直居中*/
          word-break:keep-all;       /* 不换行 */
          white-space:nowrap;        /* 不换行 */
        }
        .sliderPannel-memoey{
          width: 25%;
          height: 100%;
        }
      }
      .swap-pannel{
        margin-top: 1%;
        height: 12%;
         ::v-deep .el-card__body{
            padding: 1%;
            height: 100%;
            width: 100%;
          }
      }
      .memory-list{
        width: 100%;
        height: 25%;
        margin-top: 1%;
        .el-table{
          font-size: 12px;
          // background-color:#F3F5F9
        }

      }
    }
  }

  .all-right-pannel {
    width: 81%;
    margin-left: 1%;
  }
  .single-pannel-top {
      width: 99%;
      height: 60%;
      background: #fff;
      padding: 1% 1% 1% 1%;
      display: flex;
      .top-title{
          height: 5%;
          font-size:18px;
          margin-bottom: 1%;
          font-weight:600;
          // font-weight: bold;
          text-align: center;
      }
      .bottom-text{
          height:24% ;
          width:100%;
          display: flex;
          align-items: center; /*定义body的元素垂直居中*/
          word-break:keep-all;       /* 不换行 */
          white-space:nowrap;        /* 不换行 */
        }
        .bottom-text-gpu{
          height:18% ;
          width:100%;
          display: flex;
          align-items: center; /*定义body的元素垂直居中*/
          word-break:keep-all;       /* 不换行 */
          white-space:nowrap;        /* 不换行 */
        }
  }
  //公共属性
  .inner-right-chart{
        height: 100%;
        width:85%;
        padding:0 1% 0 1%;

        .flex-chart{
          height: 25%;
          width:100%
        }
        .flex-chart-disk{
          height: 44%;
          width:100%
        }
        .flex-chart-net{
          height: 35%;
          width:100%
        }
        .flex-chart:not(:nth-of-type(2)){
          margin-top: 1%;
        }
        .flex-chart-disk:not(:nth-of-type(2)){
          margin-top: 1%;
        }
        .flex-chart-net:not(:nth-of-type(2)){
          margin-top: 1%;
        }
        .flex-chart:nth-of-type(4){
          height: 20%;
          width:100%
        }

        .flex-chart-gpu{
          height: 25%;
          width:100%
        }

  }
  .inner-right-chart-swap{
    height: 100%;
    width:100%;
    padding:0 1% 0 1%;
    .flex-chart-swap{
      height: 100%;
      width:100%
    }
    .flex-chart-memory{
          height: 100%;
          width:100%
        }

  }
    .single-pannel-bottom {
      width: 99%;
      height: 39%;
      background: #fff;
      padding: 1% 1% 1% 1%;
      margin-top: 1%;
      display: flex;
      .top-title{
          height: 5%;
          font-size:18px;
          margin-bottom: 3%;
          font-weight:600;
          text-align: center;
        }
        .bottom-text{
          height:15% ;
          width:100%;
          display: flex;
          align-items: center; /*定义body的元素垂直居中*/
          word-break:keep-all;       /* 不换行 */
          white-space:nowrap;        /* 不换行 */
        }
    }
  .left-col {
    height: 100%;
  }
  .sliderPannel{
          ::v-deep .el-card__body{
            padding: 1%;
            height: 100%;
            width: 100%;
          }
      }
  .right-col {
    height: 100%;
    .single-pannel:not(:first-of-type) {
      margin-top: 1%;
      padding: 3% 0 3% 0;
    }
  }
.single-pannel ::v-deep .el-tabs__content{
  height: 100%;
}
.single-left-pannel{
  // width: 20%;
  height: 100%;
  ::v-deep .el-card__body{
            padding: 1%;
            height: 100%;
            width: 100%;
          }
}
.top-title{
          height: 5%;
          font-size:18px;
          margin-bottom: 1%;
          font-weight:600;
          // font-weight: bold;
          text-align: center;
      }
.right-el-card{
  ::v-deep .el-card__body{
    width: 100%;
    height:100%;
    display: flex;
  }
}
</style>
