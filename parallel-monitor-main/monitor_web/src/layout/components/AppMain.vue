<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
    <div v-if="$store.state.settings.showFooter" id="el-main-footer">
      <span v-html="$store.state.settings.footerTxt" />
      <span> ⋅ </span>
      <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">{{ $store.state.settings.caseNumber }}</a>
    </div>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 70px);
  width: 100%;
  position: relative;
  overflow: hidden;
  // padding: 100px 5px;

}

.fixed-header+.app-main {
  padding-top: 50px;
}

.hasTagsView {
  // margin-top: 84px;
  .app-main {
    /* 84 = navbar + tags-view = 70 + 34 */
    min-height: calc(100vh - 30px);
    // background-color:#F3F5F9 ;
  }
  // 调整appmian的上边距
  .fixed-header+.app-main {
    padding-top: 104px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
