<template>
  <v-card>
    <v-card-title>
      <div class="headline">투표 목록</div>
      <v-spacer></v-spacer>
      <v-text-field v-model="search" append-icon="mdi-magnify" label="검색" single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="desserts" :search="search" @click:row="moveToDetail"></v-data-table>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      search: '',
      headers: [
        {
          text: '투표명',
          align: 'start',
          sortable: false,
          value: 'name',
        },
        { text: '개설일', value: 'date' },
      ],
      desserts: [
        {
          name: 'Frozen Yogurt',
          date: '2020/01/01',
        },
        {
          name: 'Ice cream sandwich',
          date: '2020/01/01',
        },
      ],
    };
  },
  methods: {
    moveToDetail(value) {
      // OPEN DETAIL DIALOG
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.OPEN_DOCUMENT_DETAIL);
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.GET_DOCUMENT_DETAIL, value.id);
    },
  },
};
</script>
