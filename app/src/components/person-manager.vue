<template>
    <div>

        <!-- Add person modal -->
        <v-dialog v-model="showAddPerson" width="400">
            <v-card>
                <v-card-title>Add New Person</v-card-title>
                <v-form ref="addForm" lazy-validation>
                    <v-text-field v-model="newPerson.name"
                                  :rules="[validationRules.required, validationRules.format, validationRules.length]"
                                  color="primary"
                                  class="mx-5"
                                  placeholder="Name the person"
                                  label="Name"></v-text-field>
                    <v-select v-model="newPerson.roomId"
                              dense outlined
                              class="mx-4 mt-3 pt-0"
                              label="Location"
                              prepend-inner-icon="mdi-crosshairs-gps"
                              item-value="id"
                              item-text="name"
                              :items="roomLocations"
                              :menu-props="{ offsetY: true }">
                    </v-select>
                </v-form>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey darken-2" @click="showAddPerson = false">
                        <v-icon class="mr-2">fa-times</v-icon>Cancel
                    </v-btn>
                    <v-btn text color="primary" @click="onSubmitAddPerson">
                        <v-icon class="mr-2">fa-user-plus</v-icon>Add
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- Person list modal -->
        <v-dialog v-model="showPersonManager" width="600">
            <template #activator="{ on, attrs }">
                <v-btn color="primary" v-bind="attrs" v-on="on">
                    <v-icon class="f-10 mr-2">fa-address-book</v-icon>
                    Person List
                </v-btn>
            </template>
            <v-card>
                <v-card-title>Manage People</v-card-title>
                <v-text-field v-model="personSearch" class="mx-10 my-0 py-0" prepend-inner-icon="fa-search" placeholder="Search for a profile...">
                </v-text-field>
                <v-list class="pt-0 px-0 people-list">
                    <p class="mt-3 text-center grey--text" v-if="people.length <= 0">
                        <em>There are no people currently present in the simulation.</em>
                    </p>
                    <template v-else-if="filteredPeople.length > 0">
                        <v-list-item :key="person.id" v-for="person in filteredPeople" link>
                            <v-list-item-icon>
                                <v-icon :style="`color: ${person.color};`">fa-user</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>{{person.name}}</v-list-item-title>
                            <v-list-item-subtitle>{{person.location}}</v-list-item-subtitle>
                            <div class="list-item-actions">
                                <v-btn class="f-14 white--text mr-2" style="opacity: 0.8;" icon @mousedown.stop @click="$store.commit('onMovePerson', person)">
                                    <v-icon>mdi-crosshairs-gps</v-icon>
                                </v-btn>
                                <v-btn class="f-14 red--text" style="opacity: 0.8;" icon @mousedown.stop @click="$store.commit('onDeletePerson', person)">
                                    <v-icon>fa-trash</v-icon>
                                </v-btn>
                            </div>
                        </v-list-item>
                    </template>
                    <p class="mt-3 text-center grey--text" v-else>
                        <em>No profiles matching <strong>{{personSearch}}</strong></em>
                    </p>
                </v-list>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="grey" @click="showPersonManager = false">
                        Close
                    </v-btn>
                    <v-btn text color="primary" @click="onAddPerson">
                        Add New
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

    </div>
</template>

<script>
    import validation from "../mixins/validation";

    export default {
        name: "add-person",
        data() {
            return {
                showPersonManager: false,
                showAddPerson: false,

                personSearch: '',

                newPerson: {
                    name: '',
                    roomId: null
                }
            }
        },
        computed: {
            roomLocations() {
                return [{ id: null, name: 'Outside' }, ...(this.$store.state.simulation?.houseLayout?.rooms || [])];
            },
            people() {
                return this.$store.state.simulation?.people || [];
            },
            filteredPeople() {
                return this.people.filter(p => p.name.toLowerCase().includes(this.personSearch.toLowerCase())).map(p => {
                    let location = this.roomLocations.find(r => r.id === p.roomId)?.name;
                    if (location)
                        return { ...p, location };
                    return p;
                });
            }
        },
        methods: {
            onAddPerson() {
                this.newPerson = {
                    name: '',
                    roomId: null
                };
                this.showAddPerson = true;
            },
            onSubmitAddPerson() {
                if (!this.$refs.addForm || !this.$refs.addForm.validate())
                    return;
                this.dispatchEvent('addPerson', this.newPerson);
                this.showAddPerson = false;
            }
        },
        mixins: [validation]
    }
</script>

<style lang="scss">
    .people-list {
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
</style>