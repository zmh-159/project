<template>
  <div class="top-navbar">
    <div class="top-logo">
      <el-dropdown :class="{'collapse':collapse}" class="avatar-container right-menu-item hover-effect" trigger="click">
        <div v-if="collapse" key="collapse" class="avatar-wrapper">
          <img :src="user.avatarName ? baseApi + '/api/avatar/' + user.avatarName : Avatar" class="user-avatar">
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/center">
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
        </div>
        <div v-else key="expand" class="avatar-wrapper">
          <img :src="user.avatarName ? baseApi + '/api/avatar/' + user.avatarName : Avatar" class="user-avatar">
          <!-- <div class="user-name"> -->
          <div class="user-name">
            <div class="user-name-1">
              {{ time | dateTranGreetings }}
            </div>
            <div class="user-name-2">
              {{ user.username }}
            </div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/center">
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
        </div>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Avatar from '@/assets/images/avatar.png'

export default {
  components: {
  },
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      Avatar: Avatar,
      dialogVisible: false,
      content: '收起菜单',
      time: new Date()
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
    }
  },
  created() {
    // var hour, minite, second
    // if (this.time.getMinutes() < 10) {
    //   minite = '0' + this.time.getMinutes()
    // } else { minite = this.time.getMinutes() }
    // if (this.time.getHours() < 10) {
    //   hour = '0' + this.time.getHours()
    // } else { hour = this.time.getHours() }
    // if (this.time.getSeconds() < 10) {
    //   second = '0' + this.time.getSeconds()
    // } else { second = this.time.getSeconds() }
    // this.time = hour + ':' + minite + ':' + second
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
.top-navbar {
  height: 70px;
  overflow: hidden;
  position: relative;
  background: #F3F5F9;
  .top-logo {
    // float: right;
    height: 100%;
    line-height: 50px;
    position: relative;
    margin-left: -5%;
    text-align: center;
    &:focus {
      outline: none;
    }

    .avatar-container {
      // margin-right: 30px;
      // display: flex;
      .avatar-wrapper {
        // height: fit-content;
        // width: fit-content;
        margin-top: 5px;
        position: relative;
        height: 80px;
        width: 100%;
        display: flex;
        align-items: center; /*定义body的元素垂直居中*/
        justify-content: center; /*定义body的里的元素水平居中*/
        word-break:keep-all;       /* 不换行 */
        white-space:nowrap;        /* 不换行 */
        .user-avatar {
          cursor: pointer;
          width: 32px;
          height: 32px;
          border-radius: 15px;
          &:hover {
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.3);

          }
        }
        .user-name{
          // line-height: 15px;
           line-height: 50px;
          margin-left: 10%;
          text-align: left;
          cursor:default;
          .user-name-1{
            font-size: 14px;
            line-height: 25px;
          }
          .user-name-2{
            font-size: 16px;
            font-weight: bold;
            line-height: 20px;
          }
        }
      }
    }
    & .collapse{
      .avatar-wrapper {
        // height: fit-content;
        // width: fit-content;
        margin-top: 5px;
        position: relative;
        height: 80px;
        width: 100%;
        display: flex;
        align-items: center; /*定义body的元素垂直居中*/
        justify-content: center; /*定义body的里的元素水平居中*/
        word-break:keep-all;       /* 不换行 */
        white-space:nowrap;        /* 不换行 */
        .user-avatar {
          cursor: pointer;
          width: 32px;
          height: 32px;
          border-radius: 15px;
        }
      }
    }
  }
}
</style>
