# -*- coding: utf-8 -*-
def toTxt(str):
    print(str)
    str = str.replace('公司+','')
    print(str)
    fo = open("company.txt", "w")
    fo.write(str)
    print(fo)
    fo.close()
    return str