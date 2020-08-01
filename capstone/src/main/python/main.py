import numpy as np
import pandas as pd
from sklearn.tree import DecisionTreeRegressor
from google.cloud import datastore
from flask import Flask
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
    size = len(results)

    # Create Data Frame from entities
    genders = []
    ages = []
    scores = []

    for i in range(0, size):
        response = results[i]

        genders.append(response['gender'])
        ages.append(response['ageRange'])
        scores.append(response['score'])

    data_points = {'gender': genders,
                   'ageRange': ages, 'score': scores}

    df = pd.DataFrame(data_points, columns=['gender', 'ageRange', 'score'])

    return df
 

def train_model(df):
    x = df[['gender', 'ageRange']]
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
# encoded array that represents those values
# in a binary form (needed for regressor predict())
def getEncodedArray(gender, ageRange):
    categories = {
       'Female': 0,
       'Male': 0,
       'UnknownGender': 0,
       '18-24': 0,
       '25-34': 0,
       '35-44': 0,
       '45-54': 0,
       '55-64': 0,
       '65+': 0,
       'UknownAge': 0,
    }

    categories[gender] = 1
    categories[ageRange] = 1
    
    encoded_array = []

    for key, value in categories.items():
        encoded_array.append(value)
    
    return encoded_array


def add_predictions(fit_regressor):
    datastore_client = datastore.Client()

    # Categories for each feature
    cat_genders = ['Female', 'Male', 'UnknownGender']
    cat_ageRange = ['18-24', '25-34', '35-44', '45-54',
                    '55-64', '65+', 'UknownAge']
    
    counter = 0
    # Create and store an Entity for every combination
    for gender in cat_genders:
        for ageRange in cat_ageRange:
            x_encoded = [getEncodedArray(gender, ageRange)]
            score = fit_regressor.predict(x_encoded)[0]

            # Creates Entity
            kind = 'Predictions'
            name = 'prediction' + str(counter)
            entity_key = datastore_client.key(kind, name)

            prediction_entity = datastore.Entity(key=entity_key)
            prediction_entity['gender'] = gender
            prediction_entity['ageRange'] = ageRange
            prediction_entity['score'] = score
            
            datastore_client.put(prediction_entity)

            counter += 1
    