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
            <v-row>
              <v-flex xs9>
                <FormDetailPoll />
              </v-flex>
              <v-flex xs3>
                <Button text="투표하기" type="btn-tertiary" size="big" full-width @click.native="vote" />
              </v-flex>
            </v-row>
            <v-row>
              <v-flex>
                <FormDetailChart graphTitle="CPU TOP" />
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
import FormDetailPoll from './FormDetailPoll';
import FormDetailChart from './FormDetailChart';
// import _ from 'lodash';

export default {
  components: {
    Button,
    FormDetailPoll,
    FormDetailChart,
  },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    closeDialog() {
      this.dialog = false;
    },
    getFormDetail(id) {
      console.log(id);
    },
    vote() {},
  },
  mounted() {
    // OPEN & GET DETAIL
    this.$bus.$on(this.$DEFINE_EVENT_NAME.OPEN_DOCUMENT_DETAIL, () => {
      this.dialog = true;
    });
    this.$bus.$on(this.$DEFINE_EVENT_NAME.GET_DOCUMENT_DETAIL, (id) => {
      this.getFormDetail(id);
    });
  },
  beforeDestroy() {
    this.$bus.$off(this.$DEFINE_EVENT_NAME.OPEN_DOCUMENT_DETAIL);
    this.$bus.$off(this.$DEFINE_EVENT_NAME.GET_DOCUMENT_DETAIL);
  },
};
</script>
