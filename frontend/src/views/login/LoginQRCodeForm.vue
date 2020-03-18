<template>
  <v-card v-if="form" class="pa-4 elevation-12" min-width="480px">
    <v-card-title>
      <div class="headline mb-6">QR 로그인</div>
    </v-card-title>
    <v-card-text>
      <v-col>
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

export default {
  name: 'QRCodeForm',
  components: {
    QRCodeVue,
  },
  data: function() {
    return {
      value: '',
      size: 300,
      form: false,
      type: true,
      uuid: '',
    };
  },
  methods: {
    requestAuth() {
      // 회원가입처리 처리
      this.$axios
        .post('/auth', {
          uuid: this.uuid,
        })
        .then((response) => {
          if (response.status == 200) {
            alert('회원가입 완료');
            this.$router.push('/');
          } else {
            alert('가입 실패');
          }
        })
        .catch((err) => {
          alert('에러 발생' + err);
        });
    },
    goBack() {
      this.form = false;
      // true: 로그인, false: 회원가입
      if (this.type) {
        this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_LOGIN_FORM);
      } else {
        this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP_FORM);
      }
    },
  },
  mounted() {
    // 폼의 뷰을 제어하는 버스
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_LOGIN2QRCODE_FORM, (data) => {
      this.form = true;
      this.type = true;
      this.value = data;
      //   this.requestAuth();
    });
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP2QRCODE_FORM, (data) => {
      this.form = true;
      this.type = false;
      this.value = data;
      //   this.requestAuth();
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
