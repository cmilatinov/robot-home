import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        houseLayout: null,
        simulation: null,
        users: [
            {
                getId: () => 1,
                getName: () => "Parent"
            },
            {
                getId: () => 2,
                getName: () => "Child"
            },
            {
                getId: () => 3,
                getName: () => "Guest"
            },
            {
                getId: () => 4,
                getName: () => "Strangers"
            }
        ]
    },
    mutations: {
        update() {},
        setSimulation(state, simulation) {
            state.simulation = simulation;
        }
    },
    actions: {

    },
    getters: {

    }
});