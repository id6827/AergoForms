<template>
  <v-card v-if="form" class="pa-4 elevation-12" min-width="480px">
    <v-card-title>
      <div class="headline mb-6">회원가입</div>
    </v-card-title>
    <v-card-text>
      <v-form>
        <v-text-field
          v-model="userID"
          type="text"
          outlined
          required
          label="아이디"
          placeholder="아이디를 입력하세요"
          @keyup="validID"
          @keydown.native="enterLogin"
        ></v-text-field>
        <v-text-field
          v-model="userPwd"
          type="password"
          :value="userPwd"
          outlined
          required
          label="비밀번호"
          placeholder="비밀번호를 입력하세요"
          @keyup="validPwd"
          @keydown.native="enterLogin"
        ></v-text-field>
        <v-text-field
          v-model="userPwd2"
          type="password"
          outlined
          required
          label="비밀번호 확인"
          placeholder="비밀번호를 한번 더 입력하세요"
          @keyup="validPwd2"
          @keydown.native="enterLogin"
        ></v-text-field>
        <v-row class="mt-0 pt-0">
          <v-col>
            <v-select v-model="year" :items="years" label="출생연도" placeholder="선택" outlined required> </v-select>
          </v-col>
          <v-col>
            <v-radio-group label="성별" v-model="gender" :mandatory="false" row>
              <v-radio label="남자" value="0"></v-radio>
              <v-radio label="여자" value="1"></v-radio>
            </v-radio-group>
          </v-col>
        </v-row>
      </v-form>

      <Button
        text="가입"
        type="btn-primary"
        size="big"
        full-width
        :disabled="isDisabled"
        @click.native="requestSignupUUID"
      />
      <v-row justify="center" class="mt-3 mb-6">
        <p class="login" @click="viewLogin">돌아가기</p>
        <p class="login" @click="viewQRCode">QR TEST</p>
      </v-row>
      <p align="center">Copyright ⓒ 2020 BLOCKO. All right reserved.</p>
    </v-card-text>
  </v-card>
</template>

<script>
import Button from '@/components/Button';

export default {
  name: 'SignupForm',
  components: {
    Button,
  },
  data: function() {
    return {
      form: false,
      userID: '',
      userPwd: '',
      userPwd2: '',
      year: '',
      gender: '',
      isValidID: false,
      isValidPwd: false,
      isValidPwd2: false,
    };
  },
  methods: {
    validID() {
      if (this.userID !== '') this.isValidID = true;
      else this.isValidID = false;
    },
    validPwd() {
      if (this.userPwd !== '') this.isValidPwd = true;
      else this.isValidPwd = false;
    },
    validPwd2() {
      if (this.userPwd2 !== '') this.isValidPwd2 = true;
      else this.isValidPwd2 = false;
    },
    requestSignupUUID() {
      // 회원가입 처리
      let params = {
        username: this.userID,
        password: this.userPwd2,
        birthyear: this.year,
        gender: this.gender,
      };

      this.$axios
        .post('http://192.168.1.212:9000/users/presignup', params)
        .then((response) => {
          if (response.status == 200) {
            let data = response.data;
            this.form = false;
            this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP2QRCODE_FORM, data);
          } else {
            alert('회원가입 오류');
          }
        })
        .catch((err) => {
          alert('에러 발생: ' + err);
        });
    },
    // 엔터 키로 로그인
    enterLogin(event) {
      if (this.isValidID && this.isValidPwd && this.isValidPwd2 && event.keyCode === 13) {
        this.requestSignupUUID();
      }
    },
    viewLogin() {
      this.form = false;
      this.userID = '';
      this.userPwd = '';
      this.userPwd2 = '';
      this.year = '';
      this.gender = '';

      this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_LOGIN_FORM);
    },
    viewQRCode() {
      this.form = false;
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP2QRCODE_FORM, 'fromsignup');
    },
  },
  computed: {
    isDisabled: function() {
      if (this.isValidID && this.isValidPwd && this.isValidPwd2) return false;
      else return true;
    },
    years() {
      const year = new Date().getFullYear();
      return Array.from({ length: year - 1919 }, (value, index) => 2001 - index);
    },
  },
  mounted() {
    // OPEN & GET DETAIL
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP_FORM, () => {
      this.form = true;
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP_FORM);
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

.login:hover {
  cursor: pointer;
}

/* 스낵바 글자색상 */
>>> .v-snack__content {
  color: black;
}
</style>
