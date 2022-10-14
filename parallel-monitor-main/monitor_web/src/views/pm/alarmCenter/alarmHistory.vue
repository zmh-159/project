
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- <div v-if="crud.props.searchToggle">
        <el-date-picker
          v-model="Times"
          type="datetimerange"
          class="filter-item"
          align="right"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
          @change="timeChange"
        />
      </div> -->
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->

      <crudOperation :permission="permission">
        <el-button
          slot="left"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-edit"
          @click="dialogVisible = true"
        >一键处理</el-button>
      </crudOperation>

      <el-table
        ref="table"
        v-loading="crud.loading"
        highlight-current-row
        :data="crud.data"
        @selection-change="crud.selectionChangeHandler"
        @sort-change="sortChange"
        @filter-change="filterChange"
        @row-click="handdle"
      >
        <el-table-column type="selection" />
        <!-- <el-table-column prop="alarmId" label="报警编号" /> -->
        <el-table-column label="报警编号">
          <template slot-scope="scope">
            <!-- {{ (scope.$index+1)+(page.page-1)*page.size }} -->
            {{ scope.$index+1 }}
          </template>
        </el-table-column>
        <!-- <el-table-column prop="alarmSchemeId" label="方案名" /> -->
        <el-table-column prop="nodeId" label="节点编号" />
        <!-- <el-table-column prop="alarmOption" label="报警项" /> -->
        <el-table-column prop="alarmOption" label="报警项">
          <template slot-scope="scope">
            <span v-if="scope.row.alarmOption == 'cpu_user_avg'">cpu平均用户使用率</span>
            <span v-if="scope.row.alarmOption == 'cpu_busy_avg'">cpu平均总使用率</span>
            <span v-if="scope.row.alarmOption == 'gpu_temperature_avg'">显卡平均温度</span>
            <span v-if="scope.row.alarmOption == 'cpu_system_avg'">cpu平均系统使用率</span>
            <span v-if="scope.row.alarmOption == 'cpu_temperature_avg'">cpu平均温度</span>
            <span v-if="scope.row.alarmOption == 'cpu_temperature_avg'">cpu平均温度</span>

            <span v-if="scope.row.alarmOption == 'swap_used'">交换区使用率</span>
            <span v-if="scope.row.alarmOption == 'swap_rate'">交换区使用率</span>
            <span v-if="scope.row.alarmOption == 'disk_rate'">磁盘使用率</span>
            <span v-if="scope.row.alarmOption == 'memory_utilization_avg'">显卡平均使用率</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="key===status"
          prop="status"
          label="是否处理"
          column-key="aStatus"
          :filters="[{ text: '已处理', value: '1' }, { text: '未处理', value: '0' }]"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status=='1'" type="success">已处理</el-tag>
            <el-tag v-if="scope.row.status=='0'" type="danger">未处理</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报警时间" />
        <el-table-column prop="remark" label="备注" @row-click="handdle" />
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="dialogVisible1 = true;remCheck(scope.row)"> 处理</el-button>&nbsp;
            <el-button type="primary" @click="dialogVisible = true;clickTime(scope.row)"> 查看详情</el-button>
          </template>
        </el-table-column>
        <!-- 'gpu_temperature_avg': '显卡平均温度',
        'gpu_power_draw_avg': '显卡平均功耗',
        'gpu_power_draw_total': '显卡总功耗',
        'gpu_utilization_avg': '显卡平均使用率',
        'cpu_system_avg': 'cpu平均系统使用率',
        'cpu_user_avg': 'cpu平均用户使用率',
        'cpu_busy_avg': 'cpu平均总使用率',
        'cpu_frequency_avg': 'cpu平均频率',
        'cpu_temperature_avg': 'cpu平均温度',
        'network_down_speed_total': '网卡总下载速度',
        'network_up_speed_total': '网卡总上传速度',
        'memory_rate': '内存使用率',
        'memory_used': '内存使用',
        'memory_utilization_avg': '显存平均使用率',
        'disk_used': '磁盘使用',
        'disk_rate': '磁盘使用率',
        'disk_write_speed_total': '磁盘总写速度',
        'disk_read_speed_total': '磁盘总读速度',
        'swap_rate': '交换区使用率',
        'swap_used': '交换区使用' -->
        <el-table-column type="expand">
          <template slot-scope="scope">
            <div class="questionDetail">
              <h4 class="questionList title">
                <span>{{ JSON.parse(scope.row.detail) }}</span>
              </h4>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <el-pagination
        :page-size.sync="page.size"
        :total="page.total"
        :current-page.sync="page.page"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="crud.sizeChangeHandler($event)"
        @current-change="crud.pageChangeHandler"
      />
    </div>
    <!-- 弹出框显示 -->
    <div>
      <el-dialog
        title="备注填写"
        :visible.sync="dialogVisible1"
        width="30%"
        :before-close="handleClose1"
      >
        <span><el-input v-model="input" placeholder="请输入备注内容" /></span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible1 = false;submitInput()">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <div>
      <el-dialog
        title="报警时间显示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose"
      >
        <div><timeline :timeline-list="dongtai" /></div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false;arrayCancel()">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false;arrayCancel()">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <!-- <div><el-button type="primary" @click="clickTime()">主要按钮</el-button></div> -->
  </div>
</template>

<script>
import crudPmAlarm from '@/api/pm/pmAlarm'
import timeline from '@/components/Echarts/Timeline'
// import crudPmAlarmScheme from '@/api/pm/pmAlarmScheme'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operationAlarm'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operationAlarm'
// import pagination from '@crud/Pagination'
import { pagination } from '@crud/crud'

const defaultForm = { alarmId: null, alarmSchemeId: null, nodeId: null, detail: null, status: null, remark: null, createTime: null }
export default {
  inject: ['reload'],
  name: 'PmAlarm',
  components: { crudOperation, timeline },
  mixins: [pagination(), presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'alarmApi', url: 'api/pmAlarm', idField: 'alarmId', sort: 'alarmId,desc', optShow: { add: false, edit: false, del: false, download: true, reset: false }, crudMethod: { ...crudPmAlarm }})
  },
  data() {
    return {
      alarmId: '',
      input: '',
      dongtai: [],
      Times: [],
      dialogVisible: false,
      dialogVisible1: false,
      checkAll: false,
      checkedAlarm: [],
      isIndeterminate: true,
      alarmDetail: [],
      concentDetail: [],
      abnormalDetail: [],
      typeDetail: [],
      maxDetail: [],

      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      permission: {
        add: ['admin', 'pmAlarm:add'],
        edit: ['admin', 'pmAlarm:edit'],
        del: ['admin', 'pmAlarm:del']
      },
      queryTypeOptions: [
        { key: 'alarmId', display_name: '报警编号' },
        { key: 'alarmSchemeId', display_name: '报警方案编号' },
        { key: 'nodeId', display_name: '节点编号' },
        { key: 'detail', display_name: '详细报警项' },
        { key: 'status', display_name: '是否已读' },
        { key: 'createTime', display_name: '创建时间' }
      ]
    }
  },
  computed: {
  },
  watch: {

  },
  mounted() {
    this.handleCheck()
  },
  methods: {
    submitInput() {
      var that = this
      var data = '?alarm_id=' + this.alarmId + '&remark=' + this.input
      crudPmAlarm.remarkPm(data).then(function(response) {
        that.reload
      })
    },
    remCheck(row) {
      this.alarmId = row.alarmId
    },
    arrayCancel() {
      this.dongtai = []
    },
    clickTime(rowData) {
      var len = rowData.alarmDetails.length
      for (var i = 0; i < len; i++) {
        this.dongtai.push(rowData.alarmDetails[i])
      }
    },
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    [CRUD.HOOK.afterRefresh]() {
      for (var i in this.crud.data) {
        var detail = JSON.parse(this.crud.data[i].detail)
        var alarmNum = 0
        for (var j in detail) {
          alarmNum = alarmNum + Object.getOwnPropertyNames(detail[j]).length
        }

        this.$set(this.crud.data[i], 'alarmNum', alarmNum)
      }
      // this.handleCheck()
      return true
    },
    timeChange() {
      if (this.value1 !== null) {
        var range = []
        range[0] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[0])
        range[1] = this.dateFormat('YYYY-mm-dd HH:MM:SS', this.value1[1])
        var that = this
        crudPmAlarm.queryTime(this.alarmId, range).then(function(response) {
          that.crud.data = response.content
          console.log('我的作业')
          console.log(this.crud.data)
          if (that.cpudata.length !== 0) {
            const gaptime = that.gaptime(range[0], that.cpudata[that.cpudata.length - 1].updateTime)
            if (gaptime > 0) {
              that.sendMessage2()
            }
            that.initchart1(that.cpudata)
          } else {
            that.resetArray(that.chartX1, that.chartY1)
            that.sendMessage()
          }
          that.show1 = true
        })
      } else { // 清空则恢复默认时间
        var tthat = this
        crudPmAlarm.query(tthat.alarmId).then(function(response) {
          tthat.crud.data = response.crud.data
        })
      }
    },
    // 查看详情
    handleCheck(row) {
      var detail = JSON.parse(row.detail)
      for (var k1 in detail) {
        var alarmDetail = detail[k1]
        var moldDetail = Object.keys(detail[k1])[0]
        console.log(moldDetail)
        for (var k2 in alarmDetail) {
          var i = 0
          var concentDetail = alarmDetail[k2]
          var abnormalDetail = concentDetail['abnormal']
          var typeDetail = concentDetail['policy']['type']
          var maxDetail = concentDetail['policy']['max_value']
          console.log(i)
          console.log(concentDetail)
          console.log(abnormalDetail)
          console.log(typeDetail)
          console.log(maxDetail)
          // return(moldDetail.value + typeDetail.value + maxDetail)
          if (typeDetail === '0') {
            typeDetail.value = '大于'
          } else if (typeDetail === '1') {
            typeDetail.value = '小于'
          } else if (typeDetail === '2') {
            typeDetail.value = '区间内'
          } else if (typeDetail === '3') {
            typeDetail.value = '区间外'
          }
          if (moldDetail === 'disk_read_speed') {
            moldDetail.value = '磁盘读速度'
          } else if (moldDetail === 'disk_write_speed') {
            moldDetail.value = '磁盘写速度'
          } else if (moldDetail === 'cpu_temperature') {
            moldDetail.value = 'cpu温度'
          } else if (moldDetail === 'cpu_user') {
            moldDetail.value = 'cpu用户使用率'
          } else if (moldDetail === 'cpu_system') {
            moldDetail.value = 'cpu系统使用率'
          } else if (moldDetail === 'cpu_frequency') {
            moldDetail.value = 'cpu频率'
          } else if (moldDetail === 'cpu_busy') {
            moldDetail.value = 'cpu总使用率'
          } else if (moldDetail === 'memory_utilization') {
            moldDetail.value = '显存使用率'
          } else if (moldDetail === 'gpu_utilization') {
            moldDetail.value = '显卡使用率'
          } else if (moldDetail === 'gpu_temperature') {
            moldDetail.value = '显卡温度'
          } else if (moldDetail === 'gpu_power_draw') {
            moldDetail.value = '显卡功耗'
          } else if (moldDetail === 'network_down_speed') {
            moldDetail.value = '网卡下载速度'
          } else if (moldDetail === 'network_up_speed') {
            moldDetail.value = '网卡上传速度'
          } else if (moldDetail === 'gpu_temperature_avg') {
            moldDetail.value = '显卡平均温度'
          } else if (moldDetail === 'cpu_system_avg') {
            moldDetail.value = 'cpu平均系统使用率'
          } else if (moldDetail === 'network_down_speed_total') {
            moldDetail.value = '网卡总下载速度'
          } else if (moldDetail === 'cpu_frequency_avg') {
            moldDetail.value = 'cpu平均频率'
          } else if (moldDetail === 'memory_used') {
            moldDetail.value = '内存使用'
          } else if (moldDetail === 'disk_rate') {
            moldDetail.value = '磁盘使用率'
          } else if (moldDetail === 'disk_read_speed_total') {
            moldDetail.value = '磁盘总读速度'
          } else if (moldDetail === 'cpu_user_avg') {
            moldDetail.value = 'cpu平均用户使用率'
          } else if (moldDetail === 'network_up_speed_total') {
            moldDetail.value = '网卡总上传速度'
          } else if (moldDetail === 'memory_rate') {
            moldDetail.value = '内存使用率'
          } else if (moldDetail === 'disk_write_speed_total') {
            moldDetail.value = '磁盘总写速度'
          } else if (moldDetail === 'gpu_power_draw_avg') {
            moldDetail.value = '显卡平均使用率'
          } else if (moldDetail === 'gpu_power_draw_avg') {
            moldDetail.value = '显卡平均功耗'
          } else if (moldDetail === 'disk_used') {
            moldDetail.value = '磁盘使用'
          } else if (moldDetail === 'cpu_busy_avg') {
            moldDetail = 'cpu平均总使用率'
          } else if (moldDetail === 'memory_utilization_avg') {
            moldDetail.value = '显存平均使用率'
          } else if (moldDetail === 'swap_rate') {
            moldDetail.value = '交换区使用率'
          } else if (moldDetail === 'cpu_temperature_avg') {
            moldDetail.value = 'cpu平均温度'
          } else if (moldDetail === 'gpu_power_draw_total') {
            moldDetail.value = '显卡总功耗'
          } else if (moldDetail === 'swap_used') {
            moldDetail.value = '交换区使用'
          }
        }
      }
      const $table = this.$refs.table
      $table.toggleRowExpansion(row)
      $table.toggleRowSelection(row)
    }
    // handleClose1(done) {
    //   this.$confirm('确认关闭？')
    //     .then(_ => {
    //       done()
    //     })
    //     .catch(_ => {})
    // }
  },
  // 关闭后
  handleClose(done) {
    this.$confirm('确认关闭？')
      .then(_ => {
        done()
      })
      .catch(_ => {})
    this.dongtai = []
  },
  CheckAllChange(val) {
    this.checkedAlarm = val ? crud.data.alarmSchemeId : []
    this.isIndeterminate = false
  },
  CheckedAlarmChange(value) {
    const checkedCount = value.length
    this.checkAll = checkedCount === this.crud.data.alarmSchemeId.length
    this.isIndeterminate = checkedCount > 0 && checkedCount < this.crud.data.alarmSchemeId.length
  },
  // 删除
  // alarmDel(index, row) {
  //   this.$confirm('永久删除该条数据, 是否继续?', '提示', {
  //     confirmButtonText: '确定',
  //     cancelButtonText: '取消',
  //     type: 'warning'
  //   })
  //     .then(() => {
  //       this.crud.data.splice(index, 1)
  //       const rowId = row.alarmId
  //       console.log(rowId)
  //       crudPmAlarm.del([rowId]).then(res => {
  //         if (res.data) {
  //           this.$message({
  //             message: '删除成功',
  //             type: 'success'
  //           })
  //         }
  //       })
  //     })
  // },
  // 全局排序
  sortChange(column) {
    this.crud.data = []
    if (column) {
      if (column.order === 'ascending' && column.prop === 'createTime') {
        this.crud.data.sort = 'createTime,asc'
      } else if (column.order === 'descending' && column.prop === 'createTime') {
        this.crud.data.sort = 'createTime,desc'
      }
      crudPmAlarm({ ...this.crud.data }).then(response => {
        this.crud.data = response.frequency.content
      })
    }
  },
  // 全局筛选
  filterChange(filters) {
    const _this = this
    if (filters.aStatus.length > 0) {
      _this.status = filters.aStatus[0]
    } else {
      _this.status = undefined
    }
    // this.crud.toQuery
  }
  // export2Excel() {
  //   const el = this.$refs.table.$el
  //   const filename = 'alarm.xlsx'
  //   const wb = XLSX.utils.table_to_book(el)
  //   const wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
  //   try {
  //     FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), filename)
  //   } catch (e) {
  //     console.log(e)
  //   }
  //   return wbout
  // }
}
</script>

<style rel="stylesheet/scss" lang="scss" >
    // .el-popover {
    //     left: 25%;
    //     width: 15%;
    //     height: 15%;
    //     overflow: auto;
    //     position: absolute;
    //     background-color:	#FAF0E6;
    // }
    .institutionName {
        margin-top: 10px;
        text-align: right;
    }
    // .text {
    //     width: 200px;
    //     white-space: nowrap;
    //     text-overflow: ellipsis;
    //     overflow: hidden;
    //     color: #ff0000;
    //     cursor: pointer;  /*鼠标悬停变小手*/
    // }
    .questionDetail .questionList {
        padding: 10px;
        border-bottom: 1px dotted #dcdfe6;
    }
    .questionDetail span {
        display: inline-block;
    }
    .left {
        padding: 5px
    }
    .right {
        margin-right: 10px;
    }
    .fgsPosition {
        text-align: right;
    }
    .title {
        font-size: 16px;
        font-weight: normal;
    }
    .el-table__expand-column .cell {
      display: none;
    }

</style>
