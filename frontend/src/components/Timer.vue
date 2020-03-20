<template>
  <div class="timer">{{ time | prettify }}</div>
</template>

<script>
export default {
  props: ['time'],
  filters: {
    prettify: function(value) {
      let data = value.split(':');
      let minutes = data[0];
      let secondes = data[1];
      if (minutes < 10) {
        minutes = '0' + minutes;
      }
      if (secondes < 10) {
        secondes = '0' + secondes;
      }
      return minutes + ':' + secondes;
    },
  },
};
</script>

<style lang="scss" scoped>
.timer {
  font-size: 2em;
  margin: 20px;
}
</style>
