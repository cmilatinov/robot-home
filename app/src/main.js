import Vue from 'vue';
import App from './App.vue'

import vuetify from './vuetify';
import store from './store';
import router from './router';

import './directives';
import './filters';
import './mixins';

import panzoom from 'vue-panzoom';
import VueMoment from 'vue-moment';
import moment from 'moment';
import { extendMoment } from 'moment-range';

import shc from './modules/shc';
import shp from './modules/shp';
import shh from './modules/shh';

Vue.use(panzoom);
Vue.use(VueMoment, { moment: extendMoment(moment) });

Vue.component('SHC', shc);
Vue.component('SHP', shp);
Vue.component('SHH', shh);

Vue.config.productionTip = false;

window.vm = new Vue({
  vuetify,
  store,
  router,
  render: h => h(App),
}).$mount('#app')
