<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" :search-options="searchOptions" />

      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="制造商" prop="manufacturer">
            <el-input v-model="form.manufacturer" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="内存规格" prop="memoryType">
            <el-input v-model="form.memoryType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="速度" prop="speed">
            <el-input v-model="form.speed" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="容量" prop="capacity">
            <el-input v-model="form.capacity" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" :data="crud.data" size="small" style="width: 100%;" @cell-mouse-enter="myEnter" @selection-change="crud.selectionChangeHandler">
        <!-- <el-table-column type="selection" width="55" /> -->
        <!-- <el-table-column prop="memoryId" label="内存ID" /> -->
        <!-- <el-table-column prop="nodeId" label="节点ID" width="80px" /> -->
        <el-table-column prop="node.serialNumber" label="节点序号" />
        <el-table-column prop="manufacturer" label="制造商" />
        <el-table-column prop="memoryType" label="内存规格" />
        <el-table-column prop="speed" label="速度" />
        <el-table-column prop="capacity" label="容量" />
        <el-table-column prop="createTime" label="创建时间" />
        <!-- <el-table-column prop="updateTime" label="修改时间" /> -->
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
import crudPmNodeMemory from '@/api/pm/pmNodeMemory'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { memoryId: null, nodeId: null, manufacturer: null, memoryType: null, speed: null, capacity: null, createTime: null, updateTime: null }
export default {
  name: 'PmNodeMemory',
  // rrOperation,crudOperation,
  components: { pagination, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ optShow: {
      add: false,
      edit: false,
      delete: false,
      download: true
    }, title: 'pm_node_memory', url: 'api/pmNodeMemory', idField: 'memoryId', sort: 'updateTime,desc', crudMethod: { ...crudPmNodeMemory }})
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
        add: ['admin', 'pmNodeMemory:add'],
        edit: ['admin', 'pmNodeMemory:edit'],
        del: ['admin', 'pmNodeMemory:del']
      },
      rules: {
        manufacturer: [
          { required: true, message: '制造商不能为空', trigger: 'blur' }
        ],
        memoryType: [
          { required: true, message: '内存规格不能为空', trigger: 'blur' }
        ],
        speed: [
          { required: true, message: '速度不能为空', trigger: 'blur' }
        ],
        capacity: [
          { required: true, message: '容量不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'memoryId', display_name: '主键' },
        { key: 'nodeId', display_name: '外键：表pm_node' },
        { key: 'manufacturer', display_name: '制造商' },
        { key: 'memoryType', display_name: '内存规格' },
        { key: 'speed', display_name: '速度' },
        { key: 'capacity', display_name: '容量' }
      ],
      searchOptions: [{ label: '节点序号', value: 'serialNumber' }, { label: '制造商', value: 'manufacturer' }]
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
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    goTo(row, column) {
      // 获取到了当前行的uuid
      var data = column['label']
      var uuid = row.memoryId
      var nodeId = row.nodeId
      // if (data !== '节点序号') {
      this.$router.push({
        path: '/Center/memoryChart',
        query: {
          memoryID: uuid
        }
      })
      // } else {
      //   this.$emit('node', 'nodeInfo', nodeId)
      // }
    },
    myEnter(row, column, cell, event) {
      // console.log(row)
      // console.log(column.label)
      // if (column.label === '节点序号') {
      // this.$refs.table.$el.style.cursor = 'pointer'
      // } else {
      this.$refs.table.$el.style.cursor = 'default'
      // }
    }
  }
}
</script>

<style scoped>
::v-deep .el-table tbody tr:hover td{
    background: rgba(64,158,255,0) !important;
  }
</style>
