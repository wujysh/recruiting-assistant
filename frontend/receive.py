import xml.etree.ElementTree as ET

def parse_xml(web_data):
    if len(web_data) == 0:
        return None
    xmlData = ET.fromstring(web_data)
    msg_type = xmlData.find('MsgType').text
    if msg_type == 'text':
        #print('text')
        return TextMsg(xmlData)
    elif msg_type == 'image':
        return ImageMsg(xmlData)
    elif msg_type == 'location':
        #print('location')
        return LocationMsg(xmlData)
    elif msg_type == 'event':
        #print('event')
        return EventMsg(xmlData)
    elif msg_type == 'voice':
        #print('event')
        return VoiceMsg(xmlData)

class Event(object):
    def __init__(self, xmlData):
        print('event1')
        self.ToUserName = xmlData.find('ToUserName').text
        print('event2')
        self.FromUserName = xmlData.find('FromUserName').text
        print('event3')
        self.CreateTime = xmlData.find('CreateTime').text
        print('event4')
        self.MsgType = xmlData.find('MsgType').text
        print('event5')
        self.Eventkey = xmlData.find('EventKey').text

class Msg(object):
    def __init__(self, xmlData):
        self.ToUserName = xmlData.find('ToUserName').text
        self.FromUserName = xmlData.find('FromUserName').text
        self.CreateTime = xmlData.find('CreateTime').text
        self.MsgType = xmlData.find('MsgType').text
        self.MsgId = xmlData.find('MsgId').text

class TextMsg(Msg):
    def __init__(self, xmlData):
        Msg.__init__(self, xmlData)
        self.Content = xmlData.find('Content').text.encode("utf-8")

class ImageMsg(Msg):
    def __init__(self, xmlData):
        Msg.__init__(self, xmlData)
        self.PicUrl = xmlData.find('PicUrl').text
        self.MediaId = xmlData.find('MediaId').text

class LocationMsg(Msg):
    def __init__(self, xmlData):
        Msg.__init__(self, xmlData)
        self.Location_X = xmlData.find('Location_X').text
        self.Location_Y = xmlData.find('Location_Y').text

class EventMsg(Msg):
    def __init__(self, xmlData):
        #print('event init')
        #print('event1')
        self.ToUserName = xmlData.find('ToUserName').text
        #print('event2')
        self.FromUserName = xmlData.find('FromUserName').text
        #print('event3')
        self.CreateTime = xmlData.find('CreateTime').text
        #print('event4')
        self.MsgType = xmlData.find('MsgType').text
        #print('event5')
        self.Eventkey = xmlData.find('EventKey').text

        #print('event//')
        self.Event = xmlData.find('Event').text

class VoiceMsg(Msg):
    def __init__(self, xmlData):
        Msg.__init__(self, xmlData)
        self.Recognition = xmlData.find('Recognition').text.encode("utf-8")
