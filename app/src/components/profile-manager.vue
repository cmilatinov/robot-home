<template>
    <div>

        <!-- New user profile modal -->
        <v-dialog width="400" v-model="showNewProfile">
            <v-card>
                <v-card-title>Create New User Profile</v-card-title>
                <v-form ref="addForm" lazy-validation>
                    <v-text-field v-model="newProfile.name"
                                  :rules="[validationRules.required, validationRules.format, validationRules.length]"
                                  color="primary" class="mx-5" placeholder="Name the profile" label="Name"></v-text-field>
                </v-form>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showNewProfile = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="onSubmitCreateProfile">
                        <v-icon class="mr-2">fa-user-plus</v-icon>Create
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Edit user profile modal -->
        <v-dialog width="400" v-model="showEditProfile">
            <v-card v-if="editProfile">
                <v-card-title>Edit User Profile</v-card-title>
                <v-form ref="editForm" lazy-validation>
                    <v-text-field v-model="editProfile.name"
                                  :rules="[validationRules.required, validationRules.format, validationRules.length]"
                                  color="primary" class="mx-5" placeholder="Rename the profile" label="Name"></v-text-field>
                </v-form>
                <v-card-text>
                    <div class="subtitle-2" v-if="currentProfile">
                        <strong>Current:</strong> <em style="font-weight: 300">{{ currentProfile.name }}</em>
                    </div>
                </v-card-text>

                <v-subheader style="margin-top: -15px">Permissions ({{editProfile.permissions.length || 'None'}} selected)</v-subheader>
                <div class="mx-5">
                    <v-expansion-panels :key="module.name" v-for="module in simulationModules" class="mb-3">
                        <v-expansion-panel style="background-color: rgba(255, 255, 255, 0.03)">
                            <v-expansion-panel-header class="d-flex py-0">
                                <v-simple-checkbox class="flex-0 mr-3" dense color="primary"
                                                   :value="module.commandList.every(c => editProfile.permissions.includes(c))"
                                                   @input="onChangeModulePermission(module, $event)"></v-simple-checkbox>
                                {{module.name}}
                            </v-expansion-panel-header>
                            <v-expansion-panel-content class="pl-3 pt-2">
                                <v-checkbox dense class="ma-0 pa-0" :input-value="editProfile.permissions.includes(command)"
                                            :key="command" v-for="command in module.commandList"
                                            :label="command" @change="onChangePermission(command, $event)"></v-checkbox>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </div>


                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showEditProfile = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="onSubmitEditProfile">
                        <v-icon class="mr-2">fa-save</v-icon>Save
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Delete user profile modal -->
        <v-dialog width="400" v-model="showDeleteProfile">
            <v-card v-if="deleteProfile">
                <v-card-title>Delete User Profile</v-card-title>
                <v-card-text>
                    Are you sure you want to delete <strong><em>{{deleteProfile.name}}</em></strong> ?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showDeleteProfile = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="red" @click="dispatchEvent('deleteProfile', deleteProfile), showDeleteProfile = false">
                        <v-icon class="mr-2">fa-trash-alt</v-icon>Delete
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!--User list modal -->
        <v-dialog v-model="showProfileManager" width="600">
            <template #activator="{ on, attrs }">
                <div class="d-flex justify-center">
                    <v-btn class="px-4" color="primary" :disabled="simulationRunning" v-bind="attrs" v-on="on">
                        <v-icon class="mr-2">fa-users-cog</v-icon>User profiles
                    </v-btn>
                </div>
            </template>
            <v-card>
                <v-card-title>Manage User Profiles</v-card-title>
                <v-text-field v-model="profileSearch" class="mx-10 my-0 py-0" prepend-inner-icon="fa-search" placeholder="Search for a profile...">
                </v-text-field>
                <v-list class="pt-0 px-0 profile-list">
                    <template v-if="filteredUserProfiles.length > 0">
                        <v-list-item class="px-4" link :key="user.id" v-for="user in filteredUserProfiles">
                            <v-list-item-icon>
                                <v-icon>fa-user-tag</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>{{ user.name }}</v-list-item-title>
                            <div class="list-item-actions">
                                <v-btn class="f-14 blue--text mr-3" style="opacity: 0.8;" icon @mousedown.stop @click.stop="onEditProfile(user)">
                                    <v-icon>fa-edit</v-icon>
                                </v-btn>
                                <v-btn class="f-14 red--text" style="opacity: 0.8;" icon @mousedown.stop @click.stop="onDeleteProfile(user)" v-if="userProfiles.length > 1">
                                    <v-icon>fa-trash</v-icon>
                                </v-btn>
                            </div>
                        </v-list-item>
                    </template>
                    <p class="mt-3 text-center grey--text" v-else>
                        <em>No profiles matching <strong>{{profileSearch}}</strong></em>
                    </p>
                </v-list>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey" @click="showProfileManager = false">
                        Close
                    </v-btn>
                    <v-btn text color="primary" @click="onCreateProfile">
                        Create New
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

    </div>
</template>

<script>
    import validation from "../mixins/validation";

    export default {
        name: 'profile-manager',
        data() {
            return {
                showProfileManager: false,

                showNewProfile: false,
                showEditProfile: false,
                showDeleteProfile: false,

                newProfile: {
                    name: ''
                },
                editProfile: null,
                deleteProfile: null,

                profileSearch: ''
            }
        },
        computed: {
            userProfiles() {
                return this.$store.state.simulation?.userProfiles || [];
            },
            currentProfile() {
                if (this.editProfile)
                    return this.userProfiles.find(p => p.id === this.editProfile.id);
                return null;
            },
            filteredUserProfiles() {
                return this.userProfiles.filter(p => p.name.toLowerCase().includes(this.profileSearch));
            },
            simulationRunning() {
                return !!this.$store.state.simulation?.running;
            },
            simulationModules() {
                return this.$store.state.simulation?.modules || [];
            }
        },
        methods: {
            onEditProfile(profile) {
                this.editProfile = JSON.parse(JSON.stringify(profile));
                this.showEditProfile = true;
            },
            onCreateProfile() {
                this.newProfile = {
                    name: ''
                };
                this.showNewProfile = true;
            },
            onDeleteProfile(profile) {
                this.deleteProfile = JSON.parse(JSON.stringify(profile));
                this.showDeleteProfile = true;
            },
            onSubmitEditProfile() {
                if (!this.$refs.editForm || !this.$refs.editForm.validate())
                    return;
                this.dispatchEvent('editProfile', this.editProfile);
                this.showEditProfile = false;
            },
            onSubmitCreateProfile() {
                if (!this.$refs.addForm || !this.$refs.addForm.validate())
                    return;
                this.dispatchEvent('addProfile', this.newProfile);
                this.showNewProfile = false;
            },
            onChangePermission(permission) {
                let permissionIndex = this.editProfile.permissions.indexOf(permission);
                if (permissionIndex >= 0)
                    this.editProfile.permissions.splice(permissionIndex, 1);
                else
                    this.editProfile.permissions.push(permission);
            },
            onChangeModulePermission(m, value) {
                if (value) {
                    m.commandList.forEach(c => {
                        if (!this.editProfile.permissions.includes(c))
                            this.editProfile.permissions.push(c);
                    });
                } else {
                    m.commandList.forEach(c => {
                        let permissionIndex = this.editProfile.permissions.indexOf(c);
                        if (permissionIndex >= 0)
                            this.editProfile.permissions.splice(permissionIndex, 1);
                    });
                }
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss">
    .profile-list {
        .v-list-item {
            border-bottom: 1px solid rgba(white, 0.1);

            &:first-child {
                border-top: 1px solid rgba(white, 0.1);
            }
        }
    }
</style>

<style lang="scss" scoped>
    .list-item-actions {
        position: absolute;
        right: 15px;
        top: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        font-size: 12pt !important;
    }

    .module-title {
        font-size: 16pt;
        padding: 0.5rem 1rem;
        border-bottom: 1px solid white;
    }
</style>