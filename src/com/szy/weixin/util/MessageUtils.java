package com.szy.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.szy.weixin.domain.News;
import com.szy.weixin.domain.NewsMessage;
import com.szy.weixin.domain.TextMessage;
import com.thoughtworks.xstream.XStream;


public class MessageUtils {

	//MsgType:消息类型
	public final static String MESSAGE_TEXT = "text";
	public final static String MESSAGE_NEWS = "news"; //图文消息
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
	public static Map<String,String> xmlToMessage(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		Document doc = null;
		is = request.getInputStream();//获取微信post过来的输入流
		doc = reader.read(is);
		Element root = doc.getRootElement();
		List<Element> elements = root.elements();
		for(Element element : elements){
			map.put(element.getName(), element.getText());
		}
		return map;
	}
	
	/**将文本消息对象转为xml数据*/
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//设置根元素为xml.不然默认是TextMessage的全类名
		return xstream.toXML(textMessage);
	}

	/**将图文消息转为xml数据*/
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());//设置根元素为xml.不然默认是TextMessage的全类名
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}

	
	//getTextMsg模板:普通文本消息============================================
	public static TextMessage getTextMsg(String fromUserName, String toUserName) {
		TextMessage textMessage = new TextMessage();
		textMessage.setMsgType(MESSAGE_TEXT);
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		return textMessage;
	}

	//普通文本消息1
	public static String getTextMsg1(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		textMsg.setContent("宋子扬,性别男,爱好女");
		return textMessageToXml(textMsg);
	}
	
	//普通文本消息2
	public static String getTextMsg2(String fromUserName, String toUserName, String content) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		textMsg.setContent("您发送的内容是:"+content);
		return textMessageToXml(textMsg);
	}

	//帮助信息
	public static String getHelpMenu(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		StringBuffer sb = new StringBuffer();
		sb.append("回复'1'可获取宋子扬的基本信息/::*/::*\n");
		sb.append("回复'2'可获取一条图文消息/:X-)/:X-) \n");  
		sb.append("回复'3'试试看/::$/::$ \n");  
		sb.append("回复'?'可获取帮助\ue056\ue056 ");
		textMsg.setContent(sb.toString());   
		return textMessageToXml(textMsg);
	}    

	//首次关注时推送
	public static String getFirstMenu(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎你关注宋子扬的小窝/:,@-D/:,@-D\n");
		sb.append("回复'1'可获取宋子扬的基本信息/::*/::*\n");
		sb.append("回复'2'可获取一条图文消息/:X-)/:X-) \n");  
		sb.append("回复'3'试试看/::$/::$ \n");  
		sb.append("回复'?'可获取帮助\ue056\ue056 ");
		textMsg.setContent(sb.toString());
		return textMessageToXml(textMsg);  
	}
	
	
	
	//getNewsMsg模板：图文消息============================================
	public static NewsMessage getNewsMsg(String fromUserName, String toUserName) {
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setMsgType(MessageUtils.MESSAGE_NEWS); 
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		return newsMessage;
	}
	
	//图文消息1：单条
	public static String getNewsMsg1(String fromUserName, String toUserName) {
		NewsMessage newsMsg = getNewsMsg(fromUserName, toUserName);
		News article = new News();
		List<News> articles = new ArrayList<News>();
		
		article.setTitle("大数据离不开“厚数据”");
		article.setDescription("大数据是驱动下一轮创新的动力,风投公司专门确立针对大数据的投资组合。");
		/**考虑能否设置相对路径:不能*/
		article.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl1.jpg");  
		article.setUrl("https://github.com/ziysong");
		articles.add(article);
		
		newsMsg.setArticles(articles);
		newsMsg.setArticleCount(articles.size());
		return MessageUtils.newsMessageToXml(newsMsg);
	} 
		
	//图文消息2：多条
	public static String getNewsMsg2(String fromUserName, String toUserName){
		NewsMessage newsMsg = getNewsMsg(fromUserName, toUserName);
		List<News> articles = new ArrayList<News>();
		
		News article1 = new News(); //第一条显示title+大图
		article1.setTitle("Hibernate详细教程");
		article1.setDescription("一.搭建环境。二.写配置文件：hibernate.cfg.xml。");
		article1.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl2.jpg");
		article1.setUrl("https://github.com/ziysong");
		articles.add(article1);
		     
		News article2 = new News(); //后面都是显示title+小图
		article2.setTitle("Android用户查看手机通知速度币ios快两倍");
		article2.setDescription("Android用户查看手机通知速度币ios快两倍");
		article2.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl3.jpg");
		article2.setUrl("https://github.com/ziysong");
		articles.add(article2);
		
		News article3 = new News();
		article3.setTitle("从携程瘫痪看运维85条军规");
		article3.setDescription("从携程瘫痪看运维85条军规");
		article3.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/g.jpg");
		article3.setUrl("http://blog.csdn.net/u012599724/article/details/45932801");
		articles.add(article3);
		
		newsMsg.setArticles(articles);
		newsMsg.setArticleCount(articles.size());
		return MessageUtils.newsMessageToXml(newsMsg);
	}
	
	
	
	//图片消息：
	
	

	
	
}
