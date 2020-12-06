import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        simulation: null,
        hvacStates: [],
        colorCodedTemperature: false,

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
        setRoomTemperatures(state, rooms) {
            if (!state.simulation)
                return;
            state.simulation.houseLayout.rooms.forEach(room => {
                let roomFound = rooms.find(r => r.id === room.id);
                if (roomFound)
                    room.temperature = roomFound.temperature;
            });
            state.hvacStates = rooms.map(r => ({ id: r.id, hvac: r.hvac }));
        },
        setShowMovePerson(state, value) {
            state.showMovePerson = !!value;
        },
        setShowDeletePerson(state, value) {
            state.showDeletePerson = !!value;
        },
        setColorCodedTemp(state, value) {
            state.colorCodedTemperature = !!value;
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