<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <el-form ref="sizeForm" :model="sizeForm" label-width="80px" size="mini" class="head-top">
        <el-form-item
          label="采样(s)"
          class="widthcent"
          prop="small"
        >
          <el-input v-model="sizeForm.small" autocomplete="off" placeholder="采样为5的倍数(5s-1800s)" />
        </el-form-item>
        <el-form-item
          label="存储(s)"
          class="widthcent"
          prop="big"
        >
          <el-input v-model="sizeForm.big" autocomplete="off" placeholder="存储为采样的5倍(5s-1800s)" />
        </el-form-item>
        <div class="select">
          <el-radio v-model="radio" border label="1" :title="message">模式一</el-radio>
          <el-radio v-model="radio" border label="0" :title="message1">选择二</el-radio>
        </div>
        <el-form-item size="large">
          <el-button type="primary" @click="onSubmit(sizeForm)">部署</el-button>
          <el-button @click="resetForm(sizeForm)">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import crudPmDeploy from '@/api/pmDeploy/pmDeploy'

export default {
  data() {
    return {
      message: '按照采样间隔进行采样，退出时按照存储间隔进行存储',
      message1: '用户进入时，按照采样间隔进行采样，退出时按照存储间隔进行存储',
      radio: '',
      sizeForm: {
        small: '',
        big: ''
      }
    }
  },
  methods: {
    resetForm(sizeForm) {
      sizeForm.small = ''
      sizeForm.big = ''
      this.radio = ''
    },
    onSubmit(sizeForm) {
      console.log(sizeForm.small % 5)
      if ((sizeForm.small === '' || sizeForm.big === '' || this.radio === '') || (sizeForm.small % 5 !== 0)) {
        alert('采样不能为空且必须是5的倍数')
      } else {
        if ((sizeForm.small % 5 === 0 && sizeForm.big % sizeForm.small === 0) && sizeForm.small >= 5 && sizeForm.small <= 1800) {
          var that = this
          var parm = { 'mode': that.radio, 'big_interval': sizeForm.big, 'small_interval': sizeForm.small }
          crudPmDeploy.concheng(parm).then(function(response) {
            crudPmDeploy.concheck().then(function(response) {
              console.log(response)
              alert('成功')
            })
          })
        }
      }
      sizeForm.small = ''
      sizeForm.big = ''
    }
  }
}
</script>

<style scoped>
 .count{
    text-align: center;
    margin-top: 3%;
    margin-left: -83%;
    margin-bottom: 2%;
 }
 .submit{
   margin-left: 1%;
 }
 .widthcent{
   width: 23%;
   height: 30px;
 }
 .head-top{
   margin-top: 3%;
   /* padding-top: 50px; */
 }
 .select{
   margin-left: 5.4%;
   margin-bottom: 1%;
 }
</style>
