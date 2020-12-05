<template>
    <div :class="{ disabled: !simulationRunning }">
        <v-row>
            <v-col>

            </v-col>
        </v-row>
    </div>
</template>

<script>
    export default {
        name: 'shh',
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