<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" :search-options="searchOptions" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="制造商" prop="manufacturer">
            <el-input v-model="form.manufacturer" style="width: 370px;" show-overflow-tooltip />
          </el-form-item>
          <el-form-item label="显卡名" prop="name">
            <el-input v-model="form.name" style="width: 370px;" show-overflow-tooltip />
          </el-form-item>
          <el-form-item label="显存" prop="memory">
            <el-input v-model="form.memory" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="显卡uuid" prop="uuid">
            <el-input v-model="form.uuid" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" :data="crud.data" size="small" style="width: 100%;" @cell-mouse-enter="myEnter" @cell-click="goTo" @selection-change="crud.selectionChangeHandler">
        <!-- <el-table-column type="selection" width="55" /> -->
        <!-- <el-table-column prop="gpuId" label="显卡ID" width="80" /> -->
        <!-- <el-table-column prop="nodeId" label="节点ID" width="80" /> -->
        <el-table-column prop="node.serialNumber" label="节点序号" />
        <el-table-column prop="manufacturer" label="制造商" />
        <el-table-column prop="name" label="显卡名" />
        <el-table-column prop="memory" label="显存" />
        <!-- <el-table-column prop="uuid" label="显卡UUID" min-width="100px" /> -->
        <el-table-column prop="gpuUsed" label="GPU使用率">
          <template scope="scope">
            <el-progress class="gpu" :text-inside="true" :stroke-width="20" :percentage="scope.row.gpuUsed" :color="customColorMethod" />
          </template>
        </el-table-column>
        <el-table-column prop="gpuMenUsed" label="显存使用率">
          <template scope="scope">
            <el-progress class="gpu" :text-inside="true" :stroke-width="20" :percentage="scope.row.gpuMenUsed" :color="customColorMethod" />
          </template>
        </el-table-column>
        <el-table-column prop="gpuTemp" label="GPU温度">
          <template scope="scope">
            <span :style="myPerformanceColorStyle[scope.row.gpuId]">{{ scope.row.gpuTemp }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gpuMenTemp" label="显存温度">
          <template scope="scope">
            <span :style="myPerformanceColorStyle[scope.row.gpuId]">{{ scope.row.gpuMenTemp }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="performance" label="性能状态">
          <template scope="scope">
            <span :style="myPerformanceColorStyle[scope.row.gpuId]">{{ scope.row.performance }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <!-- <el-table-column prop="updateTime" label="修改时间" width="140" /> -->
        <!-- <el-table-column v-if="checkPer(['admin','pmNodeMemory:edit','pmNodeMemory:del'])" label="操作" width="150px" align="center">
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
    </div>
  </div>
</template>

<script>
import crudPmNodeGpu from '@/api/pm/pmNodeGpu'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { gpuId: null, nodeId: null, manufacturer: null, name: null, memory: null, uuid: null, createTime: null, updateTime: null }
export default {
  name: 'PmNodeGpu',
  // rrOperation,crudOperation,
  components: { pagination, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: 'pm_node_gpu',
      url: 'api/pmNodeGpu',
      idField: 'gpuId',
      sort: 'updateTime,desc',
      crudMethod: { ...crudPmNodeGpu },
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
      permission: {
        add: ['admin', 'pmNodeGpu:add'],
        edit: ['admin', 'pmNodeGpu:edit'],
        del: ['admin', 'pmNodeGpu:del']
      },
      rules: {
        manufacturer: [
          { required: true, message: '制造商不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '显卡名不能为空', trigger: 'blur' }
        ],
        memory: [
          { required: true, message: '显存', trigger: 'blur' }
        ],
        uuid: [
          { required: true, message: '显卡uuid', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'gpuId', display_name: '主键' },
        { key: 'nodeId', display_name: '外键：表pm_node' },
        { key: 'manufacturer', display_name: '制造商' },
        { key: 'name', display_name: '显卡名' },
        { key: 'memory', display_name: '显存' },
        { key: 'uuid', display_name: '显卡uuid' }
      ],
      myGpuUsedColorStyle: [],
      myGpuTempColorStyle: [],
      myGpuMenTempColorStyle: [],
      myGpuMenUsedColorStyle: [],
      myPerformanceColorStyle: [],
      searchOptions: [{ label: '节点序号', value: 'serialNumber' }, { label: '显卡名', value: 'name' }]

    }
  },
  computed: {
    data() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
  },
  watch: {
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
    },
    data: {
      handler(newVal, oldVal) {
        if (this.$cookies.get('tran') === 'gpuInfo') this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
      },
      deep: true
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    [CRUD.HOOK.afterRefresh]() {
      this.handleRealTimeData(this.data, [])
    },
    goTo(row, column) {
      // 获取到了当前行的uuid
      var data = column['label']
      var uuid = row.gpuId
      var nodeId = row.nodeId
      // if (data !== '节点序号') {
      this.$router.push({
        path: '/Center/gpuChart',
        query: {
          gpuID: uuid
        }
      })
      // } else {
      //   this.$emit('node', 'nodeInfo', nodeId)
      // }
    },
    handleRealTimeData(data, oldData) {
      // console.log(data)
      for (var i = 0; i < this.crud.data.length; ++i) {
        // 保存gpu使用率
        var oldGpuUsed = 0
        var newGpuUsed = 0
        // 保存gpu温度
        // var oldGpuTemp = 0
        var newGpuTemp = 0
        // 保存新旧显存温度
        // var oldGpuMenTemp = 0
        var newGpuMenTemp = 0
        // 保存新旧显存使用率
        var oldGpuMenUsed = 0
        var newGpuMenUsed = 0
        // 保存新旧性能状态
        // var oldPerformance = 0
        var newPerformance = 0
        var nodeId = this.crud.data[i].nodeId
        // console.log(nodeId)
        var tran = data[nodeId]
        var that = this
        if (tran !== undefined) {
          tran = tran['gpu_state']
          Object.keys(tran).forEach((key) => {
            if (tran[key]['gpu_id'] + '' === that.crud.data[i].gpuId + '') {
              newGpuUsed = tran[key]['gpu_utilization']
              newGpuTemp = tran[key]['gpu_current_temp']
              newGpuMenTemp = tran[key]['memory_current_temp']
              newGpuMenUsed = tran[key]['memory_utilization']
              newPerformance = tran[key]['performance_state']
            }
            that.$set(that.myGpuTempColorStyle, tran[key]['gpu_id'], { color: 'green' })
            that.$set(that.myGpuMenTempColorStyle, tran[key]['gpu_id'], { color: 'green' })
            that.$set(that.myPerformanceColorStyle, tran[key]['gpu_id'], { color: 'green' })
            // 控制样式
            if (Object.keys(oldData).length !== 0) {
              oldGpuUsed = oldData[nodeId]['disk_state'][key]['gpu_utilization']
              oldGpuMenUsed = oldData[nodeId]['disk_state'][key]['memory_utilization']
              if (newGpuUsed >= oldGpuUsed) {
                that.$set(that.myGpuUsedColorStyle, tran[key]['gpu_id'], { color: 'red' })
              } else {
                that.$set(that.myGpuUsedColorStyle, tran[key]['gpu_id'], { color: 'green' })
              }
              if (newGpuMenUsed >= oldGpuMenUsed) {
                that.$set(that.myGpuMenUsedColorStyle, tran[key]['gpu_id'], { color: 'red' })
              } else {
                that.$set(that.myGpuMenUsedColorStyle, tran[key]['gpu_id'], { color: 'green' })
              }
            }
          })
        }
        that.$set(that.crud.data[i], 'gpuUsed', newGpuUsed)
        that.$set(that.crud.data[i], 'gpuTemp', newGpuTemp)
        that.$set(that.crud.data[i], 'gpuMenUsed', newGpuMenUsed)
        that.$set(that.crud.data[i], 'gpuMenTemp', newGpuMenTemp)
        that.$set(that.crud.data[i], 'performance', newPerformance)
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
    format(percentage) {
      if (percentage === 'N/A') return 0 + '摄氏度'
      return parseInt(percentage) + '摄氏度'
    },
    myEnter(row, column, cell, event) {
      // console.log(row)
      // console.log(column.label)
      // if (column.label === '节点序号') {
      this.$refs.table.$el.style.cursor = 'pointer'
      // } else {
      // this.$refs.table.$el.style.cursor = 'default'
      // }
    }
  }
}
</script>

<style scoped>
::v-deep .el-table tbody tr:hover td{
    background: rgba(64,158,255,0.2) !important;
  }
  ::v-deep .gpu .el-progress-bar__innerText {
    color: #000 !important;
  }
</style>
