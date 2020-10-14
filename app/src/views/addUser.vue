<template>
  <div class="form-mask">
    <div class="form-wrapper">
      <div class="form-container">
        <form @submit="addProfile">
          <h3 class="form">New User</h3>
          <p style="color:black">
            User name:
            <input class="text" type="text" v-model="title" name="title" placeholder="name...">
          </p>
          <input type="submit" value="Submit" class="btn">
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {v4 as uuidv4} from 'uuid';
uuidv4();
export default {
  name: "addUser",
  data () {
    return {
      title: ''
    }
  },
  methods: {
    addProfile(e) {
      e.preventDefault();
      const newProfile = {
        id: uuidv4(),
        title: this.title
      }
      this.$emit('addProfile', newProfile)
    }
  }
}
</script>

<style scoped>

.text {
  margin: auto;
  width: 75%;
  border: black;
  padding: 7px 20px;
  cursor: text;
}

.form {
  text-align: center;
  color: black;
}

.btn {
  display: inline-block;
  border: none;
  background-color: black;
  color: #fff;
  padding: 7px 20px;
  cursor: pointer;
}

.form-mask {
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

.form-container {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 2px solid #33cc33;
  width: 75%;
  vertical-align: middle;
}

.form-wrapper {
  display: table-cell;
  vertical-align: middle;
}
</style>