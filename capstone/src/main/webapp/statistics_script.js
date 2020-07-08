/* global google */

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(getSurveyResponses);

async function getSurveyResponses() {
  const response = await fetch('/load-data');
  const list = await response.json(); // list of entities from datastore

  const sentimentCount = {
    'Very Positive': 0,
    'Positive': 0,
    'Neutral': 0,
    'Negative': 0,
    'Very Negative': 0,
  };

  const genderCount = {
    'Male': 0,
    'Female': 0,
    'Unknown': 0,
  };
  const ageCount = {
    '18-24': 0,
    '25-34': 0,
    '35-44': 0,
    '45-54': 0,
    '55-64': 0,
    '65+': 0,
    'Unknown': 0,
  };

  const directExpCount = {
    'Prefer not to answer': 0,
    'Yes': 0,
    'No': 0,
  };

  const completionCount = {
    'Partial': 0,
    'Complete': 0,
  };

  const responseTimeAverage = {
    'Response One': 0,
    'Response Two': 0,
    'Response Three': 0,
  };

  /* Stores a respective field in each object of the list */
  const scores = [];
  const genders = [];
  const responseTimesThree = [];
  const ages = [];
  const days = [];

  for (let i = 0; i < list.length; i++) {
    scores.push(list[i].score);
    responseTimesThree.push(list[i].responseTimeThree);
    genders.push(list[i].gender);
    ages.push(list[i].ageRange);

    const date = list[i].date;
    const day = parseInt(date.substring(date.indexOf(' '), date.indexOf(',')));
    days.push(day);

    genderCount[list[i].gender] += 1;

    const sentiment = list[i].score;
    if (sentiment >= 0.5) {
      sentimentCount['Very Positive'] += 1;
    } else if (sentiment > 0.05) {
      sentimentCount['Positive'] += 1;
    } else if (sentiment >= -0.05) {
      sentimentCount['Neutral'] += 1;
    } else if (sentiment > -0.5) {
      sentimentCount['Negative'] += 1;
    } else {
      sentimentCount['Very Negative'] += 1;
    }

    ageCount[list[i].ageRange] += 1;

    directExpCount[list[i].answerOne] += 1;

    completionCount[list[i].completion] += 1;

    responseTimeAverage['Response One'] += list[i].responseTimeOne;
    responseTimeAverage['Response Two'] += list[i].responseTimeTwo;
    responseTimeAverage['Response Three'] += list[i].responseTimeThree;
  }

  responseTimeAverage['Response One'] /= list.length;
  responseTimeAverage['Response Two'] /= list.length;
  responseTimeAverage['Response Three'] /= list.length;

  loadBubbleChart(days, scores, genders, responseTimesThree);
  loadCompletionPieChart(completionCount);
  loadResponseTimeAverageBarChart(responseTimeAverage);
  loadDirectExperiencePieChart(directExpCount);
  loadPieSentimentChart(sentimentCount);
  loadAgeColumnChart(ageCount);
  loadGenderBarChart(genderCount);
  loadSentimentVResponseTimeScatterChart(responseTimesThree, scores);
  loadSentimentVDaysScatterChart(days, scores);
  loadAgePieChart(ageCount);
}

function loadBubbleChart(listA, listB, listC, listD) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'ID');
  stats.addColumn('number', 'Day in July');
  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('string', 'Gender');
  stats.addColumn('number', 'Response time (in milliseconds)');

  const listLength = listA.length;
  const res = 'Response #';

  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [res + i.toString(10), listA[i], listB[i], listC[i], listD[i]],
    ]);
  }

  const options = {
    title: 'Correlation between sentiment, response time, gender, and days',
    hAxis: {title: 'Day in July', minValue: 1, maxValue: 10},
    vAxis: {title: 'Sentiment Score'},
    bubble: {textStyle: {fontSize: 11}},
    legend: 'left'};

  const chart = new google.visualization.BubbleChart(
      document.getElementById('prediction-panel'));
  chart.draw(stats, options);
}

function loadCompletionPieChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Status');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Partial', obj['Partial']],
    ['Complete', obj['Complete']],
  ]);

  const options = {
    title: 'Completion Percentage',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('completion-pie-chart'));
  chart.draw(stats, options);
}

function loadResponseTimeAverageBarChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Response Number');
  stats.addColumn('number', 'Time (milliseconds)');
  stats.addRows([
    ['Response One', obj['Response One']],
    ['Response Two', obj['Response Two']],
    ['Response Three', obj['Response Three']],
  ]);

  const options = {
    title: 'Response Time Average Bar Chart',
    legend: 'none',
    colors: ['#800000'],
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.BarChart(
      document.getElementById('response-time-average-bar-chart'));
  chart.draw(stats, options);
}

function loadDirectExperiencePieChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Status');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Prefer not to answer', obj['Prefer not to answer']],
    ['Yes', obj['Yes']],
    ['No', obj['No']],
  ]);

  const options = {
    title: 'Direct Experience Percentage',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('direct-experience-pie-chart'));
  chart.draw(stats, options);
}

function loadPieSentimentChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Sentiment');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Very Positive', obj['Very Positive']],
    ['Positive', obj['Positive']],
    ['Neutral', obj['Neutral']],
    ['Negative', obj['Negative']],
    ['Very Negative', obj['Very Negative']],
  ]);

  const options = {
    title: 'Sentiment Percentages',
    pieHole: 0.3,
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('donut-sentiment'));
  chart.draw(stats, options);
}

function loadAgeColumnChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Age');
  stats.addColumn('number', 'People in each age group');
  stats.addRows([
    ['18-24', obj['18-24']],
    ['25-34', obj['25-34']],
    ['35-44', obj['35-44']],
    ['45-54', obj['45-54']],
    ['55-64', obj['55-64']],
    ['65+', obj['65+']],
    ['Unknown', obj['Unknown']],
  ]);

  const options = {
    title: 'Age Column Chart',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.ColumnChart(
      document.getElementById('age-column-chart'));
  chart.draw(stats, options);
}

function loadGenderBarChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Gender');
  stats.addColumn('number', 'People of each gender');
  stats.addRows([
    ['Male', obj['Male']],
    ['Female', obj['Female']],
    ['Unknown', obj['Unknown']],
  ]);

  const options = {
    title: 'Gender Bar Chart',
    legend: 'none',
    colors: ['#8C47CD'],
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.BarChart(
      document.getElementById('gender-bar-chart'));
  chart.draw(stats, options);
}


function loadSentimentVResponseTimeScatterChart(listA, listB) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('number', 'Response Time');

  const listLength = listA.length;
  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [listA[i], listB[i]],
    ]);
  }

  const options = {
    title: 'Sentiment V. Response Time',
    hAxis: {title: 'Response Time'},
    vAxis: {title: 'Sentiment Score', minValue: -1, maxValue: 1},
    legend: 'none',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.ScatterChart(
      document.getElementById('sentiment-v-response-time'));
  chart.draw(stats, options);
}

function loadSentimentVDaysScatterChart(listA, listB) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('number', 'Day in July');

  const listLength = listA.length;
  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [listA[i], listB[i]],
    ]);
  }

  const options = {
    title: 'Sentiment V. Days',
    hAxis: {title: 'Day in July'},
    vAxis: {title: 'Sentiment Score', minValue: -1, maxValue: 1},
    legend: 'none',
    colors: ['#1FC142'],
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.ScatterChart(
      document.getElementById('sentiment-v-days'));
  chart.draw(stats, options);
}

function loadAgePieChart(obj) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Age');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['18-24', obj['18-24']],
    ['25-34', obj['25-34']],
    ['35-44', obj['35-44']],
    ['45-54', obj['45-54']],
    ['55-64', obj['55-64']],
    ['65+', obj['65+']],
    ['Unknown', obj['Unknown']],
  ]);

  const options = {
    title: 'Age Percentages',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('age-pie-chart'));
  chart.draw(stats, options);
}
