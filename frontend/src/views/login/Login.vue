<template>
  <v-app>
    <v-content fluid>
      <v-row align="center" justify="center" class="fill-height">
        <v-col md="4">
          <LoginForm />
          <SignupForm />
        </v-col>
      </v-row>
    </v-content>
  </v-app>
</template>

<script>
import LoginForm from './LoginForm';

export default {
  name: 'login',
  components: {
    LoginForm,
    SignupForm: () => import('./SignupForm.vue'),
  },
};
</script>
