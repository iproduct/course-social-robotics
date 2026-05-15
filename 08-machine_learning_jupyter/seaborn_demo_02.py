import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt

if __name__ == "__main__":
    df = sns.load_dataset('iris')
    print(df.shape)
    print(df.head(20))
    sns.pairplot(df, hue='species')
    plt.title('Irises')
    plt.show()