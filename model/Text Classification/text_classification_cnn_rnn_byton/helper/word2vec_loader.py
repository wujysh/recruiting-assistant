import multiprocessing
import re
import numpy as np
import tensorflow.contrib.keras as kr
from gensim.models import Word2Vec

# ARRAY_PIECE = 15000
CATEGORIES = {'车身外观': 0, '内饰': 1, '操控': 2, '动力/加速': 3, '电动机/电池': 4, '空间': 5, '能耗': 6, '安全辅助': 7}


def get_labels_value_length(filename):
    labels = []
    values = []
    with open(filename, 'r', encoding='utf-8') as f:
        for line in f:
            try:
                if len(line) < 200:
                    label, value = line.split('\t', 1)
                    values.append(value)
                    labels.append(CATEGORIES[label])
            except:
                print(line)
    f.close()

    # clean_lines = separate_clean_sentences(values)
    sentences = [line.split(' ') for line in values]
    max_sentence_length = max([len(sentence) for sentence in sentences])
    return [values, labels, max_sentence_length]


def w2v_process_file(clean_lines, labels, max_sentence_length, array_piece = 10000):
    padding_lines = padding_sentences(clean_lines, '<PAD>', max_sentence_length)
    embedded_lines = multi_thread_embedded_sentences(padding_lines, max_sentence_length,array_piece)
    embedded_labels = kr.utils.to_categorical(labels, num_classes=len(CATEGORIES))
    return embedded_lines, embedded_labels


def multi_thread_embedded_sentences(sentences, max_sentence_length, array_piece, embedded_size=100, window=5, min_count=5,
                                    file_to_load=None,
                                    file_to_save=None):
    if file_to_load is not None:
        w2v_model = Word2Vec.load(file_to_load)
    else:
        w2v_model = Word2Vec(sentences, size=embedded_size, window=window, min_count=min_count,
                             workers=multiprocessing.cpu_count())
        if file_to_save is not None:
            w2v_model.save(file_to_save)

    manager = multiprocessing.Manager()
    return_dict = manager.dict()
    processes = []
    number = 0
    while number <= int(len(sentences) / array_piece):
        sen_parts = sentences[number * array_piece:(number + 1) * array_piece]
        p = multiprocessing.Process(target=embedding_sentences, args=(sen_parts, w2v_model, number, return_dict))
        processes.append(p)
        p.start()
        number += 1
    for p in processes:
        p.join()

    embedded_lines = np.zeros(shape=(0, max_sentence_length, embedded_size))
    for key in return_dict.keys():
        embedded_lines = np.concatenate((embedded_lines, return_dict[key]), axis=0)
    return embedded_lines


def embedding_sentences(sentences, model, number, return_dict):
    all_vectors = []
    embedded_dim = model.vector_size
    embedded_unknown = [0 for i in range(embedded_dim)]

    for sentence in sentences:
        this_vector = []
        for word in sentence:
            if word in model.wv.vocab:
                this_vector.append(model[word])
            else:
                this_vector.append(embedded_unknown)
        all_vectors.append(this_vector)
    return_dict[number] = np.array(all_vectors)


def separate_clean_sentences(lines):
    output_lines = []
    for line in lines:
        line = ''.join([word + ' ' for word in line]).replace('\n', '').replace('，', '').replace('。', '').replace(
            '！', '')
        # line = re.sub(r"[^\u4e00-\u9fff]", " ", line)
        output_lines.append(line)
    return output_lines


def padding_sentences(lines, padding_token, max_sentence_length):
    sentences = [line.split(' ') for line in lines]
    # max_sentence_length = padding_sentence_length if padding_sentence_length is not None else max(
    #     [len(sentence) for sentence in sentences])
    for sentence in sentences:
        if len(sentence) > max_sentence_length:
            sentence = sentence[:max_sentence_length]
        else:
            sentence.extend([padding_token] * (max_sentence_length - len(sentence)))
    return sentences


if __name__=='__main__':
    w2v_model = Word2Vec(sentences, size=embedded_size, window=window, min_count=min_count,
                         workers=multiprocessing.cpu_count())