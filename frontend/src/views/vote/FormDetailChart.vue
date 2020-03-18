<template>
  <div class="small">
    <div class="headline">{{ graphTitle }}</div>
    <bar-chart :chart-data="datacollection" :options="myoption"></bar-chart>
  </div>
</template>

<script>
import Vue from 'vue';
import BarChart from './chart/BarChart.js';

export default Vue.extend({
  components: {
    BarChart,
  },
  props: {
    graphTitle: String,
  },
  data() {
    return {
      datacollection: null,
      myoption: null,
    };
  },
  mounted() {
    this.fillData();
  },
  methods: {
    fillData() {
      // Create random dummy data
      const dummy = [
        { name: 'Host01', value: this.getRandomInt() },
        { name: 'Host02', value: this.getRandomInt() },
        { name: 'Host03', value: this.getRandomInt() },
        { name: 'Host04', value: this.getRandomInt() },
        { name: 'Host05', value: this.getRandomInt() },
      ];

      this.datacollection = {
        labels: dummy.map((a) => a.name),
        datasets: [
          {
            // label: 'Data One',
            // backgroundColor: '#f87979',
            data: dummy.map((a) => a.value),
            backgroundColor: ['#db4e4e', '#e26e6e', '#e26e6e', '#f1b3b3', '#f1b3b3'],
          },
        ],
      };

      this.myoption = {
        showAllTooltips: false,
        legend: {
          display: false,
        },
        animation: {
          duration: 1000,
          onComplete() {
            const chartInstance = this.chart;
            const ctx = chartInstance.ctx;
            ctx.font = Chart.helpers.fontString(
              Chart.defaults.global.defaultFontSize,
              Chart.defaults.global.defaultFontStyle,
              Chart.defaults.global.defaultFontFamily,
            );
            ctx.textAlign = 'center';
            ctx.textBaseline = 'bottom';

            this.data.datasets.forEach((dataset, i) => {
              const meta = chartInstance.controller.getDatasetMeta(i);
              meta.data.forEach((bar, index) => {
                const data = dataset.data[index];
                ctx.fillText(data, bar._model.x, bar._model.y - 5);
              });
            });
          },
        },
        scales: {
          xAxes: [
            {
              display: true,
              ticks: {
                suggestedMin: 0,
                suggestedMax: 100,
              },
            },
          ],
        },
      };
    },
    getRandomInt() {
      return Math.floor(Math.random() * (100 - 5 + 1)) + 5;
    },
  },
});
</script>

<style>
.small {
  max-width: 600px;
  margin: auto;
}
</style>
