# install pyldavis
# !pip
# install
# pyldavis
# # imports
# !pip
# install
# gensim
# pyLDAvis
# ! python3 - m
# spacy
# download
# en_core_web_sm
# download dataset
# python -m spacy download en_core_web_sm

import pandas as pd
import numpy as np

import string
import spacy
import nltk

import gensim
from gensim import corpora

import matplotlib.pyplot as plt

import pyLDAvis
import pyLDAvis.gensim_models
from gensim.models import CoherenceModel

if __name__ == '__main__':
    nltk.download('wordnet')
    from nltk.corpus import wordnet as wn

    nltk.download('stopwords')
    from nltk.corpus import stopwords
    import spacy.cli

    # spacy.cli.download("en_core_web_md")
    import en_core_web_sm

    # fetch yelp review dataset and clean it
    yelp_review = pd.read_csv('data/yelp.csv')
    yelp_review.head()
    # print number of document and topics
    print(len(yelp_review))
    print("Unique Business")
    print(len(yelp_review.groupby('business_id')))
    print("Unique User")
    print(len(yelp_review.groupby('user_id')))


    # clean the document and remove punctuation
    def clean_text(text):
        delete_dict = {sp_char: '' for sp_char in string.punctuation}
        delete_dict[' '] = ' '
        table = str.maketrans(delete_dict)
        text1 = text.translate(table)
        textArr = text1.split()
        text2 = ' '.join([w for w in textArr if (not w.isdigit() and
                                                 (not w.isdigit() and len(w) > 3))])
        return text2.lower()


    yelp_review['text'] = yelp_review['text'].apply(clean_text)
    yelp_review['Num_words_text'] = yelp_review['text'].apply(lambda x: len(str(x).split()))

    print('-------Reviews By Stars --------')
    print(yelp_review['stars'].value_counts())
    print(len(yelp_review))
    print('-------------------------')
    max_review_data_sentence_length = yelp_review['Num_words_text'].max()

    # print short review (
    mask = (yelp_review['Num_words_text'] < 100) & (yelp_review['Num_words_text'] >= 20)
    df_short_reviews = yelp_review[mask]
    df_sampled = df_short_reviews.groupby('stars').apply(lambda x: x.sample(n=100)).reset_index(drop=True)

    print('No of Short reviews')
    print(len(df_short_reviews))


    # function to remove stopwords
    def remove_stopwords(text):
        textArr = text.split(' ')
        rem_text = " ".join([i for i in textArr if i not in stop_words])
        return rem_text


    # remove stopwords from the text
    stop_words = stopwords.words('english')
    df_sampled['text'] = df_sampled['text'].apply(remove_stopwords)

    # perform Lemmatization
    nlp = en_core_web_sm.load(disable=['parser', 'ner'])

    def lemmatization(texts, allowed_postags=['NOUN', 'ADJ']):
        output = []
        for sent in texts:
            doc = nlp(sent)
            output.append([token.lemma_
                           for token in doc if token.pos_ in allowed_postags])
        return output


    text_list = df_sampled['text'].tolist()
    print(text_list[2])
    tokenized_reviews = lemmatization(text_list)
    print(tokenized_reviews[2])

    # convert to document term frequency:
    dictionary = corpora.Dictionary(tokenized_reviews)
    doc_term_matrix = [dictionary.doc2bow(rev) for rev in tokenized_reviews]

    # Creating the object for LDA model using gensim library
    LDA = gensim.models.ldamodel.LdaModel

    # Build LDA model
    lda_model = LDA(corpus=doc_term_matrix, id2word=dictionary,
                    num_topics=10, random_state=100,
                    chunksize=1000, passes=50, iterations=100,
                    update_every=1,
                    alpha='auto',
                    per_word_topics=True)
    # print lda topics with respect to each word of document
    print("\nLDA topics:")
    for topic in lda_model.print_topics(num_topics=20):
        print(topic)

    # calculate perplexity and coherence
    print('\nPerplexity: ', lda_model.log_perplexity(doc_term_matrix,
                                                    total_docs=10000))

    # calculate coherence
    coherence_model_lda = CoherenceModel(model=lda_model,
                                         texts=tokenized_reviews, dictionary=dictionary,
                                         coherence='c_v')
    coherence_lda = coherence_model_lda.get_coherence()
    print('Coherence: ', coherence_lda)

    # Now, we use pyLDA vis to visualize it
    vis_data = pyLDAvis.gensim_models.prepare(lda_model, doc_term_matrix, dictionary, sort_topics=False)
    pyLDAvis.save_html(vis_data, 'out_file.html')