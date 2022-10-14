<template>
  <div :class="widthclassinfo">
    <div class="app-container">
      <!--工具栏-->
      <div class="head-container">
        <!-- <div v-if="crud.props.searchToggle"> -->
        <!-- 搜索 -->
        <!-- <label class="el-form-item-label">图表类型</label>
        <el-input v-model="query.description" clearable placeholder="描述" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">方案编号</label>
        <el-input v-model="query.serialNumber" clearable placeholder="方案编号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">位置编号</label>
        <el-input v-model="query.position" clearable placeholder="位置编号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div> -->
        <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
        <crudOperation :permission="permission" />
        <!--表单组件-->
        <el-dialog v-if="flag1" :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px" @open="resetForm()">
          <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
            <!-- <el-form-item label="用户" prop="userId">
            <el-input v-model="form.userId" style="width: 370px;" />
          </el-form-item> -->
            <el-form-item label="图表类型" prop="description">
              <el-radio-group v-model="chartType" @change="selectType">
                <el-radio-button label="相空间" />
                <el-radio-button label="二维散点图" />
              <!-- <el-radio-button label="二维面积图" />
              <el-radio-button label="二维折线图" /> -->
              </el-radio-group>
            </el-form-item>
            <el-form-item label="方案编号" prop="serialNumber">
              <el-input v-model="form.serialNumber" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="方案具体参数" prop="detail">
              请选择x轴指标：
              <el-select v-model="xPara" placeholder="请选择" @change="handleX">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select><br>
              请选择y轴指标：
              <el-select v-model="yPara" placeholder="请选择" @change="handleY">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
              <div v-if="flag">
                请选择z轴指标：
                <el-select v-model="zPara" placeholder="请选择" @change="handleZ">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.label"
                  />
                </el-select></div>
            </el-form-item>
            <el-form-item label="位置编号" prop="position">
              <el-select ref="positionSelect" v-model="chartPosition" placeholder="请选择图表位置" @change="positionSelect">
                <el-option
                  v-for="item in items"
                  :key="item.value"
                  :value="item.value"
                  :label="item.lable"
                  :disabled="item.disabled"
                />
              </el-select>
            </el-form-item>
          <!-- <el-form-item label="创建日期" prop="createTime">
            <el-date-picker v-model="form.createTime" type="datetime" style="width: 370px;" />
          </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU(Id)">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @cell-click="formClick" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <!-- <el-table-column prop="chartSchemeId" label="图表类型" /> -->
          <!-- <el-table-column prop="userId" label="用户" /> -->
          <el-table-column prop="description" label="图表类型" />
          <el-table-column prop="serialNumber" label="方案编号" />
          <el-table-column prop="detail" label="方案具体参数" />
          <el-table-column prop="position" label="位置编号" />
          <el-table-column prop="createTime" label="创建日期" />
          <el-table-column prop="updateTime" label="更新时间" />
          <el-table-column v-if="checkPer(['admin','pmChartScheme:edit','pmChartScheme:del'])" label="操作" width="150px" align="center">
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
              />
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </div>
    </div>
  </div>
</template>

<script>
import crudPmChartScheme from '@/api/pm/pmChartScheme'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import { mapGetters } from 'vuex'
import pmChartScheme from '@/api/pm/pmChartScheme'

const defaultForm = { chartSchemeId: null, description: null, serialNumber: null,
  detail: [{
    name: 'x',
    value: ''
  }, {
    name: 'y',
    value: ''
  }, {
    name: 'z',
    value: ''
  }], position: null, createTime: null, updateTime: null }
export default {
  name: 'PmChartScheme',
  // rrOperation,
  components: { pagination, crudOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'test', url: 'api/pmChartScheme', idField: 'chartSchemeId', sort: 'position,asc', crudMethod: { ...crudPmChartScheme }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'pmChartScheme:add'],
        edit: ['admin', 'pmChartScheme:edit'],
        del: ['admin', 'pmChartScheme:del']
      },
      rules: {
        description: [
          { required: true, message: '描述不能为空', trigger: 'blur' }
        ],
        serialNumber: [
          { required: true, message: '方案编号不能为空', trigger: 'blur' }
        ],
        detail: [
          { required: true, message: '方案具体参数不能为空', trigger: 'blur' }
        ],
        position: [
          { required: true, message: '位置编号不能为空', trigger: 'blur' }
        ]
        // createTime: [
        //   { required: true, message: '创建日期不能为空', trigger: 'blur' }
        // ]
      },
      queryTypeOptions: [
        { key: 'description', display_name: '描述' },
        { key: 'serialNumber', display_name: '方案编号' },
        { key: 'position', display_name: '位置编号' }
      ],
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      // 图表类型选择器绑定值
      chartType: '',
      // 图表位置选择器绑定值
      chartPosition: '',
      // 下拉框绑定值
      xPara: null,
      yPara: null,
      zPara: null,
      flag1: true,
      // z轴是否显示的记号
      flag: false,
      // 图表位置选择器数据
      items: [{
        value: 0,
        lable: '0号位置',
        disabled: false
      }, {
        value: 1,
        lable: '1号位置',
        disabled: false
      }, {
        value: 2,
        lable: '2号位置',
        disabled: false
      }, {
        value: 3,
        lable: '3号位置',
        disabled: false
      }],
      // 下拉框选项数据
      options: [{
        value: '选项1',
        label: 'CPU系统使用率'
      }, {
        value: '选项2',
        label: 'CPU用户使用率'
      }, {
        value: '选项3',
        label: 'CPU空闲率'
      }, {
        value: '选项4',
        label: 'CPU使用率'
      }, {
        value: '选项5',
        label: 'CPU温度'
      }, {
        value: '选项6',
        label: '核心频率'
      }, {
        value: '选项7',
        label: '内存使用率'
      }, {
        value: '选项8',
        label: '交换区使用率'
      }, {
        value: '选项9',
        label: '网卡上传速度'
      }, {
        value: '选项10',
        label: '网卡下载速度'
      }, {
        value: '选项11',
        label: '磁盘使用率'
      }, {
        value: '选项12',
        label: '磁盘写出速度'
      }, {
        value: '选项13',
        label: '磁盘写入速度'
      }],
      Id: null,
      timer: null
    }
  },
  computed: {
    // 获取当前user
    ...mapGetters([
      'user'
    ])
  },
  watch: {
    // that =this
    screenWidth(newold, oldval) {
      console.log(newold, oldval)
      this.widthclassinfo2()
    }
  },
  created() {
  },
  beforeDestroy() {
    clearInterval(this.timer)
    this.timer = null
  },
  mounted() {
    this.screenWidth = document.body.clientWidth
    this.screenHeight = document.body.clientHeight
    window.onresize = () => {
      return (() => {
        this.screenWidth = document.body.clientWidth
        this.screenHeight = document.body.clientHeight
      })()
    }
    var that = this
    pmChartScheme.queryUserId(this.user.id).then(function(response) {
      that.initPosition(response.content)
    })
  },
  methods: {
    widthclassinfo2() {
      if (this.screenWidth > 1300) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },

    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    // 新建后触发
    [CRUD.HOOK.afterToAdd]() {
      this.resetForm()
    },
    [CRUD.HOOK.afterToEdit]() {
      console.log('编辑')
      console.log(this.form)
      this.form.detail = [{
        name: 'x',
        value: ''
      }, {
        name: 'y',
        value: ''
      }, {
        name: 'z',
        value: ''
      }]
    },
    // 图表类型选择监听函数
    selectType(data) {
      if (data === '相空间') {
        this.form.description = 0
      } else if (data === '二维散点图') {
        this.form.description = 1
      }
      // z轴坐标显示函数
      if (data === '相空间') {
        this.flag = true
      } else {
        this.flag = false
        this.form.detail[2].value = 'null'
      }
    },
    // 图表存放位置监听函数
    positionSelect(data) {
      this.form.position = data
      // 选择以后将其置为不可选状态
    },
    // 三个指标下拉框选择处理函数
    handleX() {
      this.form.detail[0].value = this.xPara
    },
    handleY() {
      this.form.detail[1].value = this.yPara
    },
    handleZ() {
      this.form.detail[2].value = this.zPara
    },
    initPosition(data) {
      for (var j = 0; j < this.items.length; j++) {
        this.items[j].disabled = false
      }
      for (var i = 0; i < data.length; i++) {
        var po = data[i]['position']
        this.items[po].disabled = true
      }
    },
    // 表单刷新函数
    resetForm() {
      // 图表类型选择器绑定值
      this.chartType = ''
      // 图表位置选择器绑定值
      this.chartPosition = ''
      // 下拉框绑定值
      this.xPara = null
      this.yPara = null
      this.zPara = null
      var that = this
      pmChartScheme.queryUserId(this.user.id).then(function(response) {
        that.initPosition(response.content)
      })
    },
    formClick() {
      // console.log('点击')
    }
  }
}
</script>

<style scoped>
  .el-dropdown-link {
    cursor: pointer;
    color: #2e3338;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
      .bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
</style>
