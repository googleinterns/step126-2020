from flask import Flask
from google.cloud import datastore
from sklearn.tree import DecisionTreeRegressor
import numpy as np
import pandas as pd
app = Flask(__name__)


@app.route('/regression-analysis')
def main():
    df = load_dataframe()
    fit_regressor = train_model(df)
    add_predictions(fit_regressor)


def load_dataframe():
    datastore_client = datastore.Client()
    query = datastore_client.query(kind='Response')
    results = list(query.fetch())

    # Create Data Frame from entities
    direct_experiences = []  # Experience with police (yes or no)
    genders = []
    ages = []
    scores = []

    for response in results:
        direct_experiences.append(response['directExperience'])
        genders.append(response['gender'])
        ages.append(response['ageRange'])
        scores.append(response['score'])

    data_points = {'directExperience': direct_experiences, 'gender': genders,
                   'ageRange': ages, 'score': scores}

    df = pd.DataFrame(
        data_points,
        columns=[
            'directExperience',
            'gender',
            'ageRange',
            'score'])

    return df


def train_model(df):
    x = df[['directExperience', 'gender', 'ageRange']]
    y = df['score']

    y = y.values.reshape(-1, 1)

    # Encode categorical variables into numbers
    x_dum = pd.get_dummies(x, prefix_sep='_')

    x_matrix = x_dum.to_numpy()

    regressor = DecisionTreeRegressor()
    regressor.fit(x_matrix, y)

    return regressor


# This function takes values
# for each feature and returns an
# encoded array that represents those ordered values
# in a binary form (needed for regressor predict())
def getEncodedArray(direct_experience, gender, ageRange):
    categories = {
        'No': 0,
        'NoResponse': 0,
        'Yes': 0,
        'Female': 0,
        'Male': 0,
        'UnknownGender': 0,
        '18-24': 0,
        '25-34': 0,
        '35-44': 0,
        '45-54': 0,
        '55-64': 0,
        '65plus': 0,
        'UnknownAge': 0,
    }

    categories[direct_experience] = 1
    categories[gender] = 1
    categories[ageRange] = 1

    encoded_array = []

    for key, value in categories.items():
        encoded_array.append(value)

    return encoded_array


def add_predictions(fit_regressor):
    datastore_client = datastore.Client()

    # Categories for each feature
    cat_directExp = ['No', 'NoResponse', 'Yes']
    cat_genders = ['Female', 'Male', 'UnknownGender']
    cat_ageRange = ['18-24', '25-34', '35-44', '45-54',
                    '55-64', '65plus', 'UnknownAge']

    counter = 0
    # Create and store an Entity for every combination
    for direct_experience in cat_directExp:
        for gender in cat_genders:
            for ageRange in cat_ageRange:
                x_encoded = [
                    getEncodedArray(
                        direct_experience,
                        gender,
                        ageRange)]
                score = fit_regressor.predict(x_encoded)[0]

                # Creates Entity
                kind = 'Predictions'
                name = 'prediction' + str(counter)
                entity_key = datastore_client.key(kind, name)

                prediction_entity = datastore.Entity(key=entity_key)
                prediction_entity['directExperience'] = direct_experience
                prediction_entity['gender'] = gender
                prediction_entity['ageRange'] = ageRange
                prediction_entity['score'] = score

                datastore_client.put(prediction_entity)

                counter += 1
