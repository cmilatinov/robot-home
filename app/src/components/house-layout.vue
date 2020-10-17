<template>
    <div class="diagram-container"
         @mouseup="onMouseUp"
         @mouseleave="onMouseUp"
         @mousemove="onMouseMove">
        <pan-zoom ref="panzoom" class="diagram" selector=".diagram-content" :options="{ minZoom: 0.5, initialZoom: 0.5 }">
            <div ref="content" class="diagram-content">
                <slot />
            </div>
        </pan-zoom>
    </div>
</template>

<script>
    export default {
        name: "house-layout",
        data() {
            return {
                activeElement: null,
                draggingElement: null,

                mousePosition: {
                    x: 0,
                    y: 0
                },
            };
        },
        methods: {
            onElementFocus(event, component) {
                this.activeElement = component;
            },
            onElementBlur(event, component) {
                if (this.activeElement === component)
                    this.activeElement = null;
            },
            onElementMouseDown(event, component) {
                this.draggingElement = component;
            },
            onMouseMove(event) {
                if (this.draggingElement && typeof this.draggingElement.onDrag === 'function') {
                    this.draggingElement.onDrag(event);
                }

                let contentRect = this.$refs.content.getBoundingClientRect();
                let zoomScale = this.$refs.panzoom.$panZoomInstance.getTransform().scale;
                this.mousePosition.x = (event.pageX - contentRect.x) / zoomScale;
                this.mousePosition.y = (event.pageY - contentRect.y) / zoomScale;
            },
            onMouseUp() {
                if (this.draggingElement && typeof this.draggingElement.onDragEnd === 'function') {
                    this.draggingElement.onDragEnd();
                    this.draggingElement = null;
                }
            }
        }
    }
</script>

<style lang="scss">
    .diagram-container {
        flex: 1;
        position: relative;

        .diagram {
            height: 100%;
            position: relative;
            overflow: hidden;
            user-select: none;

            .diagram-content {
                position: absolute;
                width: 1000px;
                height: 1000px;
                top: 0;
                left: 0;
                border: 3px solid rgba(white, 0.1);
                background-color: rgba(white, 0.01);

                .diagram-room {
                    position: absolute;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    background-color: var(--v-secondary-darken2);
                    border: 2px solid var(--v-secondary-base);
                    padding: 5px;

                    &:hover {
                        border: 2px solid var(--v-secondary-lighten3);
                    }

                    &:focus {
                        z-index: 2;
                        border: 2px solid white !important;
                    }
                }
            }
        }
    }
</style>