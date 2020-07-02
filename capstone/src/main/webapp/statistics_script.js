google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(getSurveyResponses);

async function getSurveyResponses(){
    const response = await fetch('/load-data');
    const list = await response.json(); //list of entities from datastore
    let scores = new Array();
    let sentimentCount = [0,0,0,0,0]; // Very positive, positive, neutral, negative, and very negative
    const refGender = {
        'M': 0,
        'F': 1,
        'U': 2
    };
    let genderCount = [0,0,0]; // Male, female, and unknown
    const refAge = {
       '18-24': 0,
       '25-34': 1,
       '35-44': 2,
       '45-54': 3,
       '55-64': 4,
       '65+': 5,
       'Unknown': 6 
    };
    let ageCount = [0,0,0,0,0,0,0]; // 18-24, 25-34, 35-44, 45-54, 55-64, 65+, or Unknown
    let genders = new Array();
    let responseTimes = new Array();
    let ages = new Array();
    let days = new Array();
    
    for (let i = 0; i < list.length; i++) {
        scores.push(list[i].score);
        responseTimes.push(list[i].responseTime);
        genders.push(list[i].gender);
        ages.push(list[i].ageRange);
        days.push(list[i].day);

        genderCount[refGender[list[i].gender]] += 1;

        const sentiment = list[i].score;
        if (sentiment >= 0.5) {
            sentimentCount[0] += 1;
        } else if (sentiment > 0.05) {
            sentimentCount[1] += 1;
        } else if (sentiment >= -0.05) {
            sentimentCount[2] += 1;
        } else if (sentiment > -0.5) {
            sentimentCount[3] += 1;
        } else { 
            sentimentCount[4] += 1;
        }

        ageCount[refAge[list[i].ageRange]] += 1;
    }

    loadBubbleChart(days, scores, genders, responseTimes);
    loadPieSentimentChart(sentimentCount);
    loadAgeColumnChart(ageCount);
    loadGenderBarChart(genderCount);
    loadSentimentVResponseTimeScatterChart(responseTimes, scores);
    loadSentimentVDaysScatterChart(days, scores);
    loadAgePieChart(ageCount);
}

function loadBubbleChart(listA, listB, listC, listD) {
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'ID');
    stats.addColumn('number', 'Days');
    stats.addColumn('number', 'Sentiment Score');
    stats.addColumn('string', 'Gender');
    stats.addColumn('number', 'Response time (in milliseconds)');

    const listLength = listA.length;
    for (let i = 0; i < listLength; i++) {
    stats.addRows([
        ['Response #' + i.toString(10), listA[i], listB[i], listC[i], listD[i]],
    ]);
    }

    let options = {
        title: 'Correlation between sentiment score, response time, gender, and days',
        hAxis: {title: 'Days'},
        vAxis: {title: 'Sentiment Score'},
        bubble: {textStyle: {fontSize: 11}},
        legend: 'left'};

    const chart = new google.visualization.BubbleChart(document.getElementById('prediction-panel'));
    chart.draw(stats, options);
}

function loadPieSentimentChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Sentiment');
    stats.addColumn('number', 'Percentage');
    stats.addRows([
        ['Very Positive', list[0]],
        ['Positive', list[1]],
        ['Neutral', list[2]],
        ['Negative', list[3]],
        ['Very Negative', list[4]],
    ]);

    let options = {
        title: 'Sentiment Percentages',
        pieHole: 0.3,
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.PieChart(document.getElementById('donut-sentiment'));
    chart.draw(stats, options);
}

function loadAgeColumnChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Age');
    stats.addColumn('number', 'People in each age group');
    stats.addRows([
        ['18-24', list[0]],
        ['25-34', list[1]],
        ['35-44', list[2]],
        ['45-54', list[3]],
        ['55-64', list[4]],
        ['65+', list[5]],
        ['Unknown', list[6]],
    ]);

    let options = {
        title: 'Age Column Chart',
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.ColumnChart(document.getElementById('age-column-chart'));
    chart.draw(stats, options);
}

function loadGenderBarChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Gender');
    stats.addColumn('number', 'People of each gender');
    stats.addRows([
        ['Male', list[0]],
        ['Female', list[1]],
        ['Unknown', list[2]],
    ]);

    let options = {
        title: 'Gender Bar Chart',
        legend: 'none',
        colors: ['#8C47CD']
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.BarChart(document.getElementById('gender-bar-chart'));
    chart.draw(stats, options);
}


function loadSentimentVResponseTimeScatterChart(listA, listB){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('number', 'Sentiment Score');
    stats.addColumn('number', 'Response Time');

    const listLength = listA.length;
    for (let i = 0; i < listLength; i++) {
    stats.addRows([
        [listA[i], listB[i]],
    ]);
    }

    let options = {
        title: 'Sentiment V. Response Time',
        hAxis: {title: 'Response Time'},
        vAxis: {title: 'Sentiment Score', minValue: -1, maxValue: 1},
        legend: 'none',
        trendlines: { 0: {} } 
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.ScatterChart(document.getElementById('sentiment-v-response-time'));
    chart.draw(stats, options);
}

function loadSentimentVDaysScatterChart(listA, listB){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('number', 'Sentiment Score');
    stats.addColumn('number', 'Days');

    const listLength = listA.length;
    for (let i = 0; i < listLength; i++) {
    stats.addRows([
        [listA[i], listB[i]],
    ]);
    }

    let options = {
        title: 'Sentiment V. Days',
        hAxis: {title: 'Days'},
        vAxis: {title: 'Sentiment Score', minValue: -1, maxValue: 1},
        legend: 'none',
        colors: ['#1FC142'],
        trendlines: { 0: {} } 
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.ScatterChart(document.getElementById('sentiment-v-days'));
    chart.draw(stats, options);
}

function loadAgePieChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Age');
    stats.addColumn('number', 'Percentage');
    stats.addRows([
        ['18-24', list[0]],
        ['25-34', list[1]],
        ['35-44', list[2]],
        ['45-54', list[3]],
        ['55-64', list[4]],
        ['65+', list[5]],
        ['Unknown', list[6]],
    ]);

    let options = {
        title: 'Age Percentages',
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.PieChart(document.getElementById('age-pie-chart'));
    chart.draw(stats, options);
}