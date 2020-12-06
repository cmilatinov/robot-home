<template>
    <div>
        <v-subheader style="margin-top: -15px;">Time Periods</v-subheader>
        <v-card elevation="4" outlined class="mx-4 mb-4 pa-0" style="position: relative;" :key="`p${index}`" v-for="(period, index) in value">
            <v-card-title>Period {{(index + 1)}}</v-card-title>
            <v-row class="px-4">
                <v-col cols="6" class="pl-5 pr-1">
                    <v-menu :close-on-content-click="false"
                            offset-x
                            transition="scale-transition"
                            max-width="290px"
                            min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                    :value="period.startTime"
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
                                v-model="period.startTime"
                                full-width
                                required>
                        </v-time-picker>
                    </v-menu>
                </v-col>
                <v-col cols="6" class="pr-5 pl-1 mb-0">
                    <v-menu :close-on-content-click="false"
                            offset-x
                            transition="scale-transition"
                            max-width="290px"
                            min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                    :value="period.endTime"
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
                                v-model="period.endTime"
                                full-width
                                required>
                        </v-time-picker>
                    </v-menu>
                </v-col>
                <v-col cols="12" style="margin-top: -10px; margin-bottom: -15px;">
                    <v-subheader class="form-subheader" style="margin-top: -30px; margin-bottom: -10px;">
                        <v-icon class="mr-3">fa-thermometer-half</v-icon>
                        <span class="f-10 w-100">Desired Temperature<strong class="float-right white--text">{{ period.desiredTemperature }} &deg;C</strong></span>
                    </v-subheader>
                    <v-slider
                            color="info"
                            :step="0.5"
                            :min="0"
                            :max="35"
                            v-model="period.desiredTemperature"
                            thumb-label></v-slider>
                </v-col>
            </v-row>
            <div class="card-remove-icon" @click="removePeriod(index)" v-if="value.length > 1">
                <v-icon class="ma-2 pa-2 red--text">fa-trash-alt</v-icon>
            </div>
        </v-card>
        <v-card elevation="4" outlined class="mx-4 pa-4 d-flex justify-center align-center" v-ripple @click="addNewPeriod" v-if="value.length < 3">
            <h4><v-icon class="mr-2">fa-plus</v-icon>Add new period</h4>
        </v-card>
        <div class="text-center red--text mt-1" v-if="!validPeriods">
            <v-icon style="font-size: 12pt !important;" class="mr-1 red--text">fa-info-circle</v-icon>
            <em class="f-9">Time periods cannot overlap.</em>
        </div>
    </div>
</template>

<script>
    import validation from '../mixins/validation';
    export default {
        name: 'period-editor',
        props: {
            value: {
                type: Array,
                default: () => [{ startTime: '00:00', endTime: '23:59', desiredTemperature: 24.0 }]
            }
        },
        computed: {
            validPeriods() {
                for (let period of this.value) {
                    let periodRange = this.$moment.range(
                        this.$moment(period.startTime, 'HH:mm'),
                        this.$moment(period.endTime, 'HH:mm')
                    );
                    if (this.value.filter(p => p !== period)
                        .some(other => {
                            let otherRange = this.$moment.range(
                                this.$moment(other.startTime, 'HH:mm'),
                                this.$moment(other.endTime, 'HH:mm')
                            );
                            return periodRange.overlaps(otherRange, { adjacent: false });
                        }))
                        return false;
                }
                return true;
            }
        },
        methods: {
            addNewPeriod() {
                this.$emit('input', [...this.value, {
                    startTime: '00:00',
                    endTime: '23:59',
                    desiredTemperature: 24.0
                }]);
            },
            removePeriod(index) {
                if (index >= 0 && index < this.value.length) {
                    let copy = [...this.value];
                    copy.splice(index, 1);
                    this.$emit('input', copy);
                }
            }
        },
         mixins: [validation]
    }
</script>

<style lang="scss" scoped>
    .card-remove-icon {
        position: absolute;
        top: 0;
        right: 0;
        opacity: 0.5;
        transition: 0.3s;
        cursor: pointer;

        i {
            font-size: 14pt !important;
        }

        &:hover {
            opacity: 1;
        }
    }
</style>