<!--分页-->
<template>
  <el-pagination
    :pager-count="5"
    :page-size.sync="page.size"
    :total="page.total"
    :current-page.sync="page.page"
    style="margin:8px 20px 0 0;float:right"
    layout="total, prev, pager, next, sizes,jumper"
    background
    @size-change="crud.sizeChangeHandler($event)"
    @current-change="crud.pageChangeHandler"
  />
</template>
<script>
import { pagination } from '@crud/crud'
export default {
  mixins: [pagination()]
}
</script>
