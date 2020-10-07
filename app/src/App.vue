<template>
    <v-app id="app">
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
    </v-app>
</template>

<script>
    export default {
        name: 'App',
        created() {
            this.$network.get('/persons').then(res => console.log(res.data));
        }
    }
</script>

<style lang="scss">
    @import "./assets/style/global";

    #app {
        width: 100% !important;
        height: 100% !important;
    }
</style>
