<template>
  <v-app>
    <TheNavBar />
    <v-content>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import TheNavBar from '@/components/TheNavBar.vue';

export default {
  components: {
    TheNavBar,
  },
};
</script>
