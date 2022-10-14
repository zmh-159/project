<template>
  <div :class="widthclassinfo">
    <div class="app-container" style="position:relative">
      <div class="time-pannel">
        <Clocks ref="test" />
      </div>
      <div class="tem-pannel">
        <Temprature class="Tem" :data="temData" :legend="legendTem" :max="maxTem" />
      </div>
      <div class="ent-pannel">
        <Temprature class="Tem" :data="entropyData" :legend="legendEntropy" :max="maxEnt" />
      </div>
      <div class="nodeMessage">
        <el-row :gutter="40">
          <el-col :span="10" :offset="7">
            <el-col :span="7"><el-card class="card1">监控节点数量<br><h2>{{ allNode }}</h2></el-card> </el-col>
            <el-col :span="7"><el-card class="card2">在线节点数量<br><h2>{{ onLineNode }}</h2></el-card></el-col>
            <el-col :span="7"><el-card class="card3">离线节点数量<br><h2>{{ offLineNode }}</h2></el-card></el-col>
          </el-col>
        </el-row>
      </div>
      <el-row>
        <el-col :span="24" class="main-pannel">
          <Scatter3D class="pan" :data="data" :legend-data="AxisData" :style="{'height':realHeight}" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import Temprature from '@/components/Echarts/Temprature'
import Scatter3D from '@/components/Echarts/scatter3D'
import Clocks from '@/components/Echarts/Clocks'
import pmNode from '@/api/pm/pmNode'
// import Star from '@/components/star/star.vue'
export default {
  name: 'Dashboard',
  components: {
    Scatter3D,
    Temprature,
    Clocks
    // Sta
    // Radar
  },
  data() {
    return {
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      percentage: 100,
      colors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#6f7ad3', percentage: 100 }
      ],
      AxisData: [],
      data: [],
      entropy: 30,
      realHeight: (document.documentElement.clientHeight - 104) + 'px',
      card1: "padding: '20px'",
      scene: null,
      allNode: null,
      onLineNode: null,
      offLineNode: null,
      temData: 0,
      entropyData: 0,
      timer: '',
      legendTem: '当前温度',
      legendEntropy: '当前熵值',
      entropyArray: [],
      maxTem: 100,
      maxEnt: 1
    }
  },
  watch: {
    // screenWidth(newold, oldval) {
    //   this.widthclassinfo2()
    // }
  },
  // computed: {
  // },

  created() {
    this.initNodeInfo()
    // console.log(2 * 10 ** 3)
    this.initRealData()
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
  beforeDestroy() {
    // 离开页面清除定时器
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {
    // 初始化相空间数据
    initRealData() {
      var newData
      var oldData
      this.$set(this.AxisData, 0, 'CPU使用率')
      this.$set(this.AxisData, 1, '内存使用率')
      this.$set(this.AxisData, 2, '磁盘使用率')
      var that = this
      pmNode.queryData().then(function(response) {
        console.log(response)
        if (JSON.stringify(response) !== '{}') {
          that.data = []
          Object.keys(response).forEach((key) => {
            that.data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate'], response[key]['disk_rate']])
          })
        } else {
          console.log('获取失败')
        }
      })
      this.timer = setInterval(function() {
        pmNode.queryData().then(function(response) {
          if (JSON.stringify(response) !== '{}') {
            newData = []
            that.data = []
            Object.keys(response).forEach((key) => {
              // console.log(key)
              newData.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate'], response[key]['disk_rate']])
              // 对oldData第一次赋值
              that.data.push([parseFloat((100 - response[key]['cpu_idle']).toFixed(2)), response[key]['memory_rate'], response[key]['disk_rate']])
            })
            // 计算熵值
            that.temData = that.computedTem(oldData, newData)
            // that.computedEntropy(oldData, newData)
            that.entropyData = that.computedEntropy(oldData, newData)
            oldData = newData
          } else {
            console.log('获取失败')
          }
        })
      }, 5000)
    },
    widthclassinfo2() {
      if (this.screenWidth > 1202) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    computedTem(oldData, newData) {
      var nodeNum = newData.length
      var sum = 0
      var m = nodeNum
      if (oldData === undefined) {
        return 0
      } else {
        // 外层为节点循环
        for (var i = 0; i < nodeNum; i++) {
          // 内层为三个指标循环
          var sum1 = 0
          for (var j = 0; j < 3; j++) {
            sum1 = sum1 + (newData[i][j] - oldData[i][j]) ** 2
          }
          // 累积所有节点的值
          sum1 = Math.sqrt(sum1)
          sum = sum + sum1
        }
        return sum / m
      }
    },
    computedEntropy(newData) {
      // 每次计算重新赋值
      this.entropyArray = []
      var count = 0
      if (newData !== undefined) {
        for (var i = 0; i < newData.length; i++) {
          var x = parseInt((newData[i][0] / 20).toFixed(0))
          var y = parseInt((newData[i][1] / 20).toFixed(0))
          var z = parseInt((newData[i][2] / 20).toFixed(0))
          // 先判断再赋值
          if (this.entropyArray[x * 20 + y * z] !== 1) {
            count++
          }
          this.$set(this.entropyArray, x * 20 + y * z, 1)
        }
        return count / 125
      } else {
        return 0
      }
    },
    initNodeInfo() {
      var that = this
      pmNode.queryAllData().then(function(response) {
        that.allNode = response.totalElements
      })
      pmNode.offline().then(function(response) {
        that.offLineNode = Object.keys(response).length
        that.onLineNode = that.allNode - that.offLineNode
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
*{
  padding:0;
  margin:0;
}
.time-pannel{
  position: absolute;
  width: 250px;
  height: 250px;
  margin-left: 63%;
  margin-top: -1%;
  z-index: 5;
  height: 20%;
}
.tem-pannel{
  position: absolute;
  width: 250px;
  height: 250px;
  margin-left: 80%;
  margin-top: 10%;
}
.ent-pannel{
  position: absolute;
  width: 250px;
  height: 250px;
  margin-left: 80%;
  margin-top: 23%;
}
.Radar{
  position: relative;
  margin-top: -3%;
  height: 800px;
  width: 100%;
}

.vue-particles
  {
  pointer-events: none;
    // background: linear-gradient(-90deg, rgba(36,65,135,1) 0%,rgba(153,101,222,0.8) 75%);
    width: 100%;
    // max-height: 90vh;
    position: absolute;
    z-index: 100;
    // animation:circle-in-hesitate 8s ;
  }
@keyframes circle-in-center {
  from {
    clip-path: circle(0%);
  }
  to {
    clip-path: circle(125%);
  }
}

[transition-style="in:circle:center"] {
  animation: 2.5s cubic-bezier(.25, 1, .30, 1) circle-in-center both;
}
h2{
    margin-top: 3%;
  }
.nodeMessage{
  display: block;
  // margin: 0%;
  margin-top: 20px;
  pointer-events: none;
  position: absolute;
  width: 100%;
  height: 10%;
  z-index: 5;
  font-weight: bolder;
  font-family: 'tech-font-1';
  // word-break:keep-all;
    .card1{
    position: relative;
    height: 100%;
    margin-top: 1%;
    opacity: 0.8;
    color: #fff;
    text-align: center;
    // border: 0;
    -moz-box-shadow:0 0 10px 10px rgba(64,158,255,0.5) !important;
    -webkit-box-shadow:0 0 10px 10px rgba(64,158,255,0.5) !important;
    box-shadow:0 0 10px 10px rgba(64,158,255,0.5) !important;
    .el-card__body {
      background-color: #409EFF;
    }
  }
  .card2{
    position: relative;
    height: 100%;
    margin-top: 1%;
    opacity: 0.8;
    color: #fff;
    text-align: center;
    // border: 0;
    -moz-box-shadow:0 0 10px 10px rgba(48,176,143,0.5) !important;
    -webkit-box-shadow:0 0 10px 10px rgba(48,176,143,0.5) !important;
    box-shadow:0 0 10px 10px rgba(48,176,143,0.5) !important;
    .el-card__body {
      background-color: #30B08F;
    }
  }.card3{
    position: relative;
    height: 100%;
    margin-top: 1%;
    opacity: 0.8;
    // border: 0;
    border-color: rgba(228,231,237,0.8);
    text-align: center;
    color: rgba(0,0,0,0.4);
    -moz-box-shadow:0 0 10px 10px rgba(228,231,237,0.5) !important;
    -webkit-box-shadow:0 0 10px 10px rgba(228,231,237,0.5) !important;
    box-shadow:0 0 10px 10px rgba(228,231,237,0.5) !important;
    .el-card__body {
      background-color: #DCDFE6;
    }
}
}
.Tem{
  width: 100%;
  height: 100%;
  pointer-events: none;
  // position: absolute;
  z-index: 5;
  // width: 100px;
}
.text{
  color:transparent;
  font-size: 40px;
  font-weight: bold;
  background: linear-gradient(45deg, rgb(254, 255, 255) 0%, rgba(0,173,181,.4)  100%);
  -webkit-background-clip: text;
}

.main-containerr{
  min-width: calc(100vw - 200px);
  min-height: calc(100v - 104px) ;
}
.main-pannel{
  position: relative;
  margin: 0%;
  .pan{
  position: relative;
  margin: 0%;
  }
  /* height: 1000px; */
  /* background-color: aqua; */
}
.three{
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 1000;
}
.number{
  z-index: 5;
   display: inline-block;
  font-family: 'tech-font-1';
  color: #30B08F;
  font-size: 1.5rem;
  // font-weight: 4000;
}
.bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
</style>
