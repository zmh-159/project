<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" :search-options="searchOptions" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="磁盘名" prop="name">
            <el-input v-model="form.name" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="磁盘容量" prop="capacity">
            <el-input v-model="form.capacity" style="width: 370px;" />
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
        <!-- <el-table-column prop="diskId" label="磁盘ID" />
        <el-table-column prop="nodeId" label="节点ID" width="80px" /> -->
        <el-table-column prop="node.serialNumber" label="节点序号" />
        <el-table-column prop="name" label="磁盘名" show-overflow-tooltip />
        <el-table-column prop="capacity" label="磁盘容量" />
        <el-table-column prop="writeSpeed" label="写速度">
          <template scope="scope">
            <span :style="myWriteSpeedColorStyle[scope.row.diskId]">{{ scope.row.writeSpeed }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="readSpeed" label="读速度">
          <template scope="scope">
            <span :style="myReadSpeedColorStyle[scope.row.diskId]">{{ scope.row.readSpeed }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <!-- <el-table-column v-if="checkPer(['admin','pmNodeDisk:edit','pmNodeDisk:del'])" label="操作" width="150px" align="center">
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
import crudPmNodeDisk from '@/api/pm/pmNodeDisk'
// import RadarChart from '@/components/Echarts/RadarChart'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
// import pmNodeDiskState from '@/api/pm/pmNodeDiskState'

const defaultForm = { diskId: null, nodeId: null, name: null, capacity: null, createTime: null, updateTime: null }
export default {
  name: 'PmNodeDisk',
  // rrOperation crudOperation,
  components: { pagination, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: 'pm_node_disk',
      url: 'api/pmNodeDisk',
      idField: 'diskId',
      sort: 'updateTime,desc',
      crudMethod: { ...crudPmNodeDisk },
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
        add: ['admin', 'pmNodeDisk:add'],
        edit: ['admin', 'pmNodeDisk:edit'],
        del: ['admin', 'pmNodeDisk:del']
      },
      rules: {
        name: [
          { required: true, message: '磁盘名不能为空', trigger: 'blur' }
        ],
        capacity: [
          { required: true, message: '容量不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'diskId', display_name: '磁盘ID' },
        { key: 'nodeId', display_name: '节点ID' },
        { key: 'name', display_name: '磁盘名' },
        { key: 'capacity', display_name: '磁盘容量' }
      ],
      myWriteSpeedColorStyle: [],
      myReadSpeedColorStyle: [],
      searchOptions: [{ label: '节点序号', value: 'serialNumber' }]

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
        if (this.$cookies.get('tran') === 'diskInfo') this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
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
      var uuid = row.diskId
      var nodeId = row.nodeId
      // if (data !== '节点序号') {
      this.$router.push({
        path: '/Center/diskChart',
        query: {
          diskID: uuid
        }
      })
      // } else {
      //   this.$emit('node', 'nodeInfo', nodeId)
      // }
    },
    handleRealTimeData(data, oldData) {
      for (var i = 0; i < this.crud.data.length; ++i) {
        // 保存新旧写速度
        var oldReadSpeed = 0
        var newReadSpeed = 0
        // 保存新旧读速度
        var oldWriteSpeed = 0
        var newWriteSpeed = 0
        var nodeId = this.crud.data[i].nodeId
        // console.log(nodeId)
        var tran = data[nodeId]
        var that = this
        if (tran !== undefined) {
          tran = tran['disk_state']

          Object.keys(tran).forEach((key) => {
            if (tran[key]['disk_id'] + '' === that.crud.data[i].diskId + '') {
              newReadSpeed = tran[key]['read_speed']
              newWriteSpeed = tran[key]['write_speed']
            }
            // 控制样式
            if (Object.keys(oldData).length !== 0) {
              oldReadSpeed = oldData[nodeId]['disk_state'][key]['read_speed']
              oldWriteSpeed = oldData[nodeId]['disk_state'][key]['write_speed']
              // CPU使用率颜色
              if (newReadSpeed >= oldReadSpeed) {
                that.$set(that.myReadSpeedColorStyle, tran[key]['disk_id'], { color: 'red' })
              } else {
                that.$set(that.myReadSpeedColorStyle, tran[key]['disk_id'], { color: 'green' })
              }
              // CPU频率颜色
              if (newWriteSpeed >= oldWriteSpeed) {
                that.$set(that.myWriteSpeedColorStyle, tran[key]['disk_id'], { color: 'red' })
              } else {
                that.$set(that.myWriteSpeedColorStyle, tran[key]['disk_id'], { color: 'green' })
              }
            }
          })
        }
        that.$set(that.crud.data[i], 'readSpeed', newReadSpeed + 'KiB/s')
        that.$set(that.crud.data[i], 'writeSpeed', newWriteSpeed + 'KiB/s')
      }
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
.editButton{
  margin-left: -10%;
}
.pan{
  /* background-color: blueviolet; */
  width: 100%;
  height: 150px;
}
::v-deep .el-table tbody tr:hover td{
    background: rgba(64,158,255,0.2) !important;
  }
</style>
