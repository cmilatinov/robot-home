<template>
    <v-app id="app">
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
