<template>
  <div class="app-container alarm-setting">
    <!--工具栏-->
    <div class="head-container">
      <el-card>
        <el-form ref="form" :model="form" :rules="rules" class="form" style="padding:20px" label-position="right" label-width="250px">
          <el-col>
            <el-form-item label="请填写方案名称：">
              <div v-show="show[0]" class="nameEdit">
                <div class="all-my-content">
                  <!-- <span style="color: #F56C6C;margin-right: 3px;">*</span> -->
                  <el-input v-model="form.name" style="width:400px">
                    <template slot="prepend">
                      方案名
                    </template>
                  </el-input>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="报警值设置：">
              <div v-show="show[0]" class="paramsEdit">
                <div class="right-form-pannel">
                  <div v-for="(indexa,i) in 2" :key="indexa" style="margin-bottom:20px">
                    <div class="detail">
                      <span>{{ firstMyRightTitle[i] }}</span>
                      <el-divider direction="vertical" />
                    </div>
                    <el-descriptions class="my-el-descriptions" :column="1" border>
                      <el-descriptions-item v-for="(index,ii) in 4" :key="indexa*4+ii">
                        <template slot="label">
                          {{ FormName[ii+i*4] }}
                        </template>
                        <div class="cell-style">
                          <el-input
                            v-model="formDataTran1[ii+ii+i*8]"
                            :placeholder="message[i+ii+i*7]"
                            @change="inputNumberIn(formDataTran1[ii+ii+i*8],formDataTran1[ii+ii+1+i*8],iconInputColor[i*4+ii],formItemData[ii+i*4],i+ii+i*7,ii+ii+i*8,ii+ii+i*8+1)"
                          />
                        </div>
                      </el-descriptions-item>
                    </el-descriptions>
                  </div>
                  <div v-for="(indexb,j) in 1" :key="indexb" style="margin-bottom:20px">
                    <div class="detail">
                      <span>{{ firstMyRightTitle[j+2] }}</span>
                      <el-divider direction="vertical" />
                    </div>
                    <el-descriptions class="my-el-descriptions" :column="1" border>
                      <el-descriptions-item v-for="(index,jj) in 5" :key="indexb*5+jj">
                        <template slot="label">
                          {{ FormName[jj+8] }}
                        </template>
                        <div class="cell-style">
                          <el-input
                            v-model="formDataTran1[jj+jj+16]"
                            :placeholder="message[jj+8]"
                            @change="inputNumberIn(formDataTran1[jj+jj+16],formDataTran1[jj+jj+17],iconInputColor[jj+8],formItemData[jj+8],jj+jj+16,jj+jj+17)"
                          />
                        </div>
                      </el-descriptions-item>
                    </el-descriptions>
                  </div>
                  <div v-for="(indexc,k) in 1" :key="indexc" style="margin-bottom:20px">
                    <div class="detail">
                      <span>{{ firstMyRightTitle[k+3] }}</span>
                      <el-divider direction="vertical" />
                    </div>
                    <el-descriptions class="my-el-descriptions" :column="1" border>
                      <el-descriptions-item v-for="(index,kk) in 2" :key="indexb*2+kk">
                        <template slot="label">
                          {{ FormName[kk+13] }}
                        </template>
                        <div class="cell-style">
                          <el-input
                            v-model="formDataTran1[kk+kk+26]"
                            :placeholder="message[kk+13]"
                            @change="inputNumberIn(formDataTran1[kk+kk+26],formDataTran1[kk+kk+27],iconInputColor[kk+13],formItemData[kk+13],kk+kk+26,kk+kk+27)"
                          />
                        </div>
                      </el-descriptions-item>
                    </el-descriptions>
                  </div>
                  <div v-for="(indexd,l) in 1" :key="indexd" style="margin-bottom:20px">
                    <div class="detail">
                      <span>{{ firstMyRightTitle[l+4] }}</span>
                      <el-divider direction="vertical" />
                    </div>
                    <el-descriptions class="my-el-descriptions" :column="1" border>
                      <el-descriptions-item v-for="(index,ll) in 4" :key="indexb*4+ll">
                        <template slot="label">
                          {{ FormName[ll+15] }}
                        </template>
                        <div class="cell-style">
                          <el-input
                            v-model="formDataTran1[ll+ll+30]"
                            :placeholder="message[ll+15]"
                            @change="inputNumberIn(formDataTran1[ll+ll+30],formDataTran1[ll+ll+31],iconInputColor[ll+15],formItemData[ll+15],ll+ll+30,ll+ll+31)"
                          />
                        </div>
                      </el-descriptions-item>
                    </el-descriptions>
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="选择生效节点：">
              <div v-show="show[0]" class="activeNodeEdit">
                <!-- <span style="color: #F56C6C;margin-right: 3px;">*</span> -->
                <div class="select-button">
                  <el-transfer
                    v-model="selectData"
                    filterable
                    :titles="nodeList"
                    :filter-method="filterMethod"
                    filter-placeholder="快速搜索节点号"
                    :button-texts="['取消选择', '确认选择']"
                    :data="nodeData"
                  />
                </div>
              </div>
            </el-form-item>
            <el-form-item label="请选择此方案状态：">
              <div v-show="show[0]" class="nameEdit">
                <div class="all-my-content">
                  <!-- <span style="color: #F56C6C;margin-right: 3px;">*</span> -->
                  <el-switch v-model="form.status" active-text="立即生效" inactive-text="不生效(暂时保存)" active-color="#409EFF" />
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-form-item>
            <div class="submitButton">
                  &nbsp;&nbsp;
              <el-button
                :loading="crud.status.cu === 2"
                :type="submitButtonType"
                :circle="circle"
                :icon="icon"
                @click="submitForm"
              >{{ buttonText }}</el-button>
              <span class="success-message">{{ successMessage }}</span>
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import crudPmAlarmScheme from '@/api/pm/pmAlarmScheme'
import CRUD, {
  presenter,
  header,
  form,
  crud
} from '@crud/crud'

const defaultForm = {
  alarmSchemeId: null,
  name: null,
  tip: '',
  detail: {},
  effectNode: null,
  status: null,
  updateTime: null,
  createTime: null
}
export default {
  name: 'PmAlarmSetting',
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: 'asdasd',
      url: 'api/pmAlarmScheme',
      idField: 'alarmSchemeId',
      sort: 'alarmSchemeId,desc',
      crudMethod: {
        ...crudPmAlarmScheme
      }
    })
  },
  data() {
    return {
      permission: {
        add: ['admin', 'pmAlarmScheme:add'],
        edit: ['admin', 'pmAlarmScheme:edit'],
        del: ['admin', 'pmAlarmScheme:del']
      },
      rules: {
        name: [{
          required: true,
          message: '方案名不能为空',
          trigger: 'blur'
        }],
        detail: [{
          required: true,
          message: '配置不能为空',
          trigger: 'blur'
        }],
        effectNode: [{
          required: true,
          message: '生效节点不能为空',
          trigger: 'blur'
        }],
        status: [{
          required: true,
          message: '状态不能为空',
          trigger: 'blur'
        }]
      },
      // 保存表单单位数据
      message: ['GB', '%', 'Kib/s', 'Kib/s',
        '摄氏度', 'W', '%', '%',
        '摄氏度', 'Ghz', '%', '%', '%',
        'Kib/s', 'Kib/s',
        'GB', '%', 'GB', '%'
      ],
      // 保存所有单位
      totalUnitMessage: ['GB', '%', 'Kib/s', 'Kib/s',
        '摄氏度', 'W', '%', '%',
        '摄氏度', 'Ghz', '%', '%', '%',
        'Kib/s', 'Kib/s',
        'GB', '%', 'GB', '%'],
      dialogVisible: false,
      show: [true, false, false],
      activeStep: 0,
      step: 0,
      tabPosition: 'left',
      // 节点使用率设置
      diskData: [],
      marks: [],
      showStop: '显示间断点',
      iconColor: [],
      iconInputColor: [],
      // 节点选择器数据
      nodeData: [],
      selectData: [],
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1
      },
      nodeList: ['在线节点', '已选取生效节点'],
      // 按钮类型控制
      submitButtonType: 'primary',
      circle: false,
      icon: '',
      buttonText: '生成报警方案',
      successMessage: '',
      input: '',
      // 表单数据
      formDataTran1: [
        []
      ],
      // 表单大标题
      firstMyRightTitle: ['磁盘', '显卡', 'CPU', '网卡', '交换区&内存'],
      // 走马灯的表单项
      FormName: ['使用', '使用率', '读速度', '写速度',
        '温度', '功耗', '显卡使用率', '显存使用率',
        '温度', '频率', '总使用率', '系统使用率', '用户使用率',
        '上传速度', '下载速度',
        '内存使用', '内存使用率', '交换区使用', '交换区使用率'],
      // 填写表单的数据
      formItemData: ['disk_used', 'disk_rate', 'disk_read_speed', 'disk_write_speed',
        'gpu_temperature', 'gpu_power_draw', 'gpu_utilization', 'memory_utilization',
        'cpu_temperature', 'cpu_frequency', 'cpu_busy', 'cpu_system', 'cpu_user',
        'network_up_speed', 'network_down_speed',
        'memory_used', 'memory_rate', 'swap_used', 'swap_rate'
      ],
      // 存放全部英文数据
      totalItems: ['disk_used', 'disk_rate', 'disk_read_speed', 'disk_write_speed',
        'gpu_temperature', 'gpu_power_draw', 'gpu_utilization', 'memory_utilization',
        'cpu_temperature', 'cpu_frequency', 'cpu_busy', 'cpu_system', 'cpu_user',
        'network_up_speed', 'network_down_speed',
        'memory_used', 'memory_rate', 'swap_used', 'swap_rate'
      ],
      // 存放全部中文数据
      totalChItems: ['使用', '使用率', '读速度', '写速度',
        '温度', '功耗', '显卡使用率', '显存使用率',
        '温度', '频率', '总使用率', '系统使用率', '用户使用率',
        '上传速度', '下载速度',
        '内存使用', '内存使用率', '交换区使用', '交换区使用率'
      ],
      Nodes: [],
      center: {
        'text-align': 'center'
      },
      pointFlag1: [],
      pointFlag2: [],
      visible: false,
      // 详细的参数数据
      paramsDetail: [],
      // 详细的节点数据
      detail: [],
      alarmItem: [],
      // 控制是否显示最大值，最小值，区间
      showMax: [],
      showMin: [],
      showRange: [],
      // 每一行颜色
      rowColors: [],
      activeFlag: []
    }
  },
  // 获取实时数据
  computed: {
    realTimedata() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
    realTimedata: {
      handler(newVal) {
        this.initNodeSelectData(newVal)
      },
      deep: true
    }
  },
  created() {
  },
  mounted() {
  },
  methods: {
    querySort() {
      this.$router.push({ path: '/alarm/alarmSetting' })
    },
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    // 初始化在衔节点
    initNodeSelectData(data) {
      this.nodeData = []
      Object.keys(data).forEach((key) => {
        if (key !== 'node_sum') {
          this.nodeData.push({
            key: key,
            label: `${key} 号节点 暂无报警方案`,
            disabled: false
          })
        }
      })
    },
    // 提交表单
    submitForm() {
      // 填入数据
      var status = null
      if (this.form.status === true) {
        status = 1
      } else {
        status = 0
      }
      this.form.detail = JSON.stringify(this.form.detail)
      if (this.form.detail.length !== 0) {
        var params = {
          'name': this.form.name,
          'status': status,
          'detail': this.form.detail,
          'effectNode': this.selectData
        }
      }
      var that = this
      // 提交表单
      if (this.buttonText !== '编辑完成') {
        var nodes = ''
        for (var j = 0; j < params.effectNode.length; j++) {
          if (j === 0) {
            nodes = params.effectNode[0]
          } else {
            nodes = nodes + ',' + params.effectNode[j]
          }
        }
        params.effectNode = nodes
        crudPmAlarmScheme.add(params).then(function(response) {
          // 添加成功之后
          that.$router.push({ path: '/alarm/pmAlarmScheme' })
          // 立马刷新数据
          that.crud.refresh()
        })
      } else {
        this.$set(params, 'alarmSchemeId', this.form.alarmSchemeId)
        crudPmAlarmScheme.edit(params).then(function(response) {
          // 编辑成功之后
          that.$router.push({ path: '/alarm/pmAlarmScheme' })
          // 立马刷新数据
          that.crud.refresh()
        })
      }
    },
    // 往input里面输入数据触发
    inputNumberIn(min, max, flag, itemName, messageId, minId, maxId) {
      var numReg = /^[0-9]*$/
      var floatReg = /^[0-9]+([.]{1}[0-9]{2})?$/
      var numRe = new RegExp(numReg)
      var floatRe = new RegExp(floatReg)
      // 二次清空数据时触发,将属性移除
      // if (min === '') {
      //   delete this.form.detail[itemName].min_value
      //   if (max !== '' & max !== undefined) {
      //     this.$set(this.form.detail, itemName, {
      //       type: 1,
      //       max_value: max
      //     })
      //   } else {
      //     delete this.form.detail[itemName]
      //   }
      // }
      // if (max === '') {
      //   if (min !== '' & min !== undefined) {
      //     this.$set(this.form.detail, itemName, {
      //       type: 0,
      //       min_value: min
      //     })
      //   } else {
      //     delete this.form.detail[itemName]
      //   }
      // }
      // 检验最小值
      if (min !== undefined & min !== '') {
        // 检验成功
        if (numRe.test(min) || floatRe.test(min)) {
          // 未填写最大值
          if (max === undefined || max === '') {
            this.$set(this.form.detail, itemName, {
              type: 0,
              min_value: min
            })
          }
          // else {
          //   if (flag === '#30B08F') {
          //     this.$set(this.form.detail, itemName, {
          //       type: 2,
          //       min_value: min,
          //       max_value: max
          //     })
          //   } else {
          //     this.$set(this.form.detail, itemName, {
          //       type: 3,
          //       min_value: min,
          //       max_value: max
          //     })
          //   }
          // }
        } else {
          // 数字检验失败
          this.$message({
            type: 'error',
            message: '请输入数字且小数点位数不超过2位',
            duration: 5000,
            showClose: true
          })
          this.$set(this.formDataTran1, minId, null)
          return false
        }
      }
      // 检验最大值
      // if (max !== undefined & max !== '') {
      //   // 检验成功
      //   if (numRe.test(max) || floatRe.test(max)) {
      //     if (min === undefined || min === '') {
      //       this.$set(this.form.detail, itemName, {
      //         type: 1,
      //         max_value: max
      //       })
      //     } else {
      //       if (flag === '#30B08F') {
      //         this.$set(this.form.detail, itemName, {
      //           type: 2,
      //           min_value: min,
      //           max_value: max
      //         })
      //       } else {
      //         this.$set(this.form.detail, itemName, {
      //           type: 3,
      //           min_value: min,
      //           max_value: max
      //         })
      //       }
      //     }
      //   } else {
      //     // 检验失败
      //     this.$set(this.formDataTran1, maxId, null)
      //     this.$message({
      //       type: 'error',
      //       message: '请输入数字且小数点位数不超过2位',
      //       duration: 5000,
      //       showClose: true
      //     })
      //     return false
      //   }
      // }
      // 最大值最小值关系判定
      // if (min !== undefined & max !== undefined & Number(min) > Number(max)) {
      //   this.$set(this.formDataTran1, minId, null)
      //   this.$set(this.formDataTran1, maxId, null)
      //   this.$message({
      //     type: 'error',
      //     message: '最小值不得大于最大值',
      //     duration: 5000,
      //     showClose: true
      //   })
      // }
    }
  }
}
</script>

<style scoped>
  .head-container .my-el-form .my-el-carousel {
    margin-top: 1%;
    margin: auto;

  }
  .alarm-setting{
    padding-bottom: 20px;
  }
  /* 节点参数设置 */
  .paramsEdit {
    height: 100%;
    position: relative;
    display: flex;
    width: 70%;
  }

  .paramsEdit .icon {
    cursor: pointer;
  }

  /* 背景样式 */
  .slider-text ::v-deep .el-slider.is-vertical .el-slider__runway {
    background-color: #2F3035;
    width: 1.2vh;
    box-shadow: 0px 6px 12px -4px rgba(255, 255, 255, 0.6);
  }

  /* 两端样式 */
   .slider-text ::v-deep .el-slider.is-vertical .el-slider__button-wrapper {
    margin: auto;
    left: -13px;
    height: 1vh;
  }

   .slider-text ::v-deep .el-slider.is-vertical .el-slider__button-wrapper .el-slider__button {
    cursor: pointer;
    width: 1.6vh;
    height: 1.6vh;
    background: #fff;
    border: solid 0px #fff;
  }

  .paramsEdit .right-form-pannel {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
    /* width: 83%; */
    /* padding: 0 30% 0 4%; */
    /* margin: auto; */
  }

  .paramsEdit .right-form-pannel .detail {
    color: #409EFF;
    font-size: 18px;
    font-weight: 700;
  }

  .paramsEdit .right-form-pannel .my-el-descriptions {
    width: 100%;
    margin-top: 1%;
  }

  .paramsEdit .right-form-pannel .my-el-descriptions ::v-deep .cell-style {
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .paramsEdit .cell-style ::v-deep .el-input {
    width: 90%;
    padding: 0 2%;
  }

  /* 节点内参数设置 */
  /* .paramsInNodeEdit{ */
  .paramsInNodeEdit .my-icon {
    margin-top: 45px;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .paramsInNodeEdit .my-icon .icon {
    width: 100%;
    position: relative;
    margin-top: 2%;
    cursor: pointer;
  }

  .paramsInNodeEdit .left-rate-select .col-1 {
    padding: 60px;
    background-color: #3B3C41;
  }

  .paramsInNodeEdit .left-rate-select .col-2 {
    height: 55vh;
    padding: 20px 0 30px 0;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .paramsInNodeEdit .left-rate-select .my-col-1 {
    position: relative;
    height: 450px;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .paramsInNodeEdit .left-rate-select .my-col-1 .text-bar-icon {
    display: block;
    margin-top: -1%;
    width: 25%;
  }

  .paramsInNodeEdit .my-el-slider {
    width: 100%;
    margin: 18px 0;
    margin-top: 30px;
  }

  .paramsInNodeEdit .my-el-slider .el-slider {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
    padding: 5px 0;
    margin: auto;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .paramsInNodeEdit .bottom-text {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
    width: fit-content;
    color: #409EFF;
    font-weight: bold;
    margin: auto;
    position: relative;
    font-size: 14px;
  }

  /* 背景样式 */
  .paramsInNodeEdit ::v-deep .el-slider.is-vertical .el-slider__runway {
    background-color: #2F3035;
    width: 15px;
    box-shadow: 0px 6px 12px -4px rgba(255, 255, 255, 0.6);
  }

  /* 两端样式 */
  .paramsInNodeEdit ::v-deep .el-slider.is-vertical .el-slider__button-wrapper {
    left: -10px;
    height: 1vh;
  }

  .paramsInNodeEdit ::v-deep .el-slider.is-vertical .el-slider__button-wrapper .el-slider__button {
    cursor: pointer;
    width: 2vh;
    height: 2vh;
    background: #fff;
    border: solid 0px #fff;
  }

  /* 断点样式 */
  .paramsInNodeEdit ::v-deep .el-slider__stop {
    border-radius: 0px;
    width: 1.2vh;
    height: 0.3vh;
    background-color: #3B3C41;
  }

  /* 文字样式 */
  .paramsInNodeEdit ::v-deep .el-slider.is-vertical .el-slider__marks-text {
    font-size: 16px;
  }

  .paramsInNodeEdit ::v-deep .el-slider__marks-text:first-of-type {
    text-align: center;
    min-width: 27px;
    top: 220px;
    left: -40%;
  }

  .paramsInNodeEdit ::v-deep .el-slider__marks-text:not(:first-of-type) {
    text-align: center;
    top: -30%;
    left: -30%;
    min-width: 27px;
  }

  /* 右边参数面板 */
  .paramsInNodeEdit .all-my-content {
    margin: auto;
    padding: 0 7% 0 3%;
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
  }

  .paramsInNodeEdit .all-my-content .detail {
    color: #409EFF;
    font-size: 16px;
    font-weight: 700;
  }

  .paramsInNodeEdit .all-my-content .icon {
    cursor: pointer;
  }

  .paramsInNodeEdit .all-my-content .my-el-descriptions {
    width: 100%;
    margin-top: 1%;
  }

  .cell-style {
    width: 100%;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
  }
   .paramsInNodeEdit .cell-style ::v-deep .el-input {
    width: 45%;
    padding: 0 2%;
  }

  /* 方案名及生效状态设置 */
  .nameEdit {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */

    /* padding: 0 12%; */
    /* margin: auto; */

    height: 100%;
    position: relative;
    /* width: 90%; */

    display: inline;
    /* margin-top: 3%; */

  }

  .nameEdit .all-my-content {
    display: flex;
    /* width: 59vw; */
    /* padding: 0 47% 0 3%; */
    width: 137%;
  }
  .nameEdit .submitButton{
     margin-top: 10%;
     display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    font-weight: bold;
    font-size: 18px;
    color: green;
  }
  .nameEdit.el-input {
    width: 95%;
  }

  .nameEdit .el-switch {
    height: 31px;
    /* margin-left: 5%; */
  }

  .nameEdit .el-switch__label {
    color: #fff;
  }

  .nameEdit .el-switch__label.is-active {
    color: #1890ff;
    font-weight: bold;
  }

  .activeNodeEdit {
    width: fit-content;
    /* width: 71%; */
    height: 100%;
    /* margin: auto; */
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
  }

  .activeNodeEdit .select-button {
    display: block;
  }

  .activeNodeEdit .select-button ::v-deep .el-transfer .el-button--primary.is-disabled {
    background-color: #7e8185;
    border-color: #fff;
  }

  .activeNodeEdit .select-button ::v-deep .el-transfer .el-button--primary {
    border-color: #fff;
  }

  .activeNodeEdit .select-button ::v-deep .el-transfer .el-transfer-panel {
    width: 355px;
  }

  .select-button ::v-deep .el-transfer .success-message {
    color: #30B08F;
  }

  .alarmSchemeStyle ::v-deep .itemName {
    display: inline;
    color: #1890ff !important;
    line-height: 24px;
    font-size: 14px;
    font-weight: bold;
  }
  .alarmSchemeStyle ::v-deep .maxStyle {
    color: #C03639 !important;
    line-height: 24px;
    font-size: 14px;
  }
  .alarmSchemeStyle ::v-deep .minStyle {
    color: #C03639 !important;
    line-height: 24px;
    font-size: 14px;
  }
  .alarmSchemeStyle ::v-deep .rangeStyle {
    color: #C03639 !important;
    line-height: 24px;
    font-size: 14px;
  }
  .alarmSchemeStyle ::v-deep .el-table__body{
    font-size: 14px;
    color: #6A7C88;
  }
  .tips{
    margin-top: 1%;
    font-weight: bold;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    font-size: 16px;
  }
  .popover-content{
    line-height: 25px;
  }
</style>
