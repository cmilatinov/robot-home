<template>
    <div @mousedown.stop="onMouseDown"
         @focus="onFocus"
         @blur="onBlur"
         tabIndex="0"
         class="diagram-room"
         :style="`top: ${top}px; left: ${left}px; width: ${width}px; height: ${height}px;`">
        <h2>{{room.getName()}}</h2>
        <div @mousedown.stop="onResizeMouseDown" class="diagram-room-resize"></div>
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
            this.left = Number(this.room.getDimensions().getX());
            this.top = Number(this.room.getDimensions().getY());
            this.width = Number(this.room.getDimensions().getWidth());
            this.height = Number(this.room.getDimensions().getWidth());
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
                    this.width = Math.min(diagramContentWidth - this.left - borderWidth, Math.max(100, ((event.pageX - parentRect.x + this.offsetX) / scale) - this.left));
                    this.height = Math.min(diagramContentHeight - this.top - borderWidth, Math.max(100, ((event.pageY - parentRect.y + this.offsetY) / scale) - this.top));
                } else {
                    this.left = Math.min(Math.max((event.pageX - parentRect.x - this.offsetX) / scale, -borderWidth), (parentRect.width - rect.width) / scale - borderWidth);
                    this.top = Math.min(Math.max((event.pageY - parentRect.y - this.offsetY) / scale, -borderWidth), (parentRect.height - rect.height) / scale - borderWidth);
                }
            },
            onDragEnd() {
                this.dragResize = false;
            }
        }
    }
</script>

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
</style>