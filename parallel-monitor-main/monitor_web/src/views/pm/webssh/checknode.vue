<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div class="divMain">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="磁盘检测" name="first">
            <div class="nodeall">
              <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
              <div style="margin: 15px 0;" />
              <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
                <el-checkbox v-for="city in cities" :key="city" :label="city" @click="elclick">{{ city }}</el-checkbox>
              </el-checkbox-group>
            </div>
          </el-tab-pane>
          <el-tab-pane label="内存检测" name="second">
            <div class="nodeall">
              <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
              <div style="margin: 15px 0;" />
              <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
                <el-checkbox v-for="city in cities" :key="city" :label="city" @click="elclick">{{ city }}</el-checkbox>
              </el-checkbox-group>
            </div>
          </el-tab-pane>
          <el-tab-pane label="cpu监测" name="third">
            <div class="nodeall">
              <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
              <div style="margin: 15px 0;" />
              <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
                <el-checkbox v-for="city in cities" :key="city" :label="city" @click="elclick">{{ city }}</el-checkbox>
              </el-checkbox-group>
            </div>
          </el-tab-pane>
          <el-tab-pane label="显卡监测" name="fourth">
            <el-cascader :options="options" :show-all-levels="false" />
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="tijiao">
        <p>{{ Memory }}</p>
        <el-button class="submmit" @click="submmit">默认按钮</el-button>
        <el-button class="submmit" @click="elclick">按钮</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import crudPmDeploy from '@/api/pmDeploy/pmDeploy'
const cityOptions = []
export default {
  data() {
    return {
      options: [],
      activeName: 'first',
      Memory: '',
      checkedCities: [],
      checkList: [],
      labelPosition: 'right',
      checkAll: false,
      // checkedCities: ['上海', '北京'],
      cities: cityOptions,
      isIndeterminate: true,
      formLabelAlign: {
        name: '',
        region: '',
        type: ''
      }
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
        this.onlinenode(newVal, oldVal)
      }
    },
    immediate: true
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
      console.log(this.activeName)
    },
    elclick() {
      crudPmDeploy.nodeDiskList().then(function(response) {
        this.options = response
      })
      var data1 = '?nodeId=' + this.checkedCities[0]
      crudPmDeploy.nodeDisk(data1).then(function(response) {
        var len = response.content.length
        for (var i = 0; i < len; i++) {
          console.log(response.content[i].name)
        }
      })
    },
    submmit() {
      var data1 = '?nodeId=' + this.checkedCities[0]
      crudPmDeploy.nodeDisk(data1).then(function(response) {
        var len = response.content.length
        for (var i = 0; i < len; i++) {
          console.log(response.content[i].name)
        }
      })
      if (this.activeName === 'first') {
        crudPmDeploy.checkdisk(data).then(function(response) {
          console.log(response)
        })
      } else if (this.activeName === 'second') {
        var data = '?nodeId=' + this.checkedCities[0]
        var that = this
        crudPmDeploy.checkmemory(data).then(function(response) {
          if (response !== null) {
          //    console.log(that.checkList[0])
          // console.log(data)
          // console.log(111111111111)
            console.log(response.res)
            that.Memory = '节点内存' + response.res
          }
        })
      }
    },
    onlinenode(newVal, oldVal) {
      var len = Object.keys(JSON.parse(JSON.stringify(newVal))).length
      var data = Object.keys(newVal)
      for (var i = 0; i < len; i++) {
        // var nodeid = data[i]
        cityOptions[i] = data[i]
      }
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : []
      this.isIndeterminate = false
    },
    handleCheckedCitiesChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.cities.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.divMain .change{
  margin-top: 40px;
  text-align: center;
  margin-top: 200px;
}
.tijiao{
  margin-top: 30px;
}
.change .line{
  margin-top: 12px;
}
.nodeall{
 margin-left: 10px;
 text-align: left;
 padding-top: 30px;
 /* float: left; */
 width: 970px;
}
.divMain{
  border: 1px rebeccapurple;
  text-align: center;
  height: 200px;
  width: 900px;

}
.submmit{
  margin-top: 20px;
}
</style>
