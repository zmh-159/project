<template>
  <ul class="timeline-wrapper">
    <li v-for="t in timelineList" :key="t.id" class="timeline-item">
      <div class="timeline-box">
        <div class="out-circle">
          <div class="in-circle" />
        </div>
        <div class="long-line" />
      </div>
      <div class="timeline-content">
        <div class="timeline-date">{{ t.createTime }}</div>
        <!-- <div class="timeline-title">{{ t.createTime }}</div> -->
        <div class="timeline-desc">{{ t.outlier }}</div>
      </div>
    </li>
  </ul>
</template>
<script type="text/babel">
import Vue from 'vue'
export default Vue.component('Timeline', {
  name: 'Timeline',
  props: {
    timelineList: {
      type: Array,
      default: () => {
        return []
      }
    }
  }
})
</script>
<style scoped lang="scss">
    ul.timeline-wrapper {
        list-style: none;
        margin: 0;
        padding: 0;
    }

    /* 时间线 */
    .timeline-item {
        position: relative;

        .timeline-box {
            text-align: center;
            position: absolute;

            .out-circle {
                width: 16px;
                height: 16px;
                background: rgba(14, 116, 218, 0.1);
                box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
                /*opacity: 0.1;*/
                border-radius: 50%;
                display: flex;
                align-items: center;

                .in-circle {
                    width: 8px;
                    height: 8px;
                    margin: 0 auto;
                    background: rgb(218, 45, 14);
                    border-radius: 50%;
                    box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
                }
            }
.long-line {
                width: 2px;
                height: 40px;
                background: rgb(228, 5, 42);
                box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
                opacity: 0.1;
                margin-left: 8px;
            }
        }

        .timeline-content {
            box-sizing: border-box;
            margin-left: 20px;
            height: 36px;
            padding: 0 0 0 20px;
            text-align: left;
            margin-bottom: 30px;

            .timeline-title {
                font-size: 14px;
                word-break: break-all;
                margin-bottom: 16px;
                color: #333;
                font-weight: 500;
                /*display: inline;*/
            }
       .timeline-date {
                font-size: 16px;
                color: #333;
                font-weight: 500;
                margin-bottom: 16px;
            }
            .timeline-desc {
                font-size: 14px;
                color: #999999;
            }
        }

    }

    .timeline-item:last-of-type .timeline-content {
        margin-bottom: 0;
    }
</style>
