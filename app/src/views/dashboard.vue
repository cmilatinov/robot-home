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
                smartModules: [
                    'SHC', 'SHP', 'SHH'
                ],
                showModal: false,
                user: ""
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