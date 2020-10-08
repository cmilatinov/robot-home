<template>
    <v-container fluid class="h-100 d-flex flex-column justify-center align-center">
        <v-row class="d-flex">
          <v-col
            class="align-center-content"
            :cols="3"
          >
            <v-card>
              <v-card-title><h3>Simulation</h3></v-card-title>
              <div class="pa-4">
                <v-switch
                    align="center"
                    v-model="simulationToggle"
                    :label="`Simulation ${simulationToggle?'On':'Off'}`"
                ></v-switch>
                <v-img
                 max-width="100"
                 max-height="100"
                  src="https://www.c2mi.ca/wp-content/uploads/2019/03/person-silhouette-vector-icon-vector-id1129576939.jpg"
                ></v-img>
                <v-card-text>User</v-card-text>
                <v-expansion-panels>
                  <v-expansion-panel>
                    <v-expansion-panel-header>
                     Edit Context
                   </v-expansion-panel-header>
                    <v-expansion-panel-content>
                      <v-form ref="contextForm" v-model="contextForm">
                        <v-menu
                            ref="timeMenu"
                            v-model="timeMenu"
                            :close-on-content-click="false"
                            :nudge-right="40"
                            :return-value.sync="time"
                            transition="scale-transition"
                            offset-y
                            max-width="290px"
                            min-width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="time"
                                label="Time"
                                prepend-icon="mdi-clock-time-four-outline"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                            ></v-text-field>
                          </template>
                          <v-time-picker
                              v-if="timeMenu"
                              v-model="time"
                              full-width
                              @click:minute="$refs.contextForm.save(time)"
                          ></v-time-picker>
                        </v-menu>

                        <v-menu
                            ref="dateMenu"
                            v-model="dateMenu"
                            :close-on-content-click="false"
                            :return-value.sync="date"
                            transition="scale-transition"
                            offset-y
                            min-width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="date"
                                label="Date"
                                prepend-icon="mdi-calendar"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                              v-model="date"
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
                                @click="$refs.dateMenu.save(date)"
                            >
                              OK
                            </v-btn>
                          </v-date-picker>
                        </v-menu>

                        <v-text-field
                          v-model="temperature"
                          :rules="temperatureRules"
                          label="temperature"
                        ></v-text-field>

                      </v-form>
                   </v-expansion-panel-content>
                  </v-expansion-panel>
                </v-expansion-panels>
              </div>
            </v-card>
          </v-col>
          <v-col
          >
            <v-card>
              <v-card-title><h3>Smart Home Modules</h3></v-card-title>
              <v-tabs v-model="tab" grow>
                <v-tab
                  v-for="module in smartModules"
                  :key="module"
                >
                  {{ module }}
                </v-tab>
              </v-tabs>
              <v-tabs-items v-model="tab">
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      SHS is responsible for providing an API where smart home modules can be subscribed to track  environmental conditions like temperature inside and outside, date and time , as well as information regarding the layout of the house (number of rooms, windows and lights); motion sensors to detect the presence of people in the rooms;  sensors in the windows and doors.  Smart modules use that information to perform their work
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      This module is responsible to execute general actions to house items like doors, windows, lights, etc.  at user request or by  any smart home module.
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      This module ensures that the home is protected from intruders and it relies on sensors to determine the presence of undesired people.
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      Smart home heating module.
                    </v-card-text>
                  </v-card>
                </v-tab-item>
              </v-tabs-items>
            </v-card>
          </v-col>
          <v-col
            :cols=3
          >
            <v-card>
              <v-card-title><h3>EXAMPLE Home Layout</h3></v-card-title>
              <v-card-subtitle>Example Home</v-card-subtitle>
              <div class="pa-4">
              <v-img
                src="https://www.roomsketcher.com/blog/wp-content/uploads/2012/05/Unoptimal-Floor-plan.png"
                max-height="400"
                max-width="400"
              ></v-img>
              </div>
            </v-card>
          </v-col>
        </v-row>

    </v-container>
</template>

<script>
    export default {
      data () {
        return {
          contextForm: true,
          name: "dashboard",
          tab:null,
          smartModules: [
            'SHS', 'SHC', 'SHP', 'SHH', '+'
          ],
          simulationToggle: false,
          time: null,
          timeMenu: false,
          date: null,
          dateMenu: false

        }
      }
    }
</script>

<style scoped>

</style>