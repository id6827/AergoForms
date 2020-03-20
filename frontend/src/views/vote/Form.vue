<template>
  <v-container pa-5>
    <v-row>
      <v-col cols="8-lg-4">
        <v-card>
          <FormList />
        </v-card>
      </v-col>
    </v-row>
    <FormDetail />
  </v-container>
</template>

<script>
import FormList from './FormList';
import FormDetail from './FormDetail';

export default {
  components: {
    FormList,
    FormDetail,
  },
};
</script>
