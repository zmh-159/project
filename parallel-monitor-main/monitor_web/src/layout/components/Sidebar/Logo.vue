<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <div v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 v-else class="sidebar-title">{{ title }} </h1>
      </div>
      <div v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 class="sidebar-title">{{ title }} </h1>
      </div>
    </transition>
  </div>
</template>

<script>
import Logo from '@/assets/images/wuzhoulogo.png'
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      // title: '相空间智能监控系统',
      title: '大规模集群监控平台',
      logo: Logo
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  // width: 100%;
  // height: 100px;
  line-height: 50px;
  text-align: center;
  overflow: hidden;
  box-sizing:border-box;
  -moz-box-sizing:border-box; /* Firefox */
  -webkit-box-sizing:border-box; /* Safari */
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 100%;
      height: 50px;
      vertical-align: middle;
      margin-right: 6px;
    }

    & .sidebar-title {
      // display: inline-block;
      margin: 0;
      color: #6A7C88;
      font-weight: bold;
      line-height: 50px;
      font-size: 16px;
      // font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      opacity: 0;
    }
    .sidebar-title{
      opacity: 0;
    }
  }
}
</style>
