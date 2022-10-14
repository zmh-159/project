<!--页面级Vue组件-->
<template>
  <div id="app">
    <keep-alive>
      <router-view v-if="isRouterAlive" />
    </keep-alive>
  </div>
</template>
<script>
export default {
  name: 'App',
  provide() { // 在祖先组件中通过 provide 提供变量
    return {
      reload: this.reload //  声明一个变量
    }
  },
  data() {
    return {
      isRouterAlive: true // 控制 router-view 是否显示达到刷新效果
    }
  },
  methods: {
    // provide中声明的变量
    reload() {
      // 通过 this.isRouterAlive 控制 router-view 达到刷新效果
      this.isRouterAlive = false
      this.$nextTick(function() {
        this.isRouterAlive = true
      })
    }
  }
}

</script>
<style>

</style>
