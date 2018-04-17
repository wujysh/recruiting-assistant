# -*- coding: utf-8 -*-
def toTxt(str,toUser):
    print(str)
    str = str.replace('公司+','')
    print(str)
    filename = toUser+".txt"
    fo = open(filename, "w")
    fo.write(str)
    print(fo)
    fo.close()
    return str