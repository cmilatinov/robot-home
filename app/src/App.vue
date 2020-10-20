<template>
    <v-app id="app">
        <v-app-bar flat dense>
            <h2 class="d-flex align-center">Smart Home Simulator</h2>
            <v-spacer></v-spacer>
            <person-manager class="mr-4"></person-manager>
            <profile-manager class="mr-4"></profile-manager>
            <v-btn color="primary" :disabled="simulationRunning" @click="dispatchEvent('uploadHouseLayout', null)">
                <v-icon class="f-10 mr-2">fa-upload</v-icon>
                Load Layout
            </v-btn>
        </v-app-bar>
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
    </v-app>
</template>

<script>
    import ProfileManager from "./components/profile-manager";
    import PersonManager from "./components/person-manager";

    export default {
        name: 'App',
        components: {PersonManager, ProfileManager},
        computed: {
            simulationRunning() {
                return !!this.$store.state.simulation?.running;
            }
        }
    }
</script>

<style lang="scss">
    @import "./assets/style/global";

    #app {
        width: 100% !important;
        height: 100% !important;
    }

    .modal-mask {
      position: fixed;
      z-index: 1;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      display: table;
      transition: opacity 0.3s ease;
      vertical-align: middle;
    }

    .modal-container {
      background-color: #fefefe;
      margin: auto;
      padding: 20px;
      border: 2px solid #33cc33;
      width: 75%;
      vertical-align: middle;
    }

    .modal-wrapper {
      display: table-cell;
      vertical-align: middle;
    }

</style>
