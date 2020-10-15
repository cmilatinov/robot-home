<template>
    <v-app id="app">
        <v-app-bar flat>
            <h2 class="d-flex align-center">Smart Home Simulator</h2>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="dispatchEvent('uploadHouseLayout', null)">
                <v-icon class="f-10 mr-3">fa-upload</v-icon>
                Load Layout
            </v-btn>
        </v-app-bar>
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
    </v-app>
</template>

<script>
    export default {
        name: 'App'
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
