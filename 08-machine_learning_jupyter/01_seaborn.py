import seaborn as sns
from matplotlib import pyplot as plt

if __name__ == '__main__':
    df = sns.load_dataset('penguins')
    print(df.head(10))
    print(df.describe())
    sns.pairplot(df, hue='species')
    plt.show()