# -*- coding: utf-8 -*-
import web
import reply
import receive
import JsonData
import xml.dom.minidom
import backend

class Handle(object):
    def GET(self):
        try:
            data = web.input()
            if len(data) == 0:
                return "hello, this is handle view"
            signature = data.signature
            timestamp = data.timestamp
            nonce = data.nonce
            echostr = data.echostr
            token = "hello2016"

            list = [token, timestamp, nonce]
            list.sort()
            sha1 = hashlib.sha1()
            map(sha1.update, list)
            hashcode = sha1.hexdigest()
            #print("handle/GET func: hashcode, signature: ", hashcode, signature)
            if hashcode == signature:
                return echostr
            else:
                return ""
        except Exception as Argument:
            return Argument
    def POST(self):
        try:
            webData = web.data()
            print(webData)
            recMsg = receive.parse_xml(webData)
            print(recMsg)
            #print(recMsg.MsgType)
            if isinstance(recMsg, receive.Msg):
                toUser = recMsg.FromUserName
                fromUser = recMsg.ToUserName
                print(recMsg)
                if recMsg.MsgType == 'text':
                    str = recMsg.Content
                    if str[0:1] == "1":
                        company = backend.select_company("SAP",toUser)
                        content = "欢迎使用"+company+"公司智能招聘系统！如有意向请输入“我要投递”，可以直接答题笔试哦！"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    if str[0:1] == "2":
                        company = backend.select_company("微软",toUser)
                        content = "欢迎使用"+company+"公司智能招聘系统！如有意向请输入“我要投递”，可以直接答题笔试哦！"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    if str[0:1] == "3":
                        company = backend.select_company("腾讯",toUser)
                        content = "欢迎使用"+company+"公司智能招聘系统！如有意向请输入“我要投递”，可以直接答题笔试哦！"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    if str == "我要投递":
                        content = "请输入您的11位手机号码，如有后续面试环节会及时通知到您！"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    if str[0:1] == '1' and str.isdecimal() and str.len(str) == 11:
                        content = "您的联系方式已记录，下面开始答题笔试。\n"
                        backend.apply_interview(str, str, toUser)
                        problem = backend.online_interview(-1, "", toUser)
                        content = content + problem + "\n请回复【】内的编号完成答题。"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    if str.isdecimal(str[:-1]) and (str[-1:] == 'A' or str[-1:] == 'B' or str[-1:] == 'C' or str[-1:] == 'D'
                                                    or str[-1:] == 'a' or str[-1:] == 'b' or str[-1:] == 'c' or str[-1:] == 'd') :
                        problem = backend.online_interview(str[:-1], str[-1:], toUser)
                        if problem['problemId'] == "-1":
                            content = "您已完成答题笔试，请耐心等待后续通知，谢谢！"
                        else:
                            content = "下一题：\n" + problem['problem'] + "\n请回复【】内的编号完成答题。"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                    rlt = backend.ask_question(recMsg,toUser)
                    content = rlt['answer'].encode('utf-8')
                    print(content,content.__class__)
                    if content == "400":
                        content = "我好像不明白你在说什么呢"
                    if content == '401':
                        content = "换一种问法我说不定答得上来哦"
                    replyMsg = reply.TextMsg(toUser, fromUser, content)
                    return replyMsg.send()
                if recMsg.MsgType == 'voice':
                    #print('yes')
                    #print(recMsg.Recognition)
                    #content = "语音内容为："+recMsg.Recognition
                    rlt = backend.ask_question(recMsg,toUser)
                    content = rlt['answer'].encode('utf-8')
                    if content == "400":
                        content = "我好像不明白你在说什么呢"
                    if content == "401":
                        content = "换一种问法我说不定答得上来哦"
                    replyMsg = reply.TextMsg(toUser, fromUser, content)
                    return replyMsg.send()                    
                if recMsg.MsgType == 'image':
                    mediaId = recMsg.MediaId
                    replyMsg = reply.ImageMsg(toUser, fromUser, mediaId)
                    return replyMsg.send()
                if recMsg.MsgType == 'location':
                    location_x = recMsg.Location_X
                    location_y = recMsg.Location_Y
                    content = "您所在的位置是在：经度为"+location_x+"；纬度为："+location_y
                    replyMsg = reply.TextMsg(toUser, fromUser, content)
                    return replyMsg.send()
                if recMsg.MsgType == 'event':
                    #print('yes')
                    event = recMsg.Event
                    print(event)
                    if event == 'subscribe':
                        content = "欢迎关注智能招聘助手！回复数字即可选择公司开始招聘问答。（目前支持公司编号为：1、SAP；2、微软；3、腾讯）"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                else:
                    return reply.Msg().send()
            else:
                print("not do")
                return reply.Msg().send()
        except Exception as Argment:
            return Argment