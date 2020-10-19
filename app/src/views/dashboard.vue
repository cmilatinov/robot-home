<template>
    <v-container fluid class="h-100 py-0 d-flex flex-column">
        <v-row style="flex: 5;">
            <v-col cols="3" class="h-100 pr-0">
                <v-card class="h-100 main-card" flat outlined>
                    <v-card-title>
                        <v-icon class="primary--text">fa-project-diagram</v-icon>
                        Simulation
                    </v-card-title>

                    <v-subheader class="text-uppercase">User Position</v-subheader>
                    <v-select outlined dense class="mx-4 mt-0 pt-0"
                              prepend-inner-icon="mdi-crosshairs-gps"
                              :value="userLocation"
                              @change="dispatchEvent('setUserLocation', { userLocation: $event })"
                              item-value="id"
                              item-text="name"
                              :items="roomLocations"
                              :menu-props="{ offsetY: true }">
                    </v-select>

                    <v-subheader class="text-uppercase form-subheader">Active Profile</v-subheader>
                    <v-select outlined dense class="mx-4 mt-0 pt-0"
                              prepend-inner-icon="fa-user-tag"
                              v-if="activeUserProfile"
                              :value="activeUserProfile.id"
                              @change="dispatchEvent('setActiveProfile', { id: $event })"
                              item-value="id"
                              item-text="name"
                              :items="userProfiles"
                              :menu-props="{ offsetY: true }">
                    </v-select>

                    <v-subheader class="px-4 text-uppercase w-100 form-subheader">
                        <div class="w-100">
                            Simulation<strong class="float-right white--text">{{simulationRunning ? 'Running' : 'Stopped'}}</strong>
                        </div>
                    </v-subheader>
                    <v-container fluid class="h-100 py-0 d-flex flex-column overflow-auto">
                        <v-btn class="mx-3 mb-2" :color="simulationRunning ? 'red darken-4' : 'primary darken-1'" @click="dispatchEvent('toggleSimulation', { value: !simulationRunning })">
                            <template v-if="simulationRunning">
                                <v-icon class="mr-2">fa-stop</v-icon> Stop
                            </template>
                            <template v-else>
                                <v-icon class="mr-2">fa-play</v-icon> Start
                            </template>
                        </v-btn>
                        <v-form>
                            <v-menu v-model="timeMenu"
                                    :close-on-content-click="false"
                                    transition="scale-transition"
                                    :nudge-left="150"
                                    origin="top left"
                                    right
                                    offset-x
                                    max-width="290px"
                                    min-width="290px">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field
                                            :value="simulationTime"
                                            label="Time"
                                            class="mx-2"
                                            prepend-inner-icon="mdi-clock-time-four-outline"
                                            readonly
                                            :rules="[validationRules.required]"
                                            required
                                            v-bind="attrs"
                                            v-on="on"></v-text-field>
                                </template>
                                <v-time-picker
                                        v-if="timeMenu"
                                        :value="simulationTime"
                                        full-width
                                        required
                                        @input="dispatchEvent('setSimulationDateTime', { value: `${simulationDate} ${$event}` })">
                                </v-time-picker>
                            </v-menu>
                            <v-menu v-model="dateMenu"
                                    :close-on-content-click="false"
                                    transition="scale-transition"
                                    :nudge-left="150"
                                    origin="top left"
                                    right
                                    offset-x
                                    min-width="290px">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field
                                            :value="simulationDate"
                                            label="Date"
                                            class="mx-2"
                                            prepend-inner-icon="mdi-calendar"
                                            readonly
                                            :rules="[validationRules.required]"
                                            required
                                            v-bind="attrs"
                                            v-on="on"></v-text-field>
                                </template>
                                <v-date-picker
                                        :value="simulationDate"
                                        no-title
                                        scrollable
                                        @input="dispatchEvent('setSimulationDateTime', { value: `${$event} ${simulationTime}` })">
                                    <v-spacer></v-spacer>
                                    <v-btn  text
                                            color="primary"
                                            @click="dateMenu = false">
                                        OK
                                    </v-btn>
                                </v-date-picker>
                            </v-menu>

                            <v-subheader class="text-uppercase" style="margin-top: -10px; margin-left: -12px;">Temperature</v-subheader>
                            <v-subheader style="margin-top: -10px; margin-bottom: -10px;">
                                <v-icon class="mr-3">fa-thermometer-empty</v-icon>
                                <span class="f-10 w-100">Outside<strong class="float-right white--text">{{ outsideTemperature }} &deg;C</strong></span>
                            </v-subheader>
                            <v-slider
                                    color="info"
                                    :min="-30"
                                    :max="35"
                                    :value="outsideTemperature"
                                    thumb-label
                                    @change="dispatchEvent('setOutsideTemp', { value: $event })"></v-slider>
                            <v-subheader class="form-subheader" style="margin-bottom: -10px;">
                                <v-icon class="mr-3">fa-thermometer-half</v-icon>
                                <span class="f-10 w-100">Inside<strong class="float-right white--text">{{ insideTemperature }} &deg;C</strong></span>
                            </v-subheader>
                            <v-slider
                                    color="info"
                                    :min="-30"
                                    :max="35"
                                    :value="insideTemperature"
                                    thumb-label
                                    @change="dispatchEvent('setInsideTemp', { value: $event })"></v-slider>
                        </v-form>
                    </v-container>
                </v-card>
            </v-col>
            <v-col cols="9" class="h-100 d-flex flex-column">
                <v-card style="flex: 7;" class="h-100 mb-3 main-card d-flex flex-column" flat outlined>
                    <v-card-title>
                        <v-icon class="primary--text">fa-ruler-combined</v-icon>
                        House Layout
                    </v-card-title>
                    <house-layout>
                        <house-layout-room :key="room.name" v-for="room in rooms" :room="room"
                                           @movePerson="$store.commit('onMovePerson', $event)"
                                           @deletePerson="$store.commit('onDeletePerson', $event)">
                        </house-layout-room>
                    </house-layout>
                </v-card>
                <div style="flex: 3;" class="d-flex">
                    <v-card style="margin-right: 6px !important;" class="h-100 main-card flex-1" flat outlined>
                        <div class="d-flex align-stretch">
                            <v-card-title class="flex-1 d-flex align-center">
                                <v-icon class="primary--text">fa-th-large</v-icon>
                                Modules
                            </v-card-title>
                            <div>
                                <v-tabs class="main-tabs">
                                    <v-tab :key="module" v-for="module in smartModules">{{module}}</v-tab>
                                </v-tabs>
                            </div>
                            <div class="card-title d-flex flex-column justify-center">
                                <v-btn icon class="mx-2">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </div>
                        </div>
                    </v-card>
                    <v-card style="margin-left: 6px !important;" class="h-100 main-card flex-1" flat outlined>
                        <v-card-title>
                            <v-icon class="primary--text">fa-terminal</v-icon>
                            Logs
                        </v-card-title>
                    </v-card>
                </div>
            </v-col>
        </v-row>

        <!-- Edit user profile modal -->
        <v-dialog width="400" v-model="showMovePerson">
            <v-card v-if="editPerson">
                <v-card-title>Move {{editPerson.name}}</v-card-title>
                <v-form ref="editForm" lazy-validation>
                    <v-select v-model="editPerson.roomId"
                              dense outlined
                              class="mx-4 mt-3 pt-0"
                              label="New Location"
                              prepend-inner-icon="mdi-crosshairs-gps"
                              item-value="id"
                              item-text="name"
                              :items="roomLocations"
                              :menu-props="{ offsetY: true }">
                    </v-select>
                </v-form>
                <v-card-text>
                    <div class="subtitle-2" v-if="currentPersonLocation">
                        <strong>Current:</strong> <em style="font-weight: 300">{{ currentPersonLocation }}</em>
                    </div>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showMovePerson = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="dispatchEvent('movePerson', editPerson), showMovePerson = false">
                        <v-icon class="mr-2">fa-share</v-icon>Move
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Delete person modal -->
        <v-dialog width="400" v-model="showDeletePerson">
            <v-card v-if="editPerson">
                <v-card-title>Remove Person</v-card-title>
                <v-card-text>
                    Are you sure you want to remove <strong><em>{{editPerson.name}}</em></strong> ?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showDeletePerson = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="red" @click="dispatchEvent('deletePerson', editPerson), showDeletePerson = false">
                        <v-icon class="mr-2">fa-trash-alt</v-icon>Delete
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script>
    import HouseLayout from "../components/house-layout";
    import HouseLayoutRoom from "../components/house-layout-room";

    import validation from "../mixins/validation";

    export default {
        components: { HouseLayout, HouseLayoutRoom },
        data() {
            return {
                smartModules: [
                    'SHC', 'SHP', 'SHH'
                ],
                timeMenu: false,
                dateMenu: false
            }
        },
        computed: {
            simulation() {
                return this.$store.state.simulation;
            },
            simulationDate() {
                return this.$store.state.simulation?.dateTime.split(/\s+/)[0];
            },
            simulationTime() {
                return this.$store.state.simulation?.dateTime.split(/\s+/)[1];
            },
            simulationRunning() {
                return this.$store.state.simulation?.running;
            },
            insideTemperature() {
                return this.$store.state.simulation?.temperatureInside;
            },
            outsideTemperature() {
                return this.$store.state.simulation?.temperatureOutside;
            },
            rooms() {
                return this.$store.state.simulation?.houseLayout?.rooms || [];
            },
            people() {
                return this.$store.state.simulation?.people || [];
            },
            activeUserProfile() {
                return this.$store.state.simulation?.activeUserProfile;
            },
            userProfiles() {
                return this.$store.state.simulation?.userProfiles || [];
            },
            roomLocations() {
                return [{ id: null, name: 'Outside' }, ...(this.$store.state.simulation?.houseLayout?.rooms || [])];
            },
            userLocation() {
                return this.$store.state.simulation?.userLocation || null;
            },
            currentPerson() {
                if (this.editPerson)
                    return this.people.find(p => p.id === this.editPerson.id);
                return null;
            },
            currentPersonLocation() {
                if (this.currentPerson)
                    return this.roomLocations.find(l => l.id === this.currentPerson.roomId)?.name;
                return '';
            },
            showMovePerson: {
                get() {
                    return this.$store.state.showMovePerson;
                },
                set(value) {
                    this.$store.commit('setShowMovePerson', value);
                }
            },
            showDeletePerson: {
                get() {
                    return this.$store.state.showDeletePerson;
                },
                set(value) {
                    this.$store.commit('setShowDeletePerson', value);
                }
            },
            editPerson() {
                return this.$store.state.editPerson;
            }
        },
        methods: {
            onMovePerson(person) {
                this.editPerson = { ...person };
                this.showMovePerson = true;
            },
            onDeletePerson(person) {
                this.editPerson = { ...person };
                this.showDeletePerson = true;
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss" scoped>
    .form-subheader {
        margin-top: -20px;
    }
</style>