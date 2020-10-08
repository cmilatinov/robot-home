import Vue from 'vue';

Vue.mixin({
    data() {
        return {

        };
    },
    computed: {

    },
    methods: {
        dispatchEvent(event, payload) {
            if (window.javaBridge && window.javaBridge.dispatchEvent && typeof window.javaBridge.dispatchEvent === 'function')
                window.javaBridge.dispatchEvent(event, payload);
        }
    }
});