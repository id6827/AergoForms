<template>
  <div>
    <div class="text-title">{{ this.title }}</div>
    <div class="text-content">{{ this.data }}</div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true,
    },
    data: {
      type: String,
      required: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.text-title {
  width: 200px;
  height: 32px;
  line-height: 32px;
  display: inline-block;
  font-family: 'Noto Sans', 'Noto Sans CJK KR', sans-serif;
  font-size: 16px;
  font-weight: bold;
  font-style: normal;
  font-stretch: normal;
  letter-spacing: normal;
}

.text-content {
  width: 300px;
  height: 32px;
  line-height: 32px;
  font-size: 12px;
  display: inline-block;
  font-family: 'Noto Sans', 'Noto Sans CJK KR', sans-serif;
  font-size: 12px;
  font-weight: normal;
  font-style: normal;
  font-stretch: normal;
  letter-spacing: normal;
}
</style>
