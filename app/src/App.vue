<template>
    <v-app id="app">
        <v-app-bar flat>
            <h2 class="d-flex align-center">Smart Home Simulator</h2>
            <v-spacer></v-spacer>
            <v-btn color="primary">
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
</style>
