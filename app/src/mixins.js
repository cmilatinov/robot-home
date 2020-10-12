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
        },
        firstParentOfType(ref, tag) {
            while (ref.$options._componentTag !== tag && ref.$parent)
                ref = ref.$parent;
            return ref.$options._componentTag !== tag ? null : ref;
        }
    }
});