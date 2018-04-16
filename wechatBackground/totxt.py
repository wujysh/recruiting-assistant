def totxt(str):
    str = str.replace('公司+','')
    fo = open("company.txt", "w")
    fo.write(str)
    fo.close()
    return value