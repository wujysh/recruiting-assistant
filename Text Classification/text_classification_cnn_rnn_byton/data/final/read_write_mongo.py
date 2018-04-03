import os
from pymongo import MongoClient
from collections import Counter

client = MongoClient('mongodb://10.58.0.189:27017/')
db = client.final_labeled


# module_path = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))

def write_8features_data_to_mongo():
    db.eight_features.remove({})
    with open('features.txt', 'r', encoding='utf-8') as f:
        for line in f:
            try:
                line, value = line.split('\t', 1)
                if value != '':
                    db.eight_features.insert({'value': value, 'label': line})
            except:
                print(line)

    f.close()


def read_8features_data_to_mongo():
    with open('labeled_8features_data.txt', 'w', encoding='utf-8') as f:
        collections = db.eight_features
        collection = collections.find()
        for col in collection:
            f.write(col['label'] + '\t' + col['value'])
    f.close()


def delete_repeated_data():
    with open('labeled_8features_data.txt', 'r', encoding='utf-8') as f:
        lines = f.readlines()
        clean_lines = Counter(lines)
        print(len(clean_lines.keys()))
        f_1 = open('features.txt', 'w', encoding='utf-8')
        f_1.writelines(clean_lines.keys())
        f_1.close()
    f.close()


def test():
    with open('labeled_8features_data.txt', 'r', encoding='utf-8') as f:
        lines = f.readlines()
        labels = []
        for line in lines:
            label, value = line.split('\t', 1)
            labels.append(label)
        clean_labels = Counter(labels)
        print(clean_labels.keys())

        f_1 = open('test.txt', 'w', encoding='utf-8')
        lines.sort()
        f_1.writelines(lines)
    # db.eight_features.update({'label': '0'}, {'$set': {'label': '车身外观'}})
    # db.eight_features.update({'label': '动力/加速(能耗)'}, {'$set': {'label': '动力/加速'}})


# db.eight_features.update({'label': '*操控'}, {'$set': {'label': '操控'}})
#     db.eight_features.remove({'label': '？？？'})
#     db.eight_features.remove({'label': '0'})
#     db.eight_features.remove({'label': '4'})

def delete_label_name():
    labels = ['外观', '内饰', '操控', '动力', '电耗', '空间', '能耗', '油耗']
    output_features = ['车身外观', '内饰', '操控', '动力/加速', '电动机/电池', '空间', '能耗', '安全辅助']
    with open('features.txt', 'r', encoding='utf-8') as f:
        f_no_label = open('features_no_label.txt', 'w', encoding='utf-8')
        for line in f:
            label, value = line.split('\t', 1)
            for key in labels:
                if value.find(key) != -1:
                    value = value.replace(key, '')
            for key in output_features:
                if value.find(key) != -1:
                    value = value.replace(key, '')
            f_no_label.write(label + '\t' + value)


def deal_with_features():
    with open('features.txt', 'r', encoding='utf-8') as f:
        lines = f.readlines()
        a = 0
        b = 0
        c = 0
        d = 0
        e = 0
        for line in lines:
            if 0 < len(line) < 100:
                a += 1
            elif 100 < len(line) < 200:
                b += 1
            elif 200 < len(line) < 300:
                c += 1
            elif 300 < len(line) < 600:
                d += 1
            else:
                e += 1
        print(a, b, c, d, e)

def copy_data():
    with open('labeled_train.txt','r',encoding='utf-8') as f:
        f_1 = open('labeled_train00.txt','w',encoding='utf-8')
        count =0
        for line in f:
            if count<50000:
                f_1.write(line)
                count +=1
            else:
                break
        f_1.close()
    f.close()

if __name__ == '__main__':
    # write_8features_data_to_mongo()
    # read_8features_data_to_mongo()
    # delete_repeated_data()
    # test()
    # delete_label_name()
    # deal_with_features()
    copy_data()
