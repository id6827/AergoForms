<template>
  <v-row justify="center">
    <v-dialog :value="dialog" width="800px" @input="closeDialog()">
      <!-- <template v-slot:activator="{ on }">
        <v-btn color="primary" dark v-on="on">Open Dialog</v-btn>
      </template> -->
      <v-card height="fill-height">
        <v-card-title>
          <div class="headline">투표명 (날짜)</div>
        </v-card-title>
        <v-card-text>
          <v-col>
            <v-col v-if="!voted">
              <v-flex>
                <v-radio-group v-model="vote" v-for="item in items" :key="item.name" :mandatory="false">
                  <v-radio :label="item.name" :value="item.value"></v-radio>
                </v-radio-group>
              </v-flex>
              <v-flex mt-5>
                <Button text="투표하기" type="btn-tertiary" size="big" full-width @click.native="doVote" />
              </v-flex>
            </v-col>
            <v-row>
              <v-flex>
                <FormDetailChart v-if="voted && chartData.length > 0" :chartData="chartData" />
              </v-flex>
            </v-row>
          </v-col>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import Button from '@/components/Button';
import FormDetailChart from './FormDetailChart';
import _ from 'lodash';

export default {
  components: {
    Button,
    FormDetailChart,
  },
  data() {
    return {
      dialog: false,
      id: '',
      voted: false,
      vote: '',
      items: [],
      chartData: [],
      address: 'test8',
    };
  },
  methods: {
    closeDialog() {
      this.dialog = false;
      this.voted = false;
      this.vote = '';
      this.items = [];
      this.chartData = [];
    },
    isDuplicated(id) {
      let params = new FormData();
      params.append('pubkey', this.address);
      params.append('voteidx', id);

      this.$axios
        .post('http://192.168.1.66:8888/UserVoteCheck', params)
        .then((response) => {
          console.log(response.data);
          if (response.status == 200) {
            if (response.data) {
              this.voted = true;
            }
          } else {
            alert('FAILED TO CHECK');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getVoteDetail(id) {
      this.$axios
        .get('http://192.168.1.66:8888/UserVoteDetail/' + id)
        .then((response) => {
          if (response.status == 200) {
            if (!_.isEmpty(response.data)) {
              console.log(response.data);
              response.data.viewItem.filter((val) => {
                this.items.push({
                  value: val.voteitemidx,
                  name: val.item,
                });

                this.chartData.push({
                  name: val.item,
                  value: val.count,
                });
              });
            }
          } else {
            alert('FAILED TO GET DETAIL');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    doVote() {
      if (this.vote === '') {
        alert('투표해주세요.');
        return;
      }

      // let params = {
      //   voteitemidx: this.vote,
      //   pubkey: 'test',
      //   voteidx: this.id,
      // };
      let params = new FormData();
      params.append('voteitemidx', this.vote);
      params.append('pubkey', this.address);
      params.append('voteidx', this.id);

      this.$axios
        .post('http://192.168.1.66:8888/UserVoteGo', params)
        .then((response) => {
          if (response.status === 200) {
            // let objIndex = this.chartData.findIndex((obj) => obj.name == this.vote);
            // this.chartData[objIndex].value += 1;
            alert('투표 완료');
            // this.items = [];
            // this.chartData = [];
            this.voted = true;
            // this.$bus.$emit(this.$DEFINE_EVENT_NAME.CLOSE_VOTE_DETAIL);
          } else {
            alert('투표 실패');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  mounted() {
    // OPEN & GET DETAIL
    this.$bus.$on(this.$DEFINE_EVENT_NAME.OPEN_VOTE_DETAIL, () => {
      this.dialog = true;
    });
    this.$bus.$on(this.$DEFINE_EVENT_NAME.GET_VOTE_DETAIL, (id) => {
      this.id = id;
      this.isDuplicated(id);
      this.getVoteDetail(id);
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.OPEN_VOTE_DETAIL);
    this.$bus.$off(this.$DEFINE_EVENT_NAME.GET_VOTE_DETAIL);
  },
};
</script>
