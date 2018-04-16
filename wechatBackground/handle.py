# -*- coding: utf-8 -*-
import web
import reply
import receive
import JsonData
import xml.dom.minidom
import getrlt
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
            #print(webData)
            recMsg = receive.parse_xml(webData)
            #print(recMsg)
            #print(recMsg.MsgType)
            if isinstance(recMsg, receive.Msg):
                toUser = recMsg.FromUserName
                fromUser = recMsg.ToUserName
                #print(recMsg.MsgType)
                if recMsg.MsgType == 'text':
                    # try:
                    #     a = JsonData.praserJsonFile()
                    #     #print(a)
                    # except Exception as Argument:
                    #     return Argument
                    # if a['status'] == '1':
                    #     content = "No equipment"
                    # else:
                    #     if a['data'][0]['weather']=='0':
                    #         israin = '7.没有下雨'
                    #     else:
                    #         israin = '7.下雨'
                    #     #print(israin)
                    #     content = "此设备数据如下:\n"+"1.id号为 "+a['data'][0]['id']+"\n"+"2.温度为 "+a['data'][0]['temp']+"\n"+"3.湿度为 "+a['data'][0]['humidity']+"\n"+"4.PM2.5浓度为 "+a['data'][0]['pm25']+"ug\n"+"5.PM10浓度为 "+a['data'][0]['pm10']+"\n"+"6.光照 "+a['data'][0]['illumination']+"L\n"+israin
                    #     #content = "%s\n%s %s\n%s %s\n%s %s\n%s %s\n%s %s\n%s" %('环境数据如下：','设备id号为',a['data']['id'],'temp is', a['data']['temp'], 'humidity is', a['data']['humidity'],'PM25 is',a['data']['pm25'],'illumination',a['data']['illumination'],israin)
                    #     #print(content)
                    getrlt.getRlt(recMsg)
                    content = "欢迎使用"+recMsg.Content+"公司AI招聘系统！"
                    replyMsg = reply.TextMsg(toUser, fromUser, content)
                    return replyMsg.send()
                if recMsg.MsgType == 'voice':
                    #print('yes')
                    #print(recMsg.Recognition)
                    content = "语音内容为："+recMsg.Recognition
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
                    #print(event)
                    if event == 'subscribe':
                        content = "欢迎关注SAP智能招聘助手！"
                        replyMsg = reply.TextMsg(toUser, fromUser, content)
                        return replyMsg.send()
                else:
                    return reply.Msg().send()
            else:
                print("not do")
                return reply.Msg().send()
        except Exception as Argment:
            return Argment
