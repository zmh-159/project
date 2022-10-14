<template>
  <div :class="widthclassinfo" style="height:100%">
    <div class="login-container">
      <vue-particles class="vue-particles" color="#dedede" :particles-number="100" shape-type="star" :particle-opacity="0.5" />
      <el-row>
        <el-col :span="7" :offset="2">
          <img src="@/assets/images/loginLogo.png" class="src">
        </el-col>
        <el-col :span="8" :offset="5" style="background-color: #3e3e3e;height: 100vh;">
          <div class="title-logo">
            <!-- <svg-icon icon-class="app" class="svg" /> -->
            <p class="title1">大规模集群监控平台</p>
            <p class="title2">PARALELL LAB</p>
          </div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-position="left" label-width="0px" class="login-form">
            <el-form-item prop="username" style="margin-top:4%">
              <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" @keyup.enter.native="handleLogin">
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%" @keyup.enter.native="handleLogin">
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" class="permission-img" @click="getCode">
              </div>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">
                记住我
              </el-checkbox>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { encrypt } from '@/utils/rsaEncrypt'
import Config from '@/settings'
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import qs from 'qs'
import Background from '@/assets/images/background.jpg'
export default {
  name: 'Login',
  data() {
    return {
      screenWidth: '',
      screenHeight: '',
      widthclassinfo: '',
      Background: Background,
      codeUrl: '',
      cookiePass: '',
      loginForm: {
        username: 'admin',
        password: '123456',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },
      loading: false,
      redirect: undefined,
      src: '@/assets/images/loginLogo.png'
    }
  },
  watch: {
    screenWidth(newold, oldval) {
      console.log(newold, oldval)
      this.widthclassinfo2()
    },
    fullHeight(val) { // 监控浏览器高度变化
      if (!this.timer) {
        this.fullHeight = val
        this.timer = true
        const that = this
        setTimeout(function() {
          that.timer = false
        }, 400)
      }
    },
    $route: {
      handler: function(route) {
        const data = route.query
        if (data && data.redirect) {
          this.redirect = data.redirect
          delete data.redirect
          if (JSON.stringify(data) !== '{}') {
            this.redirect = this.redirect + '&' + qs.stringify(data, { indices: false })
          }
        }
      },
      immediate: true
    }
  },
  created() {
    // 获取验证码
    this.getCode()
    // 获取用户名密码等Cookie
    this.getCookie()
    // token 过期提示
    this.point()
  },
  mounted() {
    this.screenWidth = document.body.clientWidth
    this.screenHeight = document.body.clientHeight
    window.onresize = () => {
      return (() => {
        this.screenWidth = document.body.clientWidth
        this.screenHeight = document.body.clientHeight
      })()
    }
  },
  destroyed() {
    // document.body.removeAttribute('class')
  },
  methods: {
    widthclassinfo2() {
      if (this.screenWidth > 1192) {
        this.widthclassinfo = 'bodywidthmax'
      } else {
        this.widthclassinfo = 'bodywidthmin'
      }
    },
    get_bodyHeight() { // 动态获取浏览器高度
      const that = this
      window.onresize = () => {
        return (() => {
          window.fullHeight = document.documentElement.clientHeight
          that.fullHeight = window.fullHeight
        })()
      }
    },
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = res.img
        this.loginForm.uuid = res.uuid
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      let password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      // 保存cookie里面的加密后的密码
      this.cookiePass = password === undefined ? '' : password
      password = password === undefined ? this.loginForm.password : password
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: ''
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        const user = {
          username: this.loginForm.username,
          password: this.loginForm.password,
          rememberMe: this.loginForm.rememberMe,
          code: this.loginForm.code,
          uuid: this.loginForm.uuid
        }
        if (user.password !== this.cookiePass) {
          user.password = encrypt(user.password)
        }
        if (valid) {
          this.loading = true
          if (user.rememberMe) {
            Cookies.set('username', user.username, { expires: Config.passCookieExpires })
            Cookies.set('password', user.password, { expires: Config.passCookieExpires })
            Cookies.set('rememberMe', user.rememberMe, { expires: Config.passCookieExpires })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch('Login', user).then(() => {
            this.loading = false
            this.$store.commit('realData')

            // 强制跳转到首页
            this.redirect = '/mydash'
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {
            this.loading = false
            this.getCode()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
      // 设置数据中心列表位置为节点列表
      this.$cookies.set('tran', 'nodeInfo')
    },
    point() {
      const point = Cookies.get('point') !== undefined
      if (point) {
        this.$notify({
          title: '提示',
          message: '当前登录状态已过期，请重新登录！',
          type: 'warning',
          duration: 5000
        })
        Cookies.remove('point')
      }
    }
  }
}
</script>
<style lang="scss" scoped>

.permission-img{
  width: 111px;
  height: 36px;
}
.login-container{
  -webkit-touch-callout:none; /*系统默认菜单被禁用*/
 -webkit-user-select:none; /*webkit浏览器*/
 -khtml-user-select:none; /*早期浏览器*/
 -moz-user-select:none;/*火狐*/
 -ms-user-select:none; /*IE10*/
 user-select:none;
  height: 100%;
   background: linear-gradient(90deg, rgba(36,65,135,0.8) 0%,rgba(62,62,62,0.9) 75%);
  }

  .background {
    // position: ab;
    position: absolute;
    width: 100%;
    height: 100%;
  }
  .src{
    // background-color: #3e3e3e;
    position: relative;
    display: block;
    margin-top: 231px;
    // transform: translateY(50%);
    // width: 100%;
    max-width: 540px;
    height: 540px;
  }
  .vue-particles
  {
    // background: linear-gradient(-90deg, rgba(36,65,135,1) 0%,rgba(153,101,222,0.8) 75%);
    width: 100%;
    // max-height: 90vh;
    position: absolute;
    animation:circle-in-hesitate 8s ;
  }
    @keyframes circle-in-hesitate {
      0% {
        clip-path: circle(0%);
      }
      40% {
        clip-path: circle(40%);
      }
      100% {
        clip-path: circle(125%);
      }
    }
    [transition-style="in:circle:hesitate"] {
      animation: 2.5s cubic-bezier(.25, 1, .30, 1) circle-in-hesitate both;
  }
  @-webkit-keyframes shadow-drop-2-center {
  0% {
    -webkit-transform: translateZ(0);
            transform: translateZ(0);
    -webkit-box-shadow: 0 0 0 0 rgb(253, 251, 255,0.8);
            box-shadow: 0 0 0 0 rgb(248, 246, 246,0.8);
  }
  100% {
    -webkit-transform: translateZ(50px);
            transform: translateZ(50px);
    -webkit-box-shadow: 0 0 30px 0px rgb(250, 249, 249,0.3);
            box-shadow: 0 0 30px 0px rgb(247, 245, 245,0.3);
  }
}
@keyframes shadow-drop-2-center {
  0% {
    -webkit-transform: translateZ(0);
            transform: translateZ(0);
    -webkit-box-shadow: 0 0 0 0 rgb(255, 253, 255,0.5);
            box-shadow: 0 0 0 0 rgb(253, 253, 253,0.5);
  }
  100% {
    -webkit-transform: translateZ(50px);
            transform: translateZ(50px);
    -webkit-box-shadow: 0 0 30px 0px rgb(255, 253, 255,0.3);
            box-shadow: 0 0 30px 0px rgb(255, 253, 255,0.3);
  }
}

  @-webkit-keyframes text-focus-in {
  0% {
    -webkit-filter: blur(12px);
            filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-filter: blur(0px);
            filter: blur(0px);
    opacity: 1;
  }
}
@keyframes text-focus-in {
  0% {
    -webkit-filter: blur(12px);
            filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-filter: blur(0px);
            filter: blur(0px);
    opacity: 1;
  }
}

  .login-code {
    width: 33%;
    display: inline-block;
    height: 38px;
    float: right;
    img{
      cursor: pointer;
      vertical-align:middle
    }
  }
    .login-form {
      position: relative;
      -webkit-animation: shadow-drop-2-center 2s cubic-bezier(0.250, 0.460, 0.450, 0.940) both ;
      animation: shadow-drop-2-center 2s cubic-bezier(0.250, 0.460, 0.450, 0.940) both ;
      border-radius: 6px;
      margin-left: 12%;
      margin-top: 5%;
      // max-height:60vh;
      height: 350px;
      width: 75%;

      background-color: rgba(244, 244, 245, 0.8);
      padding: 25px 30px 25px 30px;
    }
    .input-icon{
      height: 39px;width: 14px;margin-left: 2px;
    }
    .title-logo{
      position: relative;
      padding: 1 auto;
      margin-top: 35%;
      .title1 {
        word-break:keep-all;       /* 不换行 */
        white-space:nowrap;        /* 不换行 */
        position: relative;
        width: fit-content;
        font-size: 32px;
        margin: auto;
        color: #faf8f8;
        -webkit-animation: text-focus-in 1.5s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
      animation: text-focus-in 1.5s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
      }
      .title2 {
        overflow:hidden;
        word-break:keep-all;       /* 不换行 */
        white-space:nowrap;        /* 不换行 */
        position: relative;
        width: fit-content;
        font-size: 16px;
        margin:auto;
        // margin-top: 1%;
        color: #faf8f8;
        -webkit-animation: text-focus-in 1.5s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
      animation: text-focus-in 1.5s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
      }
    }
</style>
