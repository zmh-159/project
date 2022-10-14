<template>
  <div style="height:fit-content" :style="styjson" class="sub-title">
    <div v-for="(item,i) in items" :key="i" class="all" :style="stylew">
      <div class="top-title">{{ topTitle[i] }}</div>
      <div class="bottom-title">{{ subTitle[i] }}</div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    topTitle: {
      type: Array,
      default: () => { return [] }
    },
    subTitle: {
      type: Array,
      default: () => { return [] }
    },
    styjson: {
      type: String,
      default: '大标题'
    },
    titleNumber: {
      type: Number,
      default: 3
    }
  },
  data() {
    return {
      items: this.titleNumber,
      stylew: 'width:' + 1 / this.titleNumber * 100 + '%'
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.sub-title{
    text-align: center;  /* 文本居中 */
    display: flex;
    .all{
        .top-title{
            font-size: 16px;
        }
        .bottom-title{
            font-size: 18px;
            margin-top: 1%;
        }
    }
}
</style>

