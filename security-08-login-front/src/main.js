import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'

Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  render: h => h(App),
}).$mount('#app')
