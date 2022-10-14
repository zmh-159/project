<template>
  <div>
    <el-row>
      <el-col :span="7" :offset="9">
        <el-radio-group v-model="radio" @change="selectChange">
          <el-radio label="1" border>1</el-radio>
          <el-radio label="2" border="">2</el-radio>
          <el-radio label="3" border="">3</el-radio>
          <!-- <el-radio label="n" border="">全部节点</el-radio> -->
        </el-radio-group>
      </el-col>
    </el-row>
    <div v-show="showSingleChart" class="backgroundPannel">
      <!-- <el-button @click="clickNodeChart">点我暂停</el-button> -->
      <el-card v-for="(item,i) in items" :key="item.index" class="RectangularTree" :style="myStyle">
        <el-row>
          <el-col :span="10">
            <span class="card">
              {{ myTitle[i] }}
            </span>
          </el-col>
          <el-col :span="10">
            <!-- <el-button type="text" @click="screen()">全屏</el-button> -->
          </el-col>
        </el-row>
        <el-row style="height:90%">
          <el-col :span="24">
            <Test ref="myChart" class="Test" :real-time-data="realTimeData[item.index]" :height="chartHeight" :width="chartWidth" :flag="myFlag" :node-id="nodeId[item.index]" />
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>
<script>
// import RectangularTree from '@/components/Echarts/RectangularTree'
import Test from '@/components/Echarts/Test'

export default {
  name: '',
  components: {
    Test
  },
  data() {
    return {
      realTimeData: [],
      chartHeight: '200px',
      chartWidth: '200px',
      flag: true,
      nodeId: [],
      items: [],
      myTitle: [],
      myAllTitle: [],
      radio: '1',
      myStyle: {
        height: '400px',
        width: '30%'
      },
      myFlag: false,
      showSingleChart: true,
      showAllChart: false,
      myAllNodeStyle: [],
      formInline: {
        cpuUsed: null,
        gpuUsed: null,
        diskUsed: null,
        memoryUsed: null,
        swapUsed: null
      },
      rules: {
        cpuUsed: [
          { type: 'number', min: 1, max: 100, message: '请输入1-100间的数值', trigger: 'blur' }
        ],
        gpuUsed: [
          { type: 'number', min: 1, max: 100, message: '请输入1-100间的数值', trigger: 'blur' }
        ],
        diskUsed: [
          { type: 'number', min: 1, max: 100, message: '请输入1-100间的数值', trigger: 'blur' }
        ],
        memoryUsed: [
          { type: 'number', min: 1, max: 100, message: '请输入1-100间的数值', trigger: 'blur' }
        ],
        swapUsed: [
          { type: 'number', min: 1, max: 100, message: '请输入1-100间的数值', trigger: 'blur' }
        ]
      },
      myOptionData: []
    }
  },
  computed: {
    data() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
    data: {
      handler(newVal) {
        // if (this.showSingleChart === true) {
        this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)))
        // }
      },
      deep: true
    }
  },
  created() {
    this.selectChange('1')
    // 锁定屏幕不可滚动
    document.addEventListener('keydown', this.down, false)
    window.addEventListener('mousewheel', this.wheel, { passive: false })
    window.addEventListener('DOMMouseScroll', this.mouseSroll, { passive: false })
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.down, false)
    window.removeEventListener('mousewheel', this.wheel, false)
    window.removeEventListener('DOMMouseScroll', this.mouseSroll, false)
  },
  methods: {
    handleRealTimeData(data) {
      var that = this
      var k = 0
      Object.keys(data).forEach((key) => {
        // console.log(data[key])
        that.$set(that.items, k, { index: k })
        that.$set(that.nodeId, k, parseInt(key))
        that.$set(that.realTimeData, k, data[key])
        that.$set(that.myTitle, k, key + ' 号节点')
        that.$set(that.myAllTitle, k, key)
        that.$set(that.myAllNodeStyle, k, 'background-color:#30B08F;color:#fff')
        k++
      })
    },
    handleAllNodeData(value, myOption) {
      var that = this
      var i = 0
      if (myOption === 0) {
        Object.keys(value).forEach(function(key) {
          if (that.items !== []) {
            that.$set(that.items, i, { index: i })
          }
          that.$set(that.myAllNodeStyle, i, 'background-color:#30B08F;color:#fff;')
          i++
        })
        // 有自己的阈值
      } else {
        Object.keys(value).forEach(function(key) {
          if (that.items !== []) {
            that.$set(that.items, i, { index: i })
          }
          if (value[key]['cpu_busy'] >= myOption[0] && value[key]['gpu_utilization'] >= myOption[1] && value[key]['disk_rate'] >= myOption[2] && value[key]['memory_rate'] >= myOption[3] && value[key]['swap_rate'] >= myOption[3]) {
            that.$set(that.myAllNodeStyle, i, 'background-color:#409EFF;color:#fff;box-shadow: 0 0 15px rgba(64,158,255,0.8);animation: fade 600ms infinite;-webkit-animation: fade 600ms infinite;')
          } else {
            that.$set(that.myAllNodeStyle, i, 'background-color:#30B08F;color:#fff;')
          }
          i++
        })
      }
    },
    selectChange(value) {
      this.myStyle = ''
      this.chartHeight = ''
      this.chartWidth = ''
      if (value === '1') {
        this.myStyle = 'height: 700px;width: 80%;margin: auto;margin-top:1%'
        this.chartHeight = '675px'
        this.chartWidth = '1290px'
      } else if (value === '2') {
        this.myStyle = 'height: 400px;width: 45%;margin: auto;margin-top:1%'
        this.chartHeight = '375px'
        this.chartWidth = '700px'
      } else if (value === '3') {
        this.myStyle = 'height: 300px;width: 30%;margin: auto;margin-top:1%'
        this.chartHeight = '275px'
        this.chartWidth = '450px'
      }
      if (this.myFlag === false) { this.myFlag = true } else { this.myFlag = false }
    },
    // 锁定屏幕不可滚动
    down(event) {
      if ((event.ctrlKey === true || event.metaKey === true) &&
			(event.which === 61 || event.which === 107 ||
				event.which === 173 || event.which === 109 ||
				event.which === 187 || event.which === 189)) {
        event.preventDefault()
      }
    },
    wheel(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    mouseSroll(event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    onSubmit(form) {
      var that = this
      var i = 0
      var j = 0
      this.$refs[form].validate((valid) => {
        if (valid) {
          // alert('提交表单成功!')
          Object.keys(that.formInline).forEach(function(key) {
            if (that.formInline[key] !== null) {
              that.$set(that.myOptionData, i, parseInt(that.formInline[key]))
            } else {
              that.$set(that.myOptionData, i, 0)
            }
            i++
          })
          // 立刻取消一次
          Object.keys(this.data).forEach(function(key) {
            if (that.data[key]['cpu_busy'] >= that.myOptionData[0] && that.data[key]['gpu_utilization'] >= that.myOptionData[1] && that.data[key]['disk_rate'] >= that.myOptionData[2] && that.data[key]['memory_rate'] >= that.myOptionData[3] && that.data[key]['swap_rate'] >= that.myOptionData[3]) {
              that.$set(that.myAllNodeStyle, j, 'background-color:#409EFF;color:#fff;box-shadow: 0 0 15px rgba(64,158,255,0.8);animation: fade 600ms infinite;-webkit-animation: fade 600ms infinite;')
            } else {
              that.$set(that.myAllNodeStyle, j, 'background-color:#30B08F;color:#fff;')
            }
            j++
          })
        } else {
          return false
        }
      })
    },
    deleteSubmit() {
      this.myOptionData = []
      this.formInline = {
        cpuUsed: null,
        gpuUsed: null,
        diskUsed: null,
        memoryUsed: null,
        swapUsed: null
      }
      // 立刻取消一次
      var i = 0
      var that = this
      Object.keys(this.data).forEach(function(key) {
        if (that.items !== []) {
          that.$set(that.items, i, { index: i })
        }
        that.$set(that.myAllNodeStyle, i, 'background-color:#409EFF;color:#fff;')
        i++
      })
    },
    screen() {
      console.log('全屏')
    },
    checkMyNodeChart(nodeId) {
      console.log(nodeId)
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.RectangularTree{
  /* background-color: brown; */
  position: relative;
  display: block;
  flex-wrap:  wrap;  /*flex 可以自动换行，这个属性很重要*/
  min-height:300px;
  height: 400px;
  // width: 30%;
  min-width: 30%;
  max-width:100%;
  margin-left: 2%;
  margin-top: 1%;
  flex-shrink: 0;
  padding: 2px;
  float: center;
  ::v-deep .el-card__body{
    height: 100%;
  }
  .el-col{
    height: 100%;
  }
  .Test{
    position: relative;
    // background-color: aqua;
    text-align: center;
    width: 100%;
    height: 100%;
    margin:auto;
  }
  .card{
    position: relative;
    // width: 80%;
    margin:auto;
    margin-bottom: 0.5%;
    /* z-index: 100; */
    color: aliceblue;
    background-color: #031A23;
    text-align: center;
    font-size: 18px;
    font-weight: bold;
    font-family: 'tech-font-1';
    padding: 3px;
    /* line-height: 4rem; */
  }
}
.nodeDiv{
  position: relative;
  display: block;
  flex-wrap:  wrap;  /*flex 可以自动换行，这个属性很重要*/
  min-height:50px;
  height: 50px;
  width: 4%;
  min-width: 4%;
  max-width:100%;
  margin-left: 1%;
  margin-top: 1%;
  flex-shrink: 0;
  padding: 2px;
  float: center;
  cursor:pointer
}
.backgroundPannel{
  position: relative;
  padding-bottom: 10%;
  display: flex;
  flex-wrap: wrap;
  align-content:flex-start;
  width:100%;
  min-height:755px;
  background:#bdc3c7;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to bottom, #2c3e50, #bdc3c7);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to bottom, #2c3e50, #bdc3c7); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}
.pannel{
  padding: 10%;
  margin: auto;
  min-width:1000px;

}

@keyframes fade {
    from {
        opacity: 1.0;
    }
    50% {
        opacity: 0.4;
    }
    to {
        opacity: 1.0;
    }
}

@-webkit-keyframes fade {
    from {
        opacity: 1.0;
    }
    50% {
        opacity: 0.4;
    }
    to {
        opacity: 1.0;
    }
}
.filter{
  margin: auto;
  margin-top: 2%;
  // margin-left: 1%;
  .demo-form-inline{
  }
}

</style>
