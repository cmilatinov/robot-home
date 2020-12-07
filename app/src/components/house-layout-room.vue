<template>
    <div @mousedown.stop="onMouseDown"
         @focus="onFocus"
         @blur="onBlur"
         tabIndex="0"
         class="diagram-room"
         :style="`left: ${left}px; top: ${top}px; width: ${width}px; height: ${height}px;`">
        <div class="diagram-room-people">
            <v-menu offset-y :key="person.id" v-for="person in people">
                <template #activator="{ on: menu, attrs }">
                    <v-tooltip top>
                        <template #activator="{ on: tooltip }">
                            <v-btn icon :disabled="!simulationRunning" v-bind="attrs" v-on="{ ...tooltip, ...menu }">
                                <v-icon :style="`color: ${person.color}`">fa-user</v-icon>
                            </v-btn>
                        </template>
                        <span>{{person.name}}</span>
                    </v-tooltip>
                </template>
                <v-card>
                    <v-list class="py-0" v-if="person.id !== 0">
                        <v-list-item link @click="$emit('movePerson', person)">
                            <v-icon class="mr-3" style="margin-left: -3px">mdi-crosshairs-gps</v-icon>Move
                        </v-list-item>
                        <v-list-item link @click="$emit('deletePerson', person)">
                            <v-icon class="mr-3 red--text">fa-trash</v-icon>Remove
                        </v-list-item>
                    </v-list>
                </v-card>
            </v-menu>
        </div>
        <h1 style="z-index: 5;">{{room.name}}</h1>
        <div class="diagram-room-status">
            <v-icon class="ma-1 white--text" v-if="hvac">mdi-fan</v-icon>
            <v-icon class="ma-1 light" :key="`l-${i}`" v-for="i in (0, numLightsOn)">fa-lightbulb</v-icon>
            <v-icon class="ma-1 lock" :key="`dl-${i}`" v-for="i in (0, numDoorsLocked)">fa-lock</v-icon>
            <v-icon class="ma-1 lock" :key="`wb-${i}`" v-for="i in (0, numWindowsBlocked)">fa-ban</v-icon>
        </div>
        <div class="diagram-room-controls">
            <v-menu offset-y :close-on-content-click="false">
                <template #activator="{ on, attrs }">
                    <v-btn icon :disabled="!simulationRunning || room.lights.length <= 0" class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fa-lightbulb</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="light.id" v-for="(light, index) in room.lights" class="pl-2" @click="dispatchEvent('toggleLightState', { id: light.id, on: !light.on, sentByUser: true })">
                            <v-icon class="icon-lightbulb" :class="{ active: light.on }">{{!light.on ? 'far' : 'fas'}} fa-lightbulb</v-icon> Light {{ index + 1 }}
                        </v-list-item>
                    </v-list>
                </v-card>
            </v-menu>

            <v-menu offset-y :close-on-content-click="false">
                <template #activator="{ on, attrs }">
                    <v-btn icon :disabled="!simulationRunning || room.doors.length <= 0" class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fa-door-open</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="door.id" v-for="(door, index) in room.doors" class="pl-2">
                            <div class="d-flex">
                                <v-btn icon class="mr-1" @mousedown.stop @click="dispatchEvent('toggleDoorState', { id: door.id, open: !door.open, locked: door.locked, sentByUser: true })">
                                    <v-icon class="icon-door" :class="{ active: door.open }">fa-{{door.open ? 'door-open' : 'door-closed'}}</v-icon>
                                </v-btn>
                                <v-btn icon class="mr-2" @mousedown.stop @click="dispatchEvent('toggleDoorState', { id: door.id, open: door.open, locked: !door.locked, sentByUser: true })">
                                    <v-icon class="icon-lock" :class="{ active: door.locked }">fa-{{door.locked ? 'lock' : 'unlock'}}</v-icon>
                                </v-btn>
                                <v-list-item-title>
                                    Door {{ index + 1 }}
                                </v-list-item-title>
                            </div>
                        </v-list-item>
                    </v-list>
                </v-card>
            </v-menu>

            <v-menu offset-y :close-on-content-click="false">
                <template #activator="{ on, attrs }">
                    <v-btn icon :disabled="!simulationRunning || room.windows.length <= 0" class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fab fa-windows</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="window.id" v-for="(window, index) in room.windows" class="pl-2">
                            <div class="d-flex">
                                <v-btn icon class="mr-1" @mousedown.stop @click="dispatchEvent('toggleWindowState', { id: window.id, open: !window.open, blocked: window.blocked, sentByUser: true })">
                                    <v-icon class="icon-door" :class="{ active: window.open }">fa-{{window.open ? 'door-open' : 'door-closed'}}</v-icon>
                                </v-btn>
                                <v-btn icon class="mr-2" @mousedown.stop @click="dispatchEvent('toggleWindowState', { id: window.id, open: window.open, blocked: !window.blocked, sentByUser: true })">
                                    <v-icon class="icon-lock" :class="{ active: window.blocked }">{{window.blocked ? 'fa-ban' : 'far fa-circle'}}</v-icon>
                                </v-btn>
                                <v-list-item-title>
                                    Window {{ index + 1 }}
                                </v-list-item-title>
                            </div>
                        </v-list-item>
                    </v-list>
                </v-card>
            </v-menu>
        </div>
        <v-dialog width="400">
            <template #activator="{ on, attrs }">
                <div :class="{ disabled: !simulationRunning }" class="diagram-room-temp ma-2">
                    <v-btn class="mr-1" color="grey" icon v-bind="attrs" v-on="on">
                        <v-icon>fa-thermometer-half</v-icon>
                    </v-btn>
                    <span class="grey--text f-14 font-weight-bold">{{Number(room.temperature).toFixed(2)}} &deg;C</span>
                </div>
            </template>
            <v-card>
                <v-card-title>{{room.name}} Temperature Controls</v-card-title>
                <div class="py-0 px-6">
                    <v-text-field v-if="zone" :value="zone.name" label="Current Zone" readonly prepend-icon="fa-object-ungroup">
                        <v-tooltip slot="append-outer" top>
                            <template #activator="{ on, attrs }">
                                <v-icon v-on="on" v-bind="attrs">fa-info-circle</v-icon>
                            </template>
                            <div class="text-justify" style="max-width: 300px;">To change this room's zone, open the zone list in the SHH module tab. Then, create a new zone and add this room to it. Rooms can only ever be in one zone at a time.</div>
                        </v-tooltip>
                    </v-text-field>
                    <v-switch :value="room.zoneTempOverridden" color="info" style="margin-top: -5px;" @change="dispatchEvent('setRoomOverride', { id: room.id, value: !!$event })">
                        <template #label>
                            <div class="ml-2">Override Zone Temperature
                                <v-tooltip top>
                                    <template #activator="{ on }">
                                        <v-icon class="ml-2" v-on="on">fa-info-circle</v-icon>
                                    </template>
                                    <div class="text-justify" style="max-width: 300px;">Use this control to specify whether a room should override the desired temperature set by the zone it belongs to. Any changes made to the room's desired temperature will automatically turn this option on.</div>
                                </v-tooltip>
                            </div>
                        </template>
                    </v-switch>
                    <v-subheader class="px-0" style="margin-top: -15px; margin-bottom: -10px;">
                        <v-icon class="mr-3">fa-sun</v-icon>
                        <span class="f-10 w-100">Desired Room Temperature
                            <strong class="float-right white--text">{{ room.desiredTemperature }} &deg;C</strong>
                        </span>
                    </v-subheader>
                    <v-slider
                            :value="room.desiredTemperature"
                            color="info"
                            :min="0"
                            :max="30"
                            thumb-label
                            @change="dispatchEvent('setRoomTemperature', { id: room.id, temperature: $event })"></v-slider>
                </div>
            </v-card>
        </v-dialog>
        <div @mousedown.stop="onResizeMouseDown" class="diagram-room-resize"></div>
        <div class="diagram-room-temp-overlay" :style="{ backgroundColor: temperatureColor }" v-if="colorCodedTemp"></div>
    </div>
</template>

<script>
    import interpolate from 'color-interpolate';
    export default {
        name: 'house-layout-room',
        props: {
            room: {
                required: true
            }
        },
        data() {
            return {
                diagram: null,
                left: 0,
                top: 0,
                width: 200,
                height: 200,
                offsetX: 0,
                offsetY: 0,
                dragResize: false,
                temperatureMin: 10,
                temperatureMax: 30,
                temperatureColorMap: interpolate(['blue', 'lime', 'red'])
            };
        },
        mounted() {
            this.diagram = this.firstParentOfType(this, 'house-layout');
            this.updateRoomDimensions();
        },
        methods: {
            onFocus(event) {
                if (this.diagram)
                    this.diagram.onElementFocus(event, this);
            },
            onBlur(event) {
                if (this.diagram)
                    this.diagram.onElementBlur(event, this);
            },
            onMouseDown(event) {
                if (this.diagram)
                    this.diagram.onElementMouseDown(event, this);
                let rect = this.$el.getBoundingClientRect();
                this.offsetX = event.pageX - rect.x;
                this.offsetY = event.pageY - rect.y;
            },
            onResizeMouseDown(event) {
                if (this.diagram)
                    this.diagram.onElementMouseDown(event, this);
                this.dragResize = true;
                let rect = this.$el.getBoundingClientRect();
                this.offsetX = rect.x + rect.width - event.pageX;
                this.offsetY = rect.y + rect.height - event.pageY;
            },
            onDrag(event) {
                let parentRect = this.$el.parentElement.getBoundingClientRect();
                let rect = this.$el.getBoundingClientRect();
                let diagramContent = this.diagram?.$refs.content;
                let scale = this.diagram?.$refs.panzoom.$panZoomInstance.getTransform().scale || 1;

                const borderWidth = 3;

                if (this.dragResize) {
                    let diagramContentWidth = diagramContent?.offsetWidth || 0;
                    let diagramContentHeight = diagramContent?.offsetHeight || 0;
                    this.width = Math.min(diagramContentWidth - this.left - borderWidth, Math.max(200, ((event.pageX - parentRect.x + this.offsetX) / scale) - this.left));
                    this.height = Math.min(diagramContentHeight - this.top - borderWidth, Math.max(200, ((event.pageY - parentRect.y + this.offsetY) / scale) - this.top));
                } else {
                    this.left = Math.min(Math.max((event.pageX - parentRect.x - this.offsetX) / scale, -borderWidth), (parentRect.width - rect.width) / scale - borderWidth);
                    this.top = Math.min(Math.max((event.pageY - parentRect.y - this.offsetY) / scale, -borderWidth), (parentRect.height - rect.height) / scale - borderWidth);
                }
            },
            onDragEnd() {
                this.dragResize = false;
                this.dispatchEvent('setRoomDimensions', { id: this.room.id, x: this.left, y: this.top, width: this.width, height: this.height });
            },
            updateRoomDimensions() {
                if (!this.room.dimensions)
                    return;
                this.left = this.room.dimensions.x;
                this.top = this.room.dimensions.y;
                this.width = this.room.dimensions.width;
                this.height = this.room.dimensions.height;
            },
            test() {
                console.log('asdal;sdksdal;kjsad');
            }
        },
        computed: {
            numLightsOn() {
                return this.room.lights?.filter(l => l.on).length || 0;
            },
            numDoorsLocked() {
                return this.room.doors?.filter(d => d.locked).length || 0;
            },
            numWindowsBlocked() {
                return this.room.windows?.filter(w => w.blocked).length || 0;
            },
            people() {
                let arr = this.$store.state.simulation?.people.filter(p => p.roomId === this.room.id) || [];
                if (this.$store.state.simulation && this.$store.state.simulation.userLocation === this.room.id)
                    arr.push({ id: 0, name: 'User', color: 'white' });
                return arr;
            },
            simulationRunning() {
                return !!this.$store.state.simulation?.running;
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            },
            shh() {
                return this.simulationModules.find(m => m.name === 'SHH');
            },
            zone() {
                return this.shh?.zones.find(z => z.rooms.find(r => r.id === this.room.id));
            },
            hvac() {
                return this.$store.state.hvacStates.find(s => s.id === this.room.id)?.hvac || false;
            },
            colorCodedTemp() {
                return this.$store.state.colorCodedTemperature;
            },
            temperatureColor() {
                return this.temperatureColorMap(Math.min(Math.max((this.room.temperature - this.temperatureMin) / (this.temperatureMax - this.temperatureMin), 0), 1));
            }
        },
        watch: {
            room() {
                this.updateRoomDimensions();
                this.$forceUpdate();
            }
        }
    }
</script>

<style lang="scss">
    .diagram-room-controls, .diagram-room-temp {
        display: flex;
        align-items: center;
        .v-btn {
            opacity: 0.5;
            &:hover {
                opacity: 1;
            }
        }
    }

    .menu-list .v-list-item {
        border-bottom: 1px solid rgba(white, 0.1);

        &:last-child {
            border-bottom: none;
        }
    }

    .icon-lightbulb {
        margin: 15px 15px 15px 5px;
        color: white;
        opacity: 0.3;

        &.active {
            color: #FFD600 !important;
            opacity: 1;
            text-shadow: 0 0 30px rgba(#FFD600, 0.6);
        }
    }

    .icon-door {
        color: white;
        opacity: 0.3;

        &.active {
            opacity: 1;
            text-shadow: 0 0 30px rgba(white, 0.4);
        }
    }

    .icon-lock {
        color: white;
        opacity: 0.3;

        &.active {
            color: #DD2C00 !important;
            opacity: 1;
            text-shadow: 0 0 30px rgba(#DD2C00, 0.6);
        }
    }
</style>

<style lang="scss" scoped>
    .diagram-room-temp-overlay {
        position: absolute;
        z-index: 0;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        opacity: 0.1;
        pointer-events: none;
    }

    .diagram-room-temp {
        position: absolute;
        bottom: 0;
        left: 0;
        z-index: 5;
    }

    .diagram-room-resize {
        position: absolute;
        bottom: -0.2px;
        right: -0.2px;
        width: 0;
        height: 0;
        z-index: 1;

        cursor: se-resize;

        $triangle-size: 20px;
        border-top: $triangle-size solid transparent;
        border-right: $triangle-size solid rgba(white, 0.05);

        &:hover {
            border-right: $triangle-size solid rgba(white, 0.2);
        }
    }

    .diagram-room-controls {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        z-index: 5;
    }

    .diagram-room-people {
        display: flex;
        flex-wrap: wrap;
        z-index: 5;
    }

    .diagram-room-status {
        display: flex;
        flex-wrap: wrap;
        opacity: 0.7;
        z-index: 5;

        i {
            font-size: 9pt !important;
        }

        .light {
            color: #FFD600;
            text-shadow: 0 0 15px rgba(#FFD600, 0.3);
        }

        .lock {
            color: #DD2C00;
            text-shadow: 0 0 15px rgba(#DD2C00, 0.3);
        }
    }
</style>