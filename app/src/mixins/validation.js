export default {
    data() {
        return {
            validationRules: {
                required: value => !!value || `Required`,
                format: value =>  /[a-zA-Z]+/.test(value) || `Must contain at least one letter`,
                length: value => (value.length < 16) || 'Must contain less than 16 characters'
            }
        };
    }
}