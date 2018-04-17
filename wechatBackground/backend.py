# -*- coding: utf-8 -*-
import httplib 
import json

def select_company(company, toUser):
	company = company.replace('公司+','')
	params = {
		'wxId': toUser,
		'company': company
	}
	header = {'Content-Type': 'application/json'}
	print(header)
	params = json.dumps(params)
	print(params)
	conn = httplib.HTTPConnection("sap.loveac.cn", 7001, timeout=10)
	#设置header数据
	conn.request("POST","/api/select",params,header)
	r1 = conn.getresponse()
	data = r1.read()
	#print(res)
	print(r1.status)
	#data = r1.read()
	print(data,type(data))
	value = json.loads(data.decode("utf-8"))
	#value = json.dumps(value,ensure_ascii=False)
	print(value,type(value))
	print(value['success'],value['success'].__class__)
	conn.close()
	return company

def ask_question(recMsg, toUser):
	if recMsg.MsgType == 'voice':
		question = recMsg.Recognition
	if recMsg.MsgType == 'text':
		question = recMsg.Content	
	params = {
    'wxId': toUser,
    'question': question
	}
	header = {'Content-Type': 'application/json'}   
	print(header)
	params = json.dumps(params)
	print(params)
	conn = httplib.HTTPConnection("sap.loveac.cn", 7001, timeout=10)
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

def apply_interview(name, phone, toUser):
	params = {
		'wxId': toUser,
		'name': name,
		'phone': phone,
	}
	header = {'Content-Type': 'application/json'}
	print(header)
	params = json.dumps(params)
	print(params)
	conn = httplib.HTTPConnection("sap.loveac.cn", 7001, timeout=10)
	#设置header数据
	conn.request("POST","/api/apply",params,header)
	r1 = conn.getresponse()
	data = r1.read()
	#print(res)
	print(r1.status)
	#data = r1.read()
	print(data,type(data))
	value = json.loads(data.decode("utf-8"))
	#value = json.dumps(value,ensure_ascii=False)
	print(value,type(value))
	print(value['success'],value['success'].__class__)
	conn.close()
	return value

def online_interview(problemId, answer, toUser):
	params = {
		'wxId': toUser,
		'problemId': problemId,
		'answer': answer,
	}
	header = {'Content-Type': 'application/json'}
	print(header)
	params = json.dumps(params)
	print(params)
	conn = httplib.HTTPConnection("sap.loveac.cn", 7001, timeout=10)
	#设置header数据
	conn.request("POST","/api/interview",params,header)
	r1 = conn.getresponse()
	data = r1.read()
	#print(res)
	print(r1.status)
	#data = r1.read()
	print(data,type(data))
	value = json.loads(data.decode("utf-8"))
	#value = json.dumps(value,ensure_ascii=False)
	print(value,type(value))
	print(value['problem'],value['problem'].__class__)
	conn.close()
	return value