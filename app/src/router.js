import Vue from 'vue';
import Router from 'vue-router';

import dashboard from './views/dashboard';
import login from './views/login';
import register from './views/register';

Vue.use(Router);

export default new Router({
    mode: `history`,
    routes: [
        {
            path: '/',
            name: 'dashboard',
            component: dashboard
        },
        {
            path: '/login',
            name: 'login',
            component: login
        },
        {
            path: '/register',
            name: 'register',
            component: register
        }
    ]
});