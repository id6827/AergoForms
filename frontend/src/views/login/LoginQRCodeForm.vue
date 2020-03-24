<template>
  <v-card v-if="form" class="pa-4 elevation-12" min-width="480px">
    <v-card-title>
      <div class="headline mb-6">2FA - QR 인증</div>
    </v-card-title>
    <v-card-text>
      <v-col>
        <v-row justify="center">
          <Timer :time="prettyTime" />
        </v-row>
        <v-row justify="center">
          <QRCodeVue :value="value" :size="size" level="H"></QRCodeVue>
        </v-row>
        <v-row justify="center" class="mt-3 mb-6">
          <p class="signup" @click="goBack">돌아가기</p>
        </v-row>
        <p align="center">Copyright ⓒ 2020 BLOCKO. All right reserved.</p>
      </v-col>
    </v-card-text>
  </v-card>
</template>
<script>
import QRCodeVue from 'qrcode.vue';
import Timer from '@/components/Timer';

export default {
  name: 'QRCodeForm',
  components: {
    QRCodeVue,
    Timer,
  },
  data: function() {
    return {
      value: {},
      size: 300,
      form: false,
      type: true,
      uuid: '',
      time: 180,
      timer: null,
    };
  },
  computed: {
    prettyTime() {
      let time = this.time / 60;
      let minutes = parseInt(time);
      let secondes = Math.round((time - minutes) * 60);
      return minutes + ':' + secondes;
    },
  },
  methods: {
    requestAuth(data) {
      // 로그인 처리
      console.log(data);
      let form = JSON.parse(data);
      let params = {
        uuid: form.uuid,
        username: form.username,
      };

      this.$axios
        .post('http://192.168.1.212:9000/users/signin', params)
        .then((response) => {
          if (response.status == 200) {
            console.log(response);
            alert('로그인 완료');
            this.$router.push('/');
          } else {
            alert('가입 실패');
          }
        })
        .catch((err) => {
          alert('에러 발생' + err);
        });
    },
    requestSignup(data) {
      // 회원가입처리 처리
      console.log(data);
      let form = JSON.parse(data);
      let params = {
        uuid: form.uuid,
        username: form.username,
      };
      console.log(params);

      this.$axios
        .post('http://192.168.1.212:9000/users', params)
        .then((response) => {
          if (response.status == 201) {
            console.log(response);
            alert('회원가입 완료');
            this.$router.push('/');
          } else {
            console.log(response);
            alert('가입 실패');
          }
        })
        .catch((err) => {
          alert('에러 발생' + err);
        });
    },
    goBack() {
      this.form = false;
      this.time = 180;
      // true: 로그인, false: 회원가입
      if (this.type) {
        this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_LOGIN_FORM);
      } else {
        this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP_FORM);
      }
    },
    start() {
      if (!this.timer) {
        this.timer = setInterval(() => {
          if (this.time > 0) {
            this.time--;
          } else {
            clearInterval(this.timer);
            this.sound.play();
            this.reset();
          }
        }, 1000);
      }
    },
  },
  mounted() {
    this.start();
    // 폼의 뷰을 제어하는 버스
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_LOGIN2QRCODE_FORM, (data) => {
      this.form = true;
      this.type = true;
      console.log(data);
      this.value = JSON.stringify(data);
      this.requestAuth(JSON.stringify(data));
    });
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP2QRCODE_FORM, (data) => {
      this.form = true;
      this.type = false;
      console.log(data);
      this.value = JSON.stringify(data);
      this.requestSignup(JSON.stringify(data));
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.VIEW_LOGIN2QRCODE_FORM);
    this.$bus.$off(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP2QRCODE_FORM);
  },
};
</script>
<style scoped>
label {
  color: black;
}
button {
  width: 100%;
}
.signup:hover {
  cursor: pointer;
}
/* 스낵바 글자색상 */
>>> .v-snack__content {
  color: black;
}
</style>
