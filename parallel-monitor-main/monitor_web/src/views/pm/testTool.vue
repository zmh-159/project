<template>
  <div :class="widthclassinfo">
    <div slot="header" class="clearfix">
      <el-row :span="25">
        <el-col style="margin:auto;width:95%;padding:1% 4% 2% 4%;height:fit-content">
          <el-card>
            <el-form ref="form" :model="form" :rules="rules" class="form">
              <el-steps :active="activeStep" align-center finish-status="success" style="width:100%; margin:auto">
                <el-step title="请选择一个节点" />
                <el-step title="请选择一至两个CPU" />
                <el-step title="请选择两个核心" />
                <el-step title="请输入测试数据" />
              </el-steps>

              <el-carousel trigger="click" height="40vh" indicator-position="outside" arrow="always" :autoplay="false" :loop="false" @change="changeStep">

                <el-carousel-item>
                  <el-col class="el-table-filter-1">
                    <el-autocomplete v-model="state" clearable placeholder="输入或者选择节点编号" :fetch-suggestions="querySearch" @select="handleSelect">
                      <i
                        slot="suffix"
                        @click="handleIconClick"
                      />
                      <template slot-scope="{ item }">
                        <span>Node:{{ item.nodeId }}; </span>
                        <span>cpu:{{ item.cpu }}; </span>
                      </template>
                    </el-autocomplete>
                  </el-col>
                </el-carousel-item>

                <el-carousel-item>
                  <el-col :span="6" class="el-table-filter-2">
                    <!-- <el-form-item label="请选择一至两个CPU(请先选择节点)" prop="cpuList"><br> -->
                    <el-checkbox-group v-model="cpustate" size="max" :min="0" :max="2" @change="handleGroup(cpustate)">
                      <el-checkbox-button
                        v-for="item in myCpu"
                        :key="item.value"
                        :label="item.value"
                        :value="item.value"
                        border
                      >{{ item.label }}
                      </el-checkbox-button>
                    </el-checkbox-group>
                    <!-- </el-form-item> -->
                  </el-col>
                </el-carousel-item>
                <el-carousel-item>
                  <el-col :span="6" class="el-table-filter-3">
                    <!-- <el-form-item label="请选择两个核心(请先选择CPU)" prop="coreList"><br> -->
                    <div v-show="flag[0]">
                      {{ 'CPU'+cpustate[0] }}
                      <el-checkbox-group v-model="coreState1" :disabled="group[0]" :max="choose1_max" @change="handleGroup1(coreState1)">

                        <el-checkbox
                          v-for="item in myCore1"
                          :key="item.value"
                          :label="item.value"
                          :value="item.value"
                        >{{ item.label }}
                        </el-checkbox>
                      </el-checkbox-group></div>
                    <div v-show="flag[1]">
                      {{ 'CPU '+cpustate[1] }}
                      <el-checkbox-group v-model="coreState2" :disabled="group[1]" :max="choose2_max" @change="handleGroup2(coreState2)">
                        <el-checkbox
                          v-for="item in myCore2"
                          :key="item.value"
                          :label="item.value"
                          :value="item.value"
                        >{{ item.label }}
                        </el-checkbox>
                      </el-checkbox-group>
                    </div>
                    <!-- </el-form-item> -->
                  </el-col>
                </el-carousel-item>
                <el-carousel-item>
                  <el-col :span="6" class="el-table-filter-4">
                    <el-form-item label="起始包大小(单位：byte)" prop="start">
                      <el-input v-model.number="form.start" placeholder="大于等于1byte" />
                    </el-form-item>
                    <el-form-item label="结束包大小(单位：byte)" prop="end">
                      <el-input v-model.number="form.end" placeholder="小于等于1024*1024*1024byte" />
                    </el-form-item>
                    <el-form-item label="步进(单位：bit)" prop="steps">
                      <el-input v-model.number="form.steps" placeholder="请输入整数值" />
                    </el-form-item>
                    <el-form-item label="重复次数(单位：次)" prop="repeat">
                      <el-input v-model.number="form.repeat" placeholder="范围在1000-1000000" />
                    </el-form-item>
                  </el-col>
                </el-carousel-item>
              </el-carousel>

              <el-form-item style="float:right">
                <el-button style="z-index:1000" :disabled="disabled" type="warning" @click="submitForm('form')">{{ buttonText }}</el-button>
                <p v-if="mes[0]" class="mes">请至少选择两项</p>
              </el-form-item>
              <span style="text-align: center;display:block;color: red; font-size: 13px">评测过程中请尽量关闭正在使用的核心，以免影响结果精度</span>

            </el-form>
          </el-card>

          <el-row :gutter="20">
            <el-col :span="24">
              <div ref="addImage">
                <div v-for="(item, i) in items" :key="item.index" :ref="capture[i]">
                  <el-button type="success" icon="el-icon-download" title="下载图片" @click="generatorImage(item.index)" />
                  <el-button type="danger" icon="el-icon-delete" title="删除图片" @click="deleteImage(item.index)" />
                  <div>
                    <el-row id="myChart">
                      <el-col :span="18" :offset="2">
                        <div>
                          <RadarChart class="chart" :legend="grapthTitle" :data-x="chartX[i]" :data-y="chartY[i]" />
                        </div>
                      </el-col>
                      <el-col :span="3">
                        <el-card class="cardRight" shadow="never">
                          <div>{{ card[item.index] }}</div>
                        </el-card>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>
            </el-col>
            <el-backtop :bottom="500" />
          </el-row>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>

import ServerApi from '@/api/pm/common/pmNodeCommon'// 引入后端接口
import RadarChart from '@/components/Echarts/RadarChart2'// 引入图表
// import schema from 'async-validator'
import html2canvas from 'html2canvas'

export default {
  name: 'TestTool',
  components: {
    RadarChart
  },
  data() {
    var checkStart = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入整数值'))
        } else {
          if (value < 1) {
            callback(new Error('起始包数据大于等于1byte'))
          } else if (value >= this.form.end) {
            callback(new Error('不能大于结束包数据'))
          } else {
            callback()
          }
        }
      }, 1000)
    }
    var checkEnd = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入整数值'))
        } else {
          if (value > 1073741824) {
            callback(new Error('结束包数据小于等于1024*1024*1024byte'))
          } else {
            callback()
          }
        }
      }, 100)
    }
    var checkSteps = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入整数值'))
        } else {
          if (value < 1) {
            callback(new Error('步进大于等于1byte'))
          } else if (value >= this.form.end) {
            callback(new Error('不能大于结束包数据'))
          } else {
            callback()
          }
        }
      }, 1000)
    }
    var checkRepeat = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入整数值'))
        } else {
          if (value < 1000) {
            callback(new Error('范围在1000-1000000'))
          } else if (value > 1000000) {
            callback(new Error('范围在1000-1000000'))
          } else {
            callback()
          }
        }
      }, 100)
    }
    return {
      rules: {
        start: [{ validator: checkStart, trigger: 'blur' }],
        end: [{ validator: checkEnd, trigger: 'blur' }],
        steps: [{ validator: checkSteps, trigger: 'blur' }],
        repeat: [{ validator: checkRepeat, trigger: 'blur' }]
      },
      form: {// 传入后端接口的参数
        nodeId: '',
        cpu1: '',
        cpu2: '',
        core1: '',
        core2: '',
        start: '',
        end: '',
        steps: '',
        repeat: ''
      },
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      grapthTitle: '测试结果', // 图表标题
      chartX: [[]], // x轴数据
      chartY: [[]], // y轴数据
      show: [false],
      items: [],
      state: '',
      cpustate: [],
      nodeData: [],
      cpuData: [],
      coreData: [],
      coresNum: [],
      coreState1: [],
      coreState2: [],
      myCpu: [],
      myCore1: [],
      myCore2: [],
      buttonText: '开启测试',
      disabled: false,
      mes: [false],
      flag: [false, false],
      group: [false, false],
      choose1_max: 2,
      choose2_max: 2,
      oldItem: null,
      oldItem2: [],
      submitNumber: 0,
      capture: [],
      card: [],
      activeStep: 0
    }
  },
  watch: {
    screenWidth(newold, oldval) {
      console.log(newold, oldval)
      this.widthclassinfo2()
    },
    'state': {
      handler: function(item) {
        if (item !== '') {
          if (this.oldItem !== null) {
            if (this.oldItem !== item) {
              this.myCpu = []
              this.myCore1 = []
              this.myCore2 = []
              this.cpustate = []
              this.coreState1 = []
              this.coreState2 = []
              this.group = [false, false]
              this.oldItem = item
            }
          } else {
            this.oldItem = item
          }
          var tran = this.nodeData.filter(this.createFilter(item))
          this.myCpu = []
          this.myCore = []
          for (var i = 0; i < tran[0]['cpu']; i++) {
            var temp = {}
            temp.value = i
            temp.label = 'CPU' + i
            this.myCpu.push(temp)
          }
        } else {
          this.myCpu = []
          this.myCore1 = []
          this.myCore2 = []
          this.cpustate = []
        }
      }
    },
    'cpustate': {
      handler: function(item) {
        if (item.length === 0) {
          this.$set(this.flag, 0, false)
          this.$set(this.flag, 1, false)
        } else {
          if (this.oldItem2 !== []) {
            if (this.oldItem2[0] !== item[0]) {
              this.myCore1 = this.myCore2
              this.coreState1 = this.coreState2
              this.myCore2 = []
              this.coreState2 = []
              this.group = [false, false]
              this.oldItem2 = item
            } else {
              // this.myCore2 = this.myCore1
              // this.coreState2 = this.coreState1
            }
          } else {
            this.oldItem2 = item
          }
          this.$set(this.flag, 0, true)
          this.myCore1 = []
          for (var i = 1; i < this.coresNum[this.state]; ++i) {
            var temp = {}
            temp.value = i
            temp.label = '核心' + (i + 1)
            this.myCore1.push(temp)
          }
          if (item.length === 2) {
            this.$set(this.flag, 1, true)

            this.myCore2 = []
            for (var j = 1; j < this.coresNum[this.state]; ++j) {
              var temp1 = {}
              temp1.value = j
              temp1.label = '核心' + (j + 1)
              this.myCore2.push(temp1)
            }
          } else {
            this.$set(this.flag, 1, false)
          }
        }
      },
      deep: true
    },
    flag: {
      handler(val) {
        if (this.flag[0] === false) {
          this.choose2_max = this.choose2_max + this.coreState1.length
          this.coreState1 = []
        }
        if (this.flag[1] === false) {
          this.choose1_max = this.choose1_max + this.coreState2.length
          this.coreState2 = []
        }
      },
      deep: true
    }
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
    this.getCpuInfo()
  },
  methods: {
    widthclassinfo2() {
      if (this.screenWidth > 1560) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    // 走马灯与步骤条结合
    changeStep(step) {
      this.activeStep = step
    },
    querySearch(queryString, cb) {
      var nodeData = this.nodeData
      var results = queryString ? nodeData.filter(this.createFilter(queryString)) : nodeData
      cb(results)
    },
    createFilter(queryString) {
      return (nodeData) => {
        return (nodeData.nodeId.toLowerCase().indexOf(queryString.toLowerCase()) !== -1)
      }
    },
    handleSelect(item) {
      this.state = item.nodeId
      this.form.nodeId = item.nodeId
    },
    handleIconClick(ev) {
    },
    getCpuInfo() {
      var that = this
      ServerApi.cpuInfo().then(function(response) {
        for (const key in response) {
          var cpus = Object.keys(response[key]).length
          that.$set(that.coresNum, key, response[key][0])
          that.nodeData.push({ nodeId: key, cpu: cpus, cores: response[key] })
        }
      })
    },
    // 延时
    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms))
    },
    getRes(submitNumber) { // 每5s轮寻结果
      var f = this.form
      var that = this
      this.sleep(5000).then(function() {
        var tthat = that
        ServerApi.cpuTestRes('?nodeId=' + f.nodeId).then(function(response) {
          if (response.status === 1) {
            tthat.$set(tthat.items, submitNumber, { index: submitNumber })
            tthat.$set(tthat.capture, submitNumber, 'capture' + submitNumber)
            tthat.sleep(100).then(function() { // 延迟100ms等待组件初始化完成
              tthat.$set(tthat.chartX, submitNumber, response.result.x)
              tthat.$set(tthat.chartY, submitNumber, response.result.y)
            })
            //   }
            // }
            tthat.disabled = false
            tthat.buttonText = '开启测试'
          } else {
            console.log('轮询结果')
            tthat.getRes()
          }
        })
      })
    },
    submitForm(formName) {
      var f = this.form
      var that = this
      that.$refs[formName].validate(valid => {
        if (valid) {
          if (that.coreState1.length === 2) {
            f.core1 = that.coresNum[f.nodeId] * that.cpustate[0] + f.core1
            f.core2 = that.coresNum[f.nodeId] * that.cpustate[0] + f.core2
          } else if (that.coreState2.length === 2) {
            f.core1 = that.coresNum[f.nodeId] * that.cpustate[1] + f.core1
            f.core2 = that.coresNum[f.nodeId] * that.cpustate[1] + f.core2
          } else {
            f.core1 = that.coresNum[f.nodeId] * f.cpu1 + f.core1
            f.core2 = that.coresNum[f.nodeId] * f.cpu2 + f.core2
          }
          var param = '?nodeId=' + f.nodeId + '&core1=' + f.core1 + '&core2=' + f.core2 + '&start=' + f.start + '&end=' + f.end + '&steps=' + f.steps + '&repeat=' + f.repeat
          // 发送指令
          ServerApi.cpuTest(param).then(function(response) {
            console.log('发出测试指令')
            that.disabled = true
            that.buttonText = '测试中'
            that.getRes(that.submitNumber)
            // var br = document.createElement('br')
            that.card[that.submitNumber] = '[nodeId：' + f.nodeId + '][cpu1：' + f.cpu1 + '][cpu2：' + f.cpu2 + '][core1：' + f.core1 + '][core2：' + f.core2 + '][start：' + f.start + '][end：' + f.end + '][steps：' + f.steps + '][repeat：' + f.repeat + ']'
            that.submitNumber = that.submitNumber + 1
          })
        } else {
          return false
        }
      })
    },
    generatorImage(imgNumber) {
      html2canvas(this.$refs[`capture${imgNumber}`][0]).then(canvas => {
        // this.$refs.addImage.append(canvas) // 这个没有加，看其他的文章有，但是我加上会有重复出图的问题
        const link = document.createElement('a')
        link.href = canvas.toDataURL()
        link.setAttribute('download', '核间通讯下载.png')
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
      })
    },
    deleteImage(imgNumber) {
      this.$confirm(`确认是否删除?`, '删除', {
        confirmButtonText: '确定'
      }).then(() => {
        const res = this.items.splice(imgNumber, 1)
        let msg = `删除成功`
        if (!res) {
          msg = `删除失败`
        }
        this.$message({
          type: 'info',
          message: msg
        })
      })
    },

    handleGroup(item) {
      if (item.length === 1) {
        this.form.cpu1 = item[0]
      } else if (item.length === 2) {
        this.form.cpu2 = item[1]
      }
    },
    handleGroup1(item) {
      if (item.length === 1) {
        this.form.core1 = item[0]
      } else if (item.length === 2) {
        this.form.core1 = item[0]
        this.form.core2 = item[1]
      }
      this.choose2_max = 2 - item.length
      if (this.choose2_max === 0) {
        this.group[1] = true
      } else {
        this.group[1] = false
      }
    },
    handleGroup2(item) {
      if (item.length === 1) {
        this.form.core2 = item[0]
      } else if (item.length === 2) {
        this.form.core1 = item[0]
        this.form.core2 = item[1]
      }
      this.choose1_max = 2 - item.length
      if (this.choose1_max === 0) {
        this.group[0] = true
      } else {
        this.group[0] = false
      }
      if (item.length < 2) {
        this.$set(this.mes, 1, true)
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both;
    overflow:hidden;
  }
  .el-card {
    /* display: flex; */
    /* flex-direction: row-reverse; */
    margin:auto;
    position:relative;

  }
  .el-table-filter-1 {
    background: #23C6C8;
    max-height: 100%;
    width:100%;
    overflow-y: scroll;
    text-align:center;
    margin:auto;
  }
    .el-table-filter-2 {
    max-height: 50%;
    width:70%;
    overflow-y: scroll;
    text-align:center;
    margin:auto;
    vertical-align: middle;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
    .el-table-filter-3 {
    max-height: 50%;
    width:40%;
    overflow-y: scroll;
    text-align:center;
    margin:auto;
    vertical-align: middle;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
    .el-table-filter-4 {
    max-height: 100%;
    width:70%;
    overflow-y: scroll;
    text-align:center;
    margin:auto;
    vertical-align: middle;
    position: relative;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
    .form {
    width: 50%;
    margin:0 auto;
  }
.saveButtom{
  position: relative;
  margin-left: 92%;
  margin-top: 1.5%;
}
.delButtom{
  position: relative;
  margin-left: 92%;
  margin-top: 3%;
}
  .cardRight{
    position: relative;
    height: 200px;
    width: 100%;
    margin-top: 2%;
    margin-bottom: 2%;
    word-break: break-all;
    word-wrap: break-word
  }
  .chart{
  height: 200px;
  width: 100%;
  margin-top: 2%;
  margin-bottom: 2%;
}
.mes{
  margin-top: 3%;
  color:#fff;
  -webkit-animation: vibrate-1 0.3s ;
	animation: vibrate-1 0.3s ;
}
.el-button--success,.el-button--success:focus,.el-button--success.is-active, .el-button--success:active{background: #23C6C8;}
.el-button--warning,.el-button--warning:focus,.el-button--warning.is-active, .el-button--warning:active{background: #FCB065;}

.el-button--success:hover {
    background: #8cd8da;
    border-color: #8cd8da;
    color: #FFF;
}
.el-button--warning:hover {
    background: #f5d7b9;
    border-color: #f5d7b9;
    color: #FFF;
}
/* .el-card__body{
  margin-left: 0%;
} */
    .bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
</style>
