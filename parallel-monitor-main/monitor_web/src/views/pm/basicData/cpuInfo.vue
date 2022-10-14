<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" :search-options="searchOptions" />
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <!-- <crudOperation :permission="permission" /> -->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="CPU型号" prop="cpuName">
            <el-input v-model="form.cpuName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="核心数" prop="cores">
            <el-input v-model="form.cores" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="线程数" prop="threads">
            <el-input v-model="form.threads" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="架构" prop="arch">
            <el-input v-model="form.arch" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="一级指令缓存" prop="l1ICache">
            <el-input v-model="form.l1ICache" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="一级数据缓存" prop="l1DCache">
            <el-input v-model="form.l1DCache" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="二级缓存" prop="l2Cache">
            <el-input v-model="form.l2Cache" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="三级缓存" prop="l3Cache">
            <el-input v-model="form.l3Cache" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="主频" prop="mainFrequency">
            <el-input v-model="form.mainFrequency" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="睿频" prop="boostFrequency">
            <el-input v-model="form.boostFrequency" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :header-cell-style="{'text-align':'center','background': 'rgba(0,0,0,0)'}" :cell-style="{'text-align':'center'}" :data="crud.data" size="small" style="width: 100%;" @cell-mouse-enter="myEnter" @cell-click="goTo" @selection-change="crud.selectionChangeHandler">
        <!-- <el-table-column type="selection" width="55" /> -->
        <!-- <el-table-column prop="cpuId" label="CPU ID" width="80px" />-->
        <!-- <el-table-column prop="nodeId" label="节点ID" width="80px" /> -->
        <el-table-column prop="node.serialNumber" label="节点序号" />
        <el-table-column prop="cpuName" label="CPU型号" show-overflow-tooltip />
        <el-table-column prop="cores" label="核心数" />
        <el-table-column prop="threads" label="线程数" />
        <el-table-column prop="arch" label="架构" />
        <!-- <el-table-column prop="l1ICache" label="一级指令缓存大小" width="120px" />
        <el-table-column prop="l1DCache" label="一级数据缓存大小" width="120px" />
        <el-table-column prop="l2Cache" label="二级缓存大小" width="100px" />
        <el-table-column prop="l3Cache" label="三级缓存大小" width="100px" /> -->
        <el-table-column prop="mainFrequency" label="主频" />
        <!-- <el-table-column prop="boostFrequency" label="睿频" width="120px" /> -->
        <el-table-column prop="cpuBusy" label="CPU使用率">
          <template scope="scope">
            <el-progress class="cpuBusy" :text-inside="true" :stroke-width="20" :percentage="scope.row.cpuBusy" :color="customColorMethod" />
          </template>
        </el-table-column>
        <el-table-column prop="cpuFrequency" label="CPU频率">
          <template scope="scope">
            <span :style="myColorStyleFre[scope.row.cpuId]">{{ scope.row.cpuFrequency }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="cpuTemp" label="CPU温度">
          <template scope="scope">
            <el-progress class="cpuTemp" :text-inside="true" :stroke-width="20" :percentage="scope.row.cpuTemp" :format="format" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <!-- <el-table-column prop="updateTime" label="修改时间" /> -->
        <!-- <el-table-column v-if="checkPer(['admin','pmNodeCpu:edit','pmNodeCpu:del'])" label="操作" width="150px" align="center" class="edit">
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
import crudPmNodeCpu from '@/api/pm/pmNodeCpu'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
// import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { cpuId: null, nodeId: null, cpuName: null, cores: null, threads: null, arch: null, l1ICache: null, l1DCache: null, l2Cache: null, l3Cache: null, mainFrequency: null, boostFrequency: null, createTime: null, updateTime: null }
export default {
  name: 'PmNodeCpu',
  // rrOperation,crudOperation,
  components: { pagination, crudOperation },
  props: {
    nodeId: {
      type: Number,
      default: null
    }
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ optShow: {
      add: false,
      edit: false,
      delete: false,
      download: true
    }, title: 'pm_node_cpu', url: 'api/pmNodeCpu', idField: 'cpuId', sort: 'updateTime,desc', crudMethod: { ...crudPmNodeCpu }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'pmNodeCpu:add'],
        edit: ['admin', 'pmNodeCpu:edit'],
        del: ['admin', 'pmNodeCpu:del']
      },
      rules: {
        cpuName: [
          { required: true, message: 'CPU型号不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'cpuId', display_name: 'CPU ID号' },
        { key: 'nodeId', display_name: '节点号' },
        { key: 'cpuName', display_name: 'CPU型号' },
        { key: 'cores', display_name: '核心数' },
        { key: 'arch', display_name: '架构' },
        { key: 'l1ICache', display_name: '一级指令缓存' },
        { key: 'l1DCache', display_name: '一级数据缓存' },
        { key: 'l2Cache', display_name: '二级缓存' },
        { key: 'l3Cache', display_name: '三级缓存' },
        { key: 'mainFrequency', display_name: '主频' },
        { key: 'boostFrequency', display_name: '睿频' }
      ],
      isUp: [],
      isDown: [],
      currentPage: 1,
      intPageSize: 10,
      myColorStyle: [],
      myColorStyleFre: [],
      searchOptions: [{ label: '节点序号', value: 'serialNumber' }, { label: 'CPU型号', value: 'cpuName' }, { label: '架构', value: 'arch' }]
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
        if (this.$cookies.get('tran') === 'cpuInfo') this.handleRealTimeData(JSON.parse(JSON.stringify(newVal)), JSON.parse(JSON.stringify(oldVal)))
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
  created() {
  },
  mounted() {
  },
  updated() {
  },
  beforeDestroy() {
    // this.nodeId = null
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      // console.log(this.crud.data)
      return true
    },
    [CRUD.HOOK.afterRefresh]() {
      this.handleRealTimeData(this.data, [])
    },
    goTo(row, column) {
      // 获取到了当前行的uuid
      var data = column['label']
      var uuid = row.cpuId
      var nodeId = row.nodeId
      // if (data !== '节点序号') {
      this.$router.push({
        path: '/Center/cpuChart',
        query: {
          cpuid: uuid,
          nodeId: nodeId
        }
      })
      // } else {
      //   this.$emit('node', 'nodeInfo', nodeId)
      // }
    },
    handleRealTimeData(data, oldData) {
      for (var i = 0; i < this.crud.data.length; ++i) {
        // 保存新旧CPU使用率
        var olBusy = 0
        var neBusy = 0
        // 保存新旧CPU温度
        var newTemp = 0
        // 保存新旧CPU频率
        var oldFre = 0
        var newFre = 0
        var nodeId = this.crud.data[i].nodeId
        var tran = data[nodeId]
        var that = this
        if (tran !== undefined) {
          tran = tran['cpu_state']
          Object.keys(tran).forEach((key) => {
            if (key !== 'temperature') {
              if (tran[key]['cpu_id'] + '' === that.crud.data[i].cpuId + '') {
                neBusy = tran[key]['cpu_busy']
                newTemp = tran[key]['temperature']
                newFre = tran[key]['frequency']
              }
            }
            // 控制样式
            if (Object.keys(oldData).length !== 0) {
              olBusy = oldData[nodeId]['cpu_state'][key]['cpu_busy']
              oldFre = oldData[nodeId]['cpu_state'][key]['frequency']
              // CPU使用率颜色
              if (neBusy >= olBusy) {
                that.$set(that.myColorStyle, tran[key]['cpu_id'], { color: 'red' })
              } else {
                that.$set(that.myColorStyle, tran[key]['cpu_id'], { color: 'green' })
              }
              // CPU频率颜色
              if (newFre >= oldFre) {
                that.$set(that.myColorStyleFre, tran[key]['cpu_id'], { color: 'red' })
              } else {
                that.$set(that.myColorStyleFre, tran[key]['cpu_id'], { color: 'green' })
              }
            }
          })
        }
        that.$set(that.crud.data[i], 'cpuBusy', parseInt(neBusy.toFixed(2)))
        that.$set(that.crud.data[i], 'cpuTemp', parseInt(newTemp))
        that.$set(that.crud.data[i], 'cpuFrequency', (newFre / 1000000).toFixed(2) + 'GHz')
      }
    },
    format(percentage) {
      return percentage + '摄氏度'
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
  .searchButton{
    margin-left: -8%;
  }
  .edit{
    z-index: 1;
  }
  .tt{
    color: blue;
  }
  ::v-deep .el-table tbody tr:hover td{
    background: rgba(64,158,255,0.2) !important;
  }
  ::v-deep .cpuBusy .el-progress-bar__innerText {
    color: #000 !important;
  }
</style>
