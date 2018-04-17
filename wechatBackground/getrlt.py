# -*- coding: utf-8 -*-
import httplib 
import json  

def getRlt(recMsg,toUser):
	try:
		filename = toUser+".txt"
		f = open(filename, 'r')
		company = f.read()
	finally:
		if f:
			f.close()
	if recMsg.MsgType == 'voice':
		question = recMsg.Recognition
	if recMsg.MsgType == 'text':
		question = recMsg.Content	
	params = {
    'company': company,
    'question': question
	}
	header = {'Content-Type': 'application/json'}   
	print(header)
	params = json.dumps(params)
	print(params)
	conn = httplib.HTTPConnection("115.159.97.204",8888, timeout=10)
	#设置header数据   
	conn.request("POST","/api/ask",params,header)  
	r1 = conn.getresponse()  
	data = r1.read()
	#print(res) 	  
	print(r1.status)  	  
	#data = r1.read()
	print(data,type(data)) 
	value = json.loads(data.decode("utf-8"))
	#value = json.dumps(value,ensure_ascii=False)
	print(value,type(value)) 	
	print(value['answer'],value['answer'].__class__)
	conn.close()
	return value