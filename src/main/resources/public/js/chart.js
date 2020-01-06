
var surveyJson = JSON.parse(surveyString);
var answers = surveyJson.answers;
var data = new Array();
for(var i=0; i<answers.length; i++){
	data.push({
		name: answers[i].text,
		y: answers[i].count / total * 100,
		count: answers[i].count,
		drilldown: answers[i].text
	});
}
console.log(data);
var series = new Array();
for(var i=0; i<answers.length; i++){
	series.push({
		name: answers[i].text,
		id: answers[i].text,
		data: [
			{
				name: "00:00",
				y: 5.6,
				count: 1
			},
			{
				name: "04:00",
				y: 3.2,
				count: 1
			},
			{
				name: "08:00",
				y: 4.4,
				count: 1
			},
			{
				name: "12:00",
				y: 6.8,
				count: 1
			},
			{
				name: "16:00",
				y: 32.0,
				count: 1
			},
			{
				name: "20:00",
				y: 25.0,
				count: 1
			},
			{
				name: "24:00",
				y: 26.0,
				count: 1
			}
        ]
	});
}
console.log(series);


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
    		max: 100,
        title: {
            text: 'Total percent'
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
                format: '{point.y:.0f}%'
            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.count:.0f}</b> of ' + total + '<br/>'
    },

    series: [
        {
            name: surveyJson.question,
            colorByPoint: true,
            data: data
        }
    ],
    drilldown: {
    		series: series
    }
});
