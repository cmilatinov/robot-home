<template>
    <v-container fluid class="h-100 py-0 d-flex flex-column">
      <v-btn color="primary" @click="showModal = true">Choose Profile</v-btn>
      <Popup v-if="showModal" @close="showModal = false" v-on:chosenProfile="updateUser($event)">
      </Popup>
        <v-row style="flex: 5;">
            <v-col cols="3" class="h-100 pr-0">
                <v-card class="h-100 main-card" flat outlined>
                    <v-card-title><v-icon class="primary--text">fa-project-diagram</v-icon>Simulation</v-card-title>
                  <h2 class="user">
                    <div>
                      <v-icon></v-icon>
                    </div>
                    {{user}}
                  </h2>
                    <v-container fluid class="h-100 py-0 d-flex flex-column">
                        <v-switch
                            v-model="simulationToggle"
                            :label="`Simulation ${simulationToggle?'On':'Off'}`"
                        ></v-switch>
                        <v-container
                            fluid class="py-0"
                            v-if="simulationToggle"
                        >
                            <div id="context">
                                <span>Time: {{ contextTime }}</span><br>
                                <span>Date: {{ contextDate }}</span><br>
                                <span>Outside Temperature: {{ contextOutsideTemperature }}</span><br>
                                <span>Inside Temperature: {{ contextInsideTemperature }}</span>
                            </div>
                        </v-container>
                        <v-expansion-panels>
                            <v-expansion-panel>
                                <v-expansion-panel-header>
                                    Edit Context
                                </v-expansion-panel-header>
                                <v-expansion-panel-content>
                                    <v-form ref="contextForm" v-model="contextForm" @submit.prevent="submit">
                                        <v-menu
                                            ref="timeMenu"
                                            v-model="timeMenu"
                                            :close-on-content-click="false"
                                            :nudge-right="40"
                                            :return-value.sync="formTime"
                                            transition="scale-transition"
                                            offset-y
                                            max-width="290px"
                                            min-width="290px"

                                        >
                                            <template v-slot:activator="{ on, attrs }">
                                                <v-text-field
                                                    v-model="formTime"
                                                    label="Time"
                                                    prepend-icon="mdi-clock-time-four-outline"
                                                    readonly
                                                    :rules="[v => v!=null|| 'Time is required']"
                                                    required
                                                    v-bind="attrs"
                                                    v-on="on"
                                                ></v-text-field>
                                            </template>
                                            <v-time-picker
                                                v-if="timeMenu"
                                                v-model="formTime"
                                                full-width
                                                required
                                                @click:minute="$refs.timeMenu.save(formTime)"
                                            ></v-time-picker>
                                        </v-menu>
                                        <v-menu
                                            ref="dateMenu"
                                            v-model="dateMenu"
                                            :close-on-content-click="false"
                                            :return-value.sync="formDate"
                                            transition="scale-transition"
                                            offset-y
                                            min-width="290px"
                                        >
                                            <template v-slot:activator="{ on, attrs }">
                                                <v-text-field
                                                    v-model="formDate"
                                                    label="Date"
                                                    prepend-icon="mdi-calendar"
                                                    readonly
                                                    :rules="[v => !!v || 'Date is required']"
                                                    required
                                                    v-bind="attrs"
                                                    v-on="on"
                                                ></v-text-field>
                                            </template>
                                            <v-date-picker
                                                v-model="formDate"
                                                no-title
                                                scrollable
                                            >
                                                <v-spacer></v-spacer>
                                                <v-btn
                                                    text
                                                    color="primary"
                                                    @click="dateMenu = false"
                                                >
                                                    Cancel
                                                </v-btn>
                                                <v-btn
                                                    text
                                                    color="primary"
                                                    @click="$refs.dateMenu.save(formDate)"
                                                >
                                                    OK
                                                </v-btn>
                                            </v-date-picker>
                                        </v-menu>
                                        <v-text-field
                                            v-model.lazy="formOutsideTemperature"
                                            :rules="temperatureRules"
                                            label="Outside temperature"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model.lazy="formInsideTemperature"
                                            :rules="temperatureRules"
                                            label="Inside temperature"
                                        ></v-text-field>
                                        <div>
                                            <v-btn
                                                :disabled="!contextForm"
                                                color="success"
                                                class="mr-4"
                                                type="submit"
                                                @click="submit"
                                            >
                                                Submit
                                            </v-btn>

                                            <v-btn
                                                color="error"
                                                class="mr-4"
                                                @click="reset"
                                            >
                                                Reset Form
                                            </v-btn>
                                        </div>
                                    </v-form>
                                </v-expansion-panel-content>
                            </v-expansion-panel>
                        </v-expansion-panels>
                    </v-container>
                </v-card>
            </v-col>
            <v-col cols="9" class="h-100 d-flex flex-column">
                <v-card style="flex: 7;" class="h-100 mb-3 main-card d-flex flex-column" flat outlined>
                    <v-card-title><v-icon class="primary--text">fa-ruler-combined</v-icon>House Layout</v-card-title>
                    <house-layout>
                        <house-layout-room :key="room.name" v-for="room in houseLayoutRooms()" :room="room"></house-layout-room>
                    </house-layout>
                </v-card>
                <div style="flex: 3;" class="d-flex">
                    <v-card style="margin-right: 6px !important;" class="h-100 main-card flex-1" flat outlined>
                        <div class="d-flex align-stretch">
                            <v-card-title class="flex-1 d-flex align-center">
                                <v-icon class="primary--text">fa-th-large</v-icon>Modules
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
                        <v-card-title><v-icon class="primary--text">fa-terminal</v-icon>Logs</v-card-title>
                    </v-card>
                </div>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import HouseLayout from "../components/house-layout";
    import HouseLayoutRoom from "../components/house-layout-room";
    import Popup from "./Popup";

    export default {
        components: {HouseLayoutRoom, HouseLayout, Popup},
        data() {
            return {
                contextForm: false,
                name: "dashboard",
                tab: null,
                smartModules: [
                    'SHC', 'SHP', 'SHH'
                ],
                showModal: false,
                user: "",
                simulationToggle: false,
                formOutsideTemperature: null,
                contextOutsideTemperature: null,
                formInsideTemperature: null,
                contextInsideTemperature:null,
                temperatureRules: [
                    v => !!v || 'Item is required',
                    v => !isNaN(Number(v)) || 'Temperature must be a number.',
                    v => (v < 100 && v > -100) || 'Invalid temperature. Valid range is -100 to 100.'
                ],
                formTime: null,
                contextTime: null,
                timeMenu: false,
                formDate: null,
                contextDate: null,
                dateMenu: false,
                rooms: [
                    {
                        name: 'Kitchen'
                    },
                    {
                        name: 'Living Room'
                    },
                    {
                        name: 'Master Bedroom'
                    },
                    {
                        name: 'Bathroom'
                    }

                ]
            }
        },

        created() {
            this.$store.subscribe(mutation => {
                if (mutation.type === 'update') {
                    this.$forceUpdate();
                    console.log(this.houseLayoutRooms().length);
                }
            });
        },
        methods: {
            submit () {
                this.contextTime = this.formTime;
                this.contextDate = this.formDate;
                this.contextOutsideTemperature = this.formOutsideTemperature;
                this.contextInsideTemperature = this.formInsideTemperature;
                this.$refs.contextForm.submit()
            },
            reset () {
                this.$refs.contextForm.reset()
            },
            houseLayoutRooms() {
                return this.$store.state.houseLayout?.getRooms().toArray().map(r => r) || [];
            },
            updateUser: function (updatedUser) {
              this.user = updatedUser;
            }
        }
    }
</script>

<style scoped>
h2 {
  text-align: center;
  width: 100%;
}
</style>