<template>
    <div @mousedown.stop="onMouseDown"
         @focus="onFocus"
         @blur="onBlur"
         tabIndex="0"
         class="diagram-room"
         :style="`left: ${left}px; top: ${top}px; width: ${width}px; height: ${height}px;`">
        <h1>{{room.name}}</h1>
        <div class="diagram-room-status">
            <v-icon class="ma-1 light" :key="`l-${i}`" v-for="i in (0, numLightsOn)">fa-lightbulb</v-icon>
            <v-icon class="ma-1 lock" :key="`dl-${i}`" v-for="i in (0, numDoorsLocked)">fa-lock</v-icon>
            <v-icon class="ma-1 lock" :key="`wb-${i}`" v-for="i in (0, numWindowsBlocked)">fa-ban</v-icon>
        </div>
        <div @mousedown.stop="onResizeMouseDown" class="diagram-room-resize"></div>
        <div class="diagram-room-controls">
            <v-menu offset-y :close-on-content-click="false">
                <template #activator="{ on, attrs }">
                    <v-btn icon class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fa-lightbulb</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="light.id" v-for="(light, index) in room.lights" class="pl-2" @click="dispatchEvent('toggleLightState', { id: light.id, on: !light.on })">
                            <v-icon class="icon-lightbulb" :class="{ active: light.on }">{{!light.on ? 'far' : 'fas'}} fa-lightbulb</v-icon> Light {{ index + 1 }}
                        </v-list-item>
                    </v-list>
                </v-card>
            </v-menu>

            <v-menu offset-y :close-on-content-click="false">
                <template #activator="{ on, attrs }">
                    <v-btn icon class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fa-door-open</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="door.id" v-for="(door, index) in room.doors" class="pl-2">
                            <div class="d-flex">
                                <v-btn icon class="mr-1" @mousedown.stop @click="dispatchEvent('toggleDoorState', { id: door.id, open: !door.open, locked: door.locked })">
                                    <v-icon class="icon-door" :class="{ active: door.open }">fa-{{door.open ? 'door-open' : 'door-closed'}}</v-icon>
                                </v-btn>
                                <v-btn icon class="mr-2" @mousedown.stop @click="dispatchEvent('toggleDoorState', { id: door.id, open: door.open, locked: !door.locked })">
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
                    <v-btn icon class="ma-2" v-bind="attrs" v-on="on">
                        <v-icon>fab fa-windows</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <v-list class="py-0 menu-list">
                        <v-list-item link :key="window.id" v-for="(window, index) in room.windows" class="pl-2">
                            <div class="d-flex">
                                <v-btn icon class="mr-1" @mousedown.stop @click="dispatchEvent('toggleWindowState', { id: window.id, open: !window.open, blocked: window.blocked })">
                                    <v-icon class="icon-door" :class="{ active: window.open }">fa-{{window.open ? 'door-open' : 'door-closed'}}</v-icon>
                                </v-btn>
                                <v-btn icon class="mr-2" @mousedown.stop @click="dispatchEvent('toggleWindowState', { id: window.id, open: window.open, blocked: !window.blocked })">
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
    </div>
</template>

<script>
    export default {
        name: "house-layout-room",
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
                dragResize: false
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
                this.left = this.room.dimensions.x;
                this.top = this.room.dimensions.y;
                this.width = this.room.dimensions.width;
                this.height = this.room.dimensions.height;
            }
        },
        computed: {
            numLightsOn() {
                return this.room.lights.filter(l => l.on).length;
            },
            numDoorsLocked() {
                return this.room.doors.filter(d => d.locked).length;
            },
            numWindowsBlocked() {
                return this.room.windows.filter(w => w.blocked).length;
            }
        },
        watch: {
            room() {
                this.updateRoomDimensions();
            }
        }
    }
</script>

<style lang="scss">
    .diagram-room-controls .v-btn {
        opacity: 0.5;
        &:hover {
            opacity: 1;
        }
    }

    .menu-list .v-list-item {
        border-bottom: 1px solid rgba(white, 0.1);

        &:last-child {
            border-bottom: none;
        }
    }

    .icon-lightbulb {
        margin: 15px;
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
    .diagram-room-resize {
        position: absolute;
        bottom: -0.2px;
        right: -0.2px;
        width: 0;
        height: 0;

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
    }

    .diagram-room-status {
        display: flex;
        flex-wrap: wrap;
        opacity: 0.7;

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