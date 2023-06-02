import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

if __name__ == '__main__':
    s = pd.Series([1, 3, 5, np.nan, 6, 8])
    print(s)

    dates = pd.date_range("20130101", periods=6)
    print(dates)

    df = pd.DataFrame(np.random.randn(6, 4), index=dates, columns=list("ABCD"))
    print(df)

    df = pd.DataFrame(
        {
            "Name": [
                "Braund, Mr. Owen Harris",
                "Allen, Mr. William Henry",
                "Bonnell, Miss. Elizabeth",
            ],
            "Age": [22, 35, 58],
            "Sex": ["male", "male", "female"],
        }
    )
    print(df)
    print(df["Age"])
    ages = pd.Series([22, 35, 58], name="Age")
    print(ages)

    air_quality = pd.read_csv("data/air_quality_no2.csv", index_col=0, parse_dates=True)
    print(air_quality.head())
    # air_quality.plot()

    x = np.arange(0, 5, 0.1)
    y = np.sin(x)
    plt.plot(x, y)
    plt.show()
