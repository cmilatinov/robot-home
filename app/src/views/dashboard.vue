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
                              :disabled="simulationRunning"
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
                        <v-btn class="mx-3 mb-2" :color="simulationRunning ? 'red darken-4' : 'primary darken-1'" :disabled="!hasHouseLayout" @click="onToggleSimulation">
                            <template v-if="simulationRunning">
                                <v-icon class="mr-2">fa-stop</v-icon>Stop
                            </template>
                            <template v-else>
                                <v-icon class="mr-2">fa-play</v-icon>Start
                            </template>
                        </v-btn>
                        <span class="red--text" v-if="!hasHouseLayout">
                            <v-icon style="font-size: 12pt !important;" class="mr-1 red--text">fa-info-circle</v-icon>
                            <em class="f-9">You must load a house layout before starting the simulation.</em>
                        </span>
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

                            <v-subheader style="margin-top: -10px; margin-bottom: -10px;">
                                <v-icon class="mr-3">fa-stopwatch</v-icon>
                                <span class="f-10 w-100">Simulation Speed<strong class="float-right white--text">x{{ simulationSpeed }}</strong></span>
                            </v-subheader>
                            <v-slider
                                    color="info"
                                    :min="1"
                                    :max="60"
                                    :step="1"
                                    :value="simulationSpeed"
                                    thumb-label
                                    @change="onChangeSimulationSpeed"></v-slider>

                            <v-subheader class="text-uppercase" style="margin-top: -25px; margin-left: -12px;">Temperature</v-subheader>
                            <v-subheader style="margin-top: -15px; margin-bottom: -10px;">
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
                        </v-form>
                    </v-container>
                </v-card>
            </v-col>
            <v-col cols="9" class="h-100 d-flex flex-column">
                <v-card style="flex: 3;" class="h-100 mb-3 main-card d-flex flex-column" flat outlined>
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
                <div style="flex: 2 2 0;" class="d-flex overflow-hidden">
                    <v-card style="margin-right: 6px !important; min-width: 0;" class="h-100 main-card flex-1 overflow-hidden" flat outlined>
                        <div class="d-flex align-stretch">
                            <v-card-title class="flex-1 d-flex align-center">
                                <v-icon class="primary--text">fa-th-large</v-icon>
                                Modules
                            </v-card-title>
                            <div>
                                <v-tabs class="main-tabs" v-model="moduleTab">
                                    <v-tab :key="module.name" v-for="module in simulationModules">{{module.name}}</v-tab>
                                </v-tabs>
                            </div>
<!--                            <div class="card-title d-flex flex-column justify-center">-->
<!--                                <v-btn icon class="mx-2">-->
<!--                                    <v-icon>mdi-plus</v-icon>-->
<!--                                </v-btn>-->
<!--                            </div>-->
                        </div>
                        <div class="d-flex flex-row align-stretch">
                            <v-tabs-items class="w-100 h-100" v-model="moduleTab">
                                <v-tab-item :key="module.name" v-for="module in simulationModules">
                                    <component :is="module.name">
                                    </component>
                                </v-tab-item>
                            </v-tabs-items>
                        </div>
                    </v-card>
                    <v-card style="margin-left: 6px !important; min-width: 0;" class="h-100 main-card flex-1 d-flex flex-column overflow-hidden" flat outlined>
                        <div class="d-flex align-stretch">
                            <v-card-title class="flex-1 d-flex align-center">
                                <v-icon class="primary--text">fa-terminal</v-icon>
                                <div class="flex-1">Logs</div>
                                <div class="flex-0" style="min-width: 150px">
                                    <v-select dense class="smaller-icon mt-0" style="margin-bottom: -20px;"
                                              prepend-inner-icon="fa-filter"
                                              v-model="logFilter"
                                              item-value="filter"
                                              item-text="label"
                                              :items="logFilters"
                                              :menu-props="{ offsetY: true }">
                                    </v-select>
                                </div>
                            </v-card-title>

                        </div>

                        <div class="logs" v-if="filteredLogs.length > 0">
                            <div :key="index" v-for="(log, index) in filteredLogs" :class="{
                                'red--text text--darken-1': log.includes('ERROR'),
                                'grey--text text--lighten-2': log.includes('INFO'),
                                'warning--text': log.includes('WARNING')
                            }">{{log}}</div>
                        </div>
                        <div style="flex: 1 1 0; opacity: 0.5;" class="d-flex justify-center align-center" v-else>
                            <em>No logs to display.</em>
                        </div>
                    </v-card>
                </div>
            </v-col>
        </v-row>
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
                moduleTab: null,
                timeMenu: false,
                dateMenu: false,
                simulationInterval: null,
                logFilter: ''
            }
        },
        computed: {
            simulation() {
                return this.$store.state.simulation;
            },
            simulationDateTime() {
                return this.$store.state.simulation?.dateTime || '';
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
            simulationSpeed() {
                return this.$store.state.simulation?.simulationSpeed.toFixed(1);
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            awayMode() {
                return this.simulationModules.find(m => m.name === 'SHP')?.away;
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
            },
            hasHouseLayout() {
                return !!this.$store.state.simulation?.houseLayout;
            },
            logs() {
                return this.$store.state.logs;
            },
            filteredLogs() {
                return this.logs.filter(log => log.includes(this.logFilter));
            },
            logFilters() {
                return [{ label: 'All', filter: '' }, { label: 'System', filter: '[System]' }, ...this.simulationModules.map(m => ({ label: m.name, filter: `[${m.name}]` }))];
            }
        },
        methods: {
            onToggleSimulation() {
                // Increment simulation time every minute if starting
                if (!this.simulationRunning) {
                    this.simulationInterval = setInterval(this.nextSimulationMinute, 60 * 1000 / this.simulationSpeed);

                // Clear interval if stopping
                } else
                    clearInterval(this.simulationInterval);

                // Change simulation state
                this.dispatchEvent('toggleSimulation', { value: !this.simulationRunning });
            },
            onMovePerson(person) {
                this.editPerson = { ...person };
                this.showMovePerson = true;
            },
            onDeletePerson(person) {
                this.editPerson = { ...person };
                this.showDeletePerson = true;
            },
            onChangeSimulationSpeed(value) {

                // If the simulation is running, update the interval function
                if (this.simulationRunning) {
                    clearInterval(this.simulationInterval);
                    this.simulationInterval = setInterval(this.nextSimulationMinute, 60 * 1000 / value);
                }

                this.dispatchEvent('setSimulationSpeed', { value });

            },
            nextSimulationMinute() {
                let newDateTime = this.$moment(this.simulationDateTime, 'YYYY-MM-DD HH:mm').add(1, 'minutes').format('YYYY-MM-DD HH:mm');
                this.dispatchEvent('setSimulationDateTime', { value: newDateTime });
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss">
    .smaller-icon i.fa {
        font-size: 12pt !important;
    }
</style>

<style lang="scss" scoped>
    .form-subheader {
        margin-top: -20px;
    }

    .logs {
        flex: 1 1 0;
        font-family: Consolas, Monospaced, sans-serif !important;
        overflow-y: auto;
        padding: 0.5rem;
        div {
            text-indent: -150px;
            padding-left: 150px;
        }
    }
</style>