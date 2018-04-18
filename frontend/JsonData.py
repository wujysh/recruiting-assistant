import types
import urllib
import json


def praserJsonFile():
    url = "115.159.97.204/api/ask"
    data = urllib.urlopen(url).read()
    value = json.loads(data.decode())
    #print(value)    
    #print(value['data'])
    return value

#praserJsonFile()
