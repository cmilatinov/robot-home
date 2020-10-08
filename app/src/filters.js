import Vue from 'vue';

import moment from 'moment';

export function datetime(value) {
    return moment(value).format('LLL');
}

Vue.filter('datetime', datetime);