<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
     <div class="modal-container">
       hello
       <v-btn color="primary" @click="$emit('close')">
         Confirm
       </v-btn>
     </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Popup"
}
</script>

<style scoped>

.modal-mask {
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
  vertical-align: middle;
}

.modal-container {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 5px solid #33cc33;
  width: 75%;
  vertical-align: middle;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

</style>