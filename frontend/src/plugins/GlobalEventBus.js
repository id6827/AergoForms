const GlobalEventBus = {
  install(Vue) {
    Vue.prototype.$bus = new Vue();
    Vue.prototype.$DEFINE_EVENT_NAME = {
      OPEN_VOTE_DETAIL: "openVoteDetail",
      GET_VOTE_DETAIL: "getVoteDetail",
      CLOSE_VOTE_DETAIL: "closeVoteDetail",
      VIEW_LOGIN_FORM: "viewLoginForm",
      VIEW_SIGNUP_FORM: "viewSignupForm",
      VIEW_LOGIN2QRCODE_FORM: "viewLogin2qrcodeForm",
      VIEW_SIGNUP2QRCODE_FORM: "viewSignup2qrcodeForm",
    };
  }
};

export default GlobalEventBus;