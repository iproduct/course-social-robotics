# Python program to generate word vectors using Word2Vec
import regex
# importing all necessary modules
from gensim.models import Word2Vec
import gensim
import nltk
from nltk.tokenize import sent_tokenize, word_tokenize
import warnings

if __name__ == '__main__':
    nltk.download('punkt')
    warnings.filterwarnings(action='ignore')

    #  Reads ‘alice.txt’ file
    sample = open("data/alice.txt", encoding="utf8")
    s = sample.read()

    # Replaces escape character with space
    f = s.replace("\n", " ")

    data = []

    # iterate through each sentence in the file
    for i in sent_tokenize(f):
        temp = []

        # tokenize the sentence into words
        for j in word_tokenize(i):
            wl = regex.split("\W", j.lower())
            temp.extend(wl)

        data.append(temp)

    # Create CBOW model
    model1 = gensim.models.Word2Vec(data, min_count=1,
                                    vector_size=100, window=5)

    # Print results
    print("Cosine similarity between 'allice' " +
          "and 'wonderland' - CBOW : ",
          model1.wv.similarity('alice', 'wonderland'))

    print("Cosine similarity between 'allice' " +
          "and 'machines' - CBOW : ",
          model1.wv.similarity('alice', 'machines'))

    print("Cosine similarity between 'rabbit' " +
          "and 'hole' - CBOW : ",
          model1.wv.similarity('rabbit', 'hole'))

    print("Cosine similarity between 'rabbit' " +
          "and 'longitude' - CBOW : ",
          model1.wv.similarity('rabbit', 'longitude'))

    # Create Skip Gram model
    model2 = gensim.models.Word2Vec(data, min_count=1, vector_size=100,
                                    window=5, sg=1)

    # Print results
    print("Cosine similarity between 'alice' " +
          "and 'wonderland' - Skip Gram : ",
          model2.wv.similarity('alice', 'wonderland'))

    print("Cosine similarity between 'alice' " +
          "and 'machines' - Skip Gram : ",
          model2.wv.similarity('alice', 'machines'))

    print("Cosine similarity between 'rabbit' " +
          "and 'hole' - Skip Gram : ",
          model2.wv.similarity('rabbit', 'hole'))

    print("Cosine similarity between 'rabbit' " +
          "and 'longitude' - Skip Gram : ",
          model2.wv.similarity('rabbit', 'longitude'))
