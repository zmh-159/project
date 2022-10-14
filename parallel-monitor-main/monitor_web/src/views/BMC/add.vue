<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" :search-options="searchOptions" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="用户名" prop="user">
            <el-input v-model="form.user" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="密码" prop="passwd">
            <el-input v-model="form.passwd" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="IP" prop="bmcIp">
            <el-input v-model="form.bmcIp" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="nodeId" label="ID" />
        <el-table-column prop="nodeType" label="类型" />
        <el-table-column prop="user" label="主机名" />
        <el-table-column prop="bmcIp" label="IP地址" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="updateTime" label="接入时间" />
        <el-table-column v-if="checkPer(['admin','pbNode:edit','pbNode:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <el-button type="text">详情</el-button>
            <el-button type="text" @click="crud.toEdit(scope.row)">重设</el-button>
            <el-popconfirm
              :title="`确定移除${scope.row.nodeId}号服务器吗？`"
              @confirm="crud.doDelete(scope.row)"
            >
              <el-button slot="reference" type="text">移除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudPbNode from '@/api/BMC/pbNode'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { user: null, passwd: null, bmcIp: null }
export default {
  name: 'PbNode',
  components: { pagination, crudOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '服务器', url: 'api/pbNode', idField: 'nodeId', sort: 'nodeId,desc', crudMethod: { ...crudPbNode }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'pbNode:add'],
        edit: ['admin', 'pbNode:edit'],
        del: ['admin', 'pbNode:del']
      },
      rules: {
        user: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        passwd: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ],
        bmcIp: [
          { required: true, message: 'IP不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'bmcIp', display_name: 'IP' }
      ],
      searchOptions: [{ label: '服务器类型', value: 'nodeType' }, { label: '服务器ID', value: 'nodeId' }, { label: 'IP地址', value: 'bmcIp' }]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    typeChange(val) {
      this.searchContent = val
    }
  }
}
</script>

<style lang="scss" scoped>
.input-with-select{
  width: 400px;
  .el-select{
    width:150px
  }
}
.el-button--text{
  padding{
    left:8px;
    right:8px
  }
}
</style>
