<template>
  <div>
    <div class="text-title">{{ this.title }}</div>
    <v-simple-table class="text-title table-style" :dense="dense" :fixed-header="fixedHeader" :height="height">
      <template v-slot:default>
        <tbody class="text-content ">
          <tr v-for="item in items" :key="item.user_id">
            <td>{{ item.user_id }}</td>
            <td>{{ item.signed_at }}</td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true,
    },
    items: {
      type: Array,
      required: true,
    },
  },
  data: () => ({
    dense: false,
    fixedHeader: false,
    height: 300,
  }),
};
</script>

<style lang="scss" scoped>
.table-style {
  border: 1px solid grey;
}

.text-title {
  font-family: 'Noto Sans', 'Noto Sans CJK KR', sans-serif;
  font-size: 16px;
  font-weight: bold;
  font-style: normal;
  font-stretch: normal;
  letter-spacing: normal;
}

.text-content {
  font-size: 12px;
  font-family: 'Noto Sans', 'Noto Sans CJK KR', sans-serif;
  font-size: 12px;
  font-weight: normal;
  font-style: normal;
  font-stretch: normal;
  letter-spacing: normal;
}
</style>
