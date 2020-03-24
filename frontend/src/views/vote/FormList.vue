<template>
  <v-card>
    <v-card-title>
      <div class="headline">투표 목록</div>
      <v-spacer></v-spacer>
      <v-text-field v-model="search" append-icon="mdi-magnify" label="검색" single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="list" :search="search" @click:row="moveToDetail"></v-data-table>
  </v-card>
</template>

<script>
import _ from 'lodash';

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
        { text: '상태', value: 'status' },
        { text: '개설일', value: 'date' },
      ],
      list: [],
    };
  },
  methods: {
    beautifulDate(timestamp) {
      let currentDate = new Date(timestamp * 1000);

      let date = currentDate.getDate();
      let month = currentDate.getMonth(); //Be careful! January is 0 not 1
      let year = currentDate.getFullYear();

      return year + '년 ' + (month + 1) + '월 ' + date + '일 ';
    },
    refreshList() {
      this.list = [];
      this.$axios
        .get('http://192.168.1.66:8888/UserVoteList')
        .then((response) => {
          console.log(response);
          if (response.status == 200) {
            if (!_.isEmpty(response.data)) {
              response.data.list.filter((val) => {
                this.list.push({
                  id: val.voteidx,
                  name: val.votename,
                  status: val.status,
                  date: this.beautifulDate(val.regdate),
                });
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
