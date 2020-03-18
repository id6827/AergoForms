import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import eventBus from "./plugins/GlobalEventBus";
import router from './router'
import "./plugins/axios";

Vue.config.productionTip = false

Vue.use(eventBus); // 이벤트 버스 플러그인

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')