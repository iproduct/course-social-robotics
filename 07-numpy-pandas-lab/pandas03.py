import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

if __name__ == "__main__":

    pd.options.display.max_rows = 9999
    workouts = pd.read_csv("data/workouts.csv")
    # workouts.to_json("data/workouts.json")
    # workouts2 = pd.read_json("data/workouts.json")

    # impute missing values
    median_calories = workouts["Calories"].median()
    print(f'Mean calories: {median_calories}')
    workouts["Calories"].fillna(median_calories, inplace=True)

    print(workouts)
    print(workouts.info())
    print(workouts.describe())

    # calculate correlation
    print(workouts.corr())

    # plot charts
    # workouts.plot()
    # workouts.plot(kind='scatter', x='Duration', y='Calories')
    # workouts.plot(kind='scatter', x='Duration', y='Maxpulse')

    workouts["Duration"].plot(kind='hist', bins=20)

    plt.savefig("workouts.png", format="png")
    plt.show()

