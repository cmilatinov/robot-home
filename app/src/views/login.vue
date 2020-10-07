<template>
    <v-container fluid class="h-100 d-flex flex-column justify-center align-center">
        <v-card elevation="15" class="login-container">
            <div class="login-image" v-bg-img="require('../assets/img/house.jpg')">
            </div>
            <div class="login-form px-10">
                <v-card-title class="login-title">Login</v-card-title>
                <v-card-subtitle class="login-subtitle">Don't have an account yet?
                    <router-link to="/register">Click here</router-link> to register.
                </v-card-subtitle>

                <div class="login-fields">
                    <v-text-field v-model="username" label="Username" prepend-icon="fa-user">
                    </v-text-field>
                    <v-text-field type="password" v-model="password" label="Password" prepend-icon="fa-lock">
                    </v-text-field>
                </div>

                <form-btn class="mt-3 mx-6" @click="login">Login</form-btn>
            </div>
        </v-card>
    </v-container>
</template>

<script>
    import FormBtn from "../components/form-btn";
    export default {
        name: "login",
        components: {FormBtn},
        data() {
            return {
                username: '',
                password: ''
            };
        },
        methods: {
            login(callback) {
                setTimeout(callback, 3000);
            }
        }
    }
</script>

<style lang="scss" scoped>

    .login-container {
        display: flex;
        width: 80%;
        max-width: 70rem;
        overflow: hidden;

        .login-image {
            flex: 1;
            box-shadow: inset 0 0 300px black;
            background-size: cover;
            background-position: center;
            border-radius: 0 !important;
        }

        .login-form {
            display: flex;
            flex-direction: column;
            justify-content: center;
            padding: 1rem 0 3rem 0;
            width: 25rem;

            .login-title {
                margin: 0 -1rem;
                padding-bottom: 0.2rem;
                font-weight: bold;
                letter-spacing: 1px;
                font-size: 20pt;
            }

            .login-subtitle {
                margin: 0 -1rem;
                font-weight: 300;
                opacity: 0.8;
            }

            .login-fields {
                margin: 6rem 0;
            }
        }
    }


</style>