<template>
  <div class="app-container pmNode">
    <!--工具栏-->
    <div class="head-container">
      <!-- <el-tabs v-model="activeName" class="pmnodelist" @tab-click="handleClick">
        <el-tab-pane label="列表模式" name="first"> -->
      <div v-if="activeName==='first'">
        <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
        <crudOperation :permission="permission" :search-options="searchOptions" />
        <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
          <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="ip地址" prop="ip">
              <el-input v-model="form.ip" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="cpu数量">
              <el-input v-model="form.cpuNums" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="算力">
              <el-input v-model="form.performance" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="最大内存容量">
              <el-input
                v-model="form.maxExtendMemory"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="内存">
              <el-input
                v-model="form.memory"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="内存插槽">
              <el-input
                v-model="form.memorySlot"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="操作系统">
              <el-input
                v-model="form.osName"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="系统版本">
              <el-input
                v-model="form.osVersion"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="位数">
              <el-input
                v-model="form.osBit"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="主机名">
              <el-input
                v-model="form.hostName"
                style="width: 370px;"
              />
            </el-form-item>
            <el-form-item label="可用磁盘">
              <el-input
                v-model="form.diskTotal"
                style="width: 370px;"
              />
            </el-form-item>
          </el-form>
          <div
            slot="footer"
            class="dialog-footer"
          >
            <el-button
              type="text"
              @click="crud.cancelCU"
            >取消</el-button>
            <el-button
              :loading="crud.status.cu === 2"
              type="primary"
              @click="crud.submitCU"
            >确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table
          ref="table"
          v-loading="crud.loading"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}"
          :data="crud.data"
          size="small"
          style="width: 100%;"
          :row-class-name="tableRowClassName"
          @cell-mouse-enter="myEnter"
          @cell-click="goTO"
          @selection-change="crud.selectionChangeHandler"
        >
          <!-- <el-table-column
          type="selection"
          width="55"
        /> -->
          <el-table-column prop="serialNumber" label="节点序号" />
          <el-table-column
            prop="ip"
            label="IP地址"
            width="150px"
            show-overflow-tooltip
          />
          <el-table-column
            prop="cpuNums"
            label="CPU数量"
          />
          <!-- <el-table-column
          prop="performance"
          label="算力"
          width="110px"
        /> -->
          <!-- <el-table-column
          prop="maxExtendMemory"
          label="最大内存容量"
          width="100px"
        /> -->
          <el-table-column
            prop="memory"
            label="内存"
          />
          <!-- <el-table-column
          prop="memorySlot"
          label="内存插槽"
          width="70px"
        /> -->
          <el-table-column
            prop="osName"
            label="操作系统"
            show-overflow-tooltip
          />
          <!-- <el-table-column
          prop="osVersion"
          label="系统版本"
          width="70px"
        /> -->
          <!-- <el-table-column
          prop="osBit"
          label="位数"
          width="50px"
        /> -->
          <el-table-column
            prop="hostName"
            label="主机名"
            show-overflow-tooltip
          />
          <el-table-column
            prop="diskTotal"
            label="可用磁盘"
            show-overflow-tooltip
          />
          <el-table-column prop="memoryUsed" label="内存使用率">
            <template scope="scope">
              <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.memoryUsed" :color="customColorMethod" />
            </template>
          </el-table-column>
          <el-table-column prop="cpuUsed" label="CPU使用率">
            <template scope="scope">
              <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.cpuUsed" :color="customColorMethod" />
            </template>
          </el-table-column>
          <el-table-column prop="swapUsed" label="交换区使用率">
            <template scope="scope">
              <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.swapUsed" :color="customColorMethod" />
            </template>
          </el-table-column>
          <el-table-column prop="gpuUsed" label="显卡使用率">
            <template scope="scope">
              <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.gpuUsed" :color="customColorMethod" />
            </template>
          </el-table-column>
          <el-table-column prop="networkUpSpeed" label="上传网速" show-overflow-tooltip>
            <template scope="scope">
              <span :style="myNetworkUpSpeedColorStyle[scope.row.nodeId]">{{ scope.row.networkUpSpeed }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="networkDownSpeed" label="下载网速" show-overflow-tooltip>
            <template scope="scope">
              <span :style="myNetworkDownSpeedColorStyle[scope.row.nodeId]">{{ scope.row.networkDownSpeed }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
          />
          <el-table-column
            prop="startTime"
            label="开机时间"
            width="200px"
          />
          <!-- <el-table-column
          prop="createTime"
          label="创建时间"
          width="140px"
        /> -->
          <!-- <el-table-column prop="updateTime" label="修改时间" /> -->
          <el-table-column
            v-if="checkPer(['admin','pmNode:webssh'])"
            fixed="right"
            label="操作"
          >
            <template slot-scope="scope">
              <el-button
                type="primary"
                @click="open(scope.row)"
              >webssh</el-button>
            </template>
          </el-table-column>
          <!-- <el-table-column v-if="checkPer(['admin','pmNode:edit','pmNode:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column> -->
        </el-table>
        <!--分页组件-->
        <pagination />
        <el-dialog
          :visible.sync="dialogFormVisible"
          title="登录"
          width="450px"
        >
          <el-form>
            <el-form-item label="用户名">
              <el-input
                v-model="loginData.usrname"
                placeholder="请输入用户名"
                clearable
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="loginData.password"
                placeholder="请输入密码"
                show-password
                clearable
              />
            </el-form-item>
            <el-form-item label="端口号">
              <el-input v-model="loginData.port" />
            </el-form-item>
          </el-form>
          <div
            slot="footer"
            class="dialog-footer"
          >
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button
              type="primary"
              @click="login"
            >确 定</el-button>
          </div>

        </el-dialog>
      </div>
      <!-- </el-tab-pane>
        <el-tab-pane label="矩形图模式" name="second">
          <nodeTreeChart v-if="activeName==='second'" />
        </el-tab-pane>
      </el-tabs> -->
    </div>
  </div>
</template>

<script>

import crudPmNode from '@/api/pm/pmNode'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import nodeTreeChart from '@/views/pm/nodeTreeChart'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
const defaultForm = { nodeId: null, uuid: null, ip: null, cpuNums: null, performance: null, maxExtendMemory: null, memory: null, memorySlot: null, osName: null, osVersion: null, osBit: null, hostName: null, diskTotal: null, remark: null, number: null, startTime: null, createTime: null, updateTime: null }
export default {
  name: 'PmNode',
  components: { pagination, nodeTreeChart, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: 'pm_node',
      url: 'api/pmNode',
      idField: 'nodeId',
      sort: 'updateTime,desc',
      crudMethod: { ...crudPmNode },
      optShow: {
        add: false,
        edit: false,
        delete: false,
        download: true
      }
    })
  },
  props: {
    nodeId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      activeName: 'first',
      node1: '',
      l: '',
      isStart: false,
      colors: [],
      datanode: null,
      dialogFormVisible: false,
      loginData: {
        password: '',
        usrname: '',
        port: 22,
        nodeId: ''
      },
      permission: {
        add: ['admin', 'pmNode:add'],
        edit: ['admin', 'pmNode:edit'],
        del: ['admin', 'pmNode:del']
      },
      rules: {
        ip: [
          { required: true, message: 'ip地址不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'nodeId', display_name: '主键' },
        { key: 'ip', display_name: 'ip地址' },
        { key: 'cpuNums', display_name: 'cpu数量' },
        { key: 'maxExtendMemory', display_name: '最大扩展的内存容量' },
        { key: 'memory', display_name: '内存' },
        { key: 'memorySlot', display_name: '内存插槽' },
        { key: 'osName', display_name: '操作系统' },
        { key: 'osVersion', display_name: '系统版本' },
        { key: 'diskTotal', display_name: '可用磁盘' },
        { key: 'remark', display_name: '备注' }
      ],
      myNetworkUpSpeedColorStyle: [],
      myNetworkDownSpeedColorStyle: [],
      onLineData: [],
      searchOptions: [{ label: '节点序号', value: 'serialNumber' }, { label: 'IP地址', value: 'ip' }]
    }
  },
  computed: {
    data() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
    data: {
      handler(newVal, oldVal) {
        console.log(newVal)
        if (newVal !== undefined) {
          var tran = []
          Object.keys(newVal).forEach(key => {
            tran.push(parseInt(key))
          })
          this.onLineData = tran
          this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
        }
      },
      deep: true
    },
    nodeId: {
      handler(val) {
        if (val !== null) {
          this.$refs.table.columns[0].filteredValue = val
          this.crud.query = { nodeId: val }
          this.crud.toQuery()
        } else {
          this.crud.query = {}
          this.crud.toQuery()
        }
      }
    }
  },
  methods: {
    [CRUD.HOOK.afterRefresh]() {
      this.handleRealTimeData(this.data, [])
    },
    goTO(row, column) {
      // 获取到了当前行的uuid
      var uuid = row.nodeId
      var data = column['label']
      if (data !== '操作') {
        this.$router.push({
          path: '/Center/newNodesDetail',
          query: {
            nodeid: uuid
          }
        })
      }
    },
    open(data) {
      this.dialogFormVisible = true
      this.loginData.nodeId = data.nodeId
    },
    login() {
      this.dialogFormVisible = false
      const webhref = this.$router.resolve({
        name: 'websshdash'
      })
      localStorage.setItem('loginData', JSON.stringify(this.loginData))
      window.open(webhref.href, '_blank')
    },
    handleRealTimeData(data, oldData) {
      // var that = this
      for (var i = 0; i < this.crud.data.length; ++i) {
        // 保存新旧内存使用率
        var MemoryUsed = 0
        // 保存新旧CPU使用率
        var CpuUsed = 0
        // 保存新旧交换区使用率
        var SwapUsed = 0
        // 保存新旧显卡使用率
        var GpuUsed = 0
        // 保存新旧上传网速
        var oldnetworkUpSpeed = 0
        var newnetworkUpSpeed = 0
        // 保存新旧下载网速
        var oldnetworkDownSpeed = 0
        var newnetworkDownSpeed = 0
        var nodeId = this.crud.data[i].nodeId
        var tran = data[nodeId]
        if (tran !== undefined) {
          var that = this
          MemoryUsed = tran['memory_rate']
          CpuUsed = tran['cpu_busy']
          SwapUsed = tran['swap_rate']
          GpuUsed = tran['gpu_utilization']
          newnetworkUpSpeed = tran['up_speed']
          newnetworkDownSpeed = tran['down_speed']
          // 控制样式
          if (Object.keys(oldData).length !== 0) {
            oldnetworkUpSpeed = oldData[nodeId]['up_speed']
            oldnetworkDownSpeed = oldData[nodeId]['down_speed']
            // 上传网速颜色
            if (newnetworkUpSpeed >= oldnetworkUpSpeed) {
              that.$set(that.myNetworkUpSpeedColorStyle, tran['node_id'], { color: 'red' })
            } else {
              that.$set(that.myNetworkUpSpeedColorStyle, tran['node_id'], { color: 'green' })
            }
            // 下载网速颜色
            if (newnetworkDownSpeed >= oldnetworkDownSpeed) {
              that.$set(that.myNetworkDownSpeedColorStyle, tran['node_id'], { color: 'red' })
            } else {
              that.$set(that.myNetworkDownSpeedColorStyle, tran['node_id'], { color: 'green' })
            }
          }
        }
        this.$set(this.crud.data[i], 'memoryUsed', parseInt(MemoryUsed.toFixed(2)))
        this.$set(this.crud.data[i], 'cpuUsed', parseInt(CpuUsed.toFixed(2)))
        this.$set(this.crud.data[i], 'swapUsed', parseInt(SwapUsed.toFixed(2)))
        if (GpuUsed !== null) { this.$set(this.crud.data[i], 'gpuUsed', parseInt(GpuUsed.toFixed(2))) }
        this.$set(this.crud.data[i], 'networkUpSpeed', newnetworkUpSpeed.toFixed(2) + 'KiB/s')
        this.$set(this.crud.data[i], 'networkDownSpeed', newnetworkDownSpeed.toFixed(2) + 'KiB/s')
      }
    },
    customColorMethod(percentage) {
      if (percentage < 50) {
        return '#5CC46D'
      } else if (percentage < 70) {
        return '#E6A23C'
      } else {
        return '#FE6D70'
      }
    },
    myEnter(row, column, cell, event) {
      if (column.label === '可用磁盘' || column.label === '内存使用率' || column.label === 'CPU使用率' || column.label === '显卡使用率' || column.label === '上传速度' || column.label === '下载速度') {
        this.$refs.table.$el.style.cursor = 'pointer'
      } else {
        this.$refs.table.$el.style.cursor = 'pointer'
      }
    },
    tableRowClassName({ row, rowIndex }) {
      if (this.onLineData.includes(row.nodeId) !== true) {
        return 'warning-row'
      }
      return ''
    }
  }
}
</script>

<style lang="scss" >
.pmNode{
  ::v-deep .el-progress-bar__innerText {
    color: #000 !important;
}
  .warning-row {
    background-color: rgb(250, 250, 250) !important;
  }
}

</style>
