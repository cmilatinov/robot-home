<template>
    <div :class="{ disabled: !simulationRunning }" class="px-2">
        <v-row class="px-3 pt-0 mt-0">
            <v-col cols="6" class="pt-0">
                <v-subheader class="text-uppercase w-100 pl-0 mt-0" style="margin-bottom: -10px">
                    <div class="w-100">
                        Seasons
                    </div>
                </v-subheader>
                <v-btn color="primary" block @click="onSetWinterRange">
                    <v-icon class="mr-2">fa-snowflake</v-icon>
                    Modify Parameters
                </v-btn>
                <v-dialog width="400" v-model="showSetWinterRange">
                    <v-card>
                        <v-card-title>Change Season Parameters</v-card-title>
                        <v-card-text>Use this dialog to change the span of winter over the year and the desired away mode temperatures for both winter and summer. The specified month range is inclusive and will determine the duration of winter. The rest of the year will be interpreted as the summer. The away mode temperatures define the target temperatures the system will keep up while it is set to away mode.</v-card-text>
                        <v-form ref="winterForm">

                            <v-row class="ma-0 px-2">
                                <v-col cols="12" class="pa-0 ma-0">
                                    <v-subheader class="text-uppercase" style="margin-top: -15px; margin-bottom: -5px;">Winter Range</v-subheader>
                                </v-col>
                                <v-col cols="6" class="pr-1 pb-0">
                                    <v-select v-model="winterStart"
                                              outlined dense
                                              label="Start Month"
                                              prepend-inner-icon="fa-calendar-alt"
                                              :items="months"
                                              :rules="[validationRules.required]"
                                              :menu-props="{ offsetY: true }">
                                    </v-select>
                                </v-col>
                                <v-col cols="6" class="pl-1 pb-0">
                                    <v-select v-model="winterEnd"
                                              outlined dense
                                              label="End Month"
                                              prepend-inner-icon="fa-calendar-alt"
                                              :items="months"
                                              :rules="[validationRules.required]"
                                              :menu-props="{ offsetY: true }">
                                    </v-select>
                                </v-col>
                                <v-col cols="12" class="pa-0">
                                    <v-card-text class="py-0 pl-4" style="margin-top: -10px;">
                                        <div class="subtitle-2">
                                            <strong>Current:</strong> <em style="font-weight: 300">{{ $moment().month(simulationWinterStart).format('MMMM') }} &mdash; {{ $moment().month(simulationWinterEnd).format('MMMM') }}</em>
                                        </div>
                                    </v-card-text>
                                    <v-subheader class="text-uppercase mt-2">Temperature In Away Mode</v-subheader>
                                    <v-subheader style="margin-top: -15px; margin-bottom: -10px;">
                                        <v-icon class="mr-3">fa-sun</v-icon>
                                        <span class="f-10 w-100">Summer&nbsp;
                                            <span class="white--text font-weight-light">(Current: {{ simulationSummerTemp }} &deg;C)</span>
                                            <strong class="float-right white--text">{{ summerTemp }} &deg;C</strong>
                                        </span>
                                    </v-subheader>
                                    <v-slider
                                            v-model="summerTemp"
                                            color="info"
                                            class="px-3"
                                            :min="15"
                                            :max="30"
                                            thumb-label></v-slider>
                                    <v-subheader class="form-subheader" style="margin-top: -30px; margin-bottom: -10px;">
                                        <v-icon class="mr-3">fa-snowflake</v-icon>
                                        <span class="f-10 w-100">Winter
                                            <span class="white--text font-weight-light">(Current: {{ simulationWinterTemp }} &deg;C)</span>
                                            <strong class="float-right white--text">{{ winterTemp }} &deg;C</strong>
                                        </span>
                                    </v-subheader>
                                    <v-slider
                                            v-model="winterTemp"
                                            color="info"
                                            class="px-3"
                                            :min="15"
                                            :max="30"
                                            thumb-label></v-slider>
                                </v-col>
                            </v-row>

                        </v-form>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn text color="grey darken-2" @click="showSetWinterRange = false">
                                <v-icon class="mr-2">fa-times</v-icon>Cancel
                            </v-btn>
                            <v-btn text color="primary" @click="onSubmitChangeWinterRange">
                                <v-icon class="mr-2">fa-save</v-icon>Save
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col cols="6" class="pt-0">
                <v-subheader class="text-uppercase w-100 pl-0 mt-0" style="margin-bottom: -10px">
                    <div class="w-100">
                        Zones
                    </div>
                </v-subheader>
                <zone-manager></zone-manager>
                <v-subheader class="text-uppercase w-100 pl-0 mt-2" style="margin-bottom: -25px">
                    <div class="w-100">
                        Room Temperature
                    </div>
                </v-subheader>
                <v-checkbox color="info" :value="colorCodedTemp" @change="$store.commit('setColorCodedTemp', !!$event)">
                    <template #label>
                        <v-icon color="warning" class="mr-2">fa-fire</v-icon>
                        <span>Heatmap Mode</span>
                    </template>
                </v-checkbox>
            </v-col>
        </v-row>
    </div>
</template>

<script>
    import ZoneManager from "../components/zone-manager";
    import validation from "../mixins/validation";
    export default {
        name: 'shh',
        components: {ZoneManager},
        data() {
            return {
                showSetWinterRange: false,
                months: [...Array(12)].map((_, index) => ({ value: index, text: this.$moment().month(index).format('MMMM') })),
                winterStart: 0,
                winterEnd: 0,
                winterTemp: 0,
                summerTemp: 0
            }
        },
        computed: {
            simulationRunning() {
                return this.$store.state.simulation?.running;
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            shh() {
                return this.simulationModules.find(m => m.name === 'SHH');
            },
            simulationWinterStart() {
                return this.$store.state.simulation?.startWinterMonth;
            },
            simulationWinterEnd() {
                return this.$store.state.simulation?.endWinterMonth;
            },
            simulationWinterTemp() {
                return this.shh?.winterTemperature;
            },
            simulationSummerTemp() {
                return this.shh?.summerTemperature;
            },
            colorCodedTemp() {
                return this.$store.state.colorCodedTemperature;
            }
        },
        methods: {
            onSetWinterRange() {
                this.winterStart = this.simulationWinterStart;
                this.winterEnd = this.simulationWinterEnd;
                this.winterTemp = this.simulationWinterTemp;
                this.summerTemp = this.simulationSummerTemp;
                this.showSetWinterRange = true;
            },
            onSubmitChangeWinterRange() {
                if (!this.$refs.winterForm.validate())
                    return;
                this.dispatchEvent('setWinterRange', { start: this.winterStart, end: this.winterEnd });
                this.dispatchEvent('setSeasonTemp', { summer: this.summerTemp, winter: this.winterTemp });
                this.showSetWinterRange = false;
            }
        },
        mixins: [validation]
    }
</script>

<style scoped>

</style>