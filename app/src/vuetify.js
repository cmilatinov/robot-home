import Vue from 'vue';
import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';
import '@mdi/font/css/materialdesignicons.css';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        dark: true,
        options: {
            customProperties: true
        },
        themes: {
            dark: {
                primary: '#1db954'
            }
        }
    }
});