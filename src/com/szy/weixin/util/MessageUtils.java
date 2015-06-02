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

	//MsgType:��Ϣ����
	public final static String MESSAGE_TEXT = "text";
	public final static String MESSAGE_NEWS = "news"; //ͼ����Ϣ
	public final static String MESSAGE_IMAGE = "image";
	public final static String MESSAGE_VOICE = "voice";
	public final static String MESSAGE_LOCATION = "location";
	public final static String MESSAGE_LINK = "link";
	public final static String MESSAGE_EVENT = "event";
	
	//�¼�����:Event.������Ϣ������event
	public final static String MESSAGE_SUBSCRIBE = "subscribe"; //��ע
	public final static String MESSAGE_UNSUBSCRIBE = "unsubscribe";//ȡ����ע
	public final static String MESSAGE_CLICK = "click";//�����Ӳ˵�
	public final static String MESSAGE_LOCATION_EVENT = "location";//����λ���¼�
	
	
	/**��xml����תΪ�ı���Ϣ����,��map����*/
	public static Map<String,String> xmlToMessage(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		Document doc = null;
		is = request.getInputStream();//��ȡ΢��post������������
		doc = reader.read(is);
		Element root = doc.getRootElement();
		List<Element> elements = root.elements();
		for(Element element : elements){
			map.put(element.getName(), element.getText());
		}
		return map;
	}
	
	/**���ı���Ϣ����תΪxml����*/
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//���ø�Ԫ��Ϊxml.��ȻĬ����TextMessage��ȫ����
		return xstream.toXML(textMessage);
	}

	/**��ͼ����ϢתΪxml����*/
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());//���ø�Ԫ��Ϊxml.��ȻĬ����TextMessage��ȫ����
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}

	
	//getTextMsgģ��:��ͨ�ı���Ϣ============================================
	public static TextMessage getTextMsg(String fromUserName, String toUserName) {
		TextMessage textMessage = new TextMessage();
		textMessage.setMsgType(MESSAGE_TEXT);
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		return textMessage;
	}

	//��ͨ�ı���Ϣ1
	public static String getTextMsg1(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		textMsg.setContent("������,�Ա���,����Ů");
		return textMessageToXml(textMsg);
	}
	
	//��ͨ�ı���Ϣ2
	public static String getTextMsg2(String fromUserName, String toUserName, String content) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		textMsg.setContent("�����͵�������:"+content);
		return textMessageToXml(textMsg);
	}

	//������Ϣ
	public static String getHelpMenu(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		StringBuffer sb = new StringBuffer();
		sb.append("�ظ�'1'�ɻ�ȡ������Ļ�����Ϣ/::*/::*\n");
		sb.append("�ظ�'2'�ɻ�ȡһ��ͼ����Ϣ/:X-)/:X-) \n");  
		sb.append("�ظ�'3'���Կ�/::$/::$ \n");  
		sb.append("�ظ�'?'�ɻ�ȡ����\ue056\ue056 ");
		textMsg.setContent(sb.toString());   
		return textMessageToXml(textMsg);
	}    

	//�״ι�עʱ����
	public static String getFirstMenu(String fromUserName, String toUserName) {
		TextMessage textMsg = getTextMsg(fromUserName,toUserName);
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ���ע�������С��/:,@-D/:,@-D\n");
		sb.append("�ظ�'1'�ɻ�ȡ������Ļ�����Ϣ/::*/::*\n");
		sb.append("�ظ�'2'�ɻ�ȡһ��ͼ����Ϣ/:X-)/:X-) \n");  
		sb.append("�ظ�'3'���Կ�/::$/::$ \n");  
		sb.append("�ظ�'?'�ɻ�ȡ����\ue056\ue056 ");
		textMsg.setContent(sb.toString());
		return textMessageToXml(textMsg);  
	}
	
	
	
	//getNewsMsgģ�壺ͼ����Ϣ============================================
	public static NewsMessage getNewsMsg(String fromUserName, String toUserName) {
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setMsgType(MessageUtils.MESSAGE_NEWS); 
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		return newsMessage;
	}
	
	//ͼ����Ϣ1������
	public static String getNewsMsg1(String fromUserName, String toUserName) {
		NewsMessage newsMsg = getNewsMsg(fromUserName, toUserName);
		News article = new News();
		List<News> articles = new ArrayList<News>();
		
		article.setTitle("�������벻���������ݡ�");
		article.setDescription("��������������һ�ִ��µĶ���,��Ͷ��˾ר��ȷ����Դ����ݵ�Ͷ����ϡ�");
		/**�����ܷ��������·��:����*/
		article.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl1.jpg");  
		article.setUrl("https://github.com/ziysong");
		articles.add(article);
		
		newsMsg.setArticles(articles);
		newsMsg.setArticleCount(articles.size());
		return MessageUtils.newsMessageToXml(newsMsg);
	} 
		
	//ͼ����Ϣ2������
	public static String getNewsMsg2(String fromUserName, String toUserName){
		NewsMessage newsMsg = getNewsMsg(fromUserName, toUserName);
		List<News> articles = new ArrayList<News>();
		
		News article1 = new News(); //��һ����ʾtitle+��ͼ
		article1.setTitle("Hibernate��ϸ�̳�");
		article1.setDescription("һ.���������.д�����ļ���hibernate.cfg.xml��");
		article1.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl2.jpg");
		article1.setUrl("https://github.com/ziysong");
		articles.add(article1);
		     
		News article2 = new News(); //���涼����ʾtitle+Сͼ
		article2.setTitle("Android�û��鿴�ֻ�֪ͨ�ٶȱ�ios������");
		article2.setDescription("Android�û��鿴�ֻ�֪ͨ�ٶȱ�ios������");
		article2.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/girl3.jpg");
		article2.setUrl("https://github.com/ziysong");
		articles.add(article2);
		
		News article3 = new News();
		article3.setTitle("��Я��̱������ά85������");
		article3.setDescription("��Я��̱������ά85������");
		article3.setPicUrl("http://ziysong.tunnel.mobi/weixin2/image/g.jpg");
		article3.setUrl("http://blog.csdn.net/u012599724/article/details/45932801");
		articles.add(article3);
		
		newsMsg.setArticles(articles);
		newsMsg.setArticleCount(articles.size());
		return MessageUtils.newsMessageToXml(newsMsg);
	}
	
	
	
	//ͼƬ��Ϣ��
	
	

	
	
}
