import os
import random

# def _read_file(filename):
#     """读取一个文件并转换为一行"""
#     with open(filename, 'r', encoding='utf-8') as f:
#         return f.read().replace('\n', '').replace('\t', '').replace('\u3000', '')

def save_file(dir_path):
    """
    文件内容格式:  类别\t内容
    """
    # module_path = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)) + dir_name
    if not os.path.exists(dir_path + 'features.txt'):
        print('data file is not exist')
        exit(0)

    f_data = open(dir_path + 'features.txt', 'r', encoding='utf-8')
    f_train = open(dir_path + 'labeled_train.txt', 'w', encoding='utf-8')
    f_test = open(dir_path + 'labeled_test.txt', 'w', encoding='utf-8')
    f_val = open(dir_path + 'labeled_val.txt', 'w', encoding='utf-8')

    # for category in os.listdir(dirname):   # 分类目录
    #     cat_dir = os.path.join(dirname, category)
    #     if not os.path.isdir(cat_dir):
    #         continue
    #     files = os.listdir(cat_dir)
    #     count = 0
    #     for cur_file in files:
    #         filename = os.path.join(cat_dir, cur_file)
    #         content = _read_file(filename)
    #         if count < 1320:
    #             f_train.write(category + '\t' + content + '\n')
    #         elif count < 1760:
    #             f_test.write(category + '\t' + content + '\n')
    #         else:
    #             f_val.write(category + '\t' + content + '\n')
    #         count += 1
    #
    #     print('Finished:', category)

    lines = f_data.readlines()
    random.shuffle(lines)
    count = 0
    sec = int(len(lines) / 10)
    for line in lines:
        if count < sec * 8:
            f_train.write(line)
        elif count < sec * 9:
            f_val.write(line)
        else:
            f_test.write(line)
        count += 1

    f_data.close()
    f_train.close()
    f_test.close()
    f_val.close()


if __name__ == '__main__':
    dir_path = 'final/'

    save_file(dir_path)

    f_data = open(dir_path + 'features.txt', 'r', encoding='utf-8')
    f_train = open(dir_path + 'labeled_train.txt', 'r', encoding='utf-8')
    f_test = open(dir_path + 'labeled_test.txt', 'r', encoding='utf-8')
    f_val = open(dir_path + 'labeled_val.txt', 'r', encoding='utf-8')

    print('data size: ', len(f_data.readlines()))
    print('train size: ', len(f_train.readlines()))
    print('val size: ', len(f_val.readlines()))
    print('test size: ', len(f_test.readlines()))
