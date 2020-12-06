<template>
    <div>

        <!-- Add zone modal -->
        <v-dialog v-model="showAddZone" width="450">
            <v-card>
                <v-card-title>Add New Zone</v-card-title>
                <v-form ref="addForm" lazy-validation>
                    <v-text-field v-model="newZone.name"
                                  :rules="[validationRules.required, validationRules.format, validationRules.length]"
                                  color="primary"
                                  class="mx-5"
                                  placeholder="Name the new zone"
                                  label="Name"></v-text-field>
                    <v-select v-model="newZone.rooms"
                              dense outlined
                              class="mx-4 mt-3 pt-0"
                              label="Rooms"
                              prepend-inner-icon="mdi-crosshairs-gps"
                              :rules="[validationRules.requiredArray]"
                              chips
                              deletable-chips
                              small-chips
                              item-value="id"
                              item-text="name"
                              :items="rooms"
                              multiple
                              :menu-props="{ offsetY: true }">
                    </v-select>
                    <period-editor ref="periodEditor" v-model="newZone.periods"></period-editor>
                </v-form>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showAddZone = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="onSubmitAddZone">
                        <v-icon class="mr-2">fa-plus</v-icon>Add
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Zone list modal -->
        <v-dialog v-model="showZoneManager" width="600">
            <template #activator="{ on, attrs }">
                <v-btn block color="primary" v-bind="attrs" v-on="on">
                    <v-icon class="f-10 mr-2">fa-list-alt</v-icon>
                    Zone List
                </v-btn>
            </template>
            <v-card>
                <v-card-title>Manage Zones</v-card-title>
                <v-text-field v-model="zoneSearch" class="mx-10 my-0 py-0" prepend-inner-icon="fa-search" placeholder="Search for a zone...">
                </v-text-field>
                <v-list class="pt-0 px-0 people-list">
                    <template v-if="filteredZones.length > 0">
                        <v-list-item :key="zone.id" v-for="zone in filteredZones" link>
                            <v-list-item-icon>
                                <v-icon>fa-object-ungroup</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>{{zone.name}}</v-list-item-title>
                            <v-list-item-subtitle v-if="zone.rooms.length > 0">{{zone.rooms.length}} room{{zone.rooms.length > 1 ? 's' : ''}}</v-list-item-subtitle>
                            <v-list-item-subtitle v-else>No rooms</v-list-item-subtitle>
                            <div class="list-item-actions">
                                <v-btn class="f-14 blue--text mr-2" style="opacity: 0.8;" icon @mousedown.stop @click="onEditZone(zone)">
                                    <v-icon>fa-edit</v-icon>
                                </v-btn>
                                <v-btn class="f-14 red--text" style="opacity: 0.8;" icon @mousedown.stop @click="onDeleteZone(zone)" v-if="!zone.default">
                                    <v-icon>fa-trash</v-icon>
                                </v-btn>
                                <v-tooltip top v-else>
                                    <template #activator="{ on }">
                                        <v-btn class="f-14 white--text" icon @mousedown.stop v-on="on" @click.stop="">
                                            <v-icon disabled>fa-info-circle</v-icon>
                                        </v-btn>
                                    </template>
                                    <div class="text-justify" style="max-width: 300px;">This is the system's default zone. You cannot delete this zone. You may, however, change its name and time periods. Any other deleted zone will have its rooms added back to the default zone.</div>
                                </v-tooltip>
                            </div>
                        </v-list-item>
                    </template>
                    <p class="mt-3 text-center grey--text" v-else>
                        <em>No zones matching <strong>{{zoneSearch}}</strong></em>
                    </p>
                </v-list>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey" @click="showZoneManager = false">
                        Close
                    </v-btn>
                    <v-btn text color="primary" @click="onAddZone">
                        Add New
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Edit zone modal -->
        <v-dialog width="400" v-model="showEditZone">
            <v-card v-if="editZone">
                <v-card-title>Edit {{currentZone ? currentZone.name : ''}}</v-card-title>
                <v-form ref="addForm" lazy-validation>
                    <v-text-field v-model="editZone.name"
                                  :rules="[validationRules.required, validationRules.format, validationRules.length]"
                                  color="primary"
                                  class="mx-5"
                                  placeholder="Rename the zone"
                                  label="Name"></v-text-field>
                    <v-card-text class="pt-0 pb-1">
                        <div class="subtitle-2" v-if="currentZone">
                            <strong>Current:</strong> <em style="font-weight: 300">{{ currentZone.name }}</em>
                        </div>
                    </v-card-text>
                    <v-select v-model="editZone.rooms"
                              dense outlined readonly
                              class="mx-4 mt-3 pt-0"
                              label="Rooms"
                              prepend-inner-icon="mdi-crosshairs-gps"
                              :rules="[validationRules.requiredArray]"
                              chips
                              deletable-chips
                              small-chips
                              item-value="id"
                              item-text="name"
                              :items="rooms"
                              multiple
                              :menu-props="{ offsetY: true }">
                    </v-select>
                    <period-editor ref="periodEditor" v-model="editZone.periods"></period-editor>
                </v-form>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showEditZone = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="dispatchEvent('editZone', editZone), showEditZone = false">
                        <v-icon class="mr-2">fa-save</v-icon>Save
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Delete zone modal -->
        <v-dialog width="400" v-model="showDeleteZone">
            <v-card v-if="deleteZone">
                <v-card-title>Remove Zone</v-card-title>
                <v-card-text>
                    Are you sure you want to remove <strong><em>{{deleteZone.name}}</em></strong> ?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showDeleteZone = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="red" @click="dispatchEvent('removeZone', { id: deleteZone.id }), showDeleteZone = false">
                        <v-icon class="mr-2">fa-trash-alt</v-icon>Delete
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

    </div>
</template>

<script>
    import validation from '../mixins/validation';
    import PeriodEditor from './period-editor';
    export default {
        name: 'zone-manager',
        components: { PeriodEditor },
        props: {  },
        data() {
            return {
                showZoneManager: false,

                showAddZone: false,
                showEditZone: false,
                showDeleteZone: false,

                newZone: {
                    name: '',
                    rooms: [],
                    periods: [
                        {
                            startTime: '00:00',
                            endTime: '23:59',
                            desiredTemperature: 24.0
                        }
                    ]
                },
                editZone: null,
                deleteZone: null,

                zoneSearch: ''
            }
        },
        computed: {
            rooms() {
                return this.$store.state.simulation?.houseLayout?.rooms || [];
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            shh() {
                return this.simulationModules.find(m => m.name === 'SHH');
            },
            zones() {
                return this.shh?.zones || [];
            },
            filteredZones() {
                return this.zones.filter(z => z.name.toLowerCase().includes(this.zoneSearch.toLowerCase()));
            },
            currentZone() {
                if (this.editZone)
                    return this.zones.find(z => z.id === this.editZone.id);
                return null;
            }
        },
        methods: {
            onSubmitAddZone() {
                if (!this.$refs.addForm.validate() || !this.$refs.periodEditor.validPeriods)
                    return;
                this.dispatchEvent('addZone', this.newZone);
                this.showAddZone = false;
            },
            onAddZone() {
                this.newZone = {
                    name: '',
                    rooms: [],
                    periods: [
                        {
                            startTime: '00:00',
                            endTime: '23:59',
                            desiredTemperature: 24.0
                        }
                    ]
                };
                this.showAddZone = true;
            },
            onEditZone(zone) {
                this.editZone = JSON.parse(JSON.stringify(zone));
                this.editZone.rooms = this.editZone.rooms.map(r => r.id);
                this.showEditZone = true;
            },
            onDeleteZone(zone) {
                this.deleteZone = JSON.parse(JSON.stringify(zone));
                this.showDeleteZone = true;
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss" scoped>
    .list-item-actions {
        position: absolute;
        right: 15px;
        top: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        font-size: 12pt !important;
    }
</style>