<template>
  <div class="Cabinets">
    <div v-for="(item,index) in items" :key="index" class="bars" @click="insert(index)" />
    <!-- <div class="bars" /> -->
    <!-- <div class="blocks">
      <div class="racks">
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
        <rack :width="'10%'" :height="'100%'"  />
      </div>
    </div> -->
    <!-- <div class="bars" /> -->
    <!-- <div class="blocks">
      <div class="racks column0">
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
        <rack :width="'5%'" :height="'100%'"  />
      </div>
      <div class="racks column0">
        <rack :width="'5%'" :height="'100%'"  />
      </div>
    </div> -->
    <!-- <div class="bars" /> -->
    <!-- <div class="flex-blocks">
      <div class="racks">
        <rack :width="'100%'" :height="'10%'"  />
      </div>
      <div class="racks">
        <rack :width="'100%'" :height="'10%'" />
      </div>
    </div> -->
    <!-- <div class="bars" /> -->
    <!-- <div class="blocks">
      <div class="racks" />
    </div> -->
  </div>
  <!-- <div :class="className" :style="{height:height,width:width}">
    <div class="light" :style="status" />
  </div> -->
</template>
<script>
import Rack from './Rack/rack.vue'
export default {
  components: {
    Rack
  },
  props: {
    uheight: {
      type: Number,
      default: 30
    }
  //   className: {
  //     type: String,
  //     default: 'rack'
  //   },
  //   width: {
  //     type: String,
  //     default: '0%'
  //   },
  //   height: {
  //     type: String,
  //     default: '0%'
  //   },
  //   status: {
  //     type: String,
  //     default: 'none'
  //   }
  },
  data() {
    return {
      items: [],
      barNum: 0
    }
  },
  mounted() {
    this.initCab()
  },
  methods: {
    initCab() {
      var that = this
      var n = this.uheight
      while (n > 0) {
        that.items.push(this.barNum++)
        n--
      }
    },
    insert(k) {
      this.$emit('bindnode', k)
    }
  }
}
</script>
<style lang="scss" scoped>
  .Cabinets{
    border: ridge 8px;
    border-color:#888181#c3c3c3;
    width: 30%;
    // border-top: solid #666363 4px;
    // border-bottom: double #3a3737 15px;
    .blocks{
      height: 80px;width: 100%;
      border-left: solid #c3c3c3 5px;
      border-right: solid #c3c3c3 5px;
      border-bottom: solid #3a3737 1px;
      .racks{
        display: flex;
        flex-direction: row;
        // background-image: linear-gradient(to bottom right,#484743,#bbb7ae);
        // border: solid #494444 1px;
        border-bottom: solid 1px;
        height: 100%;
      }
      .column0{
        height: 50%
      }
    }
    .flex-blocks{
      display: flex;
      height: 80px;width: 100%;
      border-left: solid #c3c3c3 5px;
      border-right: solid #c3c3c3 5px;
      border-bottom: solid #3a3737 1px;
      .racks{
        // transform: rotate(90deg);
        border:solid 1px;
        height: 100%;
        width: 50%;
      }
    }
    .bars{
        position: relative;
        background-image: radial-gradient(circle,#eeeeee,#c3c3c3);
        border: solid 1px;
        // box-sizing: border-box;
        // border-top: solid #000000 3px;
        // border-left: solid #666363 8px;
        // border-right: solid #666363 8px;
        height: 11px;
        box-sizing: border-box;
        cursor: pointer;
      }

  }
</style>
