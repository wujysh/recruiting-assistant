from random import shuffle

with open('train_data_cut.txt','r', encoding='utf-8') as f:
    lines = f.readlines()
    clean_lines = []
    for line in lines:
        if line != '\n':
            clean_lines.append(line)
    shuffle(clean_lines)

    f_1 = open('labeled_train.txt','w',encoding='utf-8')
    f_2 = open('labeled_val.txt','w',encoding='utf-8')
    f_3 = open('labeled_test.txt','w',encoding='utf-8')

    f_1.writelines(clean_lines[:90000])
    f_2.writelines(clean_lines[90001:100000])
    f_3.writelines(clean_lines[100000:])