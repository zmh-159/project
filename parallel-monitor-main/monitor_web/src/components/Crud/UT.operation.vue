<template>
  <div>
    <el-button v-permission="permission.edit" :loading="crud.status.cu === 2" :disabled="disabledEdit" size="mini" type="primary" icon="el-icon-edit" @click="doRoute(data)" />
    <el-button v-permission="permission.edit" :loading="crud.status.cu === 2" :disabled="disabledEdit" size="mini" type="primary" icon="el-icon-edit" @click="crud.toEdit(data)" />
    <!-- <el-button v-permission="permission.edit" :loading="crud.status.cu === 2" :disabled="disabledEdit" size="mini" type="primary" icon="el-icon-edit" @click="doChart(data)" /> -->
  </div>
</template>
<script>
import CRUD, { crud } from '@crud/crud'
import Cookie from 'js-cookie'
export default {
  mixins: [crud()],
  props: {
    data: {
      type: Object,
      required: true
    },
    permission: {
      type: Object,
      required: true
    },
    disabledEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      pop: false
    }
  },
  methods: {
    doRoute(data) {
      Cookie.set('nodeUuid', data.uuid)
      this.$router.push({ path: '/pmNode/pmNodeInfo' })
    },
    doChart(data) {
      this.$router.push({ path: '/pmNode/nodeChart', query: { nodeUuid: data.uuid }})
    },
    // doCancel() {
    //   this.pop = false
    //   this.crud.cancelDelete(this.data)
    // },
    // toDelete() {
    //   this.pop = true
    // },
    [CRUD.HOOK.afterDelete](crud, data) {
      if (data === this.data) {
        this.pop = false
      }
    }
    // onPopoverShow() {
    //   setTimeout(() => {
    //     document.addEventListener('click', this.handleDocumentClick)
    //   }, 0)
    // },
    // onPopoverHide() {
    //   document.removeEventListener('click', this.handleDocumentClick)
    // },
    // handleDocumentClick(event) {
    //   this.pop = false
    // }
  }
}
</script>
