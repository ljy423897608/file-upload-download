// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
// axios安装命令  npm install --save axios
import axios from 'axios' // 引入axios
// mint-ui安装命令  npm install mint-ui -S
import MintUI from 'mint-ui'     // 引入mint-ui
import 'mint-ui/lib/style.css'  // 引入mint-ui的样式

Vue.use(MintUI);
Vue.config.productionTip = false
// 设置全局变量
Vue.prototype.$ajax = axios
console.log('测试合并回滚1')

/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>'
})
