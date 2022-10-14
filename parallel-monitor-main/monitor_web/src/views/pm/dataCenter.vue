<template>
  <div :class="widthclassinfo">
    <div class="dataCenterCon">
      <el-tabs v-model="activeName" class="el-tab" tab-position="left" @tab-click="handleClick">
        <el-tab-pane label="节点列表" name="nodeInfo">
          <PmNodeInfo :node-id="nodeId" @memory="goToInfo" @cpu="goToInfo" @gpu="goToInfo" @network="goToInfo" @disk="goToInfo" />
        </el-tab-pane>
        <el-tab-pane label="CPU列表" name="cpuInfo">
          <CpuInfo :node-id="nodeId" @node="goToInfo" />
        </el-tab-pane>
        <el-tab-pane label="磁盘列表" name="diskInfo">
          <DiskInfo :node-id="nodeId" @node="goToInfo" />
        </el-tab-pane>
        <el-tab-pane label="网卡列表" name="networkInfo">
          <NetworkInfo :node-id="nodeId" @node="goToInfo" />
        </el-tab-pane>
        <el-tab-pane label="内存条列表" name="memoryInfo">
          <MemoryInfo :node-id="nodeId" @node="goToInfo" />
        </el-tab-pane>
        <el-tab-pane label="显卡列表" name="gpuInfo">
          <GpuInfo :node-id="nodeId" @node="goToInfo" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import CpuInfo from '@/views/pm/basicData/cpuInfo.vue'
import PmNodeInfo from '@/views/pm/basicData/pmNodeInfo'
import DiskInfo from '@/views/pm/basicData/diskInfo'
import NetworkInfo from '@/views/pm/basicData/networkInfo'
import MemoryInfo from '@/views/pm/basicData/memoryInfo'
import GpuInfo from '@/views/pm/basicData/gpuInfo'
export default {
  components: {
    CpuInfo,
    PmNodeInfo,
    DiskInfo,
    NetworkInfo,
    MemoryInfo,
    GpuInfo
  },
  data() {
    return {
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      activeName: '',
      nodeId: null
    }
  },
  watch: {
    // that =this
    screenWidth(newold, oldval) {
      // console.log(newold, oldval)
      this.widthclassinfo2()
    }
  },
  created() {
    this.activeName = this.$cookies.get('tran')
    this.$store.state.isDataCenter = true
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
  },
  methods: {
    widthclassinfo2() {
      if (this.screenWidth > 1410) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    handleClick(tab) {
      // console.log(tab.name)
      this.nodeId = null
      this.activeName = tab.name
      this.$cookies.set('tran', tab.name)
    },
    goToInfo(infoName, nodeId) {
      this.nodeId = nodeId
      switch (infoName) {
        case 'cpuInfo':
          this.$cookies.set('tran', 'cpuInfo')
          this.activeName = 'cpuInfo'
          break
        case 'networkInfo':
          this.$cookies.set('tran', 'networkInfo')
          this.activeName = 'networkInfo'
          break
        case 'memoryInfo':
          this.$cookies.set('tran', 'memoryInfo')
          this.activeName = 'memoryInfo'
          break
        case 'gpuInfo':
          this.$cookies.set('tran', 'gpuInfo')
          this.activeName = 'gpuInfo'
          break
        case 'diskInfo':
          this.$cookies.set('tran', 'diskInfo')
          this.activeName = 'diskInfo'
          break
        case 'nodeInfo':
          this.$cookies.set('tran', 'nodeInfo')
          this.activeName = 'nodeInfo'
          break
      }
    }
  }
}
</script>
<style lang="scss">
.dataCenterCon{
  width: 100%;
  height: 100%;
  min-height: calc(100vh - 104px);
  // background-color:blue;
  display: inline-block;
  padding: 1% 1%;
  // font-size: 16px;
  .el-tab{
    background-color: #F3F5F9;
    // border: 0;
  }
  .el-tabs__nav-scroll{
    // color: #fff;
    // padding-left: 1px;
    background: rgba(64,158,255,0.8);
  }
  .el-tabs--card>.el-tabs__header .el-tabs__nav {
    // box-shadow: 0px 6px 5px -5px rgba(180,160,120,.6);
  }
  .el-tabs__item{
    font-size: 14px;
    font-weight: bold;
    color: #fff !important;
    text-align: center !important;
    opacity: 0.7;
  }
  .el-tabs__item.is-active{
    // color: rgba(64,158,255,0.8) !important;
    background-color: #fff !important;
    color: rgba(64,158,255,0.8) !important;
    opacity: 1;
  }
}
    .bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
</style>
