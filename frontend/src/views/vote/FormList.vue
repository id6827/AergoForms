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
    refreshList() {
      this.$axios
        .get('/getList')
        .then((response) => {
          if (response.status == 200) {
            if (!_.isEmpty(response.data)) {
              response.data.filter((val) => {
                // this.rows.push({});
              });
            }
          } else {
            alert('FAILED TO GET LIST');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    moveToDetail(value) {
      // OPEN DETAIL DIALOG
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.OPEN_VOTE_DETAIL);
      this.$bus.$emit(this.$DEFINE_EVENT_NAME.GET_VOTE_DETAIL, value.id);
    },
  },
  mounted() {
    this.refreshList();
    this.$bus.$on(this.$DEFINE_EVENT_NAME.CLOSE_VOTE_DETAIL, () => {
      this.refreshList();
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.CLOSE_VOTE_DETAIL);
  },
};
</script>
