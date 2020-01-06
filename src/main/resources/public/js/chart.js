
var surveyJson = JSON.parse(surveyString);
var answers = surveyJson.answers;
var data = new Array();
for(var i=0; i<answers.length; i++){
	data.push({
		name: answers[i].text,
		y: answers[i].count,
		drilldown: null
	});
}

//console.log(data)
// Create the chart
Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: surveyJson.question
    },
    subtitle: {
        text: surveyJson.startTime + " ~ " + surveyJson.endTime
    },
    accessibility: {
        announceNewData: {
            enabled: true
        }
    },
    xAxis: {
        type: 'category'
    },
    yAxis: {
    		min: 0,
    		max: total,
        title: {
            text: 'Total'
        }

    },
    legend: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                format: '{point.y:.1f}'
            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> of ' + total + '.00 <br/>'
    },

    series: [
        {
            name: surveyJson.question,
            colorByPoint: true,
            data: data
        }
    ],
    drilldown: {
        series: null
    }
});

