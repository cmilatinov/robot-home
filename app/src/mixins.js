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
            if (typeof window.cefQuery === 'function') {
                window.cefQuery({
                    request: `${event},${JSON.stringify(payload)}`,
                    persistent: false,
                    onSuccess: () => {},
                    onFailure: () => {}
                });
            }
        },
        firstParentOfType(ref, tag) {
            while (ref.$options._componentTag !== tag && ref.$parent)
                ref = ref.$parent;
            return ref.$options._componentTag !== tag ? null : ref;
        }
    }
});