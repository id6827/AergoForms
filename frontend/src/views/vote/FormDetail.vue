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
                <FormDetailChart v-if="voted" />
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
// import _ from 'lodash';

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
      items: [
        { name: '찬성합니다', value: 0 },
        { name: '반대합니다', value: 1 },
      ],
    };
  },
  methods: {
    closeDialog() {
      this.dialog = false;
      this.vote = '';
    },
    getVoteDetail(id) {
      this.$axios
        .get('/getVoteDetail/' + this.id)
        .then((response) => {})
        .catch((err) => {});
    },
    doVote() {
      if (this.vote === '') {
        alert('투표해주세요.');
      }
      let params = {
        id: this.id,
        vote: this.vote,
      };
      console.log(params);
      // this.$axios
      //   .post('/vote', params)
      //   .then((response) => {
      //     if (response.status === 200) {
      //       alert('Signed');
      //       this.dialog = false;
      //       this.$bus.$emit(this.$DEFINE_EVENT_NAME.CLOSE_VOTE_DETAIL);
      //     } else {
      //       alert('Failed to sign');
      //     }
      //   })
      //   .catch((err) => {});
    },
  },
  mounted() {
    // OPEN & GET DETAIL
    this.$bus.$on(this.$DEFINE_EVENT_NAME.OPEN_VOTE_DETAIL, () => {
      this.dialog = true;
    });
    this.$bus.$on(this.$DEFINE_EVENT_NAME.GET_VOTE_DETAIL, (id) => {
      this.id = id;
      this.getVoteDetail(id);
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.OPEN_VOTE_DETAIL);
    this.$bus.$off(this.$DEFINE_EVENT_NAME.GET_VOTE_DETAIL);
  },
};
</script>
