<template>
  <!-- <v-navigation-drawer app permanent>
    <v-list>
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-icon>mdi-account-circle</v-icon>
        </v-list-item-avatar>
      </v-list-item>

      <v-list-item link>
        <v-list-item-content>
          <v-list-item-title class="title">username</v-list-item-title>
          <v-list-item-subtitle>email</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <v-divider></v-divider>

    <v-list nav dense>
      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-folder</v-icon>
        </v-list-item-icon>
        <v-list-item-title>folder 1</v-list-item-title>
      </v-list-item>
      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-account-multiple</v-icon>
        </v-list-item-icon>
        <v-list-item-title>folder 2</v-list-item-title>
      </v-list-item>
      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-star</v-icon>
        </v-list-item-icon>
        <v-list-item-title>folder 3</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer> -->
  <v-app-bar app color="primary" dark>
    <div class="d-flex align-center headline">
      AERGO Forms
    </div>

    <v-spacer></v-spacer>

    <v-btn text @click="logout">
      <span class="mr-2">Logout</span>
    </v-btn>
  </v-app-bar>
</template>

<script>
export default {
  methods: {
    logout() {
      this.$router.push('/login');
      // this.$axios
      //   .post(this.$api.LOGOUT)
      //   .then(response => {
      //     if (response.status === 200) {
      //       this.$axios.defaults.headers.common["Authorization"] = null;
      //       sessionStorage.removeItem("georgia-token");
      //       sessionStorage.removeItem("georgia-id");
      //       this.$router.push("/login");
      //     } else {
      //       console.log(response.message);
      //     }
      //   })
      //   .catch(err => {
      //     console.error(err);
      //   });
    },
  },
};
</script>
