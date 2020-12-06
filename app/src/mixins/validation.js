export default {
    data() {
        return {
            validationRules: {
                required: value => value === 0 || !!value || `Required`,
                requiredArray: value => value.length > 0 || `Required`,
                format: value =>  /[a-zA-Z0-9]+/.test(value) || `Must contain at least one letter`,
                length: value => (value.length < 17) || 'Must contain less than or equal to 16 characters'
            }
        };
    }
}