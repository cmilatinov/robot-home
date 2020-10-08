import Vue from 'vue';

Vue.directive(`bg-img`, {
    bind(el, binding) {
        el.style.backgroundImage = `url('${binding.value}')`;
    },
    update(el, binding) {
        el.style.backgroundImage = `url('${binding.value}')`;
    },
});