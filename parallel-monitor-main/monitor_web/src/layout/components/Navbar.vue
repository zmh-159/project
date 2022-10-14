<template>
  <div class="navbar">
    <!-- <el-tooltip class="item" effect="dark" :content="content" placement="right" hide-after="0"> -->
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <!-- </el-tooltip> -->
    <!-- <breadcrumb id="breadcrumb-container" class="breadcrumb-container" /> -->

    <div class="right-menu">
      <div class="my-row">
        <div v-for="(index,i) in softwareInfo" :key="i" :span="3" class="my-col">
          <!-- <div class="my-col-content">
            <svg-icon :icon-class="index.img" />
            {{ index.name }}:<div class="top-number">{{ index.value | unitFilter(index.unit) }}</div>
          </div> -->
        </div>
        <div :span="6" class="time-col">
          <span>
            {{ nowTime }}
          </span>
        </div>
      </div>
    </div>
    <!-- <search id="header-search" class="right-menu-item" /> -->

    <!-- <el-tooltip content="项目文档" effect="dark" placement="bottom">
          <Doc class="right-menu-item hover-effect" />
        </el-tooltip> -->

    <!-- <el-tooltip content="全屏缩放" effect="dark" placement="bottom">
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </el-tooltip> -->

    <!-- <el-tooltip content="布局设置" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->

    <!-- <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="user.avatarName ? baseApi + '/api/avatar/' + user.avatarName : Avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown"> -->
    <!-- <span style="display:block;" @click="show = true">
            <el-dropdown-item>
              布局设置
            </el-dropdown-item>
          </span> -->
    <!-- <router-link to="/user/center">
            <el-dropdown-item>
              个人中心
            </el-dropdown-item>
          </router-link>
          <span style="display:block;" @click="open">
            <el-dropdown-item divided>
              退出登录
            </el-dropdown-item>
          </span>
        </el-dropdown-menu>
      </el-dropdown> -->

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
// import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import tools from '@/utils/common.js'
// import Doc from '@/components/Doc'
// import Screenfull from '@/components/Screenfull'
// import SizeSelect from '@/components/SizeSelect'
// import Search from '@/components/HeaderSearch'
import Avatar from '@/assets/images/avatar.png'

export default {
  components: {
    // Breadcrumb,
    Hamburger
    // Screenfull
    // Search
    // SizeSelect,
    // Doc
  },
  // mixins: [realTime()],
  data() {
    return {
      Avatar: Avatar,
      dialogVisible: false,
      content: '收起菜单',
      softwareInfo: [{
        img: '磁盘',
        name: '磁盘已使用',
        value: '0',
        unit: 'TB'
      }, {
        img: 'CPU',
        name: 'CPU数量',
        value: 0,
        unit: ' '
      }, {
        img: 'CPU',
        name: 'CPU使用率',
        value: '0%',
        unit: ' '
      }, {
        img: '显卡',
        name: '显卡使用率',
        value: '0%',
        unit: ' '
      }, {
        img: '磁盘',
        name: '磁盘使用率',
        value: '0%',
        unit: ' '
      }, {
        img: '内存条',
        name: '内存使用率',
        value: '0%',
        unit: ' '
      }],
      nowTime: null,
      timerInter: null
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'device',
      'user',
      'baseApi'
    ]),
    show: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    totalInfo() {
      return JSON.parse(JSON.stringify(this.$store.state.totalInfo))
    }
  },
  watch: {
    totalInfo: {
      // that.state.cpuBusy, that.state.cpuNums, that.state.diskTotal, that.state.diskUse, that.state.diskTotal, that.state.gpuUtilization, that.state.memoryTotal, that.state.memoryUsed]
      handler(newVal, oldVal) {
        this.softwareInfo[0].value = newVal[3].toFixed(2)
        this.softwareInfo[1].value = newVal[1].toFixed(0)
        this.softwareInfo[2].value = newVal[0].toFixed(2) + '%'
        this.softwareInfo[3].value = newVal[5].toFixed(2) + '%'
        this.softwareInfo[4].value = (newVal[7] / newVal[6]).toFixed(2) + '%'
        this.softwareInfo[5].value = (newVal[3] / newVal[4]).toFixed(2) + '%'
      },
      deep: true
    }
  },
  created() {
    this.nowTime = tools.getNowTime()
    var that = this
    this.timerInter = setInterval(function() {
      that.nowTime = tools.getNowTime()
    }, 1000)
  },
  beforeDestroy() {
    clearInterval(this.timerInter)
  },
  methods: {
    toggleSideBar() {
      if (this.content === '展开菜单') { this.content = '收起菜单' }
      if (this.content === '收起菜单') { this.content = '展开菜单' }
      this.$store.dispatch('app/toggleSideBar')
    },
    open() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.logout()
      })
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()
      })
    }
  }
}
</script>

<style lang="scss" scoped>

.navbar {
  height: 70px;
  overflow: hidden;
  display: flex;
  align-items: center; /*定义body的元素垂直居中*/
  background: #F3F5F9;
  font-size: 18px;
  // box-shadow: 0 1px 4px rgba(0,21,41,.08);
  // border:1px #409EFF;
  // border:10px solid #409EFF;

  .hamburger-container {
    height: fit-content;
    float: left;
    width: fit-content;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;
    display: flex;
      align-items: center; /*定义body的元素垂直居中*/
    // margin-top: 1%;

    &:hover {
      background: rgba(0, 0, 0, .1)
    }
  }

  .breadcrumb-container {
    margin-top: 1%;
    // float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;

  }

  .right-menu {
    // float: right;
    height: 100%;
    // line-height: 50px;
    position: relative;
    display: inline-block;
    margin-left: 2%;
    width: 97%;
    &:focus {
      outline: none;
    }
    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 16px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }
    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
    .my-row{

      cursor: default;
      height: 100%;
      width: 100%;
      display: flex;
      align-items: center; /*定义body的元素垂直居中*/
      color: #606266;
      word-break:keep-all;       /* 不换行 */
      white-space:nowrap;        /* 不换行 */
      .my-col{
        width: 8.8%;
        display: inline-block;
        line-height: 20px;
        .my-col-content{
          width: fit-content;
          .top-number{
            display: inline-block;
            font-size: 24px;
            font-weight: bold;
            font-family: 'tech-font-1';
          }
        }
      }
      .my-col:not(:first-of-type){
        margin-left: 5%;
      }
      .time-col{
        width: 30%;
        font-size: 24px;
        font-weight: bold;
        font-family: 'tech-font-1';
        // color: #409EFF;
        span{
          float: right;
          margin-right: 5%;
        }
      }
    }

  }
}
</style>
