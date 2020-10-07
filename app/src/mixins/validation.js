export default {
    data() {
        return {
            validationRules: {
                passwordLength: pass => pass.length >= 8 || `Minimum 8 characters`,
                passwordUpper: pass => /[A-Z]/.test(pass) || `Must contain an upper case letter`,
                passwordLower: pass => /[a-z]/.test(pass) || `Must contain a lower case letter`,
                passwordNum: pass => /[0-9]/.test(pass) || `Must contain a number`,
                passwordMatch: match => (pass => pass === match || `Passwords do not match`),
                required: value => !!value || `Required`,
                email: value => {
                    const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                    return pattern.test(value) || 'Invalid e-mail'
                }
            }
        };
    }
}