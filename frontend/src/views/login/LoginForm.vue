<template>
  <v-card v-if="form" class="pa-4 elevation-12" min-width="480px">
    <v-card-title>
      <div class="headline mb-6">AERGO Forms</div>
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
        <v-checkbox v-model="isRememberChecked" :ripple="false" class="ma-0 pa-0" @change="changeRemember">
          <template v-slot:label>
            <label>로그인 정보 저장</label>
          </template>
        </v-checkbox>
      </v-form>

      <Button
        text="로그인"
        type="btn-primary"
        size="big"
        full-width
        :disabled="isDisabled"
        @click.native="requestLoginUUID"
      />
      <v-row justify="center" class="mt-3 mb-6">
        <p class="signup" @click="viewSignup">계정 만들기</p>
        <p class="signup" @click="viewQRCode">QR TEST</p>
      </v-row>
      <p align="center">Copyright ⓒ 2020 BLOCKO. All right reserved.</p>
    </v-card-text>
  </v-card>
</template>

<script>
import Button from '@/components/Button';

export default {
  name: 'LoginForm',
  components: {
    Button,
  },
  data: function() {
    return {
      form: true,
      isRememberChecked: false,
      userID: '',
      userPwd: '',
      isValidID: false,
      isValidPwd: false,
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
    requestLoginUUID() {
      // 로그인 정보
      let params = {
        username: this.userID,
        password: this.userPwd,
      };

      this.$axios
        .post('http://192.168.1.212:9000/users/presignin', params)
        .then((response) => {
          if (response.status == 200) {
            let data = response.data;
            this.form = false;
            this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_LOGIN2QRCODE_FORM, data);
          } else {
            alert('로그인 실패');
          }
        })
        .catch((err) => {
          alert('에러 발생: ' + err);
        });
    },
    // 엔터 키로 로그인
    enterLogin(event) {
      if (this.isValidID && this.isValidPwd && event.keyCode === 13) {
        this.requestLoginUUID();
      }
    },
    // 아이디 저장 기능 사용 여부
    changeRemember(checked) {
      localStorage.setItem('isRemember', checked);
      if (checked === false) {
        localStorage.removeItem('rememberID');
      }
    },
    viewSignup() {
      this.form = false;
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_SIGNUP_FORM);
    },
    viewQRCode() {
      this.form = false;
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.VIEW_LOGIN2QRCODE_FORM, 'fromlogin');
    },
  },
  computed: {
    isDisabled: function() {
      if (this.isValidID && this.isValidPwd) return false;
      else return true;
    },
  },
  mounted() {
    let isRemember = localStorage.getItem('isRemember');
    if (isRemember != null) {
      if (isRemember === 'true') {
        this.isRememberChecked = true;
      }
    } else {
      // 로그인 ID 저장 정보가 없으면 기본값 true
      this.isRememberChecked = true;
    }
    if (this.isRememberChecked) {
      let rememberID = localStorage.getItem('rememberID');
      if (rememberID != null) {
        this.userID = rememberID;
        this.validID();
      }
    }
    // 폼의 뷰을 제어하는 버스
    this.$bus.$on(this.$DEFINE_EVENT_NAME.VIEW_LOGIN_FORM, () => {
      this.form = true;
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.VIEW_LOGIN_FORM);
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
