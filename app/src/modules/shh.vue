<template>
    <div :class="{ disabled: !simulationRunning }" class="px-2">
        <v-subheader class="text-uppercase w-100" style="margin-bottom: -20px">
            <div class="w-100">
                Zone Manager
            </div>
        </v-subheader>
        <div class="pa-3">
            <zone-manager></zone-manager>
        </div>
    </div>
</template>

<script>
    import ZoneManager from "../components/zone-manager";
    export default {
        name: 'shh',
        components: {ZoneManager},
        computed: {
            simulationRunning() {
                return this.$store.state.simulation?.running;
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            shh() {
                return this.simulationModules.find(m => m.name === 'SHH');
            }
        },
    }
</script>

<style scoped>

</style>