<template>
  <button
    v-bind:class="[type, size, fullWidth ? 'full-width' : '', 'btn-width', center ? 'center' : '']"
    v-bind:disabled="disabled == 1"
  >
    <div class="concat">
      <template v-if="prependText !== ''">
        <p class="text-bold mb-0">{{ prependText }}</p>
      </template>
      <p :class="[bold ? 'text-bold' : '', 'mb-0']">{{ text }}</p>
      <template v-if="appendText !== ''">
        <p class="text-bold mb-0">{{ appendText }}</p>
      </template>
    </div>
  </button>
</template>

<script>
export default {
  name: 'Button',
  props: {
    // 가운데 Text
    text: String,
    type: {
      type: String,
      default: 'btn-primary',
      validator: (value) => {
        return ['btn-primary', 'btn-secondary', 'btn-tertiary'].indexOf(value) !== -1;
      },
    },
    size: {
      type: String,
      default: 'm',
      validator: (value) => {
        return ['big', 'xl', 'l', 'm', 's', 't'].indexOf(value) !== -1;
      },
    },
    fullWidth: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    // 맨 앞에 붙는 Bold Text
    prependText: {
      type: String,
      default: '',
    },
    // 맨 뒤에 붙는 Bold Text
    appendText: {
      type: String,
      default: '',
    },
    // 가운데 Text Bold 처리 여부
    bold: {
      type: Boolean,
      default: false,
    },
    center: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style scoped>
.full-width {
  width: 100%;
}

.text-bold {
  font-weight: bold;
}

.concat {
  display: inline-flex;
}

.center {
  margin-left: auto;
  margin-right: auto;
}
</style>

<!-- Size Style -->
<style scoped>
button {
  font-weight: 500; /* MEDIUM : 500  공통*/
  text-align: center; /* 텍스트 가로 정렬  공통*/
}
button.big {
  height: 60px;
  padding-left: 20px;
  padding-right: 20px;
  border-radius: 3px;
  text-decoration-width: 200px;
  font-size: 15px;
}

button.xl {
  min-width: 96px;
  height: 56px;
  padding-left: 16px;
  padding-right: 16px;
  border-radius: 3px;
  text-decoration-width: 64px;
  font-size: 15px;
}

button.l {
  min-width: 80px;
  height: 48px;
  padding-left: 16px;
  padding-right: 16px;
  border-radius: 3px;
  text-decoration-width: 48px;
  font-size: 15px;
}

button.m {
  min-width: 64px;
  height: 40px;
  padding-left: 16px;
  padding-right: 16px;
  border-radius: 3px;
  text-decoration-width: 38px;
  font-size: 13px;
}

button.s {
  min-width: 60px;
  height: 36px;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 3px;
  text-decoration-width: 38px;
  font-size: 13px;
}

button.t {
  min-width: 48px;
  height: 32px;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 3px;
  text-decoration-width: 38px;
  font-size: 13px;
}
</style>

<!-- Primary Button Style -->
<style scoped>
button.btn-primary {
  color: #ffffff;
  background-color: #466efd;
  border: 2px solid #6183fd;
}

/* 마우스 올림 */
button.btn-primary:hover {
  background-color: #5d80fd;
  border-color: #7592fd;
}

/* 포커싱 */
button.btn-primary:focus {
  background-color: #466efd;
  border-color: #3552be;
}

/* 마우스 다운 */
button.btn-primary:active {
  background-color: #3552be;
  border-color: #536bc7;
}

/* 비활성화 */
button.btn-primary:disabled {
  background-color: #bac9fe;
  border-color: #c4d1fe;
}
</style>

<!-- Secondary Button Style -->
<style scoped>
button.btn-secondary {
  color: #ffffff;
  background-color: #273442;
  border: 2px solid #424242;
}

/* 마우스 올림 */
button.btn-secondary:hover {
  background-color: #3e3e3e;
  border-color: #5a5a5a;
}

/* 포커싱 */
button.btn-secondary:focus {
  background-color: #3e3e3e;
  border-color: #000000;
}

/* 마우스 다운 */
button.btn-secondary:active {
  background-color: #000000;
  border-color: #262626;
}

/* 비활성화 */
button.btn-secondary:disabled {
  background-color: #c3c3c3;
  border-color: #cbcbcb;
}
</style>

<!-- Tertiary Button Style -->
<style scoped>
button.btn-tertiary {
  color: #6f6f6f;
  background-color: #ffffff;
  border: 2px solid #f2f2f2;
}

/* 마우스 올림 */
button.btn-tertiary:hover {
  background-color: #ffffff;
  border-color: #f2f2f2;
  color: #466efd;
}

/* 포커싱 */
button.btn-tertiary:focus {
  background-color: #ffffff;
  border-color: #466efd;
  color: #466efd;
}

/* 마우스 다운 */
button.btn-tertiary:active {
  background-color: #f2f2f2;
  border-color: #466efd;
  color: #466efd;
}

/* 비활성화 */
button.btn-tertiary:disabled {
  background-color: #f2f2f2;
  border-color: #f2f2f2;
  color: #dcdcdc;
}
</style>
