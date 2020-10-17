export default {
    data() {
        return {
            validationRules: {
                required: value => !!value || `Required`
            }
        };
    }
}