import numpy as np
import pandas as pd

if __name__ == "__main__":
    pd.options.display.max_rows = 9999
    workouts = pd.read_csv("data/dirtydata.csv")
    # workouts.to_json("data/workouts.json")
    # workouts2 = pd.read_json("data/workouts.json")
    print(workouts)
    print(workouts.info())

    # new_workouts = workouts.dropna()
    # print(new_workouts.to_string())
    # print(new_workouts.info())

    # missing values
    mean_calories = workouts["Calories"].mean()
    print(f'Mean calories: {mean_calories}')
    workouts["Calories"].fillna(mean_calories, inplace=True)

    # incorrect values
    workouts.loc[22, "Date"]="'2020/12/22'"
    workouts.loc[7, 'Duration'] = 45

    # incorrect date format
    workouts['Date'] = pd.to_datetime(workouts['Date'])

    # doubled values
    print(workouts.duplicated())
    workouts.drop_duplicates(inplace=True)

    print(workouts)
    print(workouts.info())
