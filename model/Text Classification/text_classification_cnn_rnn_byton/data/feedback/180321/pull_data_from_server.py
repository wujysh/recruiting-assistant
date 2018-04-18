from pymongo import MongoClient

client = MongoClient('mongodb://10.58.0.189:27017/')
db = client.autohome
labels = ['外观', '内饰', '操控', '动力', '电耗', '空间', '能耗', '油耗']
output_features = ['车身外观', '内饰', '操控', '动力/加速', '电动机/电池', '空间', '能耗', '安全辅助']


def save_labels_from_mongo(file_name):
    # y_labels = {}
    # collection = collections.find()
    # for col in collection:
    #     for key in col['items'][0].keys():
    #         if key not in y_labels and len(key) < 10:
    #             y_labels[key] = len(y_labels)
    #
    # labels = sorted(y_labels.items(), key=lambda item: item[1])
    # print('y_output: ', labels)
    #
    # f_y = open(file_name, 'w', encoding='utf-8')
    # for label in labels:
    #     f_y.write(label[0])
    #     f_y.write('\t')
    #     f_y.write(str(label[1]))
    #     f_y.write('\n')
    #
    # f_y.close()

    # with open(file_name, 'w', encoding='utf-8') as f:
    #     for i, label in enumerate(labels):
    #         if label == '油耗':
    #             pass
    #         elif label == '动力':
    #             f.write('动力/加速' + '\t' + str(i) + '\n')
    #         elif label == '外观':
    #             f.write('车身外观' + '\t' + str(i) + '\n')
    #         elif label == '舒适性':
    #             f.write('综合' + '\t' + str(i) + '\n')
    #         elif label == '电耗':
    #             f.write('电动机/电池' + '\t' + str(i) + '\n')
    #         else:
    #             f.write(label + '\t' + str(i) + '\n')
    #     f.write('安全辅助' + '\t' + str(len(labels)) + '\n')

    with open(file_name, 'w', encoding='utf-8') as f:
        for i, feature in enumerate(output_features):
            f.write(feature + '\t' + str(i) + '\n')


def save_data_from_autohome_feedbacks(file_name):
    with open(file_name, 'w', encoding='utf-8') as f:
        collections = db.feedbacks
        collection = collections.find()
        for col in collection:
            for i in range(0, len(col['items'])):
                for key in labels:
                    if key in col['items'][i].keys():
                        col_value = col['items'][i][key].replace('\n', '').replace(' ', '').replace('\r', '')
                        if key == '外观':
                            f.write('车身外观' + '\t' + col_value + '\n')
                        elif key == '动力':
                            f.write('动力/加速' + '\t' + col_value + '\n')
                        elif key == '电耗':
                            f.write('电动机/电池' + '\t' + col_value + '\n')
                        elif key == '油耗':
                            f.write('能耗' + '\t' + col_value + '\n')
                        # elif key == '舒适性':
                        #     f.write('综合' + '\t' + col_value + '\n')
                        else:
                            f.write(key + '\t' + col_value + '\n')

        f.close()


def save_data_from_autohome_new_feedbacks(file_name):
    with open(file_name, 'a', encoding='utf-8') as f:
        collections = db.new_feedbacks
        collection = collections.find()
        for col in collection:
            for i in range(0, len(col['items'])):
                for key in labels:
                    if key in col['items'][i].keys():
                        col_value = col['items'][i][key].replace('\n', '').replace(' ', '').replace('\r', '')
                        if key == '外观':
                            f.write('车身外观' + '\t' + col_value + '\n')
                        elif key == '动力':
                            f.write('动力/加速' + '\t' + col_value + '\n')
                        elif key == '电耗':
                            f.write('电动机/电池' + '\t' + col_value + '\n')
                        elif key == '油耗':
                            f.write('能耗' + '\t' + col_value + '\n')
                        # elif key == '舒适性':
                        #     f.write('综合' + '\t' + col_value + '\n')
                        else:
                            f.write(key + '\t' + col_value + '\n')
        f.close()


def delete_label_word_from_data(file_name, file_name_no_label):
    with open(file_name, 'r', encoding='utf-8') as f, open(file_name_no_label, 'w', encoding='utf-8') as f_no_label:
        for line in f:
            label, value = line.split('\t', 1)
            for key in labels:
                if value.find(key) != -1:
                    value = value.replace(key, '')
            for key in output_features:
                if value.find(key) != -1:
                    value = value.replace(key, '')
            f_no_label.write(label + '\t' + value)


if __name__ == '__main__':
    save_labels_from_mongo('y_output.txt')
    save_data_from_autohome_feedbacks('feedback.txt')
    save_data_from_autohome_new_feedbacks('feedback.txt')
    # delete_label_word_from_data('feedback.txt', 'feedback_no_label.txt')
