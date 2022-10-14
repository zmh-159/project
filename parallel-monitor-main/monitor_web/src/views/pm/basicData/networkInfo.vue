<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" :search-options="searchOptions" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="ip地址" prop="ip">
            <el-input v-model="form.ip" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="mac地址" prop="mac">
            <el-input v-model="form.mac" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="网卡名" prop="name">
            <el-input v-model="form.name" style="width: 370px;" />
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
        <!-- <el-table-column prop="networkId" label="网卡ID" /> -->
        <!-- <el-table-column prop="nodeId" label="节点ID" width="80px" /> -->
        <el-table-column prop="node.serialNumber" label="节点序号" />
        <el-table-column prop="ip" label="IP地址" />
        <el-table-column prop="mac" label="MAC地址" />
        <el-table-column prop="name" label="网卡名" show-overflow-tooltip />
        <el-table-column prop="upSpeed" label="上传速度">
          <template scope="scope">
            <span :style="myUpSpeedColorStyle[scope.row.networkId]">{{ scope.row.upSpeed }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="downSpeed" label="下载速度">
          <template scope="scope">
            <span :style="myDownSpeedColorStyle[scope.row.networkId]">{{ scope.row.downSpeed }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <!-- <el-table-column prop="updateTime" label="修改时间" /> -->
        <!-- <el-table-column v-if="checkPer(['admin','pmNodeNetwork:edit','pmNodeNetwork:del'])" label="操作" width="150px" align="center">
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
import crudPmNodeNetwork from '@/api/pm/pmNodeNetwork'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { networkId: null, nodeId: null, ip: null, mac: null, name: null, createTime: null, updateTime: null }
export default {
  name: 'PmNodeNetwork',
  // rrOperation,crudOperation
  components: { pagination, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ optShow: {
      add: false,
      edit: false,
      delete: false,
      download: true
    }, title: 'pm_node_network', url: 'api/pmNodeNetwork', idField: 'networkId', sort: 'updateTime,desc', crudMethod: { ...crudPmNodeNetwork }})
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
        add: ['admin', 'pmNodeNetwork:add'],
        edit: ['admin', 'pmNodeNetwork:edit'],
        del: ['admin', 'pmNodeNetwork:del']
      },
      rules: {
        ip: [
          { required: true, message: 'ip地址不能为空', trigger: 'blur' }
        ],
        mac: [
          { required: true, message: 'mac地址不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '网卡名不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'networkId', display_name: '主键' },
        { key: 'nodeId', display_name: '外键：表pm_node' },
        { key: 'ip', display_name: 'ip地址' },
        { key: 'name', display_name: '网卡名' }
      ],
      myUpSpeedColorStyle: [],
      myDownSpeedColorStyle: [],
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
        if (this.$cookies.get('tran') === 'networkInfo') this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
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
      var uuid = row.networkId
      var data = column['label']
      var nodeId = row.nodeId
      // if (data !== '节点序号') {
      this.$router.push({
        path: '/Center/networkChart',
        query: {
          networkID: uuid
        }
      })
      // } else {
      //   this.$emit('node', 'nodeInfo', nodeId)
      // }
    },
    handleRealTimeData(data, oldData) {
      // console.log(data)
      for (var i = 0; i < this.crud.data.length; ++i) {
        // 保存新旧上传速度
        var oldUpSpeed = 0
        var newUpSpeed = 0
        // 保存新旧下载速度
        var oldDownSpeed = 0
        var newDownSpeed = 0
        var nodeId = this.crud.data[i].nodeId
        // console.log(nodeId)
        var tran = data[nodeId]
        var that = this
        if (tran !== undefined) {
          tran = tran['network_state']

          Object.keys(tran).forEach((key) => {
            if (tran[key]['network_id'] + '' === that.crud.data[i].networkId + '') {
              newUpSpeed = tran[key]['up_speed']
              newDownSpeed = tran[key]['down_speed']
            }
            // 控制样式
            if (Object.keys(oldData).length !== 0) {
              oldUpSpeed = oldData[nodeId]['network_state'][key]['up_speed']
              oldDownSpeed = oldData[nodeId]['network_state'][key]['down_speed']
              // 上传速度颜色
              if (newUpSpeed >= oldUpSpeed) {
                that.$set(that.myUpSpeedColorStyle, tran[key]['network_id'], { color: 'red' })
              } else {
                that.$set(that.myUpSpeedColorStyle, tran[key]['network_id'], { color: 'green' })
              }
              // 下载速度颜色
              if (newDownSpeed >= oldDownSpeed) {
                that.$set(that.myDownSpeedColorStyle, tran[key]['network_id'], { color: 'red' })
              } else {
                that.$set(that.myDownSpeedColorStyle, tran[key]['network_id'], { color: 'green' })
              }
            }
          })
        }
        that.$set(that.crud.data[i], 'upSpeed', newUpSpeed + 'KiB/s')
        that.$set(that.crud.data[i], 'downSpeed', newDownSpeed + 'KiB/s')
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

<style lang="scss" scoped>
  ::v-deep .el-table tbody tr:hover td{
    background: rgba(64,158,255,0.2) !important;
  }
</style>
