import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css'

import Element from 'element-ui'
//
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

// 数据字典
import dict from './components/Dict'

// 权限指令
import checkPer from '@/utils/permission'
import permission from './components/Permission'
import './assets/styles/element-variables.scss'
// global css
import './assets/styles/index.scss'

// 代码高亮
import VueHighlightJS from 'vue-highlightjs'
import 'highlight.js/styles/atom-one-dark.css'

import App from './App'
import store from './store'
import router from './router/routers'

import './assets/icons' // icon
import './router/index' // permission control
import 'echarts-gl'
import { setCookie, getCookie, delCookie } from '@/api/cookie.js'
import VueParticles from 'vue-particles'
Vue.use(VueParticles)
import VueCookies from 'vue-cookies'
// import { create, createOrUpdateImage } from 'echarts-gl'
// 引入自定义字体
import './assets/fonts/fonts.scss'
// 引入翻转插件
import 'vue-flipper/dist/vue-flipper.css'
import Flipper from 'vue-flipper'
Vue.component('flipper', Flipper)

// 自动滚屏
import scroll from 'vue-seamless-scroll'
Vue.use(scroll)
// 注册全局过滤器
import * as filters from '@/filters/common.js'
Object.keys(filters).forEach(key => Vue.filter(key, filters[key]))

// 注册自定义指令
import myDirective from './components/MyDirective/common.js'
Vue.directive(myDirective)

Vue.use(VueCookies)
Vue.use(checkPer)
Vue.use(VueHighlightJS)
Vue.use(mavonEditor)
Vue.use(permission)
Vue.use(dict)

Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})
Vue.prototype.$cookieStore = {
  setCookie,
  getCookie,
  delCookie
}

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
