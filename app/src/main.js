import Vue from 'vue';
import App from './App.vue'

import vuetify from './vuetify';
import store from './store';
import router from './router';

import './directives';
import './filters';
import './mixins';

import panzoom from 'vue-panzoom';
import moment from 'vue-moment';

import shc from './modules/shc';
import shp from './modules/shp';

Vue.use(panzoom);
Vue.use(moment);

Vue.component('SHC', shc);
Vue.component('SHP', shp);

Vue.config.productionTip = false;

window.vm = new Vue({
  vuetify,
  store,
  router,
  render: h => h(App),
}).$mount('#app')
