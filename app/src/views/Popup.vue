<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
     <div class="modal-container">
       <p>
         <v-btn @click="showForm = true">Add User</v-btn>
         <add-user v-if="showForm" @addProfile="showForm = false" v-on:addProfile="addProfile($event)">
         </add-user>
       </p>
       <Profile
           v-bind:users="this.$store.state.users"
           v-on:close="$emit('close')"
           v-on:del-user="deleteUser"
           v-on:chosen="chosenProfile($event)">
       </Profile>
     </div>
    </div>
  </div>
</template>

<script>
import Profile from "./Profile";
import AddUser from "@/views/addUser";
export default {
  components: {AddUser, Profile},
  name: "Popup",
  data () {
    return {
      showForm: false,
    }
  },
  methods: {
    addProfile (newProfile) {
      this.$store.state.users = [...this.$store.state.users, newProfile];
    },
    deleteUser(id) {
      this.$store.state.users = this.$store.state.users.filter(user => user.id !== id);
    },
    chosenProfile(title) {
      this.$emit('chosenProfile', title)
    }
  }
}
</script>

<style scoped>

.btn {
  align-content: normal;
}

</style>