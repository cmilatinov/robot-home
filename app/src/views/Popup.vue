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
           v-bind:users="users"
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
      users: [
        {
          id: 1,
          title: "Parent"
        },
        {
          id: 2,
          title: "Child"
        },
        {
          id: 3,
          title: "Guest"
        },
        {
          id: 4,
          title: "Strangers"
        }
      ]
    }
  },
  methods: {
    addProfile (newProfile) {
      this.users = [...this.users, newProfile];
    },
    deleteUser(id) {
      this.users = this.users.filter(user => user.id !== id);
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