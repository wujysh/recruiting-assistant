from collections import Counter


def separate_data_to_files():
    with open('traindata.txt', 'r', encoding='utf-8') as f:
        f_1 = open('features.txt', 'w', encoding='utf-8')
        f_2 = open('like_dislike.txt', 'w', encoding='utf-8')
        f_3 = open('peizhi.txt', 'w', encoding='utf-8')

        for line in f:
            label, value = line.split('\t', 1)
            if label == '优点' or label == '缺点':
                f_2.write(line)
            elif label == '动力':
                f_1.write('动力/加速' + '\t' + value)
            elif label == '油耗':
                f_1.write('能耗' + '\t' + value)
            elif label == '外观':
                f_1.write('车身外观' + '\t' + value)
            elif label == '续航':
                f_1.write('电动机/电池' + '\t' + value)
            elif label == '操控' or label == '内饰' or label == '空间':
                f_1.write(line)
            elif label == '配置':
                f_3.write(line)
        f_1.close()
        f_2.close()
        f_3.close()
    f.close()


def sort_preprocess_files():
    f_1 = open('features.txt', 'r', encoding='utf-8')
    f_2 = open('like_dislike.txt', 'r', encoding='utf-8')
    f_3 = open('peizhi.txt', 'r', encoding='utf-8')

    ff_1 = open('features_sort.txt', 'w', encoding='utf-8')
    ff_2 = open('like_dislike_sort.txt', 'w', encoding='utf-8')
    ff_3 = open('peizhi_sort.txt', 'w', encoding='utf-8')

    lines_1 = f_1.readlines()
    lines_2 = f_2.readlines()
    lines_3 = f_3.readlines()

    lines_1.sort()
    lines_2.sort()
    lines_3.sort()

    c_1 = Counter(lines_1)
    c_2 = Counter(lines_2)
    c_3 = Counter(lines_3)

    print(len(c_1.keys()))
    print(len(c_2.keys()))
    print(len(c_3.keys()))

    ff_1.writelines(c_1.keys())
    ff_2.writelines(c_2.keys())
    ff_3.writelines(c_3.keys())

    f_1.close()
    f_2.close()
    f_3.close()
    ff_1.close()
    ff_2.close()
    ff_3.close()


def delete_label_word_from_data(file_name, file_name_no_label):
    labels = ['外观', '内饰', '操控', '动力', '电耗', '空间', '能耗', '油耗']
    output_features = ['车身外观', '内饰', '操控', '动力/加速', '电动机/电池', '空间', '能耗', '安全辅助']
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


def split_peizhi():
    with open('peizhi_sort.txt', 'r', encoding='utf-8') as f:
        f_1 = open('peizhi_split.txt', 'w', encoding='utf-8')
        lines = []
        for line in f:
            label, value = line.split('\t', 1)
            split_line = value.split('。')
            lines += split_line
        print(len(lines))
        f_1.writelines(lines)


if __name__ == '__main__':
    separate_data_to_files()
    sort_preprocess_files()
    # delete_label_word_from_data('features_sort.txt','features_sort_no_label.txt')
    # split_peizhi()
