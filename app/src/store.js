import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        houseLayout: null,
        users: [
            {
                id: 1,
                title: "Parent"
            },
            {
                id: 2,
                title: "Child"
            },
            {
                id: 3,
                title: "Guest"
            },
            {
                id: 4,
                title: "Strangers"
            }
        ]
    },
    mutations: {
        update() {}
    },
    actions: {

    },
    getters: {

    }
});