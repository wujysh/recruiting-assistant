import os

from pymongo import MongoClient

client = MongoClient('mongodb://10.58.0.189:27017/')
db = client.yiche
labels = ['动力', '内饰', '能耗', '操控', '外观', '空间']
labels_en = ['power', 'decoration', 'endurance', 'control', 'appreance', 'space']


def save_labels_from_mongo(file_name):
    with open(file_name, 'w', encoding='utf-8') as f:
        for i, label in enumerate(labels):
            f.write(label + '\t' + str(i) + '\n')


def save_data_from_autohome_feedbacks(file_name):
    if os.path.exists(file_name):
        os.remove(file_name)

    with open(file_name, 'w', encoding='utf-8') as f:
        collections = db.article
        collection = collections.find()
        for col in collection:
            for col_key in col:
                if col_key != '_id':
                    for i, label_key in enumerate(labels_en):
                        item_value = col[col_key][label_key].replace('\n', '').replace(' ', '').replace('\r', '')
                        if item_value != '':
                            f.write(labels[i] + '\t' + item_value + '\n')
        f.close()


def delete_label_word_from_data(file_name, file_name_no_label):
    with open(file_name, 'r', encoding='utf-8') as f, open(file_name_no_label, 'w', encoding='utf-8') as f_no_label:
        for line in f:
            label, value = line.split('\t', 1)
            for key in labels:
                if value.find(key) != -1:
                    value = value.replace(key, '')
            f_no_label.write(label + '\t' + value)


if __name__ == '__main__':
    # save_labels_from_mongo('180321/y_output.txt')
    save_data_from_autohome_feedbacks('180321/yiche.txt')
    # delete_label_word_from_data('180321/feedback.txt', '180321/feedback_no_label.txt')
