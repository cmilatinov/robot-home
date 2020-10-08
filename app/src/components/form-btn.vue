<template>
    <v-btn :loading="loading" :disabled="loading" @click="onClick" :large="large" :rounded="rounded" :outlined="outlined" :block="block" :color="color">
        <slot />
        <template #loader>
            <span class="loader">
                <v-icon>mdi-cached</v-icon>
            </span>
        </template>
    </v-btn>
</template>

<script>
    export default {
        name: 'form-btn',
        props: {
            async: {
                type: Boolean,
                default: true
            },
            large: {
                type: Boolean,
                default: false
            },
            rounded: {
                type: Boolean,
                default: false
            },
            outlined: {
                type: Boolean,
                default: false
            },
            block: {
                type: Boolean,
                default: false
            },
            color: {
                type: String,
                default: 'primary'
            }
        },
        data() {
            return {
                loading: false
            };
        },
        methods: {
            onClick() {
                if (this.async) {
                    this.loading = true;
                    this.$emit('click', () => this.loading = false);
                } else {
                    this.$emit('click');
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .loader {
        animation: loader 1s infinite;
        display: flex;
    }
    @keyframes loader {
        from {
            transform: rotate(0);
        }
        to {
            transform: rotate(360deg);
        }
    }
</style>