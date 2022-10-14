<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission">
        <el-button
          slot="left"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="querySort"
        >新增</el-button>
      </crudOperation>

      <el-table
        ref="table"
        v-loading="crud.loading"
        class="alarmSchemeStyle"
        :header-cell-style="center"
        :cell-style="cellStyle"
        :row-style="tableRowStyle"
        :data="crud.data"
        size="small"
        style="width: 100%;"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column type="selection" width="55" />
        <!-- <el-table-column prop="alarmSchemeId" label="主键" /> -->
        <el-table-column prop="name" label="方案名" min-width="100%" />
        <el-table-column class="detailStyle" prop="detail" label="参数配置" min-width="70%" style="text-align: left；margin: 0 auto;">
          <template scope="scope">
            <el-popover
              placement="right"
              title="全部参数"
              trigger="hover"
            >
              <!-- {{ paramsDetail[scope.row.alarmSchemeId] }} -->
              <div v-for="(para,i) in paramsDetail[scope.row.alarmSchemeId]" :key="i" class="popover-content">
                {{ paramsDetail[scope.row.alarmSchemeId][i] }}
              </div>
              <div slot="reference">
                <div v-for="(item,i) in alarmItem[scope.row.alarmSchemeId]" :key="i">
                  <span class="itemName">{{ alarmItem[scope.row.alarmSchemeId][i]['itemName'] }} : </span>
                  <span v-if="showMax[scope.row.alarmSchemeId][i]" class="maxStyle">
                    {{ alarmItem[scope.row.alarmSchemeId][i]['max'] }}
                  </span>
                  <span v-if="showMin[scope.row.alarmSchemeId][i]" class="minStyle">
                    >{{ alarmItem[scope.row.alarmSchemeId][i]['min'] }}
                  </span>
                  <span v-if="showRange[scope.row.alarmSchemeId][i]" class="rangeStyle">
                    {{ alarmItem[scope.row.alarmSchemeId][i]['type'] }}
                  </span>
                </div>
                <div v-show="pointFlag1[scope.row.alarmSchemeId]">...</div>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="effectNode" label="生效节点" min-width="100%">
          <template scope="scope">
            <el-popover
              placement="right"
              title="全部生效节点"
              width="200"
              trigger="hover"
              :content="detail[scope.row.alarmSchemeId]"
            >
              <div slot="reference">
                <div v-for="(single,index) in Nodes[scope.row.alarmSchemeId]" :key="index">
                  {{ single }}号节点
                </div>
                <div v-show="pointFlag2[scope.row.alarmSchemeId]">...</div>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <!-- <el-tag :type="scope.row.status === '1' ? 'success' : 'info'" disable-transitions>
              {{ scope.row.status === '1' ? '当前生效':'当前不生效' }}</el-tag><br> -->
            <el-switch v-model="activeFlag[scope.row.alarmSchemeId]" active-color="#409EFF" @change="activeFlagChange(activeFlag[scope.row.alarmSchemeId],scope.row.alarmSchemeId,scope.row.detail,scope.row.effectNode,scope.row.name)" />
          </template>
        </el-table-column>
        <!-- <el-table-column
          v-if="checkPer(['admin','pmAlarmScheme:edit','pmAlarmScheme:del'])"
          label="操作"
          width="150px"
          align="center"
        >
          <template slot-scope="scope">
            <udOperation :data="scope.row" :permission="permission" />
          </template>
        </el-table-column> -->

      </el-table>
      <!--分页组件-->
      <pagination />
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
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import crudOperation from '@crud/CRUD.operation'

const defaultForm = {
  alarmSchemeId: null,
  name: null,
  detail: {},
  effectNode: null,
  status: null,
  updateTime: null,
  createTime: null
}
export default {
  inject: ['reload'],
  name: 'PmAlarmScheme',
  components: {
    pagination,
    // udOperation,
    crudOperation
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: 'asdasd',
      url: 'api/pmAlarmScheme',
      idField: 'alarmSchemeId',
      sort: 'alarmSchemeId,desc',
      optShow: { add: false, edit: true, del: true, download: true },
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
      // 滑条数据
      formSliderData: [],
      // 表单数据
      formDataTran1: [
        []
      ],
      // 存放全部英文数据
      totalItems: ['disk_used', 'disk_rate', 'disk_read_speed', 'disk_write_speed',
        'gpu_temperature', 'gpu_power_draw', 'gpu_utilization', 'memory_utilization',
        'cpu_temperature', 'cpu_frequency', 'cpu_busy', 'cpu_system', 'cpu_user',
        'network_up_speed', 'network_down_speed',
        'memory_used', 'memory_rate', 'swap_used', 'swap_rate'
      ],
      // 存放全部中文数据
      totalChItems: ['磁盘使用', '磁盘使用率', '磁盘读速度', '磁盘写速度',
        '显卡温度', '显卡功耗', '显卡使用率', '显存使用率',
        'cpu温度', 'cpu频率', 'cpu总使用率', 'cpu系统使用率', 'cpu用户使用率',
        '网卡上传速度', '网卡下载速度',
        '内存使用', '内存使用率', '交换区使用', '交换区使用率'
      ],
      // 存放旧值
      oldSlider: '',
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
      handler(newVal, oldVal) {
        this.initNodeSelectData(newVal)
      },
      deep: true
    }
  },
  created() {
    this.reload()
    // 将滑条初始为[0,0]
  },
  mounted() {
  },
  methods: {
    querySort() {
      this.$router.push({ path: '/alarm/alarmSetting' })
    },
    tableRowStyle({ row, rowIndex }) {
      const stylejson = {}
      stylejson.background = this.rowColors[row.alarmSchemeId]
      return stylejson
    },
    cellStyle({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 2) { return `text-align:left;margin:0 auto` } else {
        return `text-align:center`
      }
    },
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    // 获取数据之后执行
    [CRUD.HOOK.afterRefresh]() {
      var data = this.crud.data
      Object.keys(data).forEach((key) => {
        var nodeData = data[key]['effectNode'].split(',')
        var id = data[key]['alarmSchemeId']
        var detail = JSON.parse(data[key]['detail'])
        var status = JSON.parse(data[key]['status'])
        // 设置初始颜色
        if (status === 1) {
          this.$set(this.rowColors, id, 'rgba(188,223,215,0.3)')
          this.$set(this.activeFlag, id, true)
        } else {
          this.$set(this.activeFlag, id, false)
          this.$set(this.rowColors, id, '#fff')
        }
        var totalTran = []
        var i = 0
        this.$set(this.showMax, id, [])
        this.$set(this.showMin, id, [])
        this.$set(this.showRange, id, [])
        Object.keys(detail).forEach((key) => {
          var tran = {}
          var index = this.contains(this.totalItems, key)
          var itemName = this.totalChItems[index]
          var initName = this.totalUnitMessage[index]
          this.$set(tran, 'itemName', itemName)
          if (detail[key]['min_value'] !== undefined && detail[key]['max_value'] !== undefined) {
            this.$set(this.showMax[id], i, false)
            this.$set(this.showMin[id], i, false)
            this.$set(this.showRange[id], i, true)
            if (detail[key]['type'] === 2) {
              this.$set(tran, 'type', detail[key]['min_value'] + initName + '-' + detail[key]['max_value'] + initName)
            } else {
              this.$set(tran, 'type', '-' + detail[key]['min_value'] + initName + ' ' + +detail[key]['max_value'] + initName + '-')
            }
          } else {
            if (detail[key]['min_value'] !== undefined) {
              this.$set(tran, 'min', detail[key]['min_value'] + initName)
              this.$set(this.showMin[id], i, true)
            }
            if (detail[key]['max_value'] !== undefined) {
              this.$set(tran, 'max', detail[key]['max_value'] + initName)
              this.$set(this.showMax[id], i, true)
            }
          }
          totalTran.push(tran)
          i++
        })
        // 选取节点数超过3时显示省略号
        if (nodeData.length > 3) {
          this.$set(this.pointFlag2, id, true)
        } else {
          this.$set(this.pointFlag2, id, false)
        }
        // 参数配置超过三条时显示省略号
        if (Object.keys(detail).length > 3) {
          this.$set(this.pointFlag1, id, true)
        } else {
          this.$set(this.pointFlag1, id, false)
        }
        this.$set(this.Nodes, id, nodeData)
        this.$set(this.alarmItem, id, totalTran)
        this.detail[id] = this.Nodes[id]
        this.paramsDetail[id] = this.alarmItem[id]
        var params = []
        Object.keys(this.paramsDetail[id]).forEach((key) => {
          if (this.paramsDetail[id][key]['min'] !== undefined) {
            params.push(this.paramsDetail[id][key]['itemName'] + ': ' + this.paramsDetail[id][key]['min'])
          }
          if (this.paramsDetail[id][key]['max'] !== undefined) {
            params.push(this.paramsDetail[id][key]['itemName'] + ': ' + this.paramsDetail[id][key]['max'])
          }
          if (this.paramsDetail[id][key]['type'] !== undefined) {
            params.push(this.paramsDetail[id][key]['itemName'] + ': ' + this.paramsDetail[id][key]['type'])
          }
        })
        this.paramsDetail[id] = params
        // 节点超过3个截断
        if (this.Nodes[id].length > 3) {
          var tran = [this.Nodes[id][0], this.Nodes[id][1], this.Nodes[id][2]]
          this.$set(this.Nodes, id, tran)
        }
        // 参数配置超过3条截断
        if (this.alarmItem[id].length > 3) {
          var tran1 = [this.alarmItem[id][0], this.alarmItem[id][1], this.alarmItem[id][2]]
          this.$set(this.alarmItem, id, tran1)
        }
      })
    },
    // 返回值arrays中为obj的下标
    contains(arrays, obj) {
      var i = arrays.length
      while (i--) {
        if (arrays[i] === obj) {
          return i
        }
      }
      return false
    },
    // 编辑
    [CRUD.HOOK.afterToEdit]() {
      // console.log(JSON.parse(this.form.detail))
      var detail = JSON.parse(this.form.detail)
      var node = JSON.parse(this.form.effectNode)
      Object.keys(detail).forEach((key) => {
      })
      if (this.form.status === '1') {
        this.form.status = true
      } else {
        this.form.status = false
      }
      this.form.detail = detail
      this.selectData = node
      this.$router.push({ path: '/alarm/alarmSetting' })
      this.buttonText = '编辑完成'
    },
    // 重置表单
    resetForm() {
      // 表单数据重置
      this.formSliderData = []
      this.formDataTran1 = [[]]
      // 滑条标记点清零
      this.marks = []
      // 节点选择器
      this.selectData = []
      // 提交按钮重置
      this.submitButtonType = 'primary'
      this.circle = false
      this.icon = ''
      this.buttonText = '生成报警方案'
      this.successMessage = ''
    },
    initNodeSelectData(data) {
      this.nodeData = []
      Object.keys(data).forEach((key) => {
        if (key !== 'node_sum') {
          this.nodeData.push({
            key: key,
            label: `${key} 号节点`,
            disabled: false
          })
        }
      })
    },

    reload() {
      this.isRouterAlive = false // 先关闭，
      this.$nextTick(function() {
        this.isRouterAlive = true // 再打开
      })
    },
    activeFlagChange(flag, id, detail, effectNode, name) {
      if (flag === true) {
        this.$set(this.rowColors, id, 'rgba(188,223,215,0.3)')
        flag = 1
      } else {
        flag = 0
        this.$set(this.rowColors, id, '#fff')
      }
      var params = {
        'name': name,
        'status': flag,
        'detail': detail,
        'effectNode': effectNode
      }
      var that = this
      this.$set(params, 'alarmSchemeId', id)
      crudPmAlarmScheme.edit(params).then(function(response) {
        // 立马刷新数据
        that.crud.refresh()
      })
    }
  }
}

</script>

<style scoped>
  .head-container .my-el-dialog .my-el-carousel {
    margin-top: 1%;
    margin: auto;

  }

  /* 节点参数设置 */
  .paramsEdit {
    height: 100%;
    position: relative;
    display: flex;
    width: 100%;
  }

  .paramsEdit .icon {
    cursor: pointer;
  }

  .paramsEdit .left-music-pannel {
    background-color: #3B3C41;
    /* fallback for old browsers */
    width: 40%;
    padding: 10px;
  }

  .paramsEdit .left-music-pannel .title-text {
    margin-top: 5%;
    text-align: center;
    color: #fff;
    font-size: 1.1rem;
    font-weight: 700;
  }

  .paramsEdit .left-music-pannel .first-row {
    margin-top: 5%;
  }

  /* 公共属性 */
  .paramsEdit .left-music-pannel .el-row {
    height: 230px;
  }

  .left-music-pannel .el-row div {
    display: flex;
  }

  .left-music-pannel .el-row .my-el-slider {
    width: 100%;
  }

  .left-music-pannel .el-row .slider-text {
    display: block;
    width: 100%;
  }

  .left-music-pannel .el-row .slider-text .my-icon {
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .left-music-pannel .el-row .slider-text .my-icon .icon {
    width: 100%;
    position: relative;
    margin-top: 2%;
    cursor: pointer;
  }

  .left-music-pannel .el-row .slider-text .my-el-slider {
    width: 100%;
    display: flex;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
  }

  .left-music-pannel .el-row .slider-text .my-el-slider .el-slider {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
    padding: 10px 0;
    margin: auto;
  }

  .left-music-pannel .el-row .slider-text .bottom-text {
    width: fit-content;
    line-height: 1.8vh;
    margin: auto;
    position: relative;
    font-size: 14px;
    font-weight: bold;
    color: #409EFF;

  }

  /* 背景样式 */
  .slider-text ::v-deep .el-slider.is-vertical .el-slider__runway {
    background-color: #2F3035;
    width: 1.2vh;
    box-shadow: 0px 6px 12px -4px rgba(255, 255, 255, 0.6);
  }

  /* 拉条样式 */
  .left-music-pannel .el-row .slider-text ::v-deep .el-slider.is-vertical .el-slider__bar {
    background: linear-gradient(to top, #24c6dc, #514a9d);
    width: 1.2vh;
    left: 0vh;
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

  /* 断点样式 */
  .left-music-pannel .el-row .slider-text ::v-deep .el-slider__stop {
    border-radius: 0px;
    width: 1.2vh;
    height: 0.3vh;
    background-color: #3B3C41;
  }

  .paramsEdit .right-form-pannel {
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
    width: 60%;
    padding: 0 4% 0 4%;
    margin: auto;
  }

  .paramsEdit .right-form-pannel .detail {
    color: #409EFF;
    font-size: 1.1rem;
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
    width: 45%;
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
    /* fallback for old browsers */
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

  /* 拉条样式 */
  .paramsInNodeEdit ::v-deep .el-slider.is-vertical .el-slider__bar {
    background: linear-gradient(to top, #24c6dc, #514a9d);
    width: 15px;
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
    font-size: 1.1rem;
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
    height: 100%;
    display: flex;
    margin-top: 3%;
    align-items: center;
    /*定义body的元素垂直居中*/
    justify-content: center;
    /*定义body的里的元素水平居中*/
    word-break: keep-all;
    /* 不换行 */
    white-space: nowrap;
    /* 不换行 */
  }

  .nameEdit .all-my-content {
    display: flex;
    width: 30vw;
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
    width: 50%;
  }

  .nameEdit .el-switch {
    height: 31px;
    margin-left: 5%;
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
    height: 100%;
    margin: auto;
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
    width: 400px;
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
    /* display: flex; */
    color: #C03639 !important;
    line-height: 24px;
    font-size: 14px;
  }
  .alarmSchemeStyle ::v-deep .minStyle {
    /* display: flex; */
    color: #C03639 !important;
    line-height: 24px;
    font-size: 14px;
  }
  .alarmSchemeStyle ::v-deep .rangeStyle {
    /* display: flex; */
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
  /* .head-containerv ::v-deep .el-table__row{
      text-align: left;
  } */
</style>
