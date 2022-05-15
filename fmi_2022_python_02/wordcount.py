import re
en_stops = {'when', "didn't", 'wasn', 'y', 'few', 'below', 'into', 'there', 'his', 'these', 'about', 'if', 'again',
            'too', 'were', 'then', 'doing', 'haven', 'such', 'this', 'me', 'the', 'further', 'whom', 'having',
            'mightn', 'both', 'him', 'she', 'don', 'yourselves', 'all', 'do', "it's", 'i', 'what', 'needn', 'had',
            'during', 'weren', "wasn't", 'up', 'will', 'just', 'isn', 'theirs', 'own', 'll', 'hadn', "aren't",
            'while', "hasn't", 'themselves', 're', 'in', "she's", "wouldn't", 'he', "couldn't", 'their', 'more',
            "that'll", 'ain', 't', 'a', 'was', 'but', 'couldn', 'only', 'been', 'so', 'until', 'have', 'from',
            'those', 'or', 'our', 'ourselves', 'as', 'doesn', 'over', 'how', "you'd", 'where', 'itself', 'against',
            'very', "you'll", 'who', "needn't", 'by', 'no', 'each', 'its', 'at', 'we', 'any', 'ours', "mightn't",
            'through', 'does', 'here', 'didn', 'between', 'some', 'which', 'other', 'did', "don't", 'hasn', 'won',
            'not', 'should', "haven't", 'on', 'now', 'with', 'you', 'them', 'himself', 'under', "you've", 'yours',
            'they', 'shouldn', "doesn't", 'of', 'can', 'be', 'above', "weren't", 'wouldn', 'for', 's', 'and',
            "hadn't", 'herself', 'nor', "should've", 'am', 'once', 've', 'an', 'hers', 'myself', 'to', 'her', 'is',
            "shan't", 'are', 'same', 'being', 'your', 'than', 'it', 'aren', 'shan', 'd', 'm', "isn't", 'down',
            "shouldn't", "you're", 'why', 'o', 'yourself', 'has', 'my', 'ma', 'out', 'that', "mustn't", 'because',
            'after', 'before', 'most', 'off', 'mustn', "won't", 'may', 'use', 'used'}

if __name__ == '__main__':
    word_counts = {}
    with open('wiki_page.txt', 'rt') as f:
        for line in f:
            line = line.strip()
            if len(line) == 0:
                continue
            words = re.split(r'\W+', line)
            for word in words:
                word = word.lower()
                if len(word) < 2 or word in en_stops:
                    continue
                word_counts[word] = word_counts.get(word, 0) + 1

        # sort word counts
        wc_list = list(word_counts.items())
        wc_list.sort(key=lambda wc: wc[1], reverse=True)
        print(wc_list[:20])