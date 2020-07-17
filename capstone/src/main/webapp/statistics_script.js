/* global google */
const precinctSelected = sessionStorage.getItem('precinct');

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(getSurveyResponses);

async function getSurveyResponses() {
  const response = await fetch('/load-data?precinct=' + precinctSelected);
  const list = await response.json(); // list of entities from datastore

  // Sentiment category for the user's survey response
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

  // Numbers of users in each age group
  const ageCount = {
    '18-24': 0,
    '25-34': 0,
    '35-44': 0,
    '45-54': 0,
    '55-64': 0,
    '65+': 0,
    'Unknown': 0,
  };

  // Track how many users had direct experience with police
  const directExpCount = {
    'Prefer not to answer': 0,
    'Yes': 0,
    'No': 0,
  };

  // Number of users who partially or fully completed the survey
  const completionCount = {
    'Partial': 0,
    'Complete': 0,
  };

  // The response times in millis for each of the three survey questions
  const responseTimeAverage = {
    'Response One': 0,
    'Response Two': 0,
    'Response Three': 0,
  };

  // Stores a respective field in each object of the list
  const scores = [];
  const genders = [];
  const responseTimesThree = []; // Third question, sentiment based on this
  const ages = [];
  const days = [];

  for (let i = 0; i < list.length; i++) {
    // Store the respective fields of each object for the charts
    scores.push(list[i].score);
    responseTimesThree.push(list[i].responseTimeThree);
    genders.push(list[i].gender);
    ages.push(list[i].ageRange);

    // Extract the day from the full date string
    const date = list[i].date;
    const day = parseInt(date.substring(date.indexOf(' '), date.indexOf(',')));
    days.push(day);

    genderCount[list[i].gender] += 1;

    // Categorize the sentiment score (-1 to 1)
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

  // Compute the averages for each response time
  responseTimeAverage['Response One'] /= list.length;
  responseTimeAverage['Response Two'] /= list.length;
  responseTimeAverage['Response Three'] /= list.length;

  // Begin loading each of the charts with the necessary data
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

function loadBubbleChart(days, scores, genders, responseTimeSThree) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'ID');
  stats.addColumn('number', 'Day in July');
  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('string', 'Gender');
  stats.addColumn('number', 'Response time (in milliseconds)');

  // All parameters are the same length
  const listLength = days.length;
  const res = 'Response #';

  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [res + i.toString(10),
        days[i], scores[i], genders[i], responseTimeSThree[i]],
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

function loadCompletionPieChart(completionCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Status');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Partial', completionCount['Partial']],
    ['Complete', completionCount['Complete']],
  ]);

  const options = {
    title: 'Completion Percentage',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('completion-pie-chart'));
  chart.draw(stats, options);
}

function loadResponseTimeAverageBarChart(averages) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Response Number');
  stats.addColumn('number', 'Time (milliseconds)');
  stats.addRows([
    ['Response One', averages['Response One']],
    ['Response Two', averages['Response Two']],
    ['Response Three', averages['Response Three']],
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

function loadDirectExperiencePieChart(directExpCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Status');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Prefer not to answer', directExpCount['Prefer not to answer']],
    ['Yes', directExpCount['Yes']],
    ['No', directExpCount['No']],
  ]);

  const options = {
    title: 'Direct Experience Percentage',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('direct-experience-pie-chart'));
  chart.draw(stats, options);
}

function loadPieSentimentChart(sentimentCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Sentiment');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Very Positive', sentimentCount['Very Positive']],
    ['Positive', sentimentCount['Positive']],
    ['Neutral', sentimentCount['Neutral']],
    ['Negative', sentimentCount['Negative']],
    ['Very Negative', sentimentCount['Very Negative']],
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

function loadAgeColumnChart(ageCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Age');
  stats.addColumn('number', 'People in each age group');
  stats.addRows([
    ['18-24', ageCount['18-24']],
    ['25-34', ageCount['25-34']],
    ['35-44', ageCount['35-44']],
    ['45-54', ageCount['45-54']],
    ['55-64', ageCount['55-64']],
    ['65+', ageCount['65+']],
    ['Unknown', ageCount['Unknown']],
  ]);

  const options = {
    title: 'Age Column Chart',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.ColumnChart(
      document.getElementById('age-column-chart'));
  chart.draw(stats, options);
}

function loadGenderBarChart(genderCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Gender');
  stats.addColumn('number', 'People of each gender');
  stats.addRows([
    ['Male', genderCount['Male']],
    ['Female', genderCount['Female']],
    ['Unknown', genderCount['Unknown']],
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


function loadSentimentVResponseTimeScatterChart(responseTimes, scores) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('number', 'Response Time');

  const listLength = responseTimes.length;
  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [responseTimes[i], scores[i]],
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

function loadSentimentVDaysScatterChart(days, scores) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('number', 'Sentiment Score');
  stats.addColumn('number', 'Day in July');

  const listLength = days.length;
  for (let i = 0; i < listLength; i++) {
    stats.addRows([
      [days[i], scores[i]],
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

function loadAgePieChart(ageCount) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Age');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['18-24', ageCount['18-24']],
    ['25-34', ageCount['25-34']],
    ['35-44', ageCount['35-44']],
    ['45-54', ageCount['45-54']],
    ['55-64', ageCount['55-64']],
    ['65+', ageCount['65+']],
    ['Unknown', ageCount['Unknown']],
  ]);

  const options = {
    title: 'Age Percentages',
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('age-pie-chart'));
  chart.draw(stats, options);
}
