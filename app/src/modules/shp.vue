<template>
    <div :class="{ disabled: !simulationRunning }">
        <v-row>
            <v-col class="pr-0">
                <v-subheader class="text-uppercase w-100" style="margin-top: -10px; margin-bottom: -20px">
                    <div class="w-100">
                        Away Mode<strong class="float-right white--text">{{awayMode ? 'On' : 'Off'}}</strong>
                    </div>
                </v-subheader>
                <div class="pa-3">
                    <v-btn block :color="awayMode ? 'red darken-4' : 'primary darken-1'" @click="toggleAwayMode">
                        <template v-if="awayMode">
                            <v-icon class="mr-2">fa-stop</v-icon> Stop Away Mode
                        </template>
                        <template v-else>
                            <v-icon class="mr-2">fa-play</v-icon> Start Away Mode
                        </template>
                    </v-btn>
                </div>

                <v-subheader style="margin-top: -10px; margin-bottom: -10px;">
                    <v-icon class="mr-3">fa-stopwatch</v-icon>
                    <span class="f-10 w-100">Authorities Alert Delay<strong class="float-right white--text">{{ alertDelay }}s</strong></span>
                </v-subheader>
                <v-slider class="mx-2"
                        color="info"
                        :min="1"
                        :max="60"
                        :step="1"
                        :value="alertDelay"
                        thumb-label
                        @change="dispatchEvent('setAlertDelay', { value: $event })"></v-slider>
            </v-col>
            <v-col class="pl-0">
                <v-subheader class="text-uppercase w-100" style="margin-top: -10px; margin-bottom: -20px">
                    Away Lights
                </v-subheader>
                <div class="pa-3 ma-0">
                    <v-btn block
                           color="primary darken-1"
                           class="mb-6"
                           @click="dispatchEvent('setAwayLights', {})">
                        Capture Current State
                    </v-btn>
                    <v-menu v-model="timeStartMenu"
                            :close-on-content-click="false"
                            offset-x
                            transition="scale-transition"
                            max-width="290px"
                            min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                    :value="awayTimeStart"
                                    label="Start Time"
                                    prepend-inner-icon="mdi-clock-time-four-outline"
                                    readonly
                                    dense
                                    class="time-icon mb-2"
                                    :rules="[validationRules.required]"
                                    required
                                    v-bind="attrs"
                                    v-on="on"></v-text-field>
                        </template>
                        <v-time-picker
                                v-if="timeStartMenu"
                                :value="awayTimeStart"
                                full-width
                                required
                                @input="dispatchEvent('setAwayTime', { startTime: $event, endTime: awayTimeEnd })">
                        </v-time-picker>
                    </v-menu>
                    <v-menu v-model="timeEndMenu"
                            :close-on-content-click="false"
                            offset-x
                            transition="scale-transition"
                            max-width="290px"
                            min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                    :value="awayTimeEnd"
                                    label="End Time"
                                    prepend-inner-icon="mdi-clock-time-four-outline"
                                    readonly
                                    dense
                                    class="time-icon"
                                    :rules="[validationRules.required]"
                                    required
                                    v-bind="attrs"
                                    v-on="on"></v-text-field>
                        </template>
                        <v-time-picker
                                v-if="timeEndMenu"
                                :value="awayTimeEnd"
                                full-width
                                required
                                @input="dispatchEvent('setAwayTime', { startTime: awayTimeStart, endTime: $event })">
                        </v-time-picker>
                    </v-menu>
                </div>
            </v-col>
        </v-row>
    </div>
</template>

<script>
    import validation from "../mixins/validation";

    export default {
        name: 'shp',
        computed: {
            simulationRunning() {
                return this.$store.state.simulation?.running;
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            shp() {
                return this.simulationModules.find(m => m.name === 'SHP');
            },
            awayMode() {
                return this.shp?.awayMode;
            },
            alertDelay() {
                return this.shp?.alertDelay;
            },
            awayTimeStart() {
                return this.shp?.awayTimeStart;
            },
            awayTimeEnd() {
                return this.shp?.awayTimeEnd;
            }
        },
        data() {
            return {
                timeStartMenu: false,
                timeEndMenu: false
            }
        },
        methods: {
            toggleAwayMode() {
                this.dispatchEvent('toggleAway', { value: !this.awayMode });
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss">
    .time-icon i {
        font-size: 14pt !important;
    }
</style>