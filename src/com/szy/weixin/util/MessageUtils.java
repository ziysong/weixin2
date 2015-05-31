package com.szy.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.szy.weixin.domain.TextMessage;
import com.thoughtworks.xstream.XStream;


public class MessageUtils {

	//MsgType:消息类型
	public final static String MESSAGE_TEXT = "text";
	public final static String MESSAGE_IMAGE = "image";
	public final static String MESSAGE_VOICE = "voice";
	public final static String MESSAGE_LOCATION = "location";
	public final static String MESSAGE_LINK = "link";
	public final static String MESSAGE_EVENT = "event";
	
	//事件类型:Event.它的消息类型是event
	public final static String MESSAGE_SUBSCRIBE = "subscribe"; //关注
	public final static String MESSAGE_UNSUBSCRIBE = "unsubscribe";//取消关注
	public final static String MESSAGE_CLICK = "click";//单击子菜单
	public final static String MESSAGE_LOCATION_EVENT = "location";//地理位置事件
	
	
	/**将xml数据转为文本消息对象,用map接收*/
	public static Map<String,String> xmlToMessage(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		Document doc = null;
		try {
			is = request.getInputStream();//获取微信post过来的输入流
			doc = reader.read(is);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			for(Element element : elements){
				map.put(element.getName(), element.getText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**将文本消息对象转为xml数据*/
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//设置根元素为xml.不然默认是TextMessage的全类名
		return xstream.toXML(textMessage);
	}

	//关注时推送
	public static String getFirstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("感谢您关注宋子扬的微信公众账号/:,@-D/:,@-D\n");
		sb.append("回复'1'可获取宋子扬的基本信息\n");
		sb.append("回复'2'可获取宋子扬的更多介绍\n");
		sb.append("回复'?'可获取帮助");
		return sb.toString();
	}
	
	//帮助时推送
	public static String getHelpMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("回复'1'可获取宋子扬的基本信息\n");
		sb.append("回复'2'可获取宋子扬的更多介绍\n");
		sb.append("回复'?'可获取帮助");
		return sb.toString();
	}

	
}
