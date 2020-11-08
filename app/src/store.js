import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        simulation: null,

        showMovePerson: false,
        showDeletePerson: false,

        editPerson: null,

        logs: []
    },
    mutations: {
        setSimulation(state, simulation) {
            state.simulation = simulation;
        },
        setLogs(state, logs) {
            state.logs = logs;
        },
        setShowMovePerson(state, value) {
            state.showMovePerson = !!value;
        },
        setShowDeletePerson(state, value) {
            state.showDeletePerson = !!value;
        },
        onMovePerson(state, person) {
            state.editPerson = { ...person };
            state.showMovePerson = true;
        },
        onDeletePerson(state, person) {
            state.editPerson = { ...person };
            state.showDeletePerson = true;
        }
    },
    actions: {

    },
    getters: {

    }
});