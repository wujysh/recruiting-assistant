# -*- coding: utf-8 -*-
import httplib 
import json  

def getRlt(recMsg):
	header = {"company":recMsg.Content,"question":recMsg.Content}   
	print(header)
	conn = httplib.HTTPConnection("115.159.97.204",80)
	#设置header数据   
	conn.request("GET","/api/ask","",header)  
	r1 = conn.getresponse()  	  
	print(r1.status, r1.reason)  	  
	data = eval(r1.read().decode())  	  
	data = json.dumps(data,indent=4,separators=(',', ': '),ensure_ascii=False)  
	print(data) 