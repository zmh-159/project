<template>
  <!-- :class="widthclass" -->
  <div :class="widthclassinfo">
    <div class="app-container">
      <!--工具栏-->
      <div class="head-container">
        <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
        <crudOperation :permission="permission" />
        <!--表单组件-->
        <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
          <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="脚本名" prop="name">
              <el-input v-model="form.name" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="版本" prop="version">
              <el-input v-model="form.version" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="操作系统" prop="os">
              <el-input v-model="form.os" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="安装脚本" prop="installShell">
              <el-input v-model="form.installShell" :rows="8" type="textarea" style="width: 370px;" />
            </el-form-item>
            <el-form-item label="卸载脚本" prop="deleteShell">
              <el-input v-model="form.deleteShell" :rows="8" type="textarea" style="width: 370px;" />
            </el-form-item>
          <!-- <el-form-item label="创建时间">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="修改时间">
            <el-input v-model="form.updateTime" style="width: 370px;" />
          </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!-- 部署弹框 -->
        <el-dialog
          id="mian"
          title="提示"
          :visible.sync="dialogVisible"
          width="40%"
        >
          <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
          <div class="Submit">
            <el-radio v-model="radio" label="1" @change="installyes">安装</el-radio>
            <el-radio v-model="radio" label="2" @change="uninstallyes">卸载</el-radio>
          </div>
          <div style="margin: 15px 0;" />
          <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
            <el-checkbox v-for="(city,i) in cities" :key="city" :disabled="dis[i]" :label="city">{{ city }}</el-checkbox>
          </el-checkbox-group>
          <!-- dialogVisible = false" ;page()-->
          <div class="Submit">
            <el-button slot="reference" @click="submint();click1();page()">{{ loadingbuttext }}</el-button>
            <el-button slot="reference" class="tijiao-1" @click="cancel">取消</el-button>
          </div>
          <div>
            <p>安装成功节点</p>
            <p>——————————————————————————————————————————</p>
            <el-checkbox-group v-model="checkedCitiesflase">
              <el-button v-for="(city,j) in checknode1" :key="city" :disabled="dis1[j]" :label="city" :loading="loadingbut" class="buntu">{{ city }}{{ loadingbuttext }}</el-button>
            </el-checkbox-group>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="脚本名" />
          <el-table-column prop="version" label="版本" />
          <el-table-column prop="os" label="操作系统" />
          <el-table-column prop="createTime" label="创建时间" />
          <el-table-column prop="updateTime" label="修改时间" />
          <!-- <el-table-column label="部署" prop="deploy_id"> -->
          <!-- <template slot-scope="scope">
            <el-button type="primary" @click="installs(scope)">部署</el-button>
          </template> -->
          <!-- </el-table-column> -->
          <!-- <el-table-column label="进度" prop="deploy_id">
          <template>
            <el-progress :text-inside="true" :stroke-width="24" :percentage="percentages" status="success" />
          </template>
        </el-table-column> -->

          <el-table-column v-if="checkPer(['admin','pmDeploy:edit','pmDeploy:del'])" label="操作" width="240px" align="center">
            <template slot-scope="scope">
              <template>
                <!-- <el-button type="primary" class="deploy" @click="installs(scope)">部署</el-button> -->
                <udOperation
                  :data="scope.row"
                  :permission="permission"
                  class="up"
                />
                <el-button type="primary" class="bushu" @click="installs(scope)">部署</el-button>
              </template>
            </template>
          <!-- <template slot-scope="scope">
            <el-button type="primary" @click="installs(scope)">部署</el-button>
          </template> -->
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </div>
    </div>
  </div>
</template>

<script>
import crudPmDeploy from '@/api/pmDeploy/pmDeploy'
// import crudPmNode from '@/api/pm/pmNode'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
// import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import qs from 'qs'
// import { Loading } from 'element-ui'
// const loading = ''
const cityOptions = []
// var installss = ''
var scriptName = ''
var dataname = ''

const defaultForm = { deployId: null, name: null, version: null, os: null, installShell: null, deleteShell: null, createTime: null, updateTime: null }
export default {
  inject: ['reload'],
  name: 'PmDeploy',
  // rrOperation,
  components: { pagination, crudOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'gg', url: 'api/pmDeploy', idField: 'deployId', sort: 'deployId,desc', crudMethod: { ...crudPmDeploy }})
  },
  data() {
    const generateData = _ => {
      const data = []
      for (let i = 1; i <= 15; i++) {
        data.push({
          key: i,
          label: `节点 ${i}`,
          disabled: i % 4 === 0
        })
      }
      return data
    }
    return {
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      loadingbuttext: '部署',
      isShow: 'true',
      dataSu: '',
      dis1: '',
      data11: '',
      data33: '',
      percentages: '',
      timer: '',
      dis: [],
      nodeidd: [],
      install: '',
      installss: '',
      radio: '',
      time1: '',
      time3: '',
      shellchec: '',
      usenodeId: [],
      checknode: [],
      checknode1: [],
      deployId: [],
      // 选择框
      checkAll: false,
      checkedCities: [],
      checkedCitiesflase: [],
      cities: cityOptions,
      isIndeterminate: true,

      data: generateData(),
      value: [1, 4],
      dialogVisible: false,
      permission: {
        add: ['admin', 'pmDeploy:add'],
        edit: ['admin', 'pmDeploy:edit'],
        del: ['admin', 'pmDeploy:del']
      },
      rules: {
        name: [
          { required: true, message: '脚本名不能为空', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '版本不能为空', trigger: 'blur' }
        ],
        os: [
          { required: true, message: '操作系统不能为空', trigger: 'blur' }
        ],
        installShell: [
          { required: true, message: '安装脚本不能为空', trigger: 'blur' }
        ],
        deleteShell: [
          { required: true, message: '卸载脚本不能为空', trigger: 'blur' }
        ]
      }}
  },
  computed: {
    datanode() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    }
    // widthclassinfo() {
    //   if (this.screenWidth > 1300 || this.screenWidth < 2000) {
    //     return 'bodywidth1'
    //   } else {
    //     return 'bodywidth'
    //   }
    // }
  },
  // 实时监控当前页面大小
  watch: {
    datanode: {
      handler(newVal, oldVal) {
        this.getOline(Object.keys(JSON.parse(JSON.stringify(newVal))), Object.keys(JSON.parse(JSON.stringify(oldVal))))
      },
      deep: true
    },
    // that =this
    screenWidth(newold, oldval) {
      // console.log(newold, oldval)
      this.widthclassinfo2()
    }
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
  },
  methods: {

    // [CRUD.HOOK.beforeRefresh]() {
    //   var that = this
    //   crudPmNode.queryData().then(function(response) {
    //     var offlineLen = Object.keys(response).length
    //     for (var i = 0; i < offlineLen; i++) {
    //       var nodeid = Object.keys(response)[i]
    //       cityOptions[i] = nodeid
    //       that.nodeidd[i] = nodeid
    //     }
    //   })
    //   return true
    // },
    getOline(newVal, oldVal) {
      var onLine = newVal.length
      for (var i = 0; i < onLine; i++) {
        var nodeid = newVal[i]
        cityOptions[i] = nodeid
        this.nodeidd[i] = nodeid
      }
    },
    open2(data) {
      this.$notify({
        title: '提示',
        message: data,
        duration: 0
      })
    },
    create() {

    },
    page() {
      var that = this
      var times3 = setInterval(function() {
        // console.log('success' + that.data22)
        if (that.data22 === '2') {
          if (that.data11 === '1') {
            // loading.close()
            that.data22 = ''
            that.click2()
            clearInterval(times3)
            // that.reload()
            setTimeout(function() {
              that.reload()
            }, 5000)
          } else if (that.data33 === '4') {
            that.data33 = ''
            // loading.close()
            clearInterval(times3)
            setTimeout(function() {
              that.reload()
            }, 5000)
          }
        }
      }, 2000)
      this.usenodeId = []
    },
    // 取消按钮
    widthclassinfo2() {
      if (this.screenWidth > 1192) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    cancel() {
      var len = cityOptions.length
      for (var g = 0; g < len; g++) {
        this.checkedCities = []
      }
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : []
      this.isIndeterminate = false
    },
    handleCheckedCitiesChange(value) {
      this.checknode = value
      const checkedCount = value.length
      this.checkAll = checkedCount === this.cities.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length
    },
    // 卸载按钮
    uninstallyes() {
      var len = cityOptions.length
      for (var g = 0; g < len; g++) {
        this.$set(this.dis, g, true)
        this.checkedCities = []
      }
      var l = this.usenodeId.length
      var k = cityOptions.length
      console.log(this.usenodeId)
      for (var i = 0; i < l; i++) {
        for (var j = 0; j < k; j++) {
          if (parseInt(this.usenodeId[i]) === parseInt(cityOptions[j])) {
            this.$set(this.dis, j, false)
          }
        }
      }
    },
    // 安装按钮
    installyes() {
      // 点击后初始节点都默认为可选
      var len = cityOptions.length
      for (var i = 0; i < len; i++) {
        this.$set(this.dis, i, false)
        this.checkedCities = []
      }
      var j = this.usenodeId.length
      var l = cityOptions.length
      // 根据节点
      for (var h = 0; h < j; h++) {
        for (var k = 0; k < l; k++) {
          if (parseInt(this.usenodeId[h]) === parseInt(cityOptions[k])) {
            this.$set(this.dis, k, true)
          }
        }
      }
    },
    installs(data) {
      var len = cityOptions.length
      for (var i = 0; i < len; i++) {
        this.$set(this.dis, i, false)
      }
      this.percentages = 50
      console.log(this.percentages)
      this.dialogVisible = true
      this.deployId = data.row.deployId
      scriptName = data.row.name
      var j = data.row.pm.length
      for (var t = 0; t < j; t++) {
        this.usenodeId[t] = data.row.pm[t].nodeId
      }
      this.radio = '1'
      var l = this.nodeidd.length
      console.log(this.usenodeId)
      for (var h = 0; h < j; h++) {
        for (var k = 0; k < l; k++) {
          if (parseInt(this.usenodeId[h]) === parseInt(cityOptions[k])) {
            this.$set(this.dis, k, true)
          }
        }
      }
    },
    click1() {
      this.loadingbut = true
      this.loadingbuttext = '安装中'
    },
    click2() {
      this.loadingbut = false
      if (this.dataSu === '2') {
        this.loadingbuttext = '成功'
        this.loadingbut = false
      } else if (this.data11 === '1') {
        this.loadingbuttext = '失败'
        this.loadingbut = false
      }
    },
    showclick() {
      this.isShow = !this.isShow
    },
    // 确定按钮
    submint() {
      if (this.radio === '1') {
        this.install = 'install'
      } if (this.radio === '2') {
        this.install = 'uninstall'
      }
      const installss = this.install
      // console.log(installss)
      var i = this.checknode.length
      for (var j = 0; j < i; ++j) {
        // this.click2()
        this.checknode1[j] = this.checknode[j]
        var parm = { 'node_id': parseInt(this.checknode[j]), 'deploy_id': this.deployId, 'type': this.install }
        var nodeid = parseInt(this.checknode[j])
        parm = qs.stringify(parm)
        var thist = this
        crudPmDeploy.install(parm).then(function(response) {
          var that = thist
          var shellid = response.shellId
          var shellchec = '?shell_id=' + shellid + '&node_id=' + nodeid
          var times = setInterval(function() {
            crudPmDeploy.checkshell(shellchec).then(function(response) {
              if (response.state === 'success') {
                if (installss === 'install') {
                  that.data11 = '1'
                  dataname = scriptName + '安装成功'
                  that.open2(dataname)
                  // that.loading = 'false'
                  clearInterval(times)
                } else if (installss === 'uninstall') {
                  that.data11 = '1'
                  dataname = scriptName + '卸载成功'
                  that.open2(dataname)
                  // that.loading = 'false'
                  clearInterval(times)
                }
              } else if (response.state === 'failed') {
                if (installss === 'install') {
                  // that.$set(that.dis1, j, false)
                  dataname = scriptName + '安装失败'
                  that.data33 = '4'
                  that.open2(dataname)
                  that.loading = 'false'
                  clearInterval(times)
                } else if (installss === 'uninstall') {
                  that.data33 = '4'
                  dataname = scriptName + '卸载失败'
                  that.open2(dataname)
                  that.loading = 'false'
                  clearInterval(times)
                }
              } else if (response.state === 'installing') {
                if (installss === 'install') {
                  dataname = scriptName + '正在安装'
                  that.open2(dataname)
                } else if (installss === 'uninstall') {
                  dataname = scriptName + '正在卸载'
                  that.open2(dataname)
                }
              }
            })
          }, 10000)
        })
      }
      this.dataSu = '2'
    }
  }
}
</script>

<style scoped>
 body {
    margin: 0;
  }
.bodywidthmin{
  width:1860px;
  /* background: blueviolet; */
}
.bodywidthmax{
  width:100%;
}
.Submit{
  margin-bottom: 30px;
  margin-top: 30px;
}
.tijiao-1{
  /* margin-left: 10px;
  padding-right: 10px;
  padding-left: 40px; */
}
.deploy{
    /* width: 20px; */

}
.bushu{
    margin-left: -1px;
    margin-right: 7px;
    margin-top: -28px;
    float: left;
    border-radius: 6px;
    height: 27px;
}
.up{
 margin-right: 20px;
}
.buntu{
  margin-right: 3px;
}
</style>

