<template>
  <!-- <div class="Cabinets">
  <div class="bars" />
  <div class="blocks">
    <div class="racks">
    </div>
  </div>
  <div class="bars" />
  <div class="blocks">
    <div class="racks column0">
      <rack :width="'5%'" :height="'100%'" :status="statuscolor.on" />
    </div>
    <div class="racks column0">
      <rack :width="'5%'" :height="'100%'" :status="statuscolor.on" />
    </div>
  </div>
  <div class="bars" />
  <div class="flex-blocks">
    <div class="racks">
      <rack :width="'100%'" :height="'10%'" :status="statuscolor.on" />
    </div>
    <div class="racks">
      <rack :width="'100%'" :height="'10%'" />
    </div>
  </div>
  <div class="bars" />
  <div class="blocks">
    <div class="racks">
    </div>
  </div>
</div> -->
  <div :class="className" :style="{height:height,width:width}">
    <div class="light" :style="status" />
  </div>
</template>
<script>
export default {
  props: {
    className: {
      type: String,
      default: 'rack'
    },
    width: {
      type: String,
      default: '0%'
    },
    height: {
      type: String,
      default: '0%'
    },
    status: {
      type: String,
      default: 'none'
    }
  }
}
</script>
<style>
  .rack{
    position: relative;
    /* border:solid 1px; */
    /* border-bottom: black; */
    background-image: radial-gradient(circle,#dbdbdb,#9c9b9b);
    align-items: center;
    min-width: 5px;
    min-height: 5px;
    box-sizing: border-box;
  }
  .light{
    position: absolute;
    border-radius:50%;
    height:5px;width:5px;
    left: 0;right: 0;top: 0;bottom: 0;
    margin: auto;
  }

</style>
