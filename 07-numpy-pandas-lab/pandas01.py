import numpy as np
import pandas as pd

if __name__ == "__main__":
    dataset = {"Name": ["Braund, Mr. Owen Harris", "Allen, Mr. William Henry", "Bonnell, Miss. Elizabeth", ],
               "Age": [22, 35, 58],
               "Sex": ["male", "male", "female"],
               }
    df = pd.DataFrame(dataset, index=['Person 1', 'Person 2', 'Person 3'])
    print(df.to_string())

    workout_durations = pd.Series([45, 69, 60], index=['Person 1', 'Person 2', 'Person 3'])
    print(workout_durations)
    e1 = workout_durations['Person 1']
    print(e1, type(e1))
    s2 = workout_durations[['Person 1', 'Person 2']]
    print(s2, type(s2))

    caloriesData = {'Person 1': 420, 'Person 2': 380, 'Person 3': 390}
    calories = pd.Series(caloriesData, name='Calories', dtype=np.int32)
    print(calories)

    dates = pd.date_range("20220314", periods=3)
    print(dates)

    workouts = pd.DataFrame({
        "Name": ['Ivan', 'Hristo', 'Georgi'],
        "Duration": workout_durations.values,
        "Pulse": np.random.randint(80, 130, 3),
    }, index=dates)

    calories2 = pd.Series(calories.values, name=calories.name, index=dates)
    print(calories2)

    # workouts = pd.concat([workouts, calories2], axis=1)
    workouts = workouts.assign(Calories=calories2)


    print(workouts.tail())
