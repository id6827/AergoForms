<template>
  <v-app>
    <v-content fluid>
      <v-row align="center" justify="center" class="fill-height">
        <v-col md="4">
          <LoginForm />
          <SignupForm />
          <QRCodeForm />
        </v-col>
      </v-row>
    </v-content>
  </v-app>
</template>

<script>
import LoginForm from './LoginForm';
import SignupForm from './LoginSignupForm';
import QRCodeForm from './LoginQRCodeForm';

export default {
  name: 'login',
  components: {
    LoginForm,
    SignupForm,
    QRCodeForm,
  },
};
</script>
