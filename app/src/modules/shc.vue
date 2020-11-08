<template>
    <div class="w-100 px-2" :class="{ disabled: !simulationRunning }">
        <v-subheader class="text-uppercase w-100" style="margin-bottom: -20px">
            <div class="w-100">
                Auto Mode<strong class="float-right white--text">{{autoMode ? 'On' : 'Off'}}</strong>
            </div>
        </v-subheader>
        <div class="pa-3">
            <v-btn block :color="autoMode ? 'red darken-4' : 'primary darken-1'" @click="toggleAutoMode">
                {{autoMode ? 'Turn Off Auto Mode' : 'Turn On Auto Mode'}}
            </v-btn>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'shc',
        computed: {
            simulationRunning() {
                return this.$store.state.simulation?.running;
            },
            autoMode() {
                return this.$store.state.simulation?.modules.find(m => m.name === 'SHC')?.autoMode || false;
            }
        },
        methods: {
            toggleAutoMode() {
                this.dispatchEvent('toggleAutoMode', { value: !this.autoMode });
            }
        }
    }
</script>

<style scoped>

</style>