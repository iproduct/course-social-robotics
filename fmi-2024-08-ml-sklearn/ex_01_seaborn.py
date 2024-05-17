import seaborn as sns
from matplotlib import pyplot as plt

if __name__ == '__main__':
    df = sns.load_dataset('penguins')
    print(df.shape)
    print(df.head(20))
    sns.pairplot(df, hue="species")
    plt.show()
