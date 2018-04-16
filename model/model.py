# -*- coding:utf-8 -*-
import numpy as np
import jieba.analyse
from sklearn.feature_extraction.text import CountVectorizer
from sklearn import svm
from sklearn.preprocessing import label_binarize
from sklearn.multiclass import OneVsRestClassifier
from sklearn.externals import joblib
import sys

clf = joblib.load('clf.model')
data = sys.argv[1]


def get_test_vec(test, corpus):
    data = corpus + test
    vectorizer = CountVectorizer()
    vec = vectorizer.fit_transform(data).todense()
    vec = np.array(vec)
    return vec[-1]

def deal_data(data):
    dataset = open('dataset.txt')
    X = []
    y = []
    for line in dataset.readlines():
        list = line.split('\t')
        X.append(list[1])
        y.append(list[0])
    X_ = []
    for i in X:
        sentence = jieba.cut(i, cut_all=True)
        words = ' '.join(sentence)
        X_.append(words)

    X_test = []
    sentence = jieba.cut(data, cut_all=True)
    words = ' '.join(sentence)
    X_test.append(words)

    test_vec = get_test_vec(X_test, X_)
    return test_vec

try:
    result = dict()
    test_vec = deal_data(data)
    test_y = clf.predict([test_vec])
except ValueError:
    result['code'] = 400
    print 'code:' + str(result['code'])
    sys.exit(1)
else:
    try:
        labels = ['公司简介','网址','工作时间','地址','感受','前景','面试','福利','要求']
        test_y = list(test_y[0])
        a = labels[test_y.index(1)]
        # print test_y
        # print labels[test_y.index(1)]
    except ValueError:
        result['code'] = 401
        print 'code:' + str(result['code'])
    else:
        result['code'] = 200
        result['label'] = labels[test_y.index(1)]
        print 'code:' + str(result['code'])
        print 'label:' + result['label']

