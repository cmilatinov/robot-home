<template>
  <div>
     <div class="inline" v-bind:key="user.id" v-for="user in users">
       <p>
         <v-btn x-small elevation="2"  @click="$emit('del-user', user.id)">
           <v-icon small>{{ icons.mdiDelete }}</v-icon>
         </v-btn>
       </p>
      <v-btn class="inline" color="primary" @click="$emit('close')">
        <v-icon>{{icons.mdiAccount}}</v-icon>
        {{user.title}}
      </v-btn>
     </div>
  </div>
</template>

<script>
import {
  mdiAccount,
  mdiDelete,
} from '@mdi/js'

export default {
  name: "Profile",
  props: ["users"],
  data: () => ({
    icons: {
      mdiAccount,
      mdiDelete
    }
  })
}
</script>

<style scoped>

p {
  text-align: center
}

.inline {
  display:inline-block;
  margin-right: 5px;
}
</style>