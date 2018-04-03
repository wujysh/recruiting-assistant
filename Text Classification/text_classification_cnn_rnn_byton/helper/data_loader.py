# coding: utf-8

import sys
import re
import multiprocessing
from collections import Counter

import numpy as np
import tensorflow.contrib.keras as kr
from gensim.models import Word2Vec
from gensim.models.word2vec import LineSentence

if sys.version_info[0] > 2:
    is_py3 = True
else:
    reload(sys)
    sys.setdefaultencoding("utf-8")
    is_py3 = False


def native_word(word, encoding='utf-8'):
    """如果在python2下面使用python3训练的模型，可考虑调用此函数转化一下字符编码"""
    if not is_py3:
        return word.encode(encoding)
    else:
        return word


def native_content(content):
    content = content.replace('\n', '')
    if not is_py3:
        return content.decode('utf-8')
    else:
        return content


def open_file(filename, mode='r'):
    """
    常用文件操作，可在python2和python3间切换.
    mode: 'r' or 'w' for read or write
    """
    if is_py3:
        return open(filename, mode, encoding='utf-8', errors='ignore')
    else:
        return open(filename, mode)


def read_file(filename):
    """读取文件数据"""
    contents, labels = [], []
    with open_file(filename) as f:
        for line in f:
            line_parts = line.strip().split('\t', 1)
            try:
                contents.append(list(native_content(line_parts[-1])))
                labels.append(native_content(line_parts[0]))
            except:
                pass
    return contents, labels


def read_file_pb(filename):
    """读取文件数据"""
    contents = []
    with open_file(filename) as f:
        for line in f:
            try:
                content = line.strip()
                if content:
                    contents.append(list(native_content(content)))
            except:
                pass
    return contents


def build_vocab(train_dir, vocab_dir, vocab_size=5000):
    """根据训练集构建词汇表，存储"""
    data_train, _ = read_file(train_dir)

    all_data = []
    for content in data_train:
        all_data.extend(content)

    counter = Counter(all_data)
    count_pairs = counter.most_common(vocab_size - 1)
    words, _ = list(zip(*count_pairs))
    # 添加一个 <PAD> 来将所有文本pad为同一长度
    words = ['<PAD>'] + list(words)
    open_file(vocab_dir, mode='w').write('\n'.join(words) + '\n')


def read_vocab(vocab_dir):
    """读取词汇表"""
    # words = open_file(vocab_dir).read().strip().split('\n')
    with open_file(vocab_dir) as fp:
        # 如果是py2 则每个值都转化为unicode
        words = [native_content(_.strip()) for _ in fp.readlines()]
    word_to_id = dict(zip(words, range(len(words))))
    return words, word_to_id


def read_category():
    """读取分类目录，固定"""
    categories = ['车身外观', '内饰', '操控', '动力/加速', '电动机/电池', '空间', '能耗', '安全辅助']

    categories = [native_content(x) for x in categories]

    cat_to_id = dict(zip(categories, range(len(categories))))

    return categories, cat_to_id


def to_words(content, words):
    """将id表示的内容转换为文字"""
    return ''.join(words[x] for x in content)


def onehot_process_file(filename, word_to_id, cat_to_id, max_length=600):
    """将文件转换为id表示"""
    contents, labels = read_file(filename)

    data_id, label_id = [], []
    for i in range(len(contents)):
        data_id.append([word_to_id[x] for x in contents[i] if x in word_to_id])
        label_id.append(cat_to_id[labels[i]])

    # 使用keras提供的pad_sequences来将文本pad为固定长度
    x_pad = kr.preprocessing.sequence.pad_sequences(data_id, max_length)
    y_pad = kr.utils.to_categorical(label_id, num_classes=len(cat_to_id))  # 将标签转换为one-hot表示

    return x_pad, y_pad


# def onehot_label_process_file(labels, cat_to_id):
#     label_id = []
#     for i in range(len(labels)):
#         label_id.append(cat_to_id[labels[i]])
#
#     y_pad = kr.utils.to_categorical(label_id, num_classes=len(cat_to_id))  # 将标签转换为one-hot表示
#
#     return y_pad
#

def process_file_pb(filename, word_to_id, max_length=600):
    """将文件转换为id表示"""
    contents = read_file_pb(filename)

    data_id, label_id = [], []
    for i in range(len(contents)):
        data_id.append([word_to_id[x] for x in contents[i] if x in word_to_id])
        # label_id.append(cat_to_id[labels[i]])

    # 使用keras提供的pad_sequences来将文本pad为固定长度
    x_pad = kr.preprocessing.sequence.pad_sequences(data_id, max_length)
    # y_pad = kr.utils.to_categorical(label_id, num_classes=len(cat_to_id))  # 将标签转换为one-hot表示

    return x_pad


# def word2vec_process_file(contents, labels, cat_to_id):
#     # labels, contents = read_and_clean_zh_file(filename)
#     x_pad, max_sentence_length = padding_sentences(contents, '<PADDING>')
#     x0 = word2vec_embedding_sentences(x_pad)
#     x = np.array(x0)
#     y = onehot_label_process_file(labels, cat_to_id)
#     return [x, y]
#
#
# def read_and_clean_zh_file(input_file, output_cleaned_file=None):
#     labels = []
#     lines = []
#     with open(input_file, 'r', encoding='utf-8') as f:
#         for line in f:
#             label, content = line.split('\t', 1)
#             labels.append(label)
#             lines.append(content)
#
#     lines = [clean_str(seperate_line(line)) for line in lines]
#     if output_cleaned_file is not None:
#         with open(output_cleaned_file, 'w') as f:
#             for line in lines:
#                 f.write((line + '\n').encode('utf-8'))
#     return lines, labels
#
#
# def seperate_line(line):
#     return ''.join([word + ' ' for word in line])
#
#
# def clean_str(string):
#     string = re.sub(r"[^\u4e00-\u9fff]", " ", string)
#     # string = re.sub(r"[^A-Za-z0-9(),!?\'\`]", " ", string)
#     # string = re.sub(r"\'s", " \'s", string)
#     # string = re.sub(r"\'ve", " \'ve", string)
#     # string = re.sub(r"n\'t", " n\'t", string)
#     # string = re.sub(r"\'re", " \'re", string)
#     # string = re.sub(r"\'d", " \'d", string)
#     # string = re.sub(r"\'ll", " \'ll", string)
#     # string = re.sub(r",", " , ", string)
#     # string = re.sub(r"!", " ! ", string)
#     # string = re.sub(r"\(", " \( ", string)
#     # string = re.sub(r"\)", " \) ", string)
#     # string = re.sub(r"\?", " \? ", string)
#     # string = re.sub(r"\s{2,}", " ", string)
#     # return string.strip().lower()
#     return string.strip()
#
#
# def padding_sentences(input_sentences, padding_token, padding_sentence_length=None):
#     sentences = [sentence.split(' ') for sentence in input_sentences]
#     # max_sentence_length = padding_sentence_length if padding_sentence_length is not None else max(
#     #     [len(sentence) for sentence in sentences])
#     max_sentence_length = 600
#     for sentence in sentences:
#         if len(sentence) > max_sentence_length:
#             sentence = sentence[:max_sentence_length]
#         else:
#             sentence.extend([padding_token] * (max_sentence_length - len(sentence)))
#     return (sentences, max_sentence_length)
#
#
# def word2vec_embedding_sentences(sentences, embedding_size=64, window=5, min_count=5, file_to_load=None,
#                                  file_to_save=None):
#     if file_to_load is not None:
#         w2vModel = Word2Vec.load(file_to_load)
#     else:
#         w2vModel = Word2Vec(sentences, size=embedding_size, window=window, min_count=min_count,
#                             workers=multiprocessing.cpu_count())
#         if file_to_save is not None:
#             w2vModel.save(file_to_save)
#     all_vectors = []
#     embeddingDim = w2vModel.vector_size
#     embeddingUnknown = [0 for i in range(embeddingDim)]
#     for sentence in sentences:
#         this_vector = []
#         for word in sentence:
#             if word in w2vModel.wv.vocab:
#                 this_vector.append(w2vModel[word])
#             else:
#                 this_vector.append(embeddingUnknown)
#         all_vectors.append(this_vector)
#     return all_vectors
#

def batch_iter(x, y, batch_size=64):
    """生成批次数据"""
    data_len = len(x)
    num_batch = int((data_len - 1) / batch_size) + 1

    indices = np.random.permutation(np.arange(data_len))
    x_shuffle = x[indices]
    y_shuffle = y[indices]
    # np.random.shuffle(x)
    # np.random.shuffle(y)

    for i in range(num_batch):
        start_id = i * batch_size
        end_id = min((i + 1) * batch_size, data_len)
        yield x[start_id:end_id], y[start_id:end_id]
