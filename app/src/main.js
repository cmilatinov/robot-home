import Vue from 'vue'
import App from './App.vue'

import vuetify from './vuetify';
import store from './store';
import router from './router';

import './directives';
import './filters';
import './mixins';

import network from './helpers/network';

Vue.prototype.$network = network;

Vue.config.productionTip = false;

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App),
}).$mount('#app')
